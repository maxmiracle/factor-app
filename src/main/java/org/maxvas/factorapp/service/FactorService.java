package org.maxvas.factorapp.service;

import org.maxvas.factorapp.entity.Factor;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface FactorService {
    CompletableFuture<List<Factor>> findAll();
}
