package API.bul;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import javax.ws.rs.core.Response;

import org.jfree.util.Log;

import com.atlassian.sal.api.net.Request.MethodType;
import com.atlassian.application.api.Application;
import com.atlassian.applinks.api.ApplicationId;
import com.atlassian.applinks.api.ApplicationLink;
import com.atlassian.applinks.api.ApplicationLinkRequest;
import com.atlassian.applinks.api.ApplicationLinkRequestFactory;
import com.atlassian.applinks.api.ApplicationLinkResponseHandler;
import com.atlassian.applinks.api.ApplicationLinkService;
import com.atlassian.applinks.api.CredentialsRequiredException;
import com.atlassian.applinks.api.TypeNotInstalledException;
import com.atlassian.sal.api.net.ResponseException;
import com.atlassian.sal.api.net.TrustedRequest;
import com.atlassian.sal.api.net.TrustedRequestFactory;
import com.sun.jersey.core.util.Base64;

import API.extension.RequestExtension;
import API.extension.TypeAuthen;

public class JiraBUL {
	private String cookieAuth;
	private String accountAuth;
	private String url;
	private String userName;
	private String passWord;
	private TypeAuthen typeAuthen;
	private TrustedRequestFactory fRequestFactory;
	private String host;
	private ApplicationLinkBUL applicationLinkBUL;
	
	public TypeAuthen getTypeAuthen() {
		return typeAuthen;
	}

	public void setTypeAuthen(TypeAuthen typeAuthen) {
		this.typeAuthen = typeAuthen;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
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

	public JiraBUL(String cookieAuth, String url) {
		super();
		this.setCookieAuth(cookieAuth);
		this.setUrl(url);
		this.typeAuthen = TypeAuthen.Cookie;
	}

	public JiraBUL(String userName, String passWord, String url) {
		super();
		this.userName = userName;
		this.passWord = passWord;
		this.setAccountAuth(new String(Base64.encode(userName + ":" + passWord)));
		this.setUrl(url);
		this.typeAuthen = TypeAuthen.Account;
	}

	public JiraBUL(TrustedRequestFactory fRequestFactory, String url) {
		super();
		this.setfRequestFactory(fRequestFactory);
		this.typeAuthen = TypeAuthen.Jira;
		this.setUrl(url);
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
		try {
			this.host=new URL(url).getHost();
		} catch (MalformedURLException e) {
			this.host=null;
		}
	}

	public Response invokePostMethod(String api, String body) {
		switch (typeAuthen) {
		case Account:
			return RequestExtension.getInstance().invokePostMethod(this.getAccountAuth(), api, body);
		case Cookie:
			return RequestExtension.getInstance().invokePostMethodCookieAuth(this.getCookieAuth(), api, body);
		case Jira:
			TrustedRequest trustedRequest = fRequestFactory.createTrustedRequest(MethodType.POST,api);
			trustedRequest.addTrustedTokenAuthentication(this.host);
			trustedRequest.setRequestBody(body, "application/json");
			try {
				String result = trustedRequest.execute();
				return Response.ok().entity(result).build();
			} catch (ResponseException e) {
				return Response.serverError().entity("\n"+this.getClass()+" invokePostMethod fail:"+e.getMessage()).build();
			}			
		}
		return null;
	}

	public Response invokeGetMethod(String api) {
		switch (typeAuthen) {
		case Account:
			return RequestExtension.getInstance().invokeGetMethod(this.getAccountAuth(),api);
		case Cookie:
			return RequestExtension.getInstance().invokeGetMethodCookieAuth(this.getCookieAuth(),api);
		case Jira:
			TrustedRequest trustedRequest = fRequestFactory.createTrustedRequest(MethodType.GET,api);
			trustedRequest.addTrustedTokenAuthentication(this.host);
			try {
				String result = trustedRequest.execute();
				return Response.ok().entity(result).build();
			} catch (ResponseException e) {
				return Response.serverError().entity("\n"+this.getClass()+"invokeGetMethod fail:"+e.getMessage()).build();
			}			
		}
		return null;
	}
	public Response invokePutMethod(String api,String body) {
		switch (typeAuthen) {
		case Account:
			return RequestExtension.getInstance().invokePutMethod(this.getAccountAuth(), api, body);
		case Cookie:
			return RequestExtension.getInstance().invokePutMethodCookieAuth(this.getCookieAuth(), api, body);
		case Jira:
			TrustedRequest trustedRequest = fRequestFactory.createTrustedRequest(MethodType.PUT,api);
			trustedRequest.addTrustedTokenAuthentication(this.host);
			trustedRequest.setRequestBody(body, "application/json");
			try {
				String result = trustedRequest.execute();
				return Response.ok().entity(result).build();
			} catch (ResponseException e) {
				return Response.serverError().entity("\n"+this.getClass()+"invokePutMethod fail:"+e.getMessage()).build();
			}			
		}
		return null;
	}

	public TrustedRequestFactory getfRequestFactory() {
		return fRequestFactory;
	}

	public void setfRequestFactory(TrustedRequestFactory fRequestFactory) {
		this.fRequestFactory = fRequestFactory;
	}

	public ApplicationLinkBUL getApplicationLinkBUL() {
		return applicationLinkBUL;
	}

	public void setApplicationLinkBUL(ApplicationLinkBUL applicationLinkBUL) {
		this.applicationLinkBUL = applicationLinkBUL;
	}
	



}
