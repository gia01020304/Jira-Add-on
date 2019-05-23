package API.extension;

import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;


public class RequestExtension {
	private static RequestExtension instance;
	private static final Logger log = Logger.getLogger(RequestExtension.class);
	public static RequestExtension getInstance() {
		if (instance == null) {
			return new RequestExtension();
		}
		return instance;
	}
	
	public Response getCookieTest(String url) {
		Client client = Client.create();
		WebResource webResource = client.resource(url);
		ClientResponse response = webResource.type("application/json")
				.accept("application/json").get(ClientResponse.class);
		int statusCode = response.getStatus();
		
		if ((statusCode/200) != 1) {
			return null;
		}
		String temp=null;
		if (response.getCookies()!=null&&response.getCookies().size()>0) {
			temp= response.getCookies().get(0).getValue();
		}
		return Response.ok(temp).build();
	}
	
	
	
	
	public Response invokeMethodGetTest(String url) {
		Client client = Client.create();
		WebResource webResource = client.resource(url);
		ClientResponse response = webResource.type("application/json")
				.accept("application/json").get(ClientResponse.class);
		int statusCode = response.getStatus();
		
		if ((statusCode/200) != 1) {
			return Response.status(statusCode).entity(response.getEntity(String.class)).build();
		}
		
		return Response.ok(response.getEntity(String.class)).build();
	}

	public Response invokeGetMethodCookieAuth(String cookieAuth, String url) {
		Client client = Client.create();
		WebResource webResource = client.resource(url);
		ClientResponse response = webResource.cookie(new Cookie("JSESSIONID", cookieAuth)).type("application/json")
				.accept("application/json").get(ClientResponse.class);
		int statusCode = response.getStatus();
			
		if ((statusCode/200) != 1) {
			return Response.status(statusCode).entity(response.getEntity(String.class)).build();
		}
		
		return Response.ok(response.getEntity(String.class)).build();
	}

	public Response invokePostMethodCookieAuth(String cookieAuth, String url, String data) {
		Client client = Client.create();
		WebResource webResource = client.resource(url);
		ClientResponse response = webResource.cookie(new Cookie("JSESSIONID", cookieAuth)).type("application/json")
				.accept("application/json").post(ClientResponse.class, data);
		int statusCode = response.getStatus();
		
		
		if ((statusCode/200) != 1) {
			return Response.status(statusCode).entity(response.getEntity(String.class)).build();
		}
		
		return Response.ok(response.getEntity(String.class)).build();
	}

	public Response invokeGetMethod(String accountAuth, String url) {
		Client client = Client.create();
		WebResource webResource = client.resource(url);
		ClientResponse response = webResource.header("Authorization", "Basic " + accountAuth).type("application/json")
				.accept("application/json").get(ClientResponse.class);
		
		
		int statusCode = response.getStatus();
		if ((statusCode/200) != 1) {
			return Response.status(statusCode).entity(response.getEntity(String.class)).build();
		}

		return Response.ok(response.getEntity(String.class)).build();
	}

	public Response invokePostMethod(String accountAuth, String url, String data) {
		Client client = Client.create();
		WebResource webResource = client.resource(url);
		ClientResponse response = webResource.header("Authorization", "Basic " + accountAuth).type("application/json")
				.accept("application/json").post(ClientResponse.class, data);
		
		
		int statusCode = response.getStatus();
		if ((statusCode/200) != 1) {
			return Response.status(statusCode).entity(response.getEntity(String.class)).build();
		}
		return Response.ok(response.getEntity(String.class)).build();
	}

	public Response invokePutMethod(String accountAuth, String url, String data) {
		Client client = Client.create();
		WebResource webResource = client.resource(url);
		ClientResponse response = webResource.header("Authorization", "Basic " + accountAuth).type("application/json")
				.accept("application/json").put(ClientResponse.class, data);
		
		
		int statusCode = response.getStatus();
		if ((statusCode/200) != 1) {
			return Response.status(statusCode).entity(response.getEntity(String.class)).build();
		}
		return Response.ok(response.getEntity(String.class)).build();

	}
	public Response invokePutMethodCookieAuth(String cookieAuth, String url, String data) {
		Client client = Client.create();
		WebResource webResource = client.resource(url);
		ClientResponse response = webResource.cookie(new Cookie("JSESSIONID", cookieAuth)).type("application/json")
				.accept("application/json").put(ClientResponse.class, data);
		
		
		int statusCode = response.getStatus();
		if ((statusCode/200) != 1) {
			return Response.status(statusCode).entity(response.getEntity(String.class)).build();
		}
		return Response.ok(response.getEntity(String.class)).build();

	}
}
