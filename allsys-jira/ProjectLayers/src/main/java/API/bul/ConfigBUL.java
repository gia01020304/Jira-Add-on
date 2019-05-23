package API.bul;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.jfree.util.Log;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import API.extension.Extension;
import API.model.AttributeConfig;
import API.model.ConfigModel;
import API.model.FieldModel;
import API.model.IssueTypeConfig;
import API.model.JiraObject;
import API.model.ObjectSchemaConfig;
import API.model.ObjectTypeConfig;
import API.model.TransitionModel;

public class ConfigBUL {
	private XMLBUL xmlBul;
	private static final Logger log = Logger.getLogger(ConfigBUL.class);

	public ConfigBUL(String fileName) throws Exception {
		this.xmlBul = new XMLBUL(fileName);
		if (!xmlBul.isRead()) {
			xmlBul = null;
		}
	}

	public ConfigBUL(InputStream inputStream) throws Exception {
		this.xmlBul = new XMLBUL(inputStream);
		if (!xmlBul.isRead()) {
			xmlBul = null;
		}
	}

	public ConfigModel getConfigModel(List<Integer> listIdTransition) throws Exception {
		if (xmlBul != null) {
			try {
				ConfigModel configModel = new ConfigModel();
				
				configModel.setIdApplicationLinkClient(xmlBul.getValueNode("applicationlink-allsystem", "id-client"));
				configModel.setIdApplicationLinkAllS(xmlBul.getValueNode("applicationlink-allsystem", "id-allsystem"));
				
				configModel.setUserNameAllS(xmlBul.getValueNode("account-accepted", "username-allsystem"));
				configModel.setUserNameClient(xmlBul.getValueNode("account-accepted", "username-client"));

				configModel.setFieldModels(getListField(xmlBul.getNodeList("field")));
				configModel.setIssueTypeConfigs(getIssueTypeConfigs(xmlBul.getNodeList("IssueType")));
				configModel.setObjectSchemaConfig(getObjectSchema());
				configModel.setKeyProjectAllS(xmlBul.getValueNode("Project", "key-allsystem"));

				TransitionModel transitionModel = getTransitionModel(listIdTransition,
						xmlBul.getNodeList("Transition"));
				if (transitionModel == null) {
					throw new Exception("\n" + this.getClass() + " Can't get id transition");
				}
				configModel.setIdTransitionRollBack(transitionModel.getIdTransitionRollBack());
				configModel.setIdTransitionResolvedByAllS(transitionModel.getIdTransitionResolvedByAllS());
				return configModel;
			} catch (Exception e) {
				throw new Exception("\n" + this.getClass() + " Get Config fail:" + e.getMessage());
			}
		}
		return null;
	}

	public ConfigModel getConfigModel() throws Exception {
		if (xmlBul != null) {
			try {
				ConfigModel configModel = new ConfigModel();
				configModel.setIdApplicationLinkClient(xmlBul.getValueNode("applicationlink-allsystem", "id-client"));
				configModel.setIdApplicationLinkAllS(xmlBul.getValueNode("applicationlink-allsystem", "id-allsystem"));
				configModel.setFieldModels(getListField(xmlBul.getNodeList("field")));
				configModel.setIssueTypeConfigs(getIssueTypeConfigs(xmlBul.getNodeList("IssueType")));
				configModel.setObjectSchemaConfig(getObjectSchema());
				// configModel.setIdStatusRefuse(xmlBul.getValueNode("status-refuse",
				// "id-client"));
				return configModel;
			} catch (Exception e) {
				throw new Exception("\n" + this.getClass() + " Get Config fail:" + e.getMessage());
			}
		}
		return null;
	}

	private TransitionModel getTransitionModel(List<Integer> listIdTransition, NodeList nodeList) {
		if (nodeList != null && nodeList.getLength() > 0) {
			for (int i = 0; i < nodeList.getLength(); i++) {
				NamedNodeMap n = nodeList.item(i).getAttributes();
				if (n != null) {
					int idTransitionRollBack = Integer.parseInt(n.getNamedItem("id-rollback").getNodeValue());
					int idResolvedAllS = Integer.parseInt(n.getNamedItem("id-resolved-allsystem").getNodeValue());
					if (listIdTransition.contains(idResolvedAllS) && listIdTransition.contains(idTransitionRollBack)) {
						return new TransitionModel(idTransitionRollBack + "", idResolvedAllS + "");
					}
				}
			}

		}
		return null;
	}

