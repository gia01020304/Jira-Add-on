package API.model;
import org.json.JSONObject;

import com.atlassian.jira.user.ApplicationUser;

public class WebHookModel {
	private ApplicationUser applicationUser;
	private JSONObject bodyIssue;
	private String statusName;
	private Long projectId;
	private String nameResolution;

	public String getNameResolution() {
		return nameResolution;
	}

	public void setNameResolution(String nameResolution) {
		this.nameResolution = nameResolution;
	}

	public Long getProjectId() {
		return projectId;
	}

	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}

	public String getIssueTypeId() {
		return issueTypeId;
	}

	public void setIssueTypeId(String issueTypeId) {
		this.issueTypeId = issueTypeId;
	}
	private String issueTypeId;
	public ApplicationUser getApplicationUser() {
		return applicationUser;
	}

	public String getIdStatusBefore() {
		return idStatusBefore;
	}

	public void setIdStatusBefore(String idStatusBefore) {
		this.idStatusBefore = idStatusBefore;
	}

	public String getIdStatusAfter() {
		return idStatusAfter;
	}
	public WebHookModel(JSONObject bodyIssue) {
		this.bodyIssue=bodyIssue;
	}
	public WebHookModel(ApplicationUser applicationUser, JSONObject bodyIssue) {
		super();
		this.applicationUser = applicationUser;
		this.bodyIssue = bodyIssue;
	}

	public void setIdStatusAfter(String idStatusAfter) {
		this.idStatusAfter = idStatusAfter;
	}

	public void setApplicationUser(ApplicationUser applicationUser) {
		this.applicationUser = applicationUser;
	}


	public JSONObject getBodyIssue() {
		return bodyIssue;
	}
	public void setBodyIssue(JSONObject bodyIssue) {
		this.bodyIssue = bodyIssue;
	}
	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}
	private String idStatusBefore;
	private String idStatusAfter;
}
