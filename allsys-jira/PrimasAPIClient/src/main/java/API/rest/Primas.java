package API.rest;

import com.atlassian.annotations.PublicApi;
import com.atlassian.applinks.api.ApplicationId;
import com.atlassian.applinks.api.ApplicationLink;
import com.atlassian.applinks.api.ApplicationLinkRequest;
import com.atlassian.applinks.api.ApplicationLinkRequestFactory;
import com.atlassian.applinks.api.ApplicationLinkResponseHandler;
import com.atlassian.applinks.api.ApplicationLinkService;
import com.atlassian.applinks.api.auth.AuthenticationProvider;
import com.atlassian.jira.bc.issue.IssueService;
import com.atlassian.jira.bc.issue.IssueService.TransitionValidationResult;
import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.issue.IssueInputParameters;
import com.atlassian.jira.issue.MutableIssue;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.jira.workflow.TransitionOptions;
import com.atlassian.plugin.spring.scanner.annotation.component.Scanned;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.plugins.rest.common.security.AnonymousAllowed;
import com.atlassian.sal.api.net.ResponseException;
import com.atlassian.sal.api.net.TrustedRequestFactory;
import com.opensymphony.workflow.loader.ActionDescriptor;
import com.atlassian.sal.api.net.Request.MethodType;
import com.sun.jersey.spi.inject.Inject;

import API.bul.ApplicationLinkBUL;
import API.bul.ConfigBUL;
import API.bul.IssueBUL;
import API.bul.WebHookBUL;
import API.bul.WorkFlowBUL;
import API.extension.Extension;
import API.model.ConfigModel;
import API.model.IssueModel;
import API.model.PropertyIssueAllSModel;
import API.model.PropertyIssueClientModel;
import API.model.WebHookModel;
import API.model.WorkFlowModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * A resource of message.
 */
@Path("/")
@Scanned
public class Primas {
	@ComponentImport
	private final JiraAuthenticationContext authContext;
	@ComponentImport
	private final ApplicationLinkService applicationLinkService;
	@ComponentImport
	private final TrustedRequestFactory fRequestFactory;

	@Inject
	@Autowired
	public Primas(JiraAuthenticationContext authContext, ApplicationLinkService applicationLinkService,
			TrustedRequestFactory fRequestFactory) {
		this.authContext = authContext;
		this.applicationLinkService = applicationLinkService;
		this.fRequestFactory = fRequestFactory;
	}

	private static final Logger log = Logger.getLogger(Primas.class);

	@GET
	@AnonymousAllowed
	@Path("/updatestransitionissue/{keyissue}/{idtransition}")
	public Response updateTransitionIssue(@PathParam("keyissue") String keyIssue,
			@PathParam("idtransition") int idTransition) {

		try {
			log.warn("Login:" + authContext.isLoggedInUser());
			log.warn("Current user:" + authContext.getLoggedInUser().getName());
			MutableIssue issue = ComponentAccessor.getIssueManager().getIssueByCurrentKey(keyIssue);
			IssueService issueService = ComponentAccessor.getIssueService();
			IssueInputParameters parameters = issueService.newIssueInputParameters();
			TransitionOptions transitionOptions = new TransitionOptions.Builder().skipConditions().skipPermissions()
					.skipValidators().build();
			ApplicationUser currentUser = authContext.getLoggedInUser();
			TransitionValidationResult result = issueService.validateTransition(currentUser, issue.getId(),
					idTransition, parameters, transitionOptions);
			if (result.isValid()) {
				issueService.transition(currentUser, result);
				return Response.ok(new PrimasModel("success", "Update transition success")).build();
			} else {
				log.warn("\n" + this.getClass() + " Update transition fail:"
						+ result.getErrorCollection().getErrorMessages());
			}
		} catch (Exception e) {
			log.warn("\n" + this.getClass() + " Update transition fail:" + e.getMessage());
		}
		return Response.serverError().entity(new PrimasModel("error", "Update transition fail")).build();
	}

	@POST
	@AnonymousAllowed
	@Path("/getrequestbody")
	@Produces({ MediaType.APPLICATION_JSON })
	@PublicApi
	public Response getRequestBody(@Context HttpServletRequest request) throws Exception {
		String body = null;
		try {
			body = getBody(request);
		} catch (Exception e) {
			throw new Exception("Get body from request fail:" + e.getMessage());
		}
		log.warn("server log:" + body);
		return Response.ok("server return:" + body).build();
	}

