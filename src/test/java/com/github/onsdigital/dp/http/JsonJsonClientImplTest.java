package com.github.onsdigital.dp.http;

import com.github.onsdigital.dp.http.examples.SimpleEntity;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

import static com.github.onsdigital.dp.http.JsonJsonClientImpl.EXECUTE_REQUEST_ERROR;
import static com.github.onsdigital.dp.http.JsonJsonClientImpl.REQUEST_NULL_ERROR;
import static com.github.onsdigital.dp.http.JsonJsonClientImpl.RESPONSE_HANDLER_EMPTY_ERROR;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class JsonJsonClientImplTest {

    @Mock
    private CloseableHttpClient closeableHttpClient;

    @Mock
    private CloseableHttpResponse closeableHttpResponse;

    @Mock
    private ResponseHandler<SimpleEntity> responseHandler;

    @Mock
    private HttpUriRequest request;

    private JsonClient jsonClient;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        jsonClient = new JsonJsonClientImpl(() -> closeableHttpClient);
    }

    @Test(expected = ClientException.class)
    public void executeRequest_requestIsNull_shouldThrowException() throws ClientException {
        try {
            jsonClient.executeRequest(null);
        } catch (ClientException ex) {
            assertThat(ex.getMessage(), equalTo(REQUEST_NULL_ERROR));
            throw ex;
        }
    }

    @Test(expected = ClientException.class)
    public void executeRequest_httpClientThrowsException_shouldCatchAndRethow() throws Exception {
        when(closeableHttpClient.execute(request))
                .thenThrow(new IOException("BANG"));

        try {
            jsonClient.executeRequest(request);
        } catch (ClientException ex) {
            assertThat(ex.getMessage(), equalTo(EXECUTE_REQUEST_ERROR));
            throw ex;
        }
    }

    @Test
    public void executeRequest_success_shouldReturnStatus() throws Exception {
        StatusLine statusLine = mock(StatusLine.class);

        when(closeableHttpClient.execute(request))
                .thenReturn(closeableHttpResponse);
        when(closeableHttpResponse.getStatusLine())
                .thenReturn(statusLine);
        when(statusLine.getStatusCode())
                .thenReturn(HttpStatus.SC_OK);

        int responseStatus = jsonClient.executeRequest(request);

        assertThat(responseStatus, equalTo(HttpStatus.SC_OK));
    }

    @Test(expected = ClientException.class)
    public void executeRequestForEntity_requestNull_shouldThrowException() throws Exception {
        try {
            jsonClient.executeRequestForEntity(null, null);
        } catch (ClientException ex) {
            assertThat(ex.getMessage(), equalTo(REQUEST_NULL_ERROR));
            throw ex;
        }
    }

    @Test(expected = ClientException.class)
    public void executeRequestForEntity_typeNull_shouldThrowException() throws Exception {
        try {
            jsonClient.executeRequestForEntity(request, null);
        } catch (ClientException ex) {
            assertThat(ex.getMessage(), equalTo(RESPONSE_HANDLER_EMPTY_ERROR));
            throw ex;
        }
    }

    @Test(expected = ClientException.class)
    public void executeRequestForEntity_httpClientError_shouldThrowException() throws Exception {
        jsonClient = new JsonJsonClientImpl(() -> closeableHttpClient);

        IOException actual = new IOException("Kerplunk");
        when(closeableHttpClient.execute(request))
                .thenThrow(actual);

        try {
            jsonClient.executeRequestForEntity(request, responseHandler);
        } catch (ClientException ex) {
            assertThat(ex.getCause(), equalTo(actual));
            assertThat(ex.getMessage(), equalTo(EXECUTE_REQUEST_ERROR));
            throw ex;
        }
    }

    @Test(expected = ClientException.class)
    public void executeRequestForEntity_responseHandlerError_shouldThrowException() throws Exception {
        jsonClient = new JsonJsonClientImpl(() -> closeableHttpClient);

        when(closeableHttpClient.execute(request))
                .thenReturn(closeableHttpResponse);

        ClientException actualEx = new ClientException("Boom");

        when(responseHandler.getEntity(closeableHttpResponse))
                .thenThrow(actualEx);

        try {
            jsonClient.executeRequestForEntity(request, responseHandler);
        } catch (ClientException ex) {
            assertThat(ex, equalTo(actualEx));
            throw ex;
        }
    }

    @Test
    public void executeRequestForEntity_success_shouldReturnEntity() throws Exception {
        jsonClient = new JsonJsonClientImpl(() -> closeableHttpClient);

        when(closeableHttpClient.execute(request))
                .thenReturn(closeableHttpResponse);

        SimpleEntity entity = new SimpleEntity("Hello world");
        when(responseHandler.getEntity(closeableHttpResponse))
                .thenReturn(entity);

        SimpleEntity actual = jsonClient.executeRequestForEntity(request, responseHandler);

        assertThat(actual, equalTo(entity));
    }
}
