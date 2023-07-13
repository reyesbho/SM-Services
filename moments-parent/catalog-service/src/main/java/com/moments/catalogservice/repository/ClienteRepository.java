package com.moments.catalogservice.repository;

import com.moments.catalogservice.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    List<Cliente> findByNombreContainingIgnoreCaseOrApellidoPaternoContainingIgnoreCase(String client, String apellido);
}
