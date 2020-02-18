package com.github.onsdigital.dp.http;

import com.github.onsdigital.dp.http.errors.DpHttpException;
import com.github.onsdigital.dp.http.examples.SimpleEntity;
import com.github.onsdigital.dp.http.json.Marshaller;
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

import static com.github.onsdigital.dp.http.RestClientImpl.ENTITY_CLASS_NULL_ERROR;
import static com.github.onsdigital.dp.http.RestClientImpl.EXECUTE_REQUEST_ERROR;
import static com.github.onsdigital.dp.http.RestClientImpl.REQUEST_NULL_ERROR;
import static com.github.onsdigital.dp.http.RestClientImpl.STATUS_CHECKER_NULL_ERROR;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RestClientImplTest {

    @Mock
    private CloseableHttpClient closeableHttpClient;

    @Mock
    private CloseableHttpResponse closeableHttpResponse;

    @Mock
    private HttpUriRequest request;

    @Mock
    private StatusCodeCheck statusCodeCheck;

    @Mock
    private Marshaller marshaller;

    private RestClient restClient;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        restClient = new RestClientImpl(() -> closeableHttpClient, marshaller);
    }

    @Test(expected = DpHttpException.class)
    public void executeRequest_requestIsNull_shouldThrowException() throws DpHttpException {
        try {
            restClient.executeRequest(null);
        } catch (DpHttpException ex) {
            assertThat(ex.getMessage(), equalTo(REQUEST_NULL_ERROR));
            throw ex;
        }
    }

    @Test(expected = DpHttpException.class)
    public void executeRequest_httpClientThrowsException_shouldCatchAndRethow() throws Exception {
        when(closeableHttpClient.execute(request))
                .thenThrow(new IOException("BANG"));

        try {
            restClient.executeRequest(request);
        } catch (DpHttpException ex) {
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

        int responseStatus = restClient.executeRequest(request);

        assertThat(responseStatus, equalTo(HttpStatus.SC_OK));
    }

    @Test(expected = DpHttpException.class)
    public void requestForObject_requestNull_shouldThrowException() throws DpHttpException {
        try {
            restClient.requestForObject(null, null, null);
        } catch (DpHttpException ex) {
            assertThat(ex.getMessage(), equalTo(REQUEST_NULL_ERROR));
            throw ex;
        }
    }

    @Test(expected = DpHttpException.class)
    public void requestForObject_entityNull_shouldThrowException() throws DpHttpException {
        try {
            restClient.requestForObject(request, null, null);
        } catch (DpHttpException ex) {
            assertThat(ex.getMessage(), equalTo(ENTITY_CLASS_NULL_ERROR));
            throw ex;
        }
    }

    @Test(expected = DpHttpException.class)
    public void requestForObject_statusCheckerNukk_shouldThrowException() throws DpHttpException {
        try {
            restClient.requestForObject(request, SimpleEntity.class, null);
        } catch (DpHttpException ex) {
            assertThat(ex.getMessage(), equalTo(STATUS_CHECKER_NULL_ERROR));
            throw ex;
        }
    }

    @Test(expected = DpHttpException.class)
    public void requestForObject_statusCheckThrowsException_shouldThrowException() throws Exception {
        when(closeableHttpClient.execute(request))
                .thenReturn(closeableHttpResponse);

        DpHttpException expected = new DpHttpException("Kerplunk!");
        doThrow(expected)
                .when(statusCodeCheck)
                .verifyResponseStatusCode(closeableHttpResponse);

        try {
            restClient.requestForObject(request, SimpleEntity.class, statusCodeCheck);
        } catch (DpHttpException ex) {
            assertThat(ex.getMessage(), equalTo(expected.getMessage()));
            throw ex;
        }
    }

    @Test(expected = DpHttpException.class)
    public void requestForObject_marshallerException_shouldThrowException() throws Exception {
        when(closeableHttpClient.execute(request))
                .thenReturn(closeableHttpResponse);

        doNothing()
                .when(statusCodeCheck)
                .verifyResponseStatusCode(closeableHttpResponse);

        when(marshaller.getEntity(closeableHttpResponse, SimpleEntity.class))
                .thenThrow(new DpHttpException("BIG EXPLOSION"));

        try {
            restClient.requestForObject(request, SimpleEntity.class, statusCodeCheck);
        } catch (DpHttpException ex) {
            assertThat(ex.getMessage(), equalTo("BIG EXPLOSION"));
            throw ex;
        }
    }

    @Test
    public void requestForObject_success_shouldReturnEntity() throws Exception {
        when(closeableHttpClient.execute(request))
                .thenReturn(closeableHttpResponse);

        doNothing()
                .when(statusCodeCheck)
                .verifyResponseStatusCode(closeableHttpResponse);

        SimpleEntity expected = new SimpleEntity("Hello world");

        when(marshaller.getEntity(closeableHttpResponse, SimpleEntity.class))
                .thenReturn(expected);

        SimpleEntity actual = restClient.requestForObject(request, SimpleEntity.class, statusCodeCheck);

        assertThat(actual, equalTo(expected));
    }
}
