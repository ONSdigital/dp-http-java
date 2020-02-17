package com.github.onsdigital.dp.http;

import org.apache.http.StatusLine;

@FunctionalInterface
public interface StatusChecker {

    boolean check(StatusLine statusLine) throws ClientException;
}
