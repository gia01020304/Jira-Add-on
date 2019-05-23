package API.model;

import java.util.List;

public class ConfigModel {
	private String domainAllSystem;
	private ObjectSchemaConfig objectSchemaConfig;
	private List<FieldModel>fieldModels;
	private List<IssueTypeConfig>issueTypeConfigs;
	private String idApplicationLinkClient;
	private String idApplicationLinkAllS;
	private String keyProjectAllS;
	private String idTransitionRollBack;
	private String idTransitionResolvedByAllS;
	private String userNameClient;
	private String userNameAllS;
	public String getUserNameAllS() {
		return userNameAllS;
	}
	public void setUserNameAllS(String userNameAllS) {
		this.userNameAllS = userNameAllS;
	}
	public String getKeyProjectAllS() {
		return keyProjectAllS;
	}
	public void setKeyProjectAllS(String keyProjectAllS) {
		this.keyProjectAllS = keyProjectAllS;
	}
	public String getIdTransitionRollBack() {
		return idTransitionRollBack;
	}
	public void setIdTransitionRollBack(String idTransitionRollBack) {
		this.idTransitionRollBack = idTransitionRollBack;
	}
	public String getIdTransitionResolvedByAllS() {
		return idTransitionResolvedByAllS;
	}
	public void setIdTransitionResolvedByAllS(String idTransitionResolvedByAllS) {
		this.idTransitionResolvedByAllS = idTransitionResolvedByAllS;
	}
	public String getIdApplicationLinkClient() {
		return idApplicationLinkClient;
	}
	public void setIdApplicationLinkClient(String idApplicationLinkClient) {
		this.idApplicationLinkClient = idApplicationLinkClient;
	}
	public String getIdApplicationLinkAllS() {
		return idApplicationLinkAllS;
	}
	public void setIdApplicationLinkAllS(String idApplicationLinkAllS) {
		this.idApplicationLinkAllS = idApplicationLinkAllS;
	}
	public String getUserNameClient() {
		return userNameClient;
	}
	public void setUserNameClient(String userNameClient) {
		this.userNameClient = userNameClient;
	}

	public String getDomainAllSystem() {
		return domainAllSystem;
	}
	public void setDomainAllSystem(String domainAllSystem) {
		this.domainAllSystem = domainAllSystem;
	}
	
	public String getIdIssueTypeAllSystem(String issueTypeIdClient) {
		for (IssueTypeConfig issueTypeConfig : issueTypeConfigs) {
			if (issueTypeConfig.getIdClient().equals(issueTypeIdClient)) {
				return issueTypeConfig.getIdAllSystem();
			}
		}
		return null;
	}
	
	public List<FieldModel> getFieldModels() {
		return fieldModels;
	}
	public void setFieldModels(List<FieldModel> fieldModels) {
		this.fieldModels = fieldModels;
	}
	public ObjectSchemaConfig getObjectSchemaConfig() {
		return objectSchemaConfig;
	}
	public void setObjectSchemaConfig(ObjectSchemaConfig objectSchemaConfig) {
		this.objectSchemaConfig = objectSchemaConfig;
	}
	public List<IssueTypeConfig> getIssueTypeConfigs() {
		return issueTypeConfigs;
	}
	public void setIssueTypeConfigs(List<IssueTypeConfig> issueTypeConfigs) {
		this.issueTypeConfigs = issueTypeConfigs;
	}



}
