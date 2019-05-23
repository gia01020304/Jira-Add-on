package API.rest;

import com.atlassian.annotations.PublicApi;
import com.atlassian.applinks.api.ApplicationLinkService;
import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.plugin.spring.scanner.annotation.component.Scanned;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.plugins.rest.common.security.AnonymousAllowed;
import com.atlassian.sal.api.net.TrustedRequestFactory;
import com.sun.jersey.spi.inject.Inject;

import API.bul.ApplicationLinkBUL;
import API.bul.IssueBUL;
import API.bul.WebHookBUL;
import API.model.PropertyIssueAllSModel;
import API.model.WebHookModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * A resource of message.
 */
@Path("/")
@Scanned
public class Primas {
	private static final Logger log = Logger.getLogger(Primas.class);
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

	@POST
	@AnonymousAllowed
	@Path("/responseclient/{keyissue}")
	@Produces({ MediaType.APPLICATION_JSON })
	@PublicApi
	public Response responseClient(@PathParam("keyissue") String keyIssue, @Context HttpServletRequest request) {
		String domain = getDomain(request);
		IssueBUL issueBUL = null;
		PropertyIssueAllSModel propertyIssueAllSModel = null;
		String bodyRequest=null;
		try {
			bodyRequest = getBody(request);
			WebHookBUL webHookBUL = new WebHookBUL(bodyRequest);
			WebHookModel webHookModel = webHookBUL.getWebHook();
			
			
			log.warn("Body Request:" + bodyRequest);

			issueBUL = new IssueBUL(fRequestFactory, domain);
			propertyIssueAllSModel = issueBUL.getPropertyIssueAllS(keyIssue);
			
			//Set session account accepted
			ApplicationUser applicationUser=ComponentAccessor.getUserManager().getUserByKey(propertyIssueAllSModel.getUserNameAllS());
			authContext.setLoggedInUser(applicationUser);

			
			if (propertyIssueAllSModel != null) {
				issueBUL.setApplicationLinkBUL(
						new ApplicationLinkBUL(applicationLinkService, propertyIssueAllSModel.getiDApplicationLink()));
				String statusName = issueBUL.getStatusNameClient(propertyIssueAllSModel.getIssueKeyClient());

				// Check issue in Client have been resolved
				if (statusName.equals(propertyIssueAllSModel.getStatusName())) {
					//update status issue
					String apiUpdateTransition="/rest/primas/1.0/updatestransitionissue/" + propertyIssueAllSModel.getIssueKeyClient()
					+ "/" + propertyIssueAllSModel.getiDTransitionResolvedAllS();
					log.warn("Api Update issue:"+apiUpdateTransition);
					Response rsUpdateTransition = issueBUL.getApplicationLinkBUL().invokeGetMethod(apiUpdateTransition);
					log.warn("Status Result:"+rsUpdateTransition.getStatus());
					log.warn("Result:"+rsUpdateTransition.getEntity());
					
					if (rsUpdateTransition.getStatus() != 200) {
						issueBUL.addCommet("Response to Jira Client fail:", keyIssue);
					}else {
						//update resolution
						issueBUL.updateResolution(propertyIssueAllSModel.getIssueKeyClient(),webHookModel.getNameResolution());
					}
				}
				return issueBUL.sendCommentAllS("AllS resolved issue", propertyIssueAllSModel.getIssueKeyClient());
			}

		} catch (Exception e) {
			log.warn("Response to Client fail:" + e.getMessage());
		}
		try {
			if (propertyIssueAllSModel != null) {
				issueBUL.addCommet("Response to Jira Client fail:", keyIssue);
			}
		} catch (Exception e) {
		}
		return Response.serverError().build();
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