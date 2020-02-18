package com.github.onsdigital.dp.http.json;

import com.github.onsdigital.dp.http.errors.DpHttpException;
import com.google.gson.Gson;
import org.apache.http.client.methods.CloseableHttpResponse;

import java.io.InputStreamReader;

public class DefaultMarshaller implements Marshaller {

    @Override
    public <T> T getEntity(CloseableHttpResponse resp, Class<T> entityClass) throws DpHttpException {
        try (InputStreamReader reader = new InputStreamReader(resp.getEntity().getContent())) {
            return new Gson().fromJson(reader, entityClass);
        } catch (Exception ex) {
            throw new DpHttpException("error reading response body", ex);
        }
    }
}
