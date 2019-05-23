package API.model;

public class PropertyIssueAllSModel {
	private String iDApplicationLink;
	private Long iDTransitionResolvedAllS;
	private String statusName;
	private String issueKeyClient;
	private String userNameAllS;
	
	public String getUserNameAllS() {
		return userNameAllS;
	}
	public void setUserNameAllS(String userNameAllS) {
		this.userNameAllS = userNameAllS;
	}
	public PropertyIssueAllSModel(String iDApplicationLink, Long iDTransitionResolvedAllS, String statusName,
			String issueKeyCLient) {
		super();
		this.iDApplicationLink = iDApplicationLink;
		this.iDTransitionResolvedAllS = iDTransitionResolvedAllS;
		this.statusName = statusName;
		this.issueKeyClient = issueKeyCLient;
	}
	public String getiDApplicationLink() {
		return iDApplicationLink;
	}
	public void setiDApplicationLink(String iDApplicationLink) {
		this.iDApplicationLink = iDApplicationLink;
	}
	public Long getiDTransitionResolvedAllS() {
		return iDTransitionResolvedAllS;
	}
	public void setiDTransitionResolvedAllS(Long iDTransitionResolvedAllS) {
		this.iDTransitionResolvedAllS = iDTransitionResolvedAllS;
	}
	public String getStatusName() {
		return statusName;
	}
	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}
	public String getIssueKeyClient() {
		return issueKeyClient;
	}
	public void setIssueKeyClient(String issueKeyClient) {
		this.issueKeyClient = issueKeyClient;
	}
}
