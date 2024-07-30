package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.util.concurrent.RateLimiter;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class CrptApi {

    private static final HttpClient httpClient = HttpClient.newBuilder().build();

    private static final URI uri;


    static {
        try {
            uri = new URI("https://javarush.com");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private static final ObjectMapper mapper = new ObjectMapper();

    private final TimeUnit timeUnit;
    private final int requestLimit;

    RateLimiter limiter;


    public CrptApi(TimeUnit timeUnit, int requestLimit) {
        this.timeUnit = timeUnit;
        this.requestLimit = requestLimit;
        System.out.println(Duration.of(1, timeUnit.toChronoUnit()).toSeconds());
        limiter = RateLimiter.create(requestLimit/Duration.of(1, timeUnit.toChronoUnit()).toSeconds());
    }

    public void accessApi(Map<String, String> requestBody) throws IOException, InterruptedException {
        String body = mapper.writeValueAsString(requestBody);
        while(true) {
            doPost(body);
            limiter.acquire();
        }

    }

    private HttpResponse<String> doPost(String body) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                        .uri(uri)
                        .POST(HttpRequest.BodyPublishers.ofString(body))
                        .build();
        System.out.println("Called api, time is " + LocalDateTime.now());
        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }
}