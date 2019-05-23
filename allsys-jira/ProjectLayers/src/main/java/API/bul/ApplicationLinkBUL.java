package API.bul;

import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.jfree.util.Log;

import com.atlassian.applinks.api.ApplicationId;
import com.atlassian.applinks.api.ApplicationLink;
import com.atlassian.applinks.api.ApplicationLinkRequest;
import com.atlassian.applinks.api.ApplicationLinkRequestFactory;
import com.atlassian.applinks.api.ApplicationLinkResponseHandler;
import com.atlassian.applinks.api.ApplicationLinkService;
import com.atlassian.applinks.api.CredentialsRequiredException;
import com.atlassian.applinks.api.TypeNotInstalledException;
import com.atlassian.sal.api.net.ResponseException;
import com.atlassian.sal.api.net.Request.MethodType;
import com.sun.jersey.core.util.Base64;

import API.extension.RequestExtension;
import API.extension.TypeAuthen;

public class ApplicationLinkBUL {
	private String cookieAuth;
	private String accountAuth;
	private String url;
	private String userName;
	private String passWord;
	private TypeAuthen typeAuthen;
	private ApplicationLinkRequestFactory requestFactory;
	private String host;
	private static final Logger log = Logger.getLogger(ApplicationLinkBUL.class);
	public ApplicationLinkBUL(String cookieAuth, String url) {
		super();
		this.setCookieAuth(cookieAuth);
		this.setUrl(url);
		this.typeAuthen = TypeAuthen.Cookie;
	}

	public ApplicationLinkBUL(String userName, String passWord, String url) {
		super();
		this.userName = userName;
		this.passWord = passWord;
		this.setAccountAuth(new String(Base64.encode(userName + ":" + passWord)));
		this.setUrl(url);
		this.typeAuthen = TypeAuthen.Account;
	}

	public ApplicationLinkBUL(ApplicationLinkService applicationLinkService, String idApplicationLink)
			throws Exception {
		try {
			ApplicationLink application = applicationLinkService
					.getApplicationLink(new ApplicationId(idApplicationLink));
			this.requestFactory = application.createAuthenticatedRequestFactory();
			this.typeAuthen = TypeAuthen.ApplicationLink;
		} catch (TypeNotInstalledException e) {
			throw new Exception("\n"+this.getClass()+" setRequestFactory fail:" + e.getMessage());
		}
	}

	public Response invokePutMethod(String api, String bodyRequest) throws Exception {
		try {
			switch (this.typeAuthen) {
			case Account:
				return RequestExtension.getInstance().invokePutMethod(this.getAccountAuth(),this.getUrl()+ api, bodyRequest);
			case Cookie:
				return RequestExtension.getInstance().invokePutMethodCookieAuth(this.getCookieAuth(),this.getUrl()+ api, bodyRequest);
			case ApplicationLink:
				return this.invokePutMethodApplication(api, bodyRequest);
			}
		} catch (Exception e) {
			log.warn("\n"+this.getClass()+"PUT method applicaton link fail:" + e.getMessage());
		}
		return null;
	}

	public Response invokeGetMethod(String api) throws Exception {
		try {
			switch (this.typeAuthen) {
			case Cookie:
				return RequestExtension.getInstance().invokeGetMethodCookieAuth(this.getCookieAuth(),
						this.getUrl() + api);
			case Account:
				return RequestExtension.getInstance().invokeGetMethod(this.getAccountAuth(), this.getUrl() + api);
			case ApplicationLink:
				return this.invokeGetMethodApplication(api);
			}
		} catch (Exception e) {
			log.warn("\n"+this.getClass()+"Get method applicaton link fail:" + e.getMessage());
		}
		return null;
	}

	public Response invokePostMethod(String api, String bodyRequest) throws Exception {
		try {
			switch (this.typeAuthen) {
			case Cookie:
				return RequestExtension.getInstance().invokeGetMethodCookieAuth(this.getUrl() + api, bodyRequest);
			case Account:
				return RequestExtension.getInstance().invokePostMethod(this.getAccountAuth(), this.getUrl() + api,
						bodyRequest);
			case ApplicationLink:
				return this.invokePostMethodApplication(api, bodyRequest);
			}
		} catch (Exception e) {
			log.warn("\n"+this.getClass()+" Post method applicaton link fail:" + e.getMessage());
		}
		return null;
	}

