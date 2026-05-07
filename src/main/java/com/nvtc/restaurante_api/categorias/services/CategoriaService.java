package com.nvtc.restaurante_api.categorias.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.nvtc.restaurante_api.categorias.dtos.CategoriaResponseDTO;
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

    public List<CategoriaResponseDTO> Listar(){
        List<Categoria> categorias = categoriaRepository.findAll();
        List<CategoriaResponseDTO> respuesta = new ArrayList<>();

        for (Categoria categoria : categorias) {
            respuesta.add(toDTO(categoria));
        }

        return respuesta;
    }

    public CategoriaResponseDTO toDTO(Categoria categoria) {
        CategoriaResponseDTO dto = new CategoriaResponseDTO();
        dto.setId(categoria.getId());
        dto.setNombre(categoria.getNombre());
        return dto;
    }
}
