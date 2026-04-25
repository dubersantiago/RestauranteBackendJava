package com.nvtc.restaurante_api.config;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.nvtc.restaurante_api.categorias.model.Categoria;
import com.nvtc.restaurante_api.categorias.repository.CategoriaRepository;

@Component
public class CategoriaSeeder implements CommandLineRunner {

    private final CategoriaRepository categoriaRepository;

    public CategoriaSeeder(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    @Override
    public void run(String... args) {
        List<String> categorias = List.of("Carnes", "Especiales", "Bebidas");

        for (String nombre : categorias) {
            if (!categoriaRepository.existsByNombre(nombre)) {
                Categoria categoria = new Categoria();
                categoria.nombre = nombre;
                categoriaRepository.save(categoria);
            }
        }
    }
}
