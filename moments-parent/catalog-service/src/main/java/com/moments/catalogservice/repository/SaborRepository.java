package com.moments.catalogservice.repository;

import com.moments.catalogservice.model.Sabor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SaborRepository extends JpaRepository<Sabor, Long> {
}
