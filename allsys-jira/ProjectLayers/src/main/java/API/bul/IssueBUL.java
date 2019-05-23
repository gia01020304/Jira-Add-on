package API.bul;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.atlassian.sal.api.net.TrustedRequestFactory;
import com.google.gson.JsonObject;

import API.extension.Extension;
import API.model.ConfigModel;
import API.model.FieldModel;
import API.model.IssueModel;
import API.model.ObjectTypeConfig;
import API.model.PropertyIssueAllSModel;
import API.model.PropertyIssueClientModel;
import API.model.WebHookModel;

public class IssueBUL extends JiraBUL {

	private static final Logger log = Logger.getLogger(IssueBUL.class);

	public IssueBUL(String userName, String passWord, String url) {
		super(userName, passWord, url);
		// TODO Auto-generated constructor stub
	}

	public IssueBUL(String cookieAuth, String url) {
		super(cookieAuth, url);
		// TODO Auto-generated constructor stub
	}
	public Response rollBackStatusIssue(String idTransitionRollBack, String keyIssue) throws Exception {
		try {
			String bodyChangeStatus = "{ \"transition\": { \"id\": \"" + idTransitionRollBack + "\" }}";
			return this.invokePostMethod(this.getUrl() + "/rest/api/2/issue/" + keyIssue + "/transitions",
					bodyChangeStatus);
		} catch (Exception e) {
			throw new Exception("\n"+this.getClass()+"Request do transition refuse fail:" + e.getMessage());
		}
	}

	public Response addCommet(String comment, String keyIssue) throws Exception {
		try {
			String bodyComment = "{\"body\": \"" + comment + "\"}";
			return this.invokePostMethod(this.getUrl() + "/rest/api/2/issue/" + keyIssue + "/comment", bodyComment);
		} catch (Exception e) {
			throw new Exception("\n"+this.getClass()+"Request add comment fail:" + e.getMessage());
		}
	}

	public Response setPropertyForIssueAllS(String keyIssueClient,String keyIssueAllS, ConfigModel configModel,String statusName)
			throws Exception {
		try {
			String bodySetProperty = "{\"IDApplicationLink\": \""+configModel.getIdApplicationLinkAllS()+"\","
									+ "\"IDTransitionResolvedAllS\":\""+configModel.getIdTransitionResolvedByAllS()+"\","
									+ "\"StatusName\":\""+statusName+"\","
									+ "\"UserNameAllS\":\""+configModel.getUserNameAllS()+"\","
									+ "\"IssueKeyClient\":\""+keyIssueClient+"\"}";
			return this.getApplicationLinkBUL().invokePutMethod(
					"/rest/api/2/issue/" + keyIssueAllS + "/properties/AllS", bodySetProperty);
		} catch (Exception e) {
			throw new Exception("\n"+this.getClass()+"Set property For Issue AllS fail:" + e.getMessage());
		}

	}

	public Response createIssueAllSystem(IssueModel issueModelClient,String keyProjectAllS) throws Exception {

		String bodyCreateIssue = "{" + "\"fields\":{" + "\"project\":{" + "\"key\": \""
				+ keyProjectAllS + "\"" + "}," + "\"issuetype\":{" + " \"id\": \""
				+ issueModelClient.getIssueTypeIdAllSystem() + "\"" + "}" + issueModelClient.getBodyFieldCreateIssue()
				+ "}" + "}";
		log.warn("Body Create Issue:" + bodyCreateIssue);
		String apiCreateIssue = "/rest/api/2/issue";
		try {
			Response rs = this.getApplicationLinkBUL().invokePostMethod(apiCreateIssue, bodyCreateIssue);
			return rs;
		} catch (Exception e) {
			throw new Exception("\n"+this.getClass()+" Request Create Issue Fail:" + e.getMessage());
		}

	}