	@POST
	@AnonymousAllowed
	@Path("/createissuealls/{keyissue}")
	@Produces({ MediaType.APPLICATION_JSON })
	@PublicApi
	public Response createIssueAllS(@PathParam("keyissue") String keyIssue, @Context HttpServletRequest request) {

		String domainJiraClient = getDomain(request);
		log.warn(keyIssue + " " + domainJiraClient);
		String bodyRequest = null;
		IssueBUL issueBUL = null;
		ConfigModel configModel = null;
		try {
			// Read body from request of webhook send to server
			bodyRequest = getBody(request);
			WebHookBUL webHookBUL = new WebHookBUL(bodyRequest);
			WebHookModel webHookModel = webHookBUL.getWebHook();
			log.warn("Body Request:" + bodyRequest);

			// Read file config of Project
			String keyProject = keyIssue.substring(0, keyIssue.indexOf("-"));
			String fileConfig = "/config/" + keyProject + "ClientConfig.xml";
			InputStream isStream = getClass().getResourceAsStream(fileConfig);

			ConfigBUL configBUL = new ConfigBUL(isStream);
			WorkFlowBUL workFlowBUL = new WorkFlowBUL();
			WorkFlowModel workFlowModel = workFlowBUL.getWorkFlowOfProjectAndIssueType(webHookModel.getProjectId(),
					webHookModel.getIssueTypeId());
			configModel = configBUL.getConfigModel(workFlowModel.getListIDTransition());
			log.warn("ID RollBack:" + configModel.getIdTransitionRollBack());
			log.warn("ID Resolved:" + configModel.getIdTransitionResolvedByAllS());

			// Set session account accepted
			ApplicationUser applicationUser = ComponentAccessor.getUserManager()
					.getUserByName(configModel.getUserNameClient());
			authContext.setLoggedInUser(applicationUser);

			// Handing data
			issueBUL = new IssueBUL(fRequestFactory, domainJiraClient);
			issueBUL.setApplicationLinkBUL(
					new ApplicationLinkBUL(applicationLinkService, configModel.getIdApplicationLinkClient()));
			IssueModel issueModel = issueBUL.getIssueFromBodyWebHook(webHookModel, configModel, keyProject, keyIssue);

			// Send request create issue to AllSystem
			Response resultCreateIssue = issueBUL.createIssueAllSystem(issueModel, configModel.getKeyProjectAllS());
			log.warn("Status Request:" + resultCreateIssue.getStatus());
			log.warn("Result:" + resultCreateIssue.getEntity().toString());
			
			if (resultCreateIssue.getStatus() == 200) {
				JSONObject issueObjectAllS = new JSONObject(resultCreateIssue.getEntity().toString());
				String keyIssueAllS = issueObjectAllS.get("key").toString();
				issueBUL.setPropertyForIssueClient(keyIssue, keyIssueAllS, configModel);
				issueBUL.setPropertyForIssueAllS(keyIssue, keyIssueAllS, configModel, webHookModel.getStatusName());
			} else {
				throw new Exception("Problem request:" + resultCreateIssue.getEntity());
			}
			issueBUL.addCommet("Create issue into Allsystem success.", keyIssue);
			return Response.ok(new PrimasModel("success", resultCreateIssue.getEntity().toString())).build();
		} catch (Exception e) {
			log.warn(e.getMessage() + " BodyRequest:" + bodyRequest);
		}

		// If Progress Bug rollback status issue
		try {
			this.updateTransitionIssue(keyIssue, Integer.parseInt(configModel.getIdTransitionRollBack()));
			String comment = "Create issue into Allsystem fail. Please contact your service provider.";
			issueBUL.addCommet(comment, keyIssue);
		} catch (Exception e) {
			log.warn(e.getMessage());
		}
		log.warn("Progress Bug rollback status issue");
		return Response.serverError().entity(new PrimasModel("error", "Create issue fail, rollback status issue"))
				.build();

	}

	@POST
	@AnonymousAllowed
	@Path("/sendcommentalls/{keyissue}")
	@Produces({ MediaType.APPLICATION_JSON })
	@PublicApi
	public Response sendCommentAllS(@PathParam("keyissue") String keyIssue, @Context HttpServletRequest request) {
		String domainJiraClient = getDomain(request);
		try {
			IssueBUL issueBUL = new IssueBUL(fRequestFactory, domainJiraClient);
			PropertyIssueClientModel propertyIssueClientModel = issueBUL.getPropertyIssueClient(keyIssue);

			// Set session account accepted
			ApplicationUser applicationUser = ComponentAccessor.getUserManager().getUserByName(propertyIssueClientModel.getUserNameClient());
			authContext.setLoggedInUser(applicationUser);
			
			issueBUL.setApplicationLinkBUL(
					new ApplicationLinkBUL(applicationLinkService, propertyIssueClientModel.getiDApplicationLink()));

			String comment = "Jira Client resolved issue";
			return issueBUL.sendCommentAllS(comment, propertyIssueClientModel.getKeyIssueAllS());
		} catch (Exception e) {
			log.warn("Send Comment to AllS fail:" + e.getMessage());
		}
		return Response.serverError().entity(new PrimasModel("error", "Send comment error")).build();
	}

	private String getBody(HttpServletRequest request) throws IOException {

		String body = null;
		StringBuilder stringBuilder = new StringBuilder();
		BufferedReader bufferedReader = null;

		try {
			InputStream inputStream = request.getInputStream();
			if (inputStream != null) {
				bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
				char[] charBuffer = new char[128];
				int bytesRead = -1;
				while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
					stringBuilder.append(charBuffer, 0, bytesRead);
				}
			} else {
				stringBuilder.append("");
			}
		} catch (IOException ex) {
			throw ex;
		} finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException ex) {
					throw ex;
				}
			}
		}
		body = stringBuilder.toString();
		return body;
	}
	private String getDomain(HttpServletRequest request) {
		return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
	}

}