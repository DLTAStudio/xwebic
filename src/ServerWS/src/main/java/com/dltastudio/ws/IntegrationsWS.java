package com.dltastudio.ws;

import com.dltastudio.GenericWS;
import com.dltastudio.exceptions.WSRestException;
import com.dltastudio.services.GeneralSettings;
import com.dltastudio.services.HTTPMethod;
import com.dltastudio.services.RESTResponse;
import com.dltastudio.services.URLParamBuilder;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * XCodeServer Integration WS
 */
@Path("/integrations")
public class IntegrationsWS extends GenericWS {

    /**
     * Retrieve a specific integration
     * @param headers HTTP Headers
     * @param response Http response
     * @param integrationId Id of integration to retrieve
     * @return JSON description of requested integration
     */
    @GET
    @Path("/{integrationId}")
    @Produces({ MediaType.APPLICATION_JSON })
    public String getIntegration(@Context HttpHeaders headers, @Context HttpServletResponse response,@PathParam("integrationId") String integrationId) {

        String url;
        RESTResponse restResponse;

        url=GeneralSettings.getXCodeBaseURL()+"integrations/"+integrationId;

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

    /**
     * Delete an integration
     * NOT TESTED
     * @param headers HTTP Headers
     * @param response Http response
     * @param integrationId Integration id
     * @return Output from XCode Server
     */
    @DELETE
    @Path("/{integrationId}")
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public String deleteIntegration(@Context HttpHeaders headers,@Context HttpServletResponse response,@PathParam("integrationId") String integrationId) {

        String url;
        RESTResponse restResponse;

        url=GeneralSettings.getXCodeBaseURL()+"integrations/"+integrationId;

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

    /**
     * Cancel an integration
     * NOT TESTED
     * @param headers HTTP Headers
     * @param response Http response
     * @param integrationId Integration id
     * @return Output from XCode Server
     */
    @POST
    @Path("/{integrationId}/cancel")
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public String cancelIntegration(@Context HttpHeaders headers,@Context HttpServletResponse response,@PathParam("integrationId") String integrationId) {

        String url;
        RESTResponse restResponse;

        url=GeneralSettings.getXCodeBaseURL()+"integrations/"+integrationId+"/cancel";

        try {
            restResponse = sendAuthHttpRequest(headers,HTTPMethod.POST, url, null);
            processRestResponse(response,restResponse);
            return restResponse.getContentString();
        }
        catch (Exception e)
        {
            throw new WSRestException("Internal error", Response.Status.INTERNAL_SERVER_ERROR);
        }

    }

    /**
     * Filter Integrations for All Bots
     * There are two filter options: non_fatal and with_build_results.
     * * non_fatal: Integrations with a result of type succeeded, test-failures, build-errors, warnings, analyzer-warnings or build-failed.
     * * with_build_results: Integrations containing build summary information.
     * NOT TESTED
     * @param headers HTTP Headers
     * @param response Http response
     * @param filter Filter (non_fatal, with_build_results)
     * @param last The last specified number of integrations
     * @param count The total number of integrations.
     * @param summary_only A Boolean value that indicates whether to return a brief summary instead of the regular payload.
     * @return Output from XCode Server
     */
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    public String filterIntegrations(@Context HttpHeaders headers,
                                 @Context HttpServletResponse response,
                                 @QueryParam("filter") String filter,
                                 @QueryParam("last") String last,
                                  @QueryParam("count") String count,
                                 @QueryParam("summary_only") String summary_only) {
        String url;
        RESTResponse restResponse;
        URLParamBuilder params = new URLParamBuilder();
        params.addParam("filter", filter);
        params.addParam("last", last);
        params.addParam("count", count);
        params.addParam("summary_only", summary_only);

        url=GeneralSettings.getXCodeBaseURL()+"integrations"+params.toString();

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


    /**
     * Retrieve Tests for an Integration
     * NOT TESTED
     * @param headers HTTP Headers
     * @param response Http response
     * @param integrationId Integration identifier
     * @param deviceIdentifier Device identifier
     * @return Output from XCode Server
     */
    @GET
    @Path("/{integrationId}/batch/{deviceIdentifier}")
    @Produces({ MediaType.APPLICATION_JSON })
    public String getIntegrationTests(@Context HttpHeaders headers,
                                     @Context HttpServletResponse response,@PathParam("integrationId") String integrationId,
                                     @QueryParam("deviceIdentifier") String deviceIdentifier
                                     ) {
        String url;
        RESTResponse restResponse;
        URLParamBuilder params = new URLParamBuilder();
        params.addParam("deviceIdentifier", deviceIdentifier);

        url=GeneralSettings.getXCodeBaseURL()+"integrations/"+integrationId+"/batch"+params.toString();

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

    /**
     * Retrieve Commits for an Integration
     * NOT TESTED
     * @param headers HTTP Headers
     * @param response Http response
     * @param integrationId Integration identifier
     * @return Output from XCode Server
     */
    @GET
    @Path("/{integrationId}/commits")
    @Produces({ MediaType.APPLICATION_JSON })
    public String getIntegrationCommits(@Context HttpHeaders headers,
                                      @Context HttpServletResponse response,@PathParam("integrationId") String integrationId) {
        String url;
        RESTResponse restResponse;

        url=GeneralSettings.getXCodeBaseURL()+"integrations/"+integrationId+"/commits";

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


    /**
     * Retrieve Issues for an Integration
     * NOT TESTED
     * @param headers HTTP Headers
     * @param response Http response
     * @param integrationId Integration identifier
     * @return Output from XCode Server
     */
    @GET
    @Path("/{integrationId}/issues")
    @Produces({ MediaType.APPLICATION_JSON })
    public String getIntegrationIssues(@Context HttpHeaders headers,
                                      @Context HttpServletResponse response,@PathParam("integrationId") String integrationId) {
        String url;
        RESTResponse restResponse;

        url=GeneralSettings.getXCodeBaseURL()+"integrations/"+integrationId+"/issues";

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


    /**
     * Retrieve Files for an Integration
     * NOT WORKING WITH XCode 7.2
     * @param headers HTTP Headers
     * @param response Http response
     * @param integrationId Integration identifier
     * @return Output from XCode Server
     */
    @GET
    @Path("/{integrationId}/files")
    @Produces({ MediaType.APPLICATION_JSON })
    public String getIntegrationFiles(@Context HttpHeaders headers,
                                       @Context HttpServletResponse response,@PathParam("integrationId") String integrationId) {
        String url;
        RESTResponse restResponse;

        url=GeneralSettings.getXCodeBaseURL()+"integrations/"+integrationId+"/files";

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


    /**
     * Retrieving Code Coverage for an Integration
     * NOT TESTED
     * @param headers HTTP Headers
     * @param response Http response
     * @param integrationId Integration identifier
     * @param include_methods If true, all available coverage data is returned. If false, only target and file coverage data is returned, excluding file methods.
     * @return Output from XCode Server
     */
    @GET
    @Path("/{integrationId}/coverage")
    @Produces({ MediaType.APPLICATION_JSON })
    public String getIntegrationCoverage(@Context HttpHeaders headers,
                                      @Context HttpServletResponse response,@PathParam("integrationId") String integrationId,
                                      @QueryParam("include_methods") String include_methods) {
        String url;
        RESTResponse restResponse;
        URLParamBuilder params = new URLParamBuilder();
        params.addParam("include_methods", include_methods);

        url=GeneralSettings.getXCodeBaseURL()+"integrations/"+integrationId+"/coverage"+params.toString();

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


    /**
     *  Retrieve Assets for an Integration
     *  Based on undocumented API
     * @param headers HTTP Headers
     * @param response Http response
     * @param integrationId Integration identifier
     * @param token Connection token
     * @return XCodeServer output files as tar.gz
     */
    @GET
    @Path("/{integrationId}/assets/{token}")
    @Produces("application/gzip")
    public byte[] getIntegrationAssets(@Context HttpHeaders headers,
                                         @Context HttpServletResponse response,@PathParam("integrationId") String integrationId,
                                       @PathParam("token") String token) {
        String url;
        RESTResponse restResponse;

        url= GeneralSettings.getXCodeInternalBaseURL()+"integrations/"+integrationId+"/assets";

        try {
            restResponse = sendAuthHttpRequest(token, HTTPMethod.GET, url, null);
            processRestResponse(response,restResponse);

            return restResponse.getContent();
        }
        catch (Exception e)
        {
            throw new WSRestException("Internal error", Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

}
