package API.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import org.json.JSONObject;


@XmlAccessorType(XmlAccessType.FIELD)
public class ObjectTypeConfig extends JiraObject implements Cloneable {
	public ObjectTypeConfig(String idAllSystem, String idClient, String name) {
		super(idAllSystem, idClient, name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public ObjectTypeConfig() {
		// TODO Auto-generated constructor stub
	}

	private List<AttributeConfig> attributeConfigs;
	private String idObjectClient;
	private String idObjectAllSystem;
	private String keyObjectClient;
	private String keyObjectAllSystem;
	private List<InsightObjectRef> objectTypeConfigsReference;
	private List<Map<String,String>>listIdReference;

	

	public List<InsightObjectRef> getObjectTypeConfigsReference() {
		return objectTypeConfigsReference;
	}

	public void setObjectTypeConfigsReference(List<InsightObjectRef> objectTypeConfigsReference) {
		this.objectTypeConfigsReference = objectTypeConfigsReference;
	}

	public void setObjectRefe(String idAttributeClient,ObjectTypeConfig objRef) {
		if (objectTypeConfigsReference==null) {
			objectTypeConfigsReference=new ArrayList<>();
		}
		objectTypeConfigsReference.add(new InsightObjectRef(idAttributeClient, objRef));
	}
	public List<Map<String, String>> getListIdReference() {
		return listIdReference;
	}

	public void setListIdReference(List<Map<String, String>> listIdReference) {
		this.listIdReference = listIdReference;
	}

	public void setValueAttrObject(String idAttrClient, JSONObject jsonObject) {
		if (attributeConfigs!=null && attributeConfigs.size()>0) {
			AttributeConfig attributeConfig=attributeConfigs.stream()
					.filter(x->x.getIdClient().equals(idAttrClient)).findFirst().orElse(null);
			if (attributeConfig!=null) {
				boolean checkReference=jsonObject.has("referencedObject");
				if (checkReference) {
					if (listIdReference==null) {
						listIdReference=new ArrayList<>();
					}
					attributeConfig.setReference(true);
					JSONObject jsonReference=jsonObject.getJSONObject("referencedObject");
					Map<String, String>map=new HashMap<>();
					map.put("idAttrClient",attributeConfig.getIdClient() );
					map.put("idObjectRefClient", jsonReference.get("id").toString());
					listIdReference.add(map);
				}else {
					attributeConfig.setValue(jsonObject.get("value").toString());
				}
			}
		}
		
	}
	
	
	
	public String getIdObjectClient() {
		return idObjectClient;
	}

	public void setIdObjectClient(String idObjectClient) {
		this.idObjectClient = idObjectClient;
	}

	public String getIdObjectAllSystem() {
		return idObjectAllSystem;
	}

	public void setIdObjectAllSystem(String idObjectAllSystem) {
		this.idObjectAllSystem = idObjectAllSystem;
	}

	public String getKeyObjectClient() {
		return keyObjectClient;
	}

	public void setKeyObjectClient(String keyObjectClient) {
		this.keyObjectClient = keyObjectClient;
	}

	public String getKeyObjectAllSystem() {
		return keyObjectAllSystem;
	}

	public void setKeyObjectAllSystem(String keyObjectAllSystem) {
		this.keyObjectAllSystem = keyObjectAllSystem;
	}

	public List<AttributeConfig> getAttributeConfigs() {
		return attributeConfigs;
	}

	public void setAttributeConfigs(List<AttributeConfig> attributeConfigs) {
		this.attributeConfigs = attributeConfigs;
	}

	public String getIQL() {
		String temp="";
		if (this.attributeConfigs!=null && this.attributeConfigs.size()>0 ) {
			for (AttributeConfig attributeConfig : attributeConfigs) {
				if (attributeConfig.getValue()!=null && attributeConfig.isIQL()) {
					if (temp.isEmpty()) {
						temp+="\\\""+attributeConfig.getName()+"\\\""+"="+"\\\""+attributeConfig.getValue()+"\\\"";
					}else {
						temp+=" And "+"\\\""+attributeConfig.getName()+"\\\""+"="+"\\\""+attributeConfig.getValue()+"\\\"";
					}
				}
			}
			
		}
		return temp;
	}

	public void setValueAttrObjectRef(String idAttrClient, Map<String, String> rs) {
		if (attributeConfigs!=null && attributeConfigs.size()>0) {
			AttributeConfig attributeConfig=attributeConfigs.stream()
					.filter(x->x.getIdClient().equals(idAttrClient)).findFirst().orElse(null);
			if (attributeConfig!=null) {
				attributeConfig.setValue(rs.get("label"));
				attributeConfig.setValueKey(rs.get("objectKey"));
			}
		}
		
	}

	public String getBodyAttributeCreate() {
		String listAtr="";
		if (this.attributeConfigs!=null && this.attributeConfigs.size()>0 ) {
			for (AttributeConfig attributeConfig : attributeConfigs) {
				if (attributeConfig.getValue()!=null) {
					if (listAtr.isEmpty()) {
						listAtr+=attributeConfig.getObjectAttribute();
					}else {
						listAtr+=","+attributeConfig.getObjectAttribute();
					}
				}
			}
		}
		String temp="["+listAtr+"]";
		return temp;
	}


}
