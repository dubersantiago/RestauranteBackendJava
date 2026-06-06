package com.nvtc.restaurante_api.categorias.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nvtc.restaurante_api.categorias.dtos.CategoriaResponseDTO;
import com.nvtc.restaurante_api.categorias.dtos.CreateCategoriaRequest;
import com.nvtc.restaurante_api.categorias.model.Categoria;
import com.nvtc.restaurante_api.categorias.services.CategoriaService;
import com.nvtc.restaurante_api.exceptions.GlobalExceptionHandler;

@ExtendWith(MockitoExtension.class)
class CategoriasControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CategoriaService categoriaService;

    @InjectMocks
    private CategoriasController categoriasController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(categoriasController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void listar_retornaListaDeCategorias() throws Exception {
        CategoriaResponseDTO dto = new CategoriaResponseDTO();
        dto.setId(1L);
        dto.setNombre("Bebidas");

        when(categoriaService.Listar()).thenReturn(List.of(dto));

        mockMvc.perform(get("/categorias"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].nombre").value("Bebidas"));
    }

    @Test
    void crear_retornaCategoriaCreada() throws Exception {
        CreateCategoriaRequest request = new CreateCategoriaRequest();
        request.setNombre("Postres");

        Categoria categoria = new Categoria();
        categoria.setId(2L);
        categoria.setNombre("Postres");

        when(categoriaService.crear(request)).thenReturn(categoria);

        mockMvc.perform(post("/categorias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Postres"));
    }

    @Test
    void actualizar_cuandoNoExiste_retorna400() throws Exception {
        CreateCategoriaRequest request = new CreateCategoriaRequest();
        request.setNombre("Inexistente");

        when(categoriaService.actualizar(999L, request))
                .thenThrow(new RuntimeException("Categoria no encontrada con ID: 999"));

        mockMvc.perform(put("/categorias/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Categoria no encontrada con ID: 999"));
    }

    @Test
    void actualizar_retornaCategoriaActualizada() throws Exception {
        CreateCategoriaRequest request = new CreateCategoriaRequest();
        request.setNombre("Bebidas Actualizado");

        CategoriaResponseDTO dto = new CategoriaResponseDTO();
        dto.setId(1L);
        dto.setNombre("Bebidas Actualizado");

        when(categoriaService.actualizar(1L, request)).thenReturn(dto);

        mockMvc.perform(put("/categorias/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Bebidas Actualizado"));
    }
}
