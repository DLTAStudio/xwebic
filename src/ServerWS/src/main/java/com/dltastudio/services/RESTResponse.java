package com.dltastudio.services;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

/**
 * Implement a response from REST Service
 */
public class RESTResponse {
    /**
     * HTTP result code
     */
    private int statusCode;

    /**
     * Response Http headers
     */
    private HashMap<String,String> headers;

    /**
     * Response body
     */
    private byte[] content;

    /**
     * Build a RESTResponse object
     * @param statusCode http status code
     * @param headers http headers
     * @param content Response body content
     */
    public RESTResponse(int statusCode, HashMap<String, String> headers,byte[] content) {
        this.statusCode = statusCode;
        this.headers = headers;
        this.content = content;
    }

    /**
     * Return response status code
     * @return Response status code
     */
    public int getStatusCode() {
        return statusCode;
    }

    /**
     * Return response http headers
     * @return Response http headers
     */
    public HashMap<String, String> getHeaders() {
        return headers;
    }

    /**
     * Return Http response content as UTF-8 String
     * @return Http response content as UTF-8 String
     */
    public String getContentString() {
        String result=null;

        try {
            result = new String(content, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Return http response content as binary
     * @return Http response content as binary
     */
    public byte[] getContent() {
        return content;
    }
}
