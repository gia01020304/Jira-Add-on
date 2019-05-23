package API.model;

import java.util.List;

public class ObjectSchemaConfig extends JiraObject {
	public ObjectSchemaConfig(String idAllSystem, String idClient, String name) {
		super(idAllSystem, idClient, name);
		// TODO Auto-generated constructor stub
	}
	public ObjectSchemaConfig() {
		
	}

	private List<ObjectTypeConfig>objectTypes;
	public ObjectTypeConfig getObjectTypeConfigByIdClient(String idClient) {
		if (objectTypes!=null && objectTypes.size()>0) {
			for (ObjectTypeConfig objectTypeConfig : objectTypes) {
				if (objectTypeConfig.getIdClient().equals(idClient)) {
					return  objectTypeConfig;
				}
			}
		}
		return null;
	}
	public List<ObjectTypeConfig> getObjectTypes() {
		return objectTypes;
	}
	public void setObjectTypes(List<ObjectTypeConfig> objectTypes) {
		this.objectTypes = objectTypes;
	}
}
