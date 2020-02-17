package com.github.onsdigital.dp.http.examples;

import com.github.onsdigital.dp.http.ClientException;
import com.github.onsdigital.dp.http.JsonClient;
import com.github.onsdigital.dp.http.JsonJsonClientImpl;
import com.github.onsdigital.dp.http.ResponseHandler;
import com.google.gson.GsonBuilder;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;

import java.io.IOException;

public class SendRequestExample {

    public static void main(String[] args) throws Exception {
        executeRequest();
    }

    static void executeRequest() throws ClientException, IOException {
        JsonClient jsonClient = new JsonJsonClientImpl();

        HttpUriRequest req = createRequest();
        SimpleEntity entity = jsonClient.executeRequestForEntity(req, simpleEntityHandler());

        System.out.println(new GsonBuilder().setPrettyPrinting().create().toJson(entity));
    }

    static HttpUriRequest createRequest() {
        HttpUriRequest request = new HttpGet("http://localhost:6666/message");
        request.setHeader("Accept", "application/json");
        request.setHeader("Content-Type", "application/json");

        return request;
    }

    static ResponseHandler<SimpleEntity> simpleEntityHandler() {
        return new ResponseHandler<SimpleEntity>() {
            @Override
            protected void checkStatus(StatusLine statusLine) throws ClientException {
                if (statusLine.getStatusCode() != 200) {
                    throw new ClientException("incorrects status code returned");
                }
            }

            @Override
            protected Class<SimpleEntity> getType() {
                return SimpleEntity.class;
            }
        };
    }

}


