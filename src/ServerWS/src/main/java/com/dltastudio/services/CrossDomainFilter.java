package com.dltastudio.services;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.MultivaluedMap;
import java.io.IOException;

/**
 * Custom cross domain filter
 */
public class CrossDomainFilter implements ContainerResponseFilter {
    /**
     * Add the cross domain data to the output if needed
     *
     * @param requestContext The container request (input)
     * @param responseContext The container request (output)
     */
    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        if (GeneralSettings.isCrossOriginAllowed()) {

            MultivaluedMap<String, Object> headers = responseContext.getHeaders();
            headers.add("Access-Control-Allow-Origin", "*");
            headers.add("Access-Control-Allow-Headers", "relativePath, login, password, token, Origin, X-Requested-With, Content-Type, X-Codingpedia, Accept, Authorization,Access-Control-Allow-Origin");
            headers.add("Access-Control-Allow-Credentials", "true");
            headers.add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD, PATCH");
            headers.add("Access-Control-Max-Age", "1209600");
        }
    }
}
