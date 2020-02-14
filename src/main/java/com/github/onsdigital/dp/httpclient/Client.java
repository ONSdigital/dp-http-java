package com.github.onsdigital.dp.httpclient;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;

public interface Client {

    /**
     * Execute the provided request.
     *
     * @param request the {@link HttpUriRequest} to execute.
     * @return if executing the request is successful returns a {@link CloseableHttpResponse}. The caller is
     * responsible for ensuring the response is closed.
     * @throws HttpClientException thrown if the request is invalid or there is an error executing the request.
     */
    CloseableHttpResponse executeRequest(HttpUriRequest request) throws HttpClientException;
}
