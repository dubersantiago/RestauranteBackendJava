package com.nvtc.restaurante_api.productos.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
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
import com.nvtc.restaurante_api.exceptions.GlobalExceptionHandler;
import com.nvtc.restaurante_api.productos.dtos.CreateProductRequest;
import com.nvtc.restaurante_api.productos.dtos.ProductoResponseDTO;
import com.nvtc.restaurante_api.productos.services.ProductoService;

@ExtendWith(MockitoExtension.class)
class ProductoControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ProductoService productoService;

    @InjectMocks
    private ProductoController productoController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(productoController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void listar_retornaListaDeProductos() throws Exception {
        ProductoResponseDTO dto = new ProductoResponseDTO();
        dto.setId(1L);
        dto.setNombre("Coca Cola");
        dto.setPrecio(1500.0);
        dto.setStock(10);

        when(productoService.listar()).thenReturn(List.of(dto));

        mockMvc.perform(get("/productos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].nombre").value("Coca Cola"));
    }

    @Test
    void probar_retornaMensajeConId() throws Exception {
        mockMvc.perform(get("/productos/5"))
                .andExpect(status().isOk())
                .andExpect(content().string("app funcionando id =5"));
    }

    @Test
    void crear_retornaProductoCreado() throws Exception {
        CreateProductRequest request = new CreateProductRequest();
        request.setNombre("Pizza");
        request.setPrecio(5000.0);
        request.setStock(3);

        ProductoResponseDTO dto = new ProductoResponseDTO();
        dto.setId(1L);
        dto.setNombre("Pizza");
        dto.setPrecio(5000.0);
        dto.setStock(3);

        when(productoService.guardar(any(CreateProductRequest.class))).thenReturn(dto);

        mockMvc.perform(post("/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Pizza"))
                .andExpect(jsonPath("$.precio").value(5000.0));
    }

    @Test
    void crear_cuandoCategoriaNoExiste_retorna400() throws Exception {
        CreateProductRequest request = new CreateProductRequest();
        request.setNombre("Producto");
        request.setCategoriaId(999L);

        when(productoService.guardar(any(CreateProductRequest.class)))
                .thenThrow(new RuntimeException("Categoria no encontrada"));

        mockMvc.perform(post("/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Categoria no encontrada"));
    }

    @Test
    void editar_cuandoProductoNoExiste_retorna400() throws Exception {
        CreateProductRequest request = new CreateProductRequest();
        request.setNombre("Inexistente");

        when(productoService.editar(eq(999L), any(CreateProductRequest.class)))
                .thenThrow(new RuntimeException("Producto no encontrado"));

        mockMvc.perform(put("/productos/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Producto no encontrado"));
    }

    @Test
    void editar_retornaProductoEditado() throws Exception {
        CreateProductRequest request = new CreateProductRequest();
        request.setNombre("Pizza Actualizada");
        request.setPrecio(6000.0);
        request.setStock(5);

        ProductoResponseDTO dto = new ProductoResponseDTO();
        dto.setId(1L);
        dto.setNombre("Pizza Actualizada");
        dto.setPrecio(6000.0);
        dto.setStock(5);

        when(productoService.editar(eq(1L), any(CreateProductRequest.class))).thenReturn(dto);

        mockMvc.perform(put("/productos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Pizza Actualizada"))
                .andExpect(jsonPath("$.precio").value(6000.0));
    }
}
