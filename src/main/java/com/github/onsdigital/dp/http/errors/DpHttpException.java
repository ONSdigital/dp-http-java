package com.github.onsdigital.dp.http.errors;

import org.apache.http.client.methods.CloseableHttpResponse;

import static java.text.MessageFormat.format;

public class DpHttpException extends Exception {

    public DpHttpException() {
    }

    public DpHttpException(final String message) {
        super(message);
    }

    public DpHttpException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public DpHttpException(final Throwable cause) {
        super(cause);
    }

    public DpHttpException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public static DpHttpException incorrectStatusException(CloseableHttpResponse resp, int expected) {
        String message = format("incorrect status code returned expected: {0}, actual: {1}", expected,
                resp.getStatusLine().getStatusCode());

        return new DpHttpException(message);
    }
}
