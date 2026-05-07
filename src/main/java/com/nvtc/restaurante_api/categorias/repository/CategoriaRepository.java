package com.nvtc.restaurante_api.categorias.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nvtc.restaurante_api.categorias.model.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    boolean existsByNombre(String nombre);
}
