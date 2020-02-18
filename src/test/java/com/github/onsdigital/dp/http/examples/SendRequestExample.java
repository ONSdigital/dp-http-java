package com.github.onsdigital.dp.http.examples;

import com.github.onsdigital.dp.http.RestClient;
import com.github.onsdigital.dp.http.RestClientImpl;
import com.github.onsdigital.dp.http.StatusCodeCheck;
import com.github.onsdigital.dp.http.errors.DpHttpException;
import com.google.gson.GsonBuilder;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;

import java.io.IOException;

import static com.github.onsdigital.dp.http.errors.DpHttpException.incorrectStatusException;

public class SendRequestExample {

    public static void main(String[] args) throws Exception {
        executeRequest();
    }

    static void executeRequest() throws DpHttpException, IOException {
        RestClient restClient = new RestClientImpl();

        HttpUriRequest req = createRequest();

        SimpleEntity entity = restClient.requestForObject(req, SimpleEntity.class, simpleEntityStatusCheck());
        System.out.println(new GsonBuilder().setPrettyPrinting().create().toJson(entity));
    }

    static StatusCodeCheck simpleEntityStatusCheck() {
        return (resp -> {
            int statusCode = resp.getStatusLine().getStatusCode();
            if (HttpStatus.SC_OK != statusCode) {
                throw incorrectStatusException(resp, HttpStatus.SC_OK);
            }
        });
    }

    static HttpUriRequest createRequest() {
        HttpUriRequest request = new HttpGet("http://localhost:6666/message");
        request.setHeader("Accept", "application/json");
        request.setHeader("Content-Type", "application/json");

        return request;
    }
}