	private List<IssueTypeConfig> getIssueTypeConfigs(NodeList nodeList) {
		if (nodeList != null && nodeList.getLength() > 0) {
			List<IssueTypeConfig> issueTypeConfigs = new ArrayList<>();
			for (int i = 0; i < nodeList.getLength(); i++) {

				NamedNodeMap n = nodeList.item(i).getAttributes();
				if (n != null) {
					IssueTypeConfig issueTypeConfig = new IssueTypeConfig();
					issueTypeConfig.setAttributeBacis(getJiraObject(n));
					issueTypeConfigs.add(issueTypeConfig);
				}
			}
			return issueTypeConfigs;
		}
		return null;
	}

	private ObjectSchemaConfig getObjectSchema() {
		Node node = xmlBul.getNode("ObjectSchema");
		ObjectSchemaConfig objectSchemaConfig = new ObjectSchemaConfig();
		objectSchemaConfig.setAttributeBacis(getJiraObject(node.getAttributes()));
		objectSchemaConfig.setObjectTypes(getObjectTypeConfigs(node.getChildNodes()));
		return objectSchemaConfig;
	}

	private List<AttributeConfig> getAttributeConfigs(NodeList nodeList) {
		if (nodeList != null && nodeList.getLength() > 0) {
			List<AttributeConfig> attributeConfigs = new ArrayList<>();
			for (int i = 0; i < nodeList.getLength(); i++) {

				NamedNodeMap n = nodeList.item(i).getAttributes();
				if (n != null) {
					AttributeConfig attributeConfig = new AttributeConfig();
					attributeConfig.setAttributeBacis(getJiraObject(n));
					attributeConfig.setIQL(n.getNamedItem("iql")==null?false:true);
					attributeConfigs.add(attributeConfig);
				}

			}
			return attributeConfigs;
		}
		return null;
	}

	private List<ObjectTypeConfig> getObjectTypeConfigs(NodeList nodeList) {
		if (nodeList != null && nodeList.getLength() > 0) {
			
			List<ObjectTypeConfig> objectTypeConfigs = new ArrayList<>();
			for (int i = 0; i < nodeList.getLength(); i++) {
				
				Node node = nodeList.item(i);
				NamedNodeMap n = node.getAttributes();
				
				if (n != null) {
					ObjectTypeConfig objectTypeConfig = new ObjectTypeConfig();
					objectTypeConfig.setAttributeBacis(getJiraObject(n));
					objectTypeConfig.setAttributeConfigs(getAttributeConfigs(node.getChildNodes()));
					objectTypeConfigs.add(objectTypeConfig);
				}
			}
			return objectTypeConfigs;
		}
		return null;
	}

	private List<FieldModel> getListField(NodeList nodeList) {
		if (nodeList != null && nodeList.getLength() > 0) {
			List<FieldModel> fieldModels = new ArrayList<>();
			for (int i = 0; i < nodeList.getLength(); i++) {
				NamedNodeMap n = nodeList.item(i).getAttributes();
				if (n != null) {
					fieldModels.add(new FieldModel(
							Extension.getInstance().progressIdField(n.getNamedItem("id-allsystem").getNodeValue()),
							Extension.getInstance().progressIdField(n.getNamedItem("id-client").getNodeValue()),
							n.getNamedItem("name").getNodeValue(), n.getNamedItem("insight") == null ? false : true,
							null));
				}
			}
			return fieldModels;
		}
		return null;
	}

	private JiraObject getJiraObject(NamedNodeMap n) {
		return new JiraObject(n.getNamedItem("id-allsystem").getNodeValue(), n.getNamedItem("id-client").getNodeValue(),
				n.getNamedItem("name").getNodeValue());
	}

}
