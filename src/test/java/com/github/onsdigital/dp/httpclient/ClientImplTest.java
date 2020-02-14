package com.github.onsdigital.dp.httpclient;

import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class ClientImplTest {

    @Mock
    private CloseableHttpClient closeableHttpClient;

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
            assertThat(ex.getMessage(), equalTo("HttpUriRequest expected but was null"));
            throw ex;
        }
    }

    @Test(expected = HttpClientException.class)
    public void executeRequest_httpClientSupplierReturnsNull_shouldThrowException() throws HttpClientException {
        client = new ClientImpl(() -> null);

        try {
            client.executeRequest(request);
        } catch (HttpClientException ex) {
            assertThat(ex.getMessage(), equalTo("error executing request CloseableHttpClient required but was null"));
            throw ex;
        }
    }
}