	public IssueBUL(TrustedRequestFactory fRequestFactory, String url) {
		super(fRequestFactory, url);
		// TODO Auto-generated constructor stub
	}
	public List<Integer>getIdTransitionOfIssue(String keyIssue) throws Exception{
		try {
			String apiGetTransitionIssue=this.getUrl()+"/rest/api/2/issue/"+keyIssue+"/transitions";
			Response response=this.invokeGetMethod(apiGetTransitionIssue);
			if (response.getStatus()==200) {
				JSONObject jsonObject=new JSONObject(response.getEntity().toString());
				JSONArray jsonArray=jsonObject.getJSONArray("transitions");
				List<Integer>integers=new ArrayList<>();
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject jsonTransition=jsonArray.getJSONObject(i);
					integers.add(jsonTransition.getInt("id"));
				}
				return integers;
			}else {
				throw new Exception("\n"+this.getClass()+" getIdTransitionOfIssue fail:"+response.getEntity());
			}
		} catch (Exception e) {
			throw new Exception("\n"+this.getClass()+" getIdTransitionOfIssue fail:"+e.getMessage());
		}
		
	}
	//rename
	public IssueModel getIssueFromBodyWebHook(WebHookModel webHookModel, ConfigModel configModel, String projectKey,
			String issueKey) throws Exception {
		try {
			JSONObject bodyIssue = webHookModel.getBodyIssue();
			setValueForFieldModels(bodyIssue, configModel);
			String issueTypeIdClient = bodyIssue.getJSONObject("fields").getJSONObject("issuetype").get("id")
					.toString();
			String issueTypeIdAllSystem = configModel.getIdIssueTypeAllSystem(issueTypeIdClient);

			IssueModel issueModel = new IssueModel();
			issueModel.setProjectKey(projectKey);
			issueModel.setIssueKey(issueKey);
			issueModel.setFieldModels(configModel.getFieldModels());
			issueModel.setIssueTypeIdAllSystem(issueTypeIdAllSystem);
			issueModel.setIssueTypeIdClient(issueTypeIdClient);
			return issueModel;
		} catch (Exception e) {
			throw new Exception("\n"+this.getClass()+"Get issue fail:" + e.getMessage());
		}

	}

	public IssueModel getIssue(String issueKey, ConfigModel configModel, String projectKey) throws Exception {

		String apiGetIssue = this.getUrl() + "/rest/api/2/issue/" + issueKey;
		Response rs = this.invokeGetMethod(apiGetIssue);

		if (rs.getStatus() == 200) {
			JSONObject jsonObject = new JSONObject(rs.getEntity().toString());

			// setValueForFieldModels(jsonObject, configModel);
			String issueTypeIdClient = getIssueTypeId(rs.getEntity().toString());
			String issueTypeIdAllSystem = configModel.getIdIssueTypeAllSystem(issueTypeIdClient);

			IssueModel issueModel = new IssueModel();
			issueModel.setProjectKey(projectKey);
			issueModel.setIssueKey(issueKey);
			issueModel.setFieldModels(configModel.getFieldModels());
			issueModel.setIssueTypeIdAllSystem(issueTypeIdAllSystem);
			issueModel.setIssueTypeIdClient(issueTypeIdClient);
			// issueModel.setUserNameAllSystem(getInfoAccAllS(jsonObject,configModel.getIdFieldUserNameAllS()));
			// issueModel.setPassWordAllSystem(getInfoAccAllS(jsonObject,configModel.getIdFieldPassWordAllS()));
			return issueModel;
		} else {
			return null;
		}
	}

	private String getIssueTypeId(String json) {
		return new JSONObject(json).getJSONObject("fields").getJSONObject("issuetype").get("id").toString();
	}

