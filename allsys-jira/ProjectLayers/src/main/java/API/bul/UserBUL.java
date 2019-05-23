package API.bul;

import javax.ws.rs.core.Response;

import com.sun.jersey.core.util.Base64;

import API.extension.Extension;
import API.extension.RequestExtension;
import API.model.UserModel;

public class UserBUL extends JiraBUL {

	public UserBUL(String cookieAuth, String url) {
		super(cookieAuth, url);
		// TODO Auto-generated constructor stub
	}

	public UserBUL(String userName, String passWord, String url) {
		super(userName, passWord, url);
		// TODO Auto-generated constructor stub
	}

	public UserModel getUser() {
		
		String apiGetUser=this.getUrl()+"/rest/auth/1/session";
		Response rs=this.invokeGetMethod(apiGetUser);

		UserModel userModel=new UserModel();
		userModel.setUserName(Extension.getInstance().getValueJsonObjectByKey(rs.getEntity().toString(), "name"));
		
		return userModel;
	}
}
