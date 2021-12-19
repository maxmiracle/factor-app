package org.maxvas.factorapp.service;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * Get resource from internet with retry.
 */
@Service
@EnableRetry
@Slf4j
public class DocumentServiceInternal {

    @Value("${retry.maxAttempts}")
    private int maxAttempts;

    @Value("${retry.maxDelay}")
    private int maxDelay;

    @Retryable(value = IOException.class,
            maxAttemptsExpression = "${retry.maxAttempts}",
            backoff = @Backoff(delayExpression = "${retry.maxDelay}"))
    public Document getDocumentRetryInternal(String url) throws IOException {
        log.info("{}", url);
        return Jsoup.connect(url).get();
    }
}