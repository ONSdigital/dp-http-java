package com.github.onsdigital.dp.http;

import org.apache.http.client.methods.HttpUriRequest;

public interface JsonClient {

    int executeRequest(HttpUriRequest request) throws ClientException;

    <T> T executeRequestForEntity(HttpUriRequest request, ResponseHandler<T> responseHandler) throws ClientException;
}
