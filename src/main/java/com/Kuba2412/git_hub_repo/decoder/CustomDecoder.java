package com.Kuba2412.git_hub_repo.decoder;

import feign.Response;
import feign.RetryableException;
import feign.codec.ErrorDecoder;

import java.time.Instant;
import java.util.Date;

public class CustomDecoder implements ErrorDecoder {

    private final ErrorDecoder defaultErrorDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        if (response.status() == 503) {
            String message = "Service unavailable, please try again later.";
            return new RetryableException(
                    response.status(),
                    message,
                    response.request().httpMethod(),
                    Date.from(Instant.now().plusMillis(5000)),
                    response.request());
        }
        return defaultErrorDecoder.decode(methodKey, response);
    }
}