package com.dltastudio.ws;

import com.dltastudio.GenericWS;
import com.dltastudio.exceptions.WSRestException;
import com.dltastudio.services.GeneralSettings;
import com.dltastudio.services.HTTPMethod;
import com.dltastudio.services.RESTResponse;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import java.net.URLEncoder;

/**
 * Wrapper for XCodeServer Assets WS
 * Most of this API is documented but actually not implemented
 * in XCode 7.2
 */
@Path("/assets")
public class AssetsWS  extends GenericWS {

    /*
    * Downloading an Asset
    * Xcode 7.2 does not support this method
    *
     */

    /**
     * Download an Assert for Bot
     * Xcode 7.2 does not support this method
     * @param headers Http header that contains token
     * @param response Response to be sent
     * @param botId Bot identifier to retrieve assets
     * @return binary result from XCodeServer
     */
    @GET
    @Path("/{botId}")
    public byte[] getAssetContent(@Context HttpHeaders headers,
                                  @Context HttpServletResponse response,@PathParam("botId") String botId) {
        String url;
        RESTResponse restResponse;

        String relativePath=headers.getHeaderString("relativePath");

        if ((null==relativePath) || (0==relativePath.length())) {
            throw new WSRestException("Empty relativePath", Response.Status.UNAUTHORIZED);
        }


        try {
            url = GeneralSettings.getXCodeBaseURL() + "assets/" + URLEncoder.encode(relativePath,"UTF-8");
            restResponse = sendAuthHttpRequest(headers, HTTPMethod.GET, url, null);
            processRestResponse(response,restResponse);
            return restResponse.getContent();
        } catch (Exception e) {
            throw new WSRestException("Internal error", Response.Status.INTERNAL_SERVER_ERROR);
        }
    }
}
