package API.extension;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.json.JSONObject;
import org.w3c.dom.Document;



public class Extension {
	private static Extension instance;
	public static Extension getInstance() {
		if (instance==null) {
			return new Extension();
		}
		return instance;
	}
	public String getValueJsonObjectByKey(String json,String key) {
		return new JSONObject(json).get(key).toString();
	}
	public boolean tryParseInt(String value) {  
	     try {  
	         Integer.parseInt(value);  
	         return true;  
	      } catch (NumberFormatException e) {  
	         return false;  
	      }  
	}
	public Document readFileXmlDeploy(InputStream is) throws Exception {
		Document document = null;
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder;
		try {
			documentBuilder = documentBuilderFactory.newDocumentBuilder();
			document = documentBuilder.parse(is);
		} catch (Exception e) {
			throw new Exception("\n"+this.getClass()+" Read file xml fail:"+e.getMessage());
		}finally {
			try {
				is.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return document;
	}
	
	public Document readFileXml(String fileXml) throws Exception {
		Document document = null;
		File file = new File(fileXml);
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder;
		try {
			documentBuilder = documentBuilderFactory.newDocumentBuilder();
			document = documentBuilder.parse(file);
		} catch (Exception e) {
			throw new Exception("\n"+this.getClass()+" Read file xml fail:"+e.getMessage());
		}
		return document;
	}
	public boolean isJsonValid(String valueJson) {
		try {
	        new JSONObject(valueJson);
	    } catch (Exception ex) {
	    	 return false;
	    }
	    return true;
	}
	private boolean isNumeric(String strNum) {
	    try {
	        double d = Double.parseDouble(strNum);
	    } catch (NumberFormatException | NullPointerException nfe) {
	        return false;
	    }
	    return true;
	}
	public String progressIdField(String idField) {
		if (isNumeric(idField)) {
			return "customfield_"+idField;
		}
		return idField;
	}
}
