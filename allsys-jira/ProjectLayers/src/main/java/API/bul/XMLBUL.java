package API.bul;


import java.io.InputStream;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import API.extension.Extension;


public class XMLBUL {
	private Document document;
	public XMLBUL(String fileName) throws Exception {
		document=Extension.getInstance().readFileXml(fileName);
	}
	public XMLBUL(InputStream inputStream) throws Exception {
		document=Extension.getInstance().readFileXmlDeploy(inputStream);
	}
	public boolean isRead() {
		if (document==null) {
			return false;
		}
		return true;
	}
	public NamedNodeMap getNamedNodeMap(String nodeName) {
		return getNode(nodeName).getAttributes();
	}
	public NodeList getNodeList(String nodeName) {
		return document.getElementsByTagName(nodeName);
	}
	public Node getNode(String nodeName) {
		return getNodeList(nodeName).item(0);
	}
	public String getValueNode(String nodeName,String key) {
		return getNamedNodeMap(nodeName).getNamedItem(key).getNodeValue();
	}
}
