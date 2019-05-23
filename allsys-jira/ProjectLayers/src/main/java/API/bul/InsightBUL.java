package API.bul;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.atlassian.sal.api.net.TrustedRequestFactory;

import API.model.ConfigModel;
import API.model.InsightObjectRef;
import API.model.ObjectTypeConfig;

public class InsightBUL extends JiraBUL {

	private static final Logger log = Logger.getLogger(InsightBUL.class);

	public InsightBUL(String userName, String passWord, String url) {
		super(userName, passWord, url);
		// TODO Auto-generated constructor stub
	}

	public InsightBUL(String cookieAuth, String url) {
		super(cookieAuth, url);
		// TODO Auto-generated constructor stub
	}

	public InsightBUL(TrustedRequestFactory fRequestFactory, String url) {
		super(fRequestFactory, url);
	}

//	public Response testGetInsight(String api) {
//		return this.invokeGetMethod(api);
//	}

	public Map<String, String> getObjectInsightAllSystem(ObjectTypeConfig objectInsightClient,
			ConfigModel configModel) throws Exception {	
		try {
			List<InsightObjectRef> objectRef = objectInsightClient.getObjectTypeConfigsReference();
			if (objectRef != null && objectRef.size() > 0) {
				
				for (InsightObjectRef insightObjectRef : objectRef) {
					
					Map<String, String> rs = getObjectInsightAllSystem(insightObjectRef.getObjectTypeConfigRef(),
							configModel);
					log.warn(objectInsightClient.getName()+" "+insightObjectRef.getObjectTypeConfigRef().getName()+" "+insightObjectRef.getIdAttrClient());
					if (rs != null && rs.size() > 0) {
						log.warn("rs:"+rs.get("label")+" "+rs.get("objectKey"));
						objectInsightClient.setValueAttrObjectRef(insightObjectRef.getIdAttrClient(), rs);
					}
				}
			}
			String iql = objectInsightClient.getIQL();
			String bodyFindObject = "{" + "\"objectTypeId\": " + "\"" + objectInsightClient.getIdAllSystem() + "\","
					+ "\"resultsPerPage\":" + 1 + "," + "\"objectSchemaId\":" + "\""
					+ configModel.getObjectSchemaConfig().getIdAllSystem() + "\"," + "\"iql\":" + "\"" + iql + "\""
					+ "}";
			log.warn("Api find Object insight:"+bodyFindObject);
			String apiFindObjectInsight = "/rest/insight/1.0/object/navlist/iql";

			Response rsFindObject = this.getApplicationLinkBUL().invokePostMethod(apiFindObjectInsight, bodyFindObject);
			log.warn("Result find Object insight:"+rsFindObject.getStatus());
			if (rsFindObject.getStatus() == 200) {
				JSONObject jsonObject = new JSONObject(rsFindObject.getEntity().toString());
				JSONArray arrayObject = jsonObject.getJSONArray("objectEntries");
				if (arrayObject != null && arrayObject.length() > 0) {
					JSONObject object = arrayObject.getJSONObject(0);
					Map<String, String> map = new HashMap<String, String>();
					map.put("objectKey", object.get("objectKey").toString());
					map.put("label", object.get("label").toString());
					return map;
				} else {
					String bodyCreate = "{" + "\"objectTypeId\":" + objectInsightClient.getIdAllSystem() + ","
							+ "\"attributes\":" + objectInsightClient.getBodyAttributeCreate() + "}";
					String apiCreateObjectInsight = "/rest/insight/1.0/object/create";
					// Response rsCreate=this.invokePostMethod(apiCreateObjectInsight,bodyCreate);
					log.warn("api create Object insight:"+bodyCreate);
					Response rsCreate = this.getApplicationLinkBUL().invokePostMethod( apiCreateObjectInsight, bodyCreate);
					log.warn("Result create Object insight:"+rsCreate.getStatus());
					if (rsCreate.getStatus() == 200) {
						JSONObject jsonCreate = new JSONObject(rsCreate.getEntity().toString());
						Map<String, String> map = new HashMap<String, String>();
						map.put("objectKey", jsonCreate.get("objectKey").toString());
						map.put("label", jsonCreate.get("label").toString());
						return map;
					}

				}
			}
		} catch (Exception e) {
			throw new Exception("\n"+this.getClass()+"Get Object insight allsystem fail:"+e.getMessage());
		}

		return null;
	}

	public ObjectTypeConfig getObjectInsightClient(String keyObjectInsightClient, ConfigModel configModel) throws Exception {

		try {
			String apiGetObjectInsight = this.getUrl() + "/rest/insight/1.0/object/" + keyObjectInsightClient;
			Response rs = this.invokeGetMethod(apiGetObjectInsight);

			if (rs.getStatus() == 200) {
				
				JSONObject jsonObject = new JSONObject(rs.getEntity().toString());
				String idObjectTypeClient = jsonObject.getJSONObject("objectType").get("id").toString();
				ObjectTypeConfig objectTypeConfig = (ObjectTypeConfig) configModel.getObjectSchemaConfig()
						.getObjectTypeConfigByIdClient(idObjectTypeClient).clone();
				
				setValueAttrObjectInsight(jsonObject.getJSONArray("attributes"), objectTypeConfig);
				List<Map<String, String>> listIdRef = objectTypeConfig.getListIdReference();
				
				if (listIdRef != null && listIdRef.size() > 0) {
					for (Map<String, String> map : listIdRef) {
						
						String idRef = map.get("idObjectRefClient");
						ObjectTypeConfig objectTypeConfigReference = getObjectInsightClient(idRef, configModel);
						if (objectTypeConfigReference != null) {
							String idAttrClient=map.get("idAttrClient");
							objectTypeConfig.setObjectRefe(idAttrClient, objectTypeConfigReference);
						}
					}
				}
				return objectTypeConfig;
			}
		} catch (Exception e) {
			throw new Exception("\n"+this.getClass()+"Get object insight client fail:"+e.getMessage());
		}
		return null;
	}

	private void setValueAttrObjectInsight(JSONArray attrs, ObjectTypeConfig objectTypeConfig) {
		for (int i = 0; i < attrs.length(); i++) {
			JSONObject jsonObject = attrs.getJSONObject(i);
			String idAttrClient = jsonObject.get("objectTypeAttributeId").toString();
			JSONArray jsonArray = jsonObject.getJSONArray("objectAttributeValues");
			if (jsonArray.length() > 0) {
				objectTypeConfig.setValueAttrObject(idAttrClient, jsonArray.getJSONObject(0));
			}
		}
	}
}
