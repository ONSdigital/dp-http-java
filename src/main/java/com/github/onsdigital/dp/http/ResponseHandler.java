package com.github.onsdigital.dp.http;

import com.google.gson.Gson;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;

import java.io.InputStreamReader;

public abstract class ResponseHandler<T> {

    private Gson gson;

    public ResponseHandler() {
        this.gson = new Gson();
    }

    public T getEntity(CloseableHttpResponse resp) throws ClientException {
        checkStatus(resp.getStatusLine());
        return extractEntity(resp);
    }

    private T extractEntity(CloseableHttpResponse resp) throws ClientException {
        try (InputStreamReader reader = new InputStreamReader(resp.getEntity().getContent())) {
            return gson.fromJson(reader, getType());
        } catch (Exception ex) {
            throw new ClientException("error reading response body", ex);
        }
    }

    /**
     * Define how to check the response status and handle inccorrect statuses.
     *
     * @param statusLine the {@link StatusLine} field of {@link CloseableHttpResponse} returned to the client.
     * @throws ClientException throw if the actual response status does not match the expected status.
     */
    protected abstract void checkStatus(StatusLine statusLine) throws ClientException;

    /**
     * @return the expected {@link Class} to marshall the response body to.
     */
    protected abstract Class<T> getType();
}
