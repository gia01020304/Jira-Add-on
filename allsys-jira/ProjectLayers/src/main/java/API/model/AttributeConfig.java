package API.model;

public class AttributeConfig extends JiraObject{

	public AttributeConfig(String idAllSystem, String idClient, String name) {
		super(idAllSystem, idClient, name);
		// TODO Auto-generated constructor stub
	}
	private String value;
	private String valueKey;
	private boolean isReference;
	private boolean isIQL;
	public boolean isIQL() {
		return isIQL;
	}

	public void setIQL(boolean isIQL) {
		this.isIQL = isIQL;
	}

	public String getObjectAttribute() {
		String temp = "";
		if (isReference==false) {
			temp = "{" + "\"objectTypeAttributeId\":" + this.getIdAllSystem() + ","
					+ "\"objectAttributeValues\": [{ \"value\": \"" + getValue() + "\"}]" + "}";
		}else {
			temp = "{" + "\"objectTypeAttributeId\":" + this.getIdAllSystem() + ","
					+ "\"objectAttributeValues\": [{ \"value\": \"" + this.valueKey + "\"}]" + "}";
		}

		return temp;
	}
	
	public String getValueKey() {
		return valueKey;
	}
	public void setValueKey(String valueKey) {
		this.valueKey = valueKey;
	}
	public boolean isReference() {
		return isReference;
	}
	public void setReference(boolean isReference) {
		this.isReference = isReference;
	}
	public AttributeConfig() {
		// TODO Auto-generated constructor stub
	}
	public String getValue() {
		if (this.value!=null) {
			String temp=this.value.replace("\"", "\\\\\\\"");
			return temp;
		}
		return value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}
	
	
	
}