//	private String getInfoAccAllS(JSONObject jsonObject, String idFieldUserName) {
//		return jsonObject.getJSONObject("fields").get("customfield_" + idFieldUserName).toString();
//	}

	public void setValueForFieldModels(JSONObject jsonObject, ConfigModel configModel) throws Exception {

		try {
			JSONObject jsonFields = jsonObject.getJSONObject("fields");
			List<FieldModel> fieldModels = configModel.getFieldModels();

			if (fieldModels != null && fieldModels.size() > 0) {
				InsightBUL insightBUL = null;
				switch (this.getTypeAuthen()) {
				case Account:
					insightBUL = new InsightBUL(this.getUserName(), this.getPassWord(), this.getUrl());
					break;
				case Cookie:
					insightBUL = new InsightBUL(this.getCookieAuth(), this.getUrl());
					break;
				case Jira:
					insightBUL = new InsightBUL(this.getfRequestFactory(), this.getUrl());
					break;
				}
				insightBUL.setApplicationLinkBUL(this.getApplicationLinkBUL());
				for (FieldModel fieldModel : fieldModels) {

					String valueJson = jsonFields.get(fieldModel.getIdClient()).toString();
					if (valueJson == null||valueJson.equals("null")) {
						fieldModel.setValue(null);
						continue;
					}
					boolean checkValue = Extension.getInstance().isJsonValid(valueJson);
					if (checkValue) {
						fieldModel.setValue(
								"{ \"value\": \"" + new JSONObject(valueJson).get("value").toString() + "\" }");
						continue;
					}

					if (fieldModel.isInsight()) {

						int indexId = valueJson.lastIndexOf("-");
						int lastPoint = valueJson.lastIndexOf(")");
						String idObject = valueJson.substring(indexId + 1, lastPoint);

						ObjectTypeConfig objectTypeConfig = insightBUL.getObjectInsightClient(idObject, configModel);
						if (objectTypeConfig != null) {
							Map<String, String> keyObjectAllSystem = insightBUL
									.getObjectInsightAllSystem(objectTypeConfig, configModel);
							log.warn("key object:" + keyObjectAllSystem.get("objectKey"));
							if (keyObjectAllSystem != null && keyObjectAllSystem.size() > 0) {
								fieldModel.setValue(keyObjectAllSystem.get("objectKey"));
							}
						}
					} else {
						fieldModel.setValue("\"" + valueJson + "\"");
					}
				}
			}

		} catch (Exception e) {
			throw new Exception("\n"+this.getClass()+"Set value for field fail:" + e.getMessage());
		}
	}

	public Response setPropertyForIssueClient(String keyIssue, String keyIssueAllS, ConfigModel configModel) throws Exception {
		try {
			String bodySetProperty="{\"IDApplicationLink\": \""+configModel.getIdApplicationLinkClient()+
									"\",\"KeyIssueAllS\":\""+keyIssueAllS+
									"\",\"UserNameClient\":\""+configModel.getUserNameClient()+"\"}";
			log.warn("Body property Client:"+bodySetProperty);
			return this.invokePutMethod(this.getUrl()+"/rest/api/2/issue/"+keyIssue+"/properties/Client", bodySetProperty);
		} catch (Exception e) {
			throw new Exception("\n"+this.getClass()+"Set property For Issue Client fail:" + e.getMessage());
		}
	}

	public Response sendCommentAllS(String comment,String keyIssueAllS) throws Exception {
		try {
			String bodyComment = "{\"body\": \"" + comment + "\"}";
			Response rs=this.getApplicationLinkBUL().invokePostMethod("/rest/api/2/issue/" + keyIssueAllS + "/comment", bodyComment);
			if (rs.getStatus()==200) {
				return rs;
			}else {
				throw new Exception("\n"+this.getClass()+"Send comment fail:"+rs.getEntity());
			}
		} catch (Exception e) {
			throw new Exception("\n"+this.getClass()+"Send comment fail:"+e.getMessage());
		}
	}

	public PropertyIssueClientModel getPropertyIssueClient(String keyIssue) throws Exception {
		String apiGetPropertyIssueClient=this.getUrl()+"/rest/api/2/issue/"+keyIssue+"/properties/Client";
		try {
			Response rs=this.invokeGetMethod(apiGetPropertyIssueClient);
			log.warn(rs.getEntity());
			if (rs.getStatus()==200) {
				JSONObject jsonObject=new JSONObject(rs.getEntity().toString());
				JSONObject jsonObject2=jsonObject.getJSONObject("value");
				PropertyIssueClientModel propertyIssueClientModel=new PropertyIssueClientModel(jsonObject2.get("IDApplicationLink").toString()
						, jsonObject2.get("KeyIssueAllS").toString());
				propertyIssueClientModel.setUserNameClient(jsonObject2.getString("UserNameClient"));
				return propertyIssueClientModel;
			}else {
				throw new Exception("\n"+this.getClass()+"Get property issue client fail:"+rs.getEntity());
			}
		} catch (Exception e) {
			throw new Exception("\n"+this.getClass()+"Get property issue client fail:"+e.getMessage());
		}
	}
	public PropertyIssueAllSModel getPropertyIssueAllS(String keyIssue) throws Exception {
		String apiGetPropertyIssueAllS=this.getUrl()+"/rest/api/2/issue/"+keyIssue+"/properties/AllS";
		try {
			Response rs=this.invokeGetMethod(apiGetPropertyIssueAllS);
			log.warn(rs.getEntity());
			if (rs.getStatus()==200) {
				JSONObject jsonObject=new JSONObject(rs.getEntity().toString());
				JSONObject jsonObject2=jsonObject.getJSONObject("value");
				PropertyIssueAllSModel propertyIssueAllSModel=new PropertyIssueAllSModel(
						jsonObject2.getString("IDApplicationLink"),
						jsonObject2.getLong("IDTransitionResolvedAllS"),
						jsonObject2.get("StatusName").toString(),
						jsonObject2.get("IssueKeyClient").toString());
				propertyIssueAllSModel.setUserNameAllS(jsonObject2.getString("UserNameAllS"));
				return propertyIssueAllSModel;
			}else {
				throw new Exception("\n"+this.getClass()+"Get property issue alls fail:"+rs.getEntity());
			}
		} catch (Exception e) {
			throw new Exception("\n"+this.getClass()+"Get property issue alls fail:"+e.getMessage());
		}
	}
	public String getStatusNameClient(String keyIssueClient) throws Exception {
		try {
			Response rs=this.getApplicationLinkBUL().invokeGetMethod("/rest/api/2/issue/"+keyIssueClient);
			if (rs.getStatus()==200) {
				JSONObject jsonObject=new JSONObject(rs.getEntity().toString());
				JSONObject jsonField=jsonObject.getJSONObject("fields");
				JSONObject jsonStatus=jsonField.getJSONObject("status");
				return jsonStatus.get("name").toString();
			}else {
				throw new Exception("\n"+this.getClass()+"Get status name client fail:"+rs.getEntity());
			}
		} catch (Exception e) {
			throw new Exception("\n"+this.getClass()+"Get status name client fail:"+e.getMessage());
		}
	}
	public Response updateTransitionIssueClient(String idTransitionResolvedClient,String keyIssueClient) throws Exception {
		try {
			String bodyChangeStatus = "{ \"transition\": { \"id\": \"" + idTransitionResolvedClient + "\" }}";
			Response rs=this.getApplicationLinkBUL().invokePostMethod("/rest/api/2/issue/" + keyIssueClient + "/transitions", bodyChangeStatus);
			if (rs.getStatus()==200) {
				return rs;
			}else {
				throw new Exception("\n"+this.getClass()+"Update status client fail:"+rs.getEntity());
			}
		} catch (Exception e) {
			throw new Exception("\n"+this.getClass()+"Update status client fail:"+e.getMessage());
		}
	}

	public Response updateResolution(String issueKeyClient, String nameResolution) throws Exception {
		try {
			String apiUpdateResolutionClient="/rest/api/2/issue/"+issueKeyClient;
			String bodyRequest="{\"fields\": {\"resolution\": {\"name\": \""+nameResolution+"\"}}}";
			return this.getApplicationLinkBUL().invokePutMethod(apiUpdateResolutionClient, bodyRequest);
		} catch (Exception e) {
			throw new Exception("\n"+this.getClass()+" updateResolution fail:"+e.getMessage());
		}
		
	}
}
