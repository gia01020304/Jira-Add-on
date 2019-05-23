package API.bul;

import java.util.List;
import java.util.Map;

import org.jfree.util.Log;
import org.json.JSONObject;

import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.sal.api.net.TrustedRequestFactory;
import com.google.gson.JsonObject;

import API.extension.Extension;
import API.model.ConfigModel;
import API.model.FieldModel;
import API.model.IssueModel;
import API.model.ObjectTypeConfig;
import API.model.WebHookModel;

public class WebHookBUL {

	private JSONObject jsonBody;

	public WebHookBUL(String bodyRequest) {
		this.jsonBody = new JSONObject(bodyRequest);
	}

	public WebHookModel getWebHook() throws Exception {
		try {
			JSONObject jsonIssue = jsonBody.getJSONObject("issue");
			JSONObject jsonField = jsonIssue.getJSONObject("fields");
			WebHookModel webHookModel = new WebHookModel(getCurrentUser(), jsonIssue);
			if (jsonField.has("transition") && !jsonField.isNull("transition")) {
				webHookModel.setStatusName(getStatusName(jsonBody.getJSONObject("transition")));
			}
			webHookModel.setProjectId(getProjectId(jsonField));
			webHookModel.setIssueTypeId(getIssueTypeId(jsonField));
			if (jsonField.has("resolution") && !jsonField.isNull("resolution")) {
				webHookModel.setNameResolution(jsonField.getJSONObject("resolution").getString("name"));
			}
			return webHookModel;
		} catch (Exception e) {
			throw new Exception("\n" + this.getClass() + "Webhook excute body fail:" + e.getMessage());
		}
	}

	private String getIssueTypeId(JSONObject jsonField) {
		return jsonField.getJSONObject("issuetype").get("id").toString();
	}

	private Long getProjectId(JSONObject jsonField) {
		return jsonField.getJSONObject("project").getLong("id");
	}

	private String getStatusName(JSONObject jsonObject) {
		return jsonObject.getString("to_status");
	}

	private ApplicationUser getCurrentUser() throws Exception {
		try {
			JSONObject jsonUser = jsonBody.getJSONObject("user");
			return ComponentAccessor.getUserManager().getUserByName(jsonUser.get("name").toString());
		} catch (Exception e) {
			throw new Exception("\n" + this.getClass() + " getCurrentUser fail:" + e.getMessage());
		}
	}

}
