package API.model;

public class PropertyIssueClientModel {
	private String iDApplicationLink;
	private String keyIssueAllS;
	private String userNameClient;
	
	public String getiDApplicationLink() {
		return iDApplicationLink;
	}

	public String getUserNameClient() {
		return userNameClient;
	}

	public void setUserNameClient(String userNameClient) {
		this.userNameClient = userNameClient;
	}

	public void setiDApplicationLink(String iDApplicationLink) {
		this.iDApplicationLink = iDApplicationLink;
	}



	public PropertyIssueClientModel(String iDApplicationLink, String keyIssueAllS) {
		super();
		this.iDApplicationLink = iDApplicationLink;
		this.keyIssueAllS = keyIssueAllS;
	}

	public String getKeyIssueAllS() {
		return keyIssueAllS;
	}

	public void setKeyIssueAllS(String keyIssueAllS) {
		this.keyIssueAllS = keyIssueAllS;
	}
}
