package com.github.onsdigital.dp.http;

import com.github.onsdigital.dp.http.errors.DpHttpException;
import org.apache.http.client.methods.CloseableHttpResponse;

@FunctionalInterface
public interface StatusCodeCheck {

    void verifyResponseStatusCode(CloseableHttpResponse resp) throws DpHttpException;
}
