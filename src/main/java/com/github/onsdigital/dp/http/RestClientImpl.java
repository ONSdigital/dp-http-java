package com.github.onsdigital.dp.http;

import com.github.onsdigital.dp.http.errors.DpHttpException;
import com.github.onsdigital.dp.http.json.DefaultMarshaller;
import com.github.onsdigital.dp.http.json.Marshaller;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.util.function.Supplier;

public class RestClientImpl implements RestClient {

    static final String EXECUTE_REQUEST_ERROR = "error executing request";
    static final String REQUEST_NULL_ERROR = "HttpUriRequest expected but was null";
    static final String ENTITY_CLASS_NULL_ERROR = "Class type for response entity required but was null";
    static final String STATUS_CHECKER_NULL_ERROR = "StatusCodeCheck required but was null";

    private Supplier<CloseableHttpClient> httpClientSupplier;
    private Marshaller marshaller;

    /**
     * Create a new instance of the Client.
     *
     * @param httpClientSupplier a {@link Supplier} that provides the {@link CloseableHttpClient} to use when
     *                           executing requests.
     */
    public RestClientImpl(final Supplier<CloseableHttpClient> httpClientSupplier, Marshaller marshaller) {
        this.httpClientSupplier = httpClientSupplier;
        this.marshaller = marshaller;

    }

    /**
     * Construct a new instance of the client using the default configuration.
     */
    public RestClientImpl() {
        this(() -> HttpClients.createDefault(), new DefaultMarshaller());
    }

    @Override
    public int executeRequest(HttpUriRequest request) throws DpHttpException {
        validateParameters(request);

        try (
                CloseableHttpClient httpClient = httpClientSupplier.get();
                CloseableHttpResponse response = httpClient.execute(request)
        ) {
            return response.getStatusLine().getStatusCode();
        } catch (Exception ex) {
            throw new DpHttpException(EXECUTE_REQUEST_ERROR, ex);
        }
    }

    protected void validateParameters(HttpUriRequest request) throws DpHttpException {
        if (request == null) {
            throw new DpHttpException(REQUEST_NULL_ERROR);
        }
    }

    @Override
    public <T> T requestForObject(HttpUriRequest request, Class<T> entityClass, StatusCodeCheck statusCheck)
            throws DpHttpException {
        validateParameters(request, entityClass, statusCheck);

        try (
                CloseableHttpClient httpClient = httpClientSupplier.get();
                CloseableHttpResponse response = httpClient.execute(request)
        ) {
            statusCheck.verifyResponseStatusCode(response);
            return marshaller.getEntity(response, entityClass);
        } catch (DpHttpException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new DpHttpException(EXECUTE_REQUEST_ERROR, ex);
        }
    }

    protected <T> void validateParameters(HttpUriRequest request, Class<T> entityClass, StatusCodeCheck statusCheck)
            throws DpHttpException {
        if (request == null) {
            throw new DpHttpException(REQUEST_NULL_ERROR);
        }

        if (entityClass == null) {
            throw new DpHttpException(ENTITY_CLASS_NULL_ERROR);
        }

        if (statusCheck == null) {
            throw new DpHttpException(STATUS_CHECKER_NULL_ERROR);
        }
    }
}
