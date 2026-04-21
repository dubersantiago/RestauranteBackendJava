package com.nvtc.restaurante_api.categorias.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nvtc.restaurante_api.categorias.dtos.CreateCategoriaRequest;
import com.nvtc.restaurante_api.categorias.model.Categoria;
import com.nvtc.restaurante_api.categorias.services.CategoriaService;

@RestController
@RequestMapping("/categorias")
public class CategoriasController {
    private final CategoriaService categoriaService;

    public CategoriasController(CategoriaService categoriaService){
        this.categoriaService=categoriaService;
    }

    @PostMapping(consumes = "application/json")
    public Categoria crear(@RequestBody CreateCategoriaRequest categoriaRequest){
        return categoriaService.crear(categoriaRequest);
    }
}
