package com.github.onsdigital.dp.httpclient;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.util.function.Supplier;

public class ClientImpl implements Client {

    private static final String EXECUTE_REQUEST_ERROR = "error executing request";
    private static final String CLIENT_NULL_ERROR = "error executing request CloseableHttpClient required but was null";

    private Supplier<CloseableHttpClient> httpClientSupplier;

    public ClientImpl(final Supplier<CloseableHttpClient> httpClientSupplier) {
        this.httpClientSupplier = httpClientSupplier;
    }

    public ClientImpl() {
        this(() -> HttpClients.createDefault());
    }

    @Override
    public CloseableHttpResponse executeRequest(HttpUriRequest request) throws HttpClientException {
        validateParameters(request);

        try {
            return sendRequest(request);
        } catch (HttpClientException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new HttpClientException(EXECUTE_REQUEST_ERROR, ex);
        }
    }

    private void validateParameters(HttpUriRequest request) throws HttpClientException {
        if (request == null) {
            throw new HttpClientException("HttpUriRequest expected but was null");
        }
    }

    private CloseableHttpResponse sendRequest(HttpUriRequest request) throws HttpClientException, IOException {
        try (CloseableHttpClient httpClient = httpClientSupplier.get()) {
            validateCloseableHttpClient(httpClient);
            return httpClient.execute(request);
        }
    }

    private void validateCloseableHttpClient(CloseableHttpClient client) throws HttpClientException {
        if (client == null) {
            throw new HttpClientException(CLIENT_NULL_ERROR);
        }
    }
}
