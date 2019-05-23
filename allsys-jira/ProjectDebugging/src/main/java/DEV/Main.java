package DEV;

import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.sun.jersey.core.util.Base64;

import API.bul.ApplicationLinkBUL;
import API.bul.ConfigBUL;
import API.bul.IssueBUL;
import API.extension.RequestExtension;
import API.model.ConfigModel;
import API.model.IssueModel;
import API.model.WebHookModel;




@Path("/")
public class Main {
	private static final Logger log = Logger.getLogger(Main.class);
	
	
	@GET
	@Path("/test")
	public void testTemp() {
		String accountAuthClient=new String(Base64.encode("admin" + ":" + "All@1234"));
		Response rs=RequestExtension.getInstance().invokeGetMethod(accountAuthClient,
				"http://192.168.1.158:8145/rest/api/2/issue/TTT-54/properties/Client");
		if (rs.getStatus()!=200) {
			
		}else {
			try {
				JSONObject jsonObject=new JSONObject(rs.getEntity().toString());
				JSONObject jsonObject2=jsonObject.getJSONObject("value");
				System.out.println(jsonObject2.get("KeyIssueAllS"));
				System.out.println(jsonObject2.get("IDApplicationLink"));
				if (jsonObject2!=null) {
					
				}
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
	}
	@GET
	@Path("/testcreateissuealls/{keyissue}")
	public Response testCreateIssueAllS(@PathParam("keyissue") String keyIssue, @Context HttpServletRequest request) {

		String domainJiraClient = "http://localhost:8145";
		String userNameClient="admin";
		String passWordClient="All@1234";
		
		String doaminJiraAllSystem="http://192.168.1.237:8145";
		String userNameAllS="admin";
		String passWordAlls="123";
		
		String accountAuthClient=new String(Base64.encode(userNameClient + ":" + passWordClient));
		
		log.warn(keyIssue + " " + domainJiraClient);
		String bodyRequest=null;
		IssueBUL issueBUL=null;
		ConfigModel configModel=null;
		try {
			
			
			
			//Read info from request of webhook send to server
			//bodyRequest=getBody(request);
			bodyRequest=RequestExtension.getInstance().invokeGetMethod(accountAuthClient,domainJiraClient+"/rest/api/2/issue/"+keyIssue).getEntity().toString();
			//WebHookBUL webHookBUL=new WebHookBUL(bodyRequest);
			//WebHookModel webHookModel=webHookBUL.getWebHook();
			WebHookModel webHookModel=new WebHookModel(new JSONObject(bodyRequest));
			log.warn("Status Before:"+webHookModel.getIdStatusBefore());
			log.warn("Body Request:"+bodyRequest);
			
			//Set session user current excute
//			ApplicationUser applicationUser=webHookModel.getApplicationUser();
//			authContext.setLoggedInUser(applicationUser);
//			log.warn("User excute: "+applicationUser.getName()+" "+applicationUser.getEmailAddress());
//			log.warn("Logged: "+authContext.isLoggedInUser());
			
			//Read file config of Project
			String keyProject = keyIssue.substring(0, keyIssue.indexOf("-"));
			String fileConfig = "/config/" + keyProject + "ClientConfig.xml";
			InputStream isStream = getClass().getResourceAsStream(fileConfig);
			
			ConfigBUL configBUL = new ConfigBUL(isStream);
			configModel = configBUL.getConfigModel();

			//Handing data
			issueBUL = new IssueBUL(userNameClient, passWordClient, domainJiraClient);
			issueBUL.setApplicationLinkBUL(new ApplicationLinkBUL(userNameAllS,passWordAlls,doaminJiraAllSystem));
			IssueModel issueModel=issueBUL.getIssueFromFields(webHookModel, configModel, keyProject,keyIssue);
			
			//Send request create issue to AllSystem
			Response resultCreateIssue=issueBUL.createIssueAllSystem(issueModel);
			log.warn("Status Request:"+resultCreateIssue.getStatus());
			log.warn("Result:"+resultCreateIssue.getEntity().toString());
			
			if (resultCreateIssue.getStatus()==200) {
				//Set property application link for issue
				Response rs=issueBUL.setPropertyApplicationLinkForIssue(resultCreateIssue.getEntity().toString(), "123456");
			}
			return Response.ok(new PrimasModel("success", resultCreateIssue.getEntity().toString())).build();
		}catch (Exception e) {
			log.warn(e.getMessage()+" BodyRequest:"+bodyRequest);
		}
		
		//If Progress Bug rollback status issue
		try {
			issueBUL.changeStatusIssueToRefuse(configModel.getIdStatusRefuse(), keyIssue);
			issueBUL.addCommet("Create issue into Allsystem fail. Please contact your service provider", keyIssue);
		} catch (Exception e) {
			log.warn(e.getMessage());
		}
		log.warn("Progress Bug rollback status issue");
		return Response.serverError().entity(new PrimasModel("error","Create issue fail, rollback status issue")).build();
	}
	
}
