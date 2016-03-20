package com.dltastudio;


import com.dltastudio.model.TokenDAO;
import com.dltastudio.services.HTTPMethod;
import com.dltastudio.services.RESTClient;
import com.dltastudio.services.RESTResponse;
import com.dltastudio.services.GeneralSettings;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.HttpHeaders;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Abstract class for exposing REST WebService
 */
public abstract class GenericWS {

    /**
     * Process a client RESTResponse to build output HttpServletResponse
     * @param response Response to be send to client
     * @param restResponse RESTResponse sent by server
     * @throws IOException IOException
     */
    public void processRestResponse(HttpServletResponse response, RESTResponse restResponse) throws IOException {
        response.setStatus(restResponse.getStatusCode());
        Map<String, String> responseHeaders = restResponse.getHeaders();
        StringBuilder allHeaders = new StringBuilder();
        for (String header : responseHeaders.keySet()) {
            response.setHeader(header, responseHeaders.get(header));
            if (allHeaders.length()>0)
                allHeaders.append(",");
            allHeaders.append(header);
        }

        // cross origin
        if (GeneralSettings.isCrossOriginAllowed()) {
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Access-Control-Expose-Headers", allHeaders.toString());
        }
        response.flushBuffer();
    }

    /**
     * Send synchronous http request
     * @param token Authentification token
     * @param method Http verb
     * @param url URL request
     * @param body Content http request
     * @return Response for http request
     * @throws KeyManagementException Security Exception
     * @throws NoSuchAlgorithmException Security Exception
     * @throws KeyStoreException Security Exception
     * @throws UnsupportedEncodingException Security Exception
     */
    public RESTResponse sendAuthHttpRequest(String token, HTTPMethod method, String url, String body) throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException, UnsupportedEncodingException {
        RESTResponse restResponse;

        List<String> stringHeaders;
        HashMap<String, String> httpHeaders = new HashMap<String, String>();

        if (null != token) {
            String authorization = TokenDAO.loadAuthString(token);
            if (null != authorization) {
                httpHeaders.put("Authorization", authorization);
            }
        }
        restResponse = RESTClient.sendHttpRequest(method, url, httpHeaders, body);
        return restResponse;
    }

    /**
     * Send synchronous http request using authentification  provided in http headers
     * @param headers Http headers
     * @param method Http verb
     * @param url URL request
     * @param body Content http request
     * @return Response for http request
     * @throws KeyManagementException Security Exception
     * @throws NoSuchAlgorithmException Security Exception
     * @throws KeyStoreException Security Exception
     * @throws UnsupportedEncodingException Security Exception
     */
    public RESTResponse sendAuthHttpRequest(HttpHeaders headers, HTTPMethod method, String url, String body) throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException, UnsupportedEncodingException {
        List<String> stringHeaders;
        HashMap<String, String> httpHeaders = new HashMap<String, String>();

        String token = null;
        stringHeaders = headers.getRequestHeader("token");
        if ((null != stringHeaders) && (stringHeaders.size() > 0)) {
            token = stringHeaders.get(0);
        }

        return sendAuthHttpRequest(token,  method,  url,  body);
    }
}
