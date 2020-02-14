package com.github.onsdigital.dp.httpclient;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;

public interface Client {

    CloseableHttpResponse executeRequest(HttpUriRequest request) throws HttpClientException;
}
