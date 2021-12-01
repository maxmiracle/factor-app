package org.maxvas.factorapp.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.maxvas.factorapp.entity.Factor;
import org.maxvas.factorapp.repository.FactorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@AllArgsConstructor
@Slf4j
public class FactorServiceImpl implements FactorService {
    private final static String GET_FACTOR_METRIC = "factors";
    private final static String STATUS_TAG = "status";
    private final static String STATUS_OK = "ok";
    private final static String STATUS_FAIL = "fail";
    private final FactorRepository factorRepository;
    private final MeterRegistry meterRegistry;
    private final String FACTOR_SERVICE = "factorService";

    @CircuitBreaker(name = FACTOR_SERVICE, fallbackMethod = "fallback")
    @TimeLimiter(name = FACTOR_SERVICE, fallbackMethod = "fallback")
    @Retry(name = FACTOR_SERVICE, fallbackMethod = "fallback")
    public CompletableFuture<List<Factor>> findAll() {
        return CompletableFuture.supplyAsync( () -> {
                    Timer.Sample sample = Timer.start();
                    try {
                        List<Factor> list = factorRepository.findAll();
                        sample.stop(meterRegistry.timer(GET_FACTOR_METRIC, STATUS_TAG, STATUS_OK));
                        return list;
                    } catch (Exception exception) {
                        log.error("Get all factors error.", exception);
                        sample.stop(meterRegistry.timer(GET_FACTOR_METRIC, STATUS_TAG, STATUS_FAIL));
                        throw exception;
                    }
                });
    }

    public CompletableFuture<List<Factor>> fallback(Exception ex){
        log.info("Fallback: {}", ex.getMessage());
        return CompletableFuture.completedFuture(List.of());
    }
}
