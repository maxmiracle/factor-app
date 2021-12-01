package org.maxvas.factorapp.rest;


import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.maxvas.factorapp.entity.Factor;
import org.maxvas.factorapp.service.FactorServiceImpl;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.ExecutionException;

@CrossOrigin
@RestController
@RequestMapping("/factor")
@AllArgsConstructor
@Slf4j
public class FactorController {

    private static final String FACTOR_SERVICE = "factorService" ;
    private final FactorServiceImpl factorService;

    @GetMapping(value = "/")
    @RateLimiter(name = FACTOR_SERVICE, fallbackMethod = "fallback")
    public List<Factor> findAll() throws ExecutionException, InterruptedException {
        return factorService.findAll().get();
    }

    public List<Factor> fallback(Exception ex){
        log.info("Controller fallback: {}", ex.getMessage());
        return List.of();
    }

}
