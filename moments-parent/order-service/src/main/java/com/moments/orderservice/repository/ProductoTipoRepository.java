package com.moments.orderservice.repository;

import com.moments.orderservice.model.ProductoTipo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoTipoRepository extends JpaRepository<ProductoTipo, Long> {
    List<ProductoTipo> findByIdProducto(Long id);
}
