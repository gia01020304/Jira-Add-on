package API.rest;

import javax.xml.bind.annotation.*;
@XmlRootElement(name = "message")
@XmlAccessorType(XmlAccessType.FIELD)
public class PrimasModel {

    @XmlElement(name = "value")
    private String message;

    @XmlElement(name = "key")
    private String key;
    
    public PrimasModel() {
    }
    
    public PrimasModel(String message) {
        this.message = message;
    }
    
    public PrimasModel(String key,String message) {
		super();
		this.message = message;
		this.key = key;
	}

	public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
}