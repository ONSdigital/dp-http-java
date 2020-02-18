package com.github.onsdigital.dp.http.json;

import com.github.onsdigital.dp.http.errors.DpHttpException;
import org.apache.http.client.methods.CloseableHttpResponse;

@FunctionalInterface
public interface Marshaller {

    <T> T getEntity(CloseableHttpResponse resp, Class<T> entityClass) throws DpHttpException;
}