	private Response invokePutMethodApplication(String api, String body) throws Exception {
		if (this.requestFactory != null) {
			com.atlassian.sal.api.net.Response res = null;
			ApplicationLinkRequest request = null;
			try {

				request = requestFactory.createRequest(MethodType.PUT, api);
				request.setRequestBody(body);
				request.setHeader("Content-Type", "application/json");
				res = request.execute(new ApplicationLinkResponseHandler<com.atlassian.sal.api.net.Response>() {

					@Override
					public com.atlassian.sal.api.net.Response handle(com.atlassian.sal.api.net.Response response)
							throws ResponseException {
						return response;
					}

					@Override
					public com.atlassian.sal.api.net.Response credentialsRequired(
							com.atlassian.sal.api.net.Response response) throws ResponseException {
						return response;
					}

				});
				if (res.isSuccessful()) {
					return Response.ok(res.getEntity(String.class)).build();
				} else {
					return Response.status(res.getStatusCode()).entity(res.getEntity(String.class)).build();
				}
			} catch (Exception e) {
				log.warn("\n"+this.getClass()+"Request AllSystem fail:" + e.getMessage());
			}
		}
		return null;
	}

	private Response invokePostMethodApplication(String api, String body) throws Exception {
		if (this.requestFactory != null) {
			com.atlassian.sal.api.net.Response res = null;
			ApplicationLinkRequest request = null;
			try {

				request = requestFactory.createRequest(MethodType.POST, api);
				request.setRequestBody(body);
				request.setHeader("Content-Type", "application/json");
				res = request.execute(new ApplicationLinkResponseHandler<com.atlassian.sal.api.net.Response>() {

					@Override
					public com.atlassian.sal.api.net.Response handle(com.atlassian.sal.api.net.Response response)
							throws ResponseException {
						return response;
					}

					@Override
					public com.atlassian.sal.api.net.Response credentialsRequired(
							com.atlassian.sal.api.net.Response response) throws ResponseException {
						return response;
					}

				});
				if (res.isSuccessful()) {
					return Response.ok(res.getEntity(String.class)).build();
				} else {
					return Response.status(res.getStatusCode()).entity(res.getEntity(String.class)).build();
				}
			} catch (Exception e) {
				log.warn("\n"+this.getClass()+" Request AllSystem fail:" + e.getMessage());
			}
		}
		return null;
	}

	private Response invokeGetMethodApplication(String api) throws Exception {
		if (this.requestFactory != null) {
			com.atlassian.sal.api.net.Response res = null;
			ApplicationLinkRequest request = null;
			try {

				request = requestFactory.createRequest(MethodType.GET, api);
				res = request.execute(new ApplicationLinkResponseHandler<com.atlassian.sal.api.net.Response>() {

					@Override
					public com.atlassian.sal.api.net.Response handle(com.atlassian.sal.api.net.Response response)
							throws ResponseException {
						return response;
					}

					@Override
					public com.atlassian.sal.api.net.Response credentialsRequired(
							com.atlassian.sal.api.net.Response response) throws ResponseException {
						return response;
					}

				});
				if (res.isSuccessful()) {
					return Response.ok(res.getEntity(String.class)).build();
				}else {
					return Response.status(res.getStatusCode()).entity(res.getEntity(String.class)).build();
				}
			} catch (Exception e) {
				log.warn("\n"+this.getClass()+"invoke method fail"+e.getMessage());
			} 
		}
		return null;
	}

	public String getCookieAuth() {
		return cookieAuth;
	}

	public void setCookieAuth(String cookieAuth) {
		this.cookieAuth = cookieAuth;
	}

	public String getAccountAuth() {
		return accountAuth;
	}

	public void setAccountAuth(String accountAuth) {
		this.accountAuth = accountAuth;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassWord() {
		return passWord;
	}

	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}

	public TypeAuthen getTypeAuthen() {
		return typeAuthen;
	}

	public void setTypeAuthen(TypeAuthen typeAuthen) {
		this.typeAuthen = typeAuthen;
	}

	public ApplicationLinkRequestFactory getRequestFactory() {
		return requestFactory;
	}

	public void setRequestFactory(ApplicationLinkRequestFactory requestFactory) {
		this.requestFactory = requestFactory;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}
}
