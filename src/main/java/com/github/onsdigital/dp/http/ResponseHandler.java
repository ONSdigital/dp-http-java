package com.github.onsdigital.dp.http;

import com.google.gson.Gson;
import org.apache.http.client.methods.CloseableHttpResponse;

import java.io.InputStreamReader;

public abstract class ResponseHandler<T> {

    private Gson gson;

    public ResponseHandler() {
        this.gson = new Gson();
    }

    public T getEntity(CloseableHttpResponse resp) throws ClientException {
        boolean isSuccessful = checkStatus(resp);
        if (!isSuccessful) {
            return null;
        }
        return extractEntity(resp);
    }

    private T extractEntity(CloseableHttpResponse resp) throws ClientException {
        try (InputStreamReader reader = new InputStreamReader(resp.getEntity().getContent())) {
            return gson.fromJson(reader, getType());
        } catch (Exception ex) {
            throw new ClientException("error reading response body", ex);
        }
    }

    protected abstract boolean checkStatus(CloseableHttpResponse response) throws ClientException;

    protected abstract Class<T> getType();
}
