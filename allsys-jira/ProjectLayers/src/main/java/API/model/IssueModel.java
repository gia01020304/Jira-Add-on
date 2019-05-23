package API.model;

import java.util.List;

public class IssueModel {

	public IssueModel() {

	}

	public IssueModel(String issueKey, String issueTypeIdClient, String issueTypeIdAllSystem,
			List<FieldModel> fieldModels) {
		super();
		this.issueKey = issueKey;
		this.issueTypeIdClient = issueTypeIdClient;
		this.issueTypeIdAllSystem = issueTypeIdAllSystem;
		this.fieldModels = fieldModels;
	}
	private String projectKey;
	private String issueKey;
	private String issueTypeIdClient;
	private String issueTypeIdAllSystem;
	private List<FieldModel> fieldModels;
	private String userNameAllSystem;
	private String passWordAllSystem;
	
	
	public String getUserNameAllSystem() {
		return userNameAllSystem;
	}

	public void setUserNameAllSystem(String userNameAllSystem) {
		this.userNameAllSystem = userNameAllSystem;
	}

	public String getPassWordAllSystem() {
		return passWordAllSystem;
	}

	public void setPassWordAllSystem(String passWordAllSystem) {
		this.passWordAllSystem = passWordAllSystem;
	}

	public String getIssueKey() {
		return issueKey;
	}

	public void setIssueKey(String issueKey) {
		this.issueKey = issueKey;
	}

	public List<FieldModel> getFieldModels() {
		return fieldModels;
	}

	public void setFieldModels(List<FieldModel> fieldModels) {
		this.fieldModels = fieldModels;
	}

	public String getIssueTypeIdClient() {
		return issueTypeIdClient;
	}

	public void setIssueTypeIdClient(String issueTypeIdClient) {
		this.issueTypeIdClient = issueTypeIdClient;
	}

	public String getIssueTypeIdAllSystem() {
		return issueTypeIdAllSystem;
	}

	public void setIssueTypeIdAllSystem(String issueTypeIdAllSystem) {
		this.issueTypeIdAllSystem = issueTypeIdAllSystem;
	}

	public String getBodyFieldCreateIssue() {
		String temp = "";
		if (fieldModels != null && fieldModels.size() > 0) {
			for (FieldModel fieldModel : fieldModels) {
				if (fieldModel.getValue()!=null) {
					temp+=","+fieldModel.getBodyCreate();
				}
			}
		}
		return temp;

	}

	public String getProjectKey() {
		return projectKey;
	}

	public void setProjectKey(String projectKey) {
		this.projectKey = projectKey;
	}
}
