# dp-http-java

Library providing standardized HTTP functionality for Java apps - taking care of boilerplate code.

## Example

Create an instance of the client.
```
    JsonClient jsonClient = new JsonJsonClientImpl();
```

Create a HTTP request to execute
```
    HttpUriRequest request = new HttpGet("http://localhost:6666/message");
    request.setHeader("Accept", "application/json");
    request.setHeader("Content-Type", "application/json");
```

Send the request and get the response entity
```
    SimpleEntity entity = jsonClient.executeRequestForEntity(req, ResponseHandler<T>);
```

### `ResponseHandler<T>`

`ResponseHandler` is an abstract class providing functionality for handling the HTTP response returned to the
 client. Internally it handles extracting the JSON body of the response and marshalling into the required Java object
 . The are 2 abstract methods you must implement:

  - `void checkStatus(StatusLine statusLine) throws ClientException`
    - Define how to check the response status of your request and how to handle incorrect statuses.
  - `Class<T> getType()`
    - The type to marshal the response body to.

Example:

```java
ResponseHandler<SimpleEntity> simpleEntityHandler() {
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
```