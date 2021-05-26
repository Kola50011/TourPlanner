package at.fhtw.client;

import feign.Feign;
import feign.Logger;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.okhttp.OkHttpClient;
import feign.optionals.OptionalDecoder;
import feign.slf4j.Slf4jLogger;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TourPlannerClientFactory {
    public static TourPlannerClient getClient() {
        // TODO Make Mockable
        return Feign.builder()
                .client(new OkHttpClient())
                .decoder(new OptionalDecoder(new JacksonDecoder()))
                .encoder(new JacksonEncoder())
                .decode404()
                .logger(new Slf4jLogger())
                .logLevel(Logger.Level.FULL)
                .target(TourPlannerClient.class, "http://127.0.0.1:8080");
    }
}
