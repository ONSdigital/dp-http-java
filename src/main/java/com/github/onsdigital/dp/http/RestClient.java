package com.github.onsdigital.dp.http;

import com.github.onsdigital.dp.http.errors.DpHttpException;
import org.apache.http.client.methods.HttpUriRequest;

public interface RestClient {

    int executeRequest(HttpUriRequest request) throws DpHttpException;

    <T> T requestForObject(HttpUriRequest request, Class<T> entityClass, StatusCodeCheck statusCheck) throws DpHttpException;
}
