package org.example;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class SendRequestToServer {
    private static final String BEARER_TOKEN = "DAEBACD6Xah0z/ZmhwjiOQvxsMFmC0+JAq1ka43mTaS7hyVOxyu5rspzWdpsBTOozNBVKltyfh6fNf1cFsk7aZrtAjiuzy+lqI5dMg=="; //TODO
    private static final String CLIENT_ID = "107264187";
    private static final URI URL = URI.create(
            "http://localhost:8080/token-service/api/hello"
    );

    public static void main(String[] args) throws IOException, InterruptedException {
        String token = "IQAAAACy0w2dAAAZS5eSi7mJ8fZrjpPQbWAthDcdDtBCzso4bisn31ihVELp8c_8G6tFyhKm3zm9VXFRvjAFlb_97QHPwKEiG1cNbdyEmsa7AqMo00";
        String data = "\"PAYLOAD\"";
        HttpRequest.BodyPublisher bodyPublisher = createMessage(token, data);


        HttpRequest request = HttpRequest.newBuilder()
                .setHeader("User-Agent", "Java HttpClient")
                .header("Authorization", String.format("Bearer %s", BEARER_TOKEN))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .uri(URL)
                .POST(bodyPublisher)
                .build();

        HttpClient httpClient = HttpClient.newHttpClient();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("status code: " + response.statusCode());
        System.out.println("response body: " + response.body());
    }

// при успехе
//    status code: 200
//    response body: {"code":"80000000","msg":"Success","requestId":"166921245184434306000109"}

    //     Истек Bearer Token
//    status code: 200
//    response body: {"code":"80200003","msg":"Access token expired","requestId":"166921322423006990000109"}

    // токены пользователей не действительны
//    status code: 200
//    response body: {"code":"80300007","msg":"All the tokens are invalid","requestId":"166921833168303910000309"}
    private static HttpRequest.BodyPublisher createMessage(String token, String data) {
        String message = String.format(
                "{\"validate_only\": false,\"message\":{\"data\":%s,\"token\":[\"%s\"]}}",
                data, token
        );
        System.out.println(message);
        return HttpRequest.BodyPublishers.ofString(message);
    }
}
