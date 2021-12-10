package org.maxvas.factorapp.repository;

import org.maxvas.factorapp.entity.Factor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(path = "rest")
public interface FactorRepository extends CrudRepository<Factor, Integer> {
    List<Factor> findAll();
    Factor findByLink(String link);
}
