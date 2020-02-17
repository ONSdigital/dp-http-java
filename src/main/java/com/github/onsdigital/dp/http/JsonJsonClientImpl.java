package com.github.onsdigital.dp.http;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.util.function.Supplier;

public class JsonJsonClientImpl implements JsonClient {

    static final String EXECUTE_REQUEST_ERROR = "error executing request";
    static final String REQUEST_NULL_ERROR = "HttpUriRequest expected but was null";
    static final String RESPONSE_HANDLER_EMPTY_ERROR = "response handler required but was null";

    private Supplier<CloseableHttpClient> httpClientSupplier;

    /**
     * Create a new instance of the Client.
     *
     * @param httpClientSupplier a {@link Supplier} that provides the {@link CloseableHttpClient} to use when
     *                           executing requests.
     */
    public JsonJsonClientImpl(final Supplier<CloseableHttpClient> httpClientSupplier) {
        this.httpClientSupplier = httpClientSupplier;
    }

    /**
     * Construct a new instance of the client using the default configuration.
     */
    public JsonJsonClientImpl() {
        this(() -> HttpClients.createDefault());
    }

    @Override
    public int executeRequest(HttpUriRequest request) throws ClientException {
        validateParameters(request);

        try (
                CloseableHttpClient httpClient = httpClientSupplier.get();
                CloseableHttpResponse response = httpClient.execute(request)
        ) {
            return response.getStatusLine().getStatusCode();
        } catch (Exception ex) {
            throw new ClientException(EXECUTE_REQUEST_ERROR, ex);
        }
    }

    private void validateParameters(HttpUriRequest request) throws ClientException {
        if (request == null) {
            throw new ClientException(REQUEST_NULL_ERROR);
        }
    }

    @Override
    public <T> T executeRequestForEntity(HttpUriRequest request, ResponseHandler<T> responseHandler) throws ClientException {
        validateParameters(request, responseHandler);

        try (
                CloseableHttpClient httpClient = httpClientSupplier.get();
                CloseableHttpResponse response = httpClient.execute(request)
        ) {
            return responseHandler.getEntity(response);
        } catch (ClientException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ClientException(EXECUTE_REQUEST_ERROR, ex);
        }
    }

    private <T> void validateParameters(HttpUriRequest request, ResponseHandler<T> responseHandler) throws ClientException {
        if (request == null) {
            throw new ClientException(REQUEST_NULL_ERROR);
        }

        if (responseHandler == null) {
            throw new ClientException(RESPONSE_HANDLER_EMPTY_ERROR);
        }
    }
}
