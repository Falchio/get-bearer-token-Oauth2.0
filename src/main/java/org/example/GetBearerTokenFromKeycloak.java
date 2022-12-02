package org.example;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

//{"access_token":"DAEBAEjK6MtgXhe6Yh+pohU+vcYd7gKQ8O3g59lah69rAisfLVZWuV7iYEM53VUfKi141OBHul/PE99VpOyCHLKz0xq+znKQXBfLbA==","expires_in":3600,"token_type":"Bearer"}

public class GetBearerTokenFromKeycloak {
    private static final String CLIENT_ID = "086a6b25-4a1d-4fe2-b717-c596e9c19c57";
    private static final URI URL = URI.create("http://localhost:7979/realms/token-realm/protocol/openid-connect/token");
//
//    URI: http://keycloak:8080/auth/realms/myrealm/protocol/openid-connect/token
//    Type: POST
//    Content-Type: application/x-www-form-urlencoded
//    grant_type:password
//    username:user
//    password:user_password
//    client_id:client_id
//    secret_id:client_secret

    public static void main(String[] args) throws IOException, InterruptedException {

        Map<String, String> params = new HashMap<>() {{
            put("Content-Type", "application/x-www-form-urlencoded");
            put("grant_type", "client_credentials");
            put("username", "user");
            put("password", "user");
            put("client_id", "token-service");
            put("secret_id", "xI9ZKXQ97iJ6Y1fBgIMbBaOK5xIGvLrC"); //TODO: репозиторий публичный
            put("response_type", "token");
        }};

        HttpRequest request = HttpRequest.newBuilder()
                .setHeader("User-Agent", "Java HttpClient")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .uri(URL)
                .POST(buildFormDataFromMap(params))
                .build();

        HttpClient httpClient = HttpClient.newHttpClient();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("status code: " + response.statusCode());
        System.out.println("response body: " + response.body());
    }

    private static HttpRequest.BodyPublisher buildFormDataFromMap(Map<String, String> data) {
        var builder = new StringBuilder();
        for (Map.Entry<String, String> entry : data.entrySet()) {
            if (builder.length() > 0) {
                builder.append("&");
            }
            builder.append(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8));
            builder.append("=");
            builder.append(URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8));
        }
        return HttpRequest.BodyPublishers.ofString(builder.toString());
    }
}