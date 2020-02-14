package com.github.onsdigital.dp.httpclient;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

import static com.github.onsdigital.dp.httpclient.ClientImpl.CLIENT_NULL_ERROR;
import static com.github.onsdigital.dp.httpclient.ClientImpl.EXECUTE_REQUEST_ERROR;
import static com.github.onsdigital.dp.httpclient.ClientImpl.REQUEST_NULL_ERROR;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

public class ClientImplTest {

    @Mock
    private CloseableHttpClient closeableHttpClient;

    @Mock
    private CloseableHttpResponse closeableHttpResponse;

    @Mock
    private HttpUriRequest request;

    private Client client;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        client = new ClientImpl(() -> closeableHttpClient);
    }

    @Test(expected = HttpClientException.class)
    public void executeRequest_requestIsNull_shouldThrowException() throws HttpClientException {
        try {
            client.executeRequest(null);
        } catch (HttpClientException ex) {
            assertThat(ex.getMessage(), equalTo(REQUEST_NULL_ERROR));
            throw ex;
        }
    }

    @Test(expected = HttpClientException.class)
    public void executeRequest_httpClientSupplierReturnsNull_shouldThrowException() throws HttpClientException {
        client = new ClientImpl(() -> null);

        try {
            client.executeRequest(request);
        } catch (HttpClientException ex) {
            assertThat(ex.getMessage(), equalTo(CLIENT_NULL_ERROR));
            throw ex;
        }
    }

    @Test(expected = HttpClientException.class)
    public void executeRequest_httpClientThrowsException_shouldCatchAndRethow() throws Exception {
        when(closeableHttpClient.execute(request))
                .thenThrow(new IOException("BANG"));

        try {
            client.executeRequest(request);
        } catch (HttpClientException ex) {
            assertThat(ex.getMessage(), equalTo(EXECUTE_REQUEST_ERROR));
            throw ex;
        }
    }

    @Test
    public void executeRequest_success_shouldReturnCloseableHttpResponse() throws Exception {
        when(closeableHttpClient.execute(request))
                .thenReturn(closeableHttpResponse);

        CloseableHttpResponse response = client.executeRequest(request);

        assertThat(response, equalTo(closeableHttpResponse));
    }
}
