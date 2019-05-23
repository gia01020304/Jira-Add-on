package API.model;

import API.bul.InsightBUL;

public class FieldModel extends JiraObject{
	
	public FieldModel(String idAllSystem, String idClient, String name, boolean isInsight, Object value) {
		super(idAllSystem, idClient, name);
		this.isInsight = isInsight;
		this.value = value;
	}

	private boolean isInsight;
	private Object value;

	public boolean isInsight() {
		return isInsight;
	}

	public void setInsight(boolean isInsight) {
		this.isInsight = isInsight;
	}

	public Object getValue() {
		if (this.value!=null) {
			if (this.value.toString().equals("\"null\"")) {
				return null;
			}else {
				return value;
			}
		}
		return value;
	}

	public void setValue(Object value) {
		if (value!=null && !value.equals("null")) {
			this.value = value.toString().replaceAll("\\r", "").replaceAll("\\n", "");
		}
	}
	
	public String getBodyCreate() {
		if (!isInsight) {
			return "\""+this.getIdAllSystem()+"\":"+this.getValue();
		}else {
			return "\""+this.getIdAllSystem()+"\" : [{\"key\" : \""+this.getValue()+"\"}]";
		}
	}
}
