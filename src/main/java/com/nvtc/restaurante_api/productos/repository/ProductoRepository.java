package com.nvtc.restaurante_api.productos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.nvtc.restaurante_api.productos.model.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
}