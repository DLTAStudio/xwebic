package com.dltastudio.ws;

import com.dltastudio.GenericWS;
import com.dltastudio.exceptions.WSRestException;
import com.dltastudio.model.TokenDAO;
import com.dltastudio.services.GeneralSettings;
import com.dltastudio.services.HTTPMethod;
import com.dltastudio.services.RESTClient;
import com.dltastudio.services.RESTResponse;
import org.json.JSONObject;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import java.util.HashMap;

/**
 * WS that manage token authentification
 */
@Path("/token")
public class TokenWS extends GenericWS {

    /**
     * Check if login/password are valid
     * @param login User login
     * @param password user password
     * @return true if login/password is valid, otherwise false.
     */
    public static boolean checkAuth(String login,String password) {
        String url;
        RESTResponse restResponse;

        url= GeneralSettings.getXCodeBaseURL()+"bots";

        try {
            HashMap<String,String> headers=new HashMap<String,String>();
            headers.put("Authorization",TokenDAO.buildHttpAuthString(login,password));
            restResponse = RESTClient.sendHttpRequest(HTTPMethod.GET, url, headers, null);
            return (1==restResponse.getStatusCode()/200);
        }
        catch (Exception e)
        {
            return false;
        }
    }

    /**
     * Check if an Authorization header are valid
     * @param authHeader Authorization header
     * @return true if header is valid, otherwise false.
     */
    public static boolean checkAuthHeader(String authHeader) {
        String url;
        RESTResponse restResponse;

        url= GeneralSettings.getXCodeBaseURL()+"bots";

        try {
            HashMap<String,String> headers=new HashMap<String,String>();
            headers.put("Authorization",authHeader);
            restResponse = RESTClient.sendHttpRequest(HTTPMethod.GET, url, headers, null);
            return (1==restResponse.getStatusCode()/200);
        }
        catch (Exception e)
        {
            return false;
        }
    }

    /**
     * Create and register a new token for givent username/password
     * @param headers Http headers
     * @param response Http response
     * @return JSON containing authorization token
     */
    @GET
    public String getToken(@Context HttpHeaders headers, @Context HttpServletResponse response) {
        String login=headers.getHeaderString("login");
        String password=headers.getHeaderString("password");

        if ((null==login) || (0==login.length())) {
            throw new WSRestException("Empty login", Response.Status.UNAUTHORIZED);
        }

        if ((null==password) || (0==password.length())) {
            throw new WSRestException("Empty password", Response.Status.UNAUTHORIZED);
        }

        if (!checkAuth(login, password)) {
            throw new WSRestException("Invalid login/password", Response.Status.UNAUTHORIZED);
        }

        String token=TokenDAO.registerToken(login,password);

        if (null==token) {
            throw new WSRestException("Internal error", Response.Status.INTERNAL_SERVER_ERROR);
        }

        JSONObject jsonResult = new JSONObject();
        jsonResult.put("token",token);
        return jsonResult.toString();
    }

    /**
     * Forbid call to CheckToken without token
     * @param headers Http headers
     * @param response Http response
     * @return Empty string on  success...Otherwise Exception is thrown
     */
    @GET
    @Path("/check")
    public String checkToken(@Context HttpHeaders headers, @Context HttpServletResponse response) {
        if (true)
            throw new WSRestException("Empty token", Response.Status.UNAUTHORIZED);
        return "{}";
    }

    /**
     * Check if a given authorization token is valid
     * @param headers Http headers
     * @param response Http response
     * @param token token to be checked
     * @return Empty string on  success...Otherwise Exception is thrown
     */
    @GET
    @Path("/check/{token}")
    public String checkToken(@Context HttpHeaders headers, @Context HttpServletResponse response,@PathParam("token") String token) {

        if (0==token.length()) {
            throw new WSRestException("Empty token", Response.Status.UNAUTHORIZED);
        }

        String authHeader = TokenDAO.loadAuthString(token);

        if (null==authHeader) {
            throw new WSRestException("Invalid token", Response.Status.UNAUTHORIZED);
        }

        if (!checkAuthHeader(authHeader)) {
            throw new WSRestException("Forbidden", Response.Status.UNAUTHORIZED);
        }
        return "{}";
    }
}
