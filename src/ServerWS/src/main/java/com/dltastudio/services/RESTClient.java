package com.dltastudio.services;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

/**
 * Implements REST Client API services
 */
public class RESTClient {

    /**
     * configure HttpRequestBase with custom headers and body content
     * @param request HttpRequest to configure
     * @param headers Map containing custom http headers. Can be null
     * @param body Body of the request. Can be null
     * @throws UnsupportedEncodingException Thrown if UTF-8 is insupported
     */
    private static void configureHttpRequest(HttpRequestBase request, Map<String,String> headers, String body) throws UnsupportedEncodingException {

        // set Headers
        if (null!=headers){
            for (String key : headers.keySet()) {
                request.setHeader(key,headers.get(key));
            }
        }
        if (null!=body) {
            HttpEntityEnclosingRequestBase enclosingRequest = (HttpEntityEnclosingRequestBase)request;
            HttpEntity contentEntity = new ByteArrayEntity(body.getBytes("UTF-8"));
            enclosingRequest.setEntity(contentEntity);
        }
    }

    /**
     * Convert Http headers to HashMap
     * @param headers Array of http headers
     * @return HashMap for headers
     */
    private static  HashMap<String, String> convertHeadersToHashMap(Header[] headers) {
        HashMap<String, String> result = new HashMap<String, String>(headers.length);
        for (Header header : headers) {
            result.put(header.getName(), header.getValue());
        }
        return result;
    }

    /**
     * Build an HttpClient that accept SSL Connexion that accepts untrusted certificate
     * @return An HttpClient
     * @throws KeyStoreException keyStoreException
     * @throws NoSuchAlgorithmException Missing SSL algorithm
     * @throws KeyManagementException Error in key management
     */
    private static CloseableHttpClient createHttpClient_AcceptsUntrustedCerts() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        HttpClientBuilder b = HttpClientBuilder.create();

        // Trust Strategy that allows all certificates.
        SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
            @Override
            public boolean isTrusted(java.security.cert.X509Certificate[] chain, String authType) throws java.security.cert.CertificateException {
                return true;
            }
        }).build();
        b.setSslcontext( sslContext);

        // don't check Hostnames, either.
        //      -- use SSLConnectionSocketFactory.getDefaultHostnameVerifier(), if you don't want to weaken
        HostnameVerifier hostnameVerifier = SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;

        SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(sslContext, hostnameVerifier);
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.getSocketFactory())
                .register("https", sslSocketFactory)
                .build();

         PoolingHttpClientConnectionManager connMgr = new PoolingHttpClientConnectionManager( socketFactoryRegistry);
        b.setConnectionManager( connMgr);

         CloseableHttpClient client = b.build();
        return client;
    }

    /**
     * Send an HTTP request in a synchronous way
     * @param method Http Verbs
     * @param url URL
     * @param headers headers, may be null
     * @param body request body, may be null
     * @return Response from the server, null if error encountered
     * @throws UnsupportedEncodingException Invalid encoding
     * @throws KeyStoreException Key store error
     * @throws NoSuchAlgorithmException SSL Error
     * @throws KeyManagementException SSL error
     */
    public static RESTResponse sendHttpRequest(HTTPMethod method,String url, Map<String,String> headers, String body) throws UnsupportedEncodingException, KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        CloseableHttpResponse httpResponse = null;
        RESTResponse result=null;
        HttpRequestBase request=null;

         try (CloseableHttpClient httpclient = createHttpClient_AcceptsUntrustedCerts()) {

             switch(method)
            {
                case GET:
                    request=new HttpGet(url);
                     body=null; // securite
                    break;

                case PUT:
                    request=new HttpPut(url);
                    body=null; // securite
                    break;

                case POST:
                    request=new HttpPost(url);
                    break;

                case PATCH:
                    request=new HttpPatch(url);
                    break;

                case DELETE:
                    request=new HttpDelete(url);
                    break;

                default:
                    request=new HttpGet(url);
                    body=null; // securite
                    break;

            }
            configureHttpRequest(request,headers,body);

            httpResponse = httpclient.execute(request);

            HashMap<String, String> responseHeaders=convertHeadersToHashMap(httpResponse.getAllHeaders());
            int statusCode=httpResponse.getStatusLine().getStatusCode();
            HttpEntity entity = httpResponse.getEntity();
            byte[] content;

            if (null==entity) {
                content=new byte[0];
            }
            else {
                content = EntityUtils.toByteArray(entity);
            }
            result=new RESTResponse(statusCode,responseHeaders,content);
        }
        catch (ClientProtocolException e) {
            result=null;
        }
        catch (IOException e) {
            result=null;
        }
        return result;
    }
}
