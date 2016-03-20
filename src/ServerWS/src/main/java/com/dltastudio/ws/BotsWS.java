package com.dltastudio.ws;

import com.dltastudio.GenericWS;
import com.dltastudio.exceptions.WSRestException;
import com.dltastudio.model.TokenDAO;
import com.dltastudio.services.*;
import org.glassfish.jersey.httppatch.PATCH;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


/**
 * Wrapper for XCodeServer 7.2 Bots WS API
 */
@Path("/bots")
public class BotsWS extends GenericWS {

    /**
     * Create a new bot
     * @param headers Http headers
     * @param response Http response
     * @param jsonInput JSON content for XCodeServer API
     * @return JSON content from XCodeServer
     */
    @POST
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public String createBot(@Context HttpHeaders headers,@Context HttpServletResponse response,String jsonInput) {

        String url;
        RESTResponse restResponse;

        url= GeneralSettings.getXCodeBaseURL()+"bots";

        try {
            restResponse = sendAuthHttpRequest(headers,HTTPMethod.POST, url, jsonInput);
            processRestResponse(response,restResponse);
            return restResponse.getContentString();
         }
        catch (Exception e)
        {
            throw new WSRestException("Internal error", Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

     /**
     * Retrieve all bots
     * @param headers Http headers
     * @param response Http response
     * @return JSON content from XCodeServer
     */
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    public String getBots(@Context HttpHeaders headers,@Context HttpServletResponse response) {

        String url;
        RESTResponse restResponse;

        url=GeneralSettings.getXCodeBaseURL()+"bots";

        try {
            restResponse = sendAuthHttpRequest(headers,HTTPMethod.GET, url, null);
            processRestResponse(response,restResponse);
            return restResponse.getContentString();
        }
        catch (Exception e)
        {
            throw new WSRestException("Internal error", Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Retrieve a given bot
     * @param headers Http headers
     * @param response Http response
     * @param botId Identifier of the bot to retrieve
     * @return JSON content describing the requested Bot
     */
    @GET
    @Path("/{botId}")
    @Produces({ MediaType.APPLICATION_JSON })
    public String getBot(@Context HttpHeaders headers,@Context HttpServletResponse response,@PathParam("botId") String botId) {

        String url;
        RESTResponse restResponse;

        url=GeneralSettings.getXCodeBaseURL()+"bots/"+botId;

        try {
            restResponse = sendAuthHttpRequest(headers,HTTPMethod.GET, url, null);
            processRestResponse(response,restResponse);
            return restResponse.getContentString();
        }
        catch (Exception e)
        {
            throw new WSRestException("Internal error", Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Update a bot
     * @param headers Http headers
     * @param response Http response
     * @param botId Identifier of the bot to update
     * @return JSON content from XCode Server
     */
    @PATCH
    @Path("/{botId}")
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public String updateBot(@Context HttpHeaders headers,@Context HttpServletResponse response,@PathParam("botId") String botId) {

        String url;
        RESTResponse restResponse;

        url=GeneralSettings.getXCodeBaseURL()+"bots/"+botId;

        try {
            restResponse = sendAuthHttpRequest(headers,HTTPMethod.PATCH, url, null);
            processRestResponse(response,restResponse);
            return restResponse.getContentString();
        }
        catch (Exception e)
        {
            throw new WSRestException("Internal error", Response.Status.INTERNAL_SERVER_ERROR);
        }

    }

    /**
     * Remove a bot from XCodeServer
     * @param headers Http headers
     * @param response Http response
     * @param botId Identifier of the bot to delete
     * @return JSON content from XCode Server
     */
    @DELETE
    @Path("/{botId}")
    @Produces({ MediaType.APPLICATION_JSON })
    public String deleteBot(@Context HttpHeaders headers,@Context HttpServletResponse response,@PathParam("botId") String botId) {

        String url;
        RESTResponse restResponse;

        url=GeneralSettings.getXCodeBaseURL()+"bots/"+botId;

        try {
            restResponse = sendAuthHttpRequest(headers,HTTPMethod.DELETE, url, null);
            processRestResponse(response,restResponse);
            return restResponse.getContentString();
        }
        catch (Exception e)
        {
            throw new WSRestException("Internal error", Response.Status.INTERNAL_SERVER_ERROR);
        }

    }

    @POST
    @Path("/{botId}/duplicate")
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public String duplicateBot(@Context HttpHeaders headers,@Context HttpServletResponse response,String jsonInput,@PathParam("botId") String botId) {

        String url;
        RESTResponse restResponse;

        url=GeneralSettings.getXCodeBaseURL()+"bots/"+botId+"/duplicate";

        try {
            restResponse = sendAuthHttpRequest(headers,HTTPMethod.POST, url, jsonInput);
            processRestResponse(response,restResponse);
            return restResponse.getContentString();
        }
        catch (Exception e)
        {
            throw new WSRestException("Internal error", Response.Status.INTERNAL_SERVER_ERROR);
        }
    }


    @POST
    @Path("/{botId}/integrations")
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public String integrateBot(@Context HttpHeaders headers,@Context HttpServletResponse response,String jsonInput,@PathParam("botId") String botId) {

        String url;
        RESTResponse restResponse;

        url=GeneralSettings.getXCodeBaseURL()+"bots/"+botId+"/integrations";

        try {
            restResponse = sendAuthHttpRequest(headers,HTTPMethod.POST, url, jsonInput);
            processRestResponse(response,restResponse);
            return restResponse.getContentString();
        }
        catch (Exception e)
        {
            throw new WSRestException("Internal error", Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    /*
Filtering Integrations for a Bot
/bots/{id}/integrations{?filter,last,number,from,next,prev,count,summary_only}
 */
    @GET
    @Path("/{botId}/integrations")
    @Produces({ MediaType.APPLICATION_JSON })
    public String filterIntegrations(@Context HttpHeaders headers,
                                 @Context HttpServletResponse response,@PathParam("botId") String botId,
                                 @QueryParam("filter") String filter,
                                 @QueryParam("last") String last,
                                 @QueryParam("number") String number,
                                 @QueryParam("from") String from,
                                 @QueryParam("next") String next,
                                 @QueryParam("prev") String prev,
                                 @QueryParam("count") String count,
                                 @QueryParam("summary_only") String summary_only) {
        String url;
        RESTResponse restResponse;
        URLParamBuilder params = new URLParamBuilder();
        params.addParam("filter", filter);
        params.addParam("last", last);
        params.addParam("number", number);
        params.addParam("from", from);
        params.addParam("next", next);
        params.addParam("prev", prev);
        params.addParam("count", count);
        params.addParam("summary_only", summary_only);

        url=GeneralSettings.getXCodeBaseURL()+"bots/"+botId+"/integrations/"+params.toString();

        try {
            restResponse = sendAuthHttpRequest(headers, HTTPMethod.GET, url, null);
            processRestResponse(response,restResponse);
            return restResponse.getContentString();
        }
        catch (Exception e)
        {
            throw new WSRestException("Internal error", Response.Status.INTERNAL_SERVER_ERROR);
        }
    }
}
