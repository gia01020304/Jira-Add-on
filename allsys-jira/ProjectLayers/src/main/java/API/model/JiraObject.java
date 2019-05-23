package API.model;

public class JiraObject {
	
	public JiraObject(String idAllSystem,String idClient, String name) {
		super();
		this.idClient = idClient;
		this.idAllSystem = idAllSystem;
		this.name = name;
	}
	public JiraObject() {
		super();
	}
	private String idClient;
	private String idAllSystem;
	private String name;

	

	public String getIdClient() {
		return idClient;
	}

	public void setIdClient(String idClient) {
		this.idClient = idClient;
	}

	public String getIdAllSystem() {
		return idAllSystem;
	}

	public void setIdAllSystem(String idAllSystem) {
		this.idAllSystem = idAllSystem;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setAttributeBacis(JiraObject jiraObject) {
		this.name = jiraObject.getName();
		this.idAllSystem = jiraObject.getIdAllSystem();
		this.idClient = jiraObject.getIdClient();
	}
}
