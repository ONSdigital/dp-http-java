package com.github.onsdigital.dp.httpclient;

public class HttpClientException extends Exception {

    public HttpClientException() {
    }

    public HttpClientException(final String message) {
        super(message);
    }

    public HttpClientException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public HttpClientException(final Throwable cause) {
        super(cause);
    }

    public HttpClientException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
