package com.dltastudio.services;

import java.util.HashMap;

/**
 * Classe for building URL parameters list from HashMap
 */
public class URLParamBuilder {

    /**
     * URL parameters hashMap
     */
    private HashMap<String,String> urlParams;

    /**
     * Build an URLParamBuilder object
     */
    public URLParamBuilder() {
        urlParams=new HashMap<String,String>();
    }

    /**
     * Add parameters to object
     * @param name Name of parameters
     * @param value Value of parameter
     */
    public void addParam(String name,String value) {
        if (null!=value) {
            urlParams.put(name,value);
        }
    }

    /**
     * Build String version of parameters list
     * @return String representation of object
     */
    public String toString() {

        StringBuilder resultBuilder = new StringBuilder();

        for (String key:urlParams.keySet()) {
            if (0==resultBuilder.length()) {
                resultBuilder.append('?');
            }
            else {
                resultBuilder.append('&');
            }

            String value=urlParams.get(key);
            if (value.length()>0) {
                resultBuilder.append(key + "=" + urlParams.get(key));
            }
            else {
                resultBuilder.append(key);
            }
        }
        return resultBuilder.toString();
    }
}
