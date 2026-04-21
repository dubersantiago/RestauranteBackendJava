package com.nvtc.restaurante_api.categorias.services;

import org.springframework.stereotype.Service;

import com.nvtc.restaurante_api.categorias.dtos.CreateCategoriaRequest;
import com.nvtc.restaurante_api.categorias.model.Categoria;
import com.nvtc.restaurante_api.categorias.repository.CategoriaRepository;

@Service
public class CategoriaService {
    
    private final CategoriaRepository categoriaRepository;

    public CategoriaService(CategoriaRepository categoriaRepository){
        this.categoriaRepository=categoriaRepository;
    }

    public Categoria crear(CreateCategoriaRequest categoriaRequest){
        Categoria categoria = new Categoria();
        categoria.setNombre(categoriaRequest.getNombre());
        return categoriaRepository.save(categoria);
    }
}
