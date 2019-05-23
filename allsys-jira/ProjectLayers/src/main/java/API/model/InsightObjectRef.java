package API.model;

public class InsightObjectRef {
	private String idAttrClient;
	private ObjectTypeConfig objectTypeConfigRef;
	public String getIdAttrClient() {
		return idAttrClient;
	}
	public void setIdAttrClient(String idAttrClient) {
		this.idAttrClient = idAttrClient;
	}
	public ObjectTypeConfig getObjectTypeConfigRef() {
		return objectTypeConfigRef;
	}
	public void setObjectTypeConfigRef(ObjectTypeConfig objectTypeConfigRef) {
		this.objectTypeConfigRef = objectTypeConfigRef;
	}
	public InsightObjectRef(String idAttrClient, ObjectTypeConfig objectTypeConfigRef) {
		super();
		this.idAttrClient = idAttrClient;
		this.objectTypeConfigRef = objectTypeConfigRef;
	}
}
