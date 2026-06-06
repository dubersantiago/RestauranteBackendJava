package com.nvtc.restaurante_api.pedido.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
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
import com.nvtc.restaurante_api.exceptions.ItemAgotadoException;
import com.nvtc.restaurante_api.pedido.dtos.CreatePedidoDTO;
import com.nvtc.restaurante_api.pedido.dtos.DetalleRequest;
import com.nvtc.restaurante_api.pedido.dtos.DetalleResponse;
import com.nvtc.restaurante_api.pedido.dtos.PedidoResponseDTO;
import com.nvtc.restaurante_api.pedido.model.EstadoPedido;
import com.nvtc.restaurante_api.pedido.services.PedidoService;

@ExtendWith(MockitoExtension.class)
class PedidoControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PedidoService pedidoService;

    @InjectMocks
    private PedidoController pedidoController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(pedidoController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void crearPedido_retornaPedidoCreado() throws Exception {
        DetalleRequest detalle = new DetalleRequest();
        detalle.setProductoId(1L);
        detalle.setCantidad(2);

        CreatePedidoDTO request = new CreatePedidoDTO();
        request.setDetalles(List.of(detalle));

        DetalleResponse detalleResp = new DetalleResponse();
        detalleResp.setProductoId(1L);
        detalleResp.setNombreProducto("Coca Cola");
        detalleResp.setCantidad(2);
        detalleResp.setPrecioUnitario(1500.0);

        PedidoResponseDTO dto = new PedidoResponseDTO();
        dto.setId(1L);
        dto.setTotal(3000.0);
        dto.setFecha(LocalDateTime.now());
        dto.setEstado(EstadoPedido.PENDIENTE);
        dto.setDetalles(List.of(detalleResp));

        when(pedidoService.crearPedido(any(CreatePedidoDTO.class))).thenReturn(dto);

        mockMvc.perform(post("/pedidos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value(3000.0))
                .andExpect(jsonPath("$.estado").value("PENDIENTE"));
    }

    @Test
    void crearPedido_cuandoProductoAgotado_retorna409() throws Exception {
        DetalleRequest detalle = new DetalleRequest();
        detalle.setProductoId(1L);
        detalle.setCantidad(1);

        CreatePedidoDTO request = new CreatePedidoDTO();
        request.setDetalles(List.of(detalle));

        when(pedidoService.crearPedido(any(CreatePedidoDTO.class)))
                .thenThrow(new ItemAgotadoException("El producto 'Pizza' se ha agotado y no puede ser añadido."));

        mockMvc.perform(post("/pedidos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("El producto 'Pizza' se ha agotado y no puede ser añadido."));
    }

    @Test
    void crearPedido_cuandoProductoNoExiste_retorna400() throws Exception {
        DetalleRequest detalle = new DetalleRequest();
        detalle.setProductoId(999L);
        detalle.setCantidad(1);

        CreatePedidoDTO request = new CreatePedidoDTO();
        request.setDetalles(List.of(detalle));

        when(pedidoService.crearPedido(any(CreatePedidoDTO.class)))
                .thenThrow(new RuntimeException("Producto no encontrado"));

        mockMvc.perform(post("/pedidos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Producto no encontrado"));
    }

    @Test
    void buscarPedido_cuandoNoExiste_retorna400() throws Exception {
        when(pedidoService.obtenerPorId(999L))
                .thenThrow(new RuntimeException("Pedido no encontrado"));

        mockMvc.perform(get("/pedidos/999"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Pedido no encontrado"));
    }

    @Test
    void listarPedidos_retornaLista() throws Exception {
        PedidoResponseDTO dto = new PedidoResponseDTO();
        dto.setId(1L);
        dto.setTotal(5000.0);
        dto.setEstado(EstadoPedido.PENDIENTE);

        when(pedidoService.listar()).thenReturn(List.of(dto));

        mockMvc.perform(get("/pedidos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].total").value(5000.0));
    }

    @Test
    void buscarPedido_retornaPedidoPorId() throws Exception {
        PedidoResponseDTO dto = new PedidoResponseDTO();
        dto.setId(1L);
        dto.setTotal(3000.0);
        dto.setEstado(EstadoPedido.PENDIENTE);

        when(pedidoService.obtenerPorId(1L)).thenReturn(dto);

        mockMvc.perform(get("/pedidos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.total").value(3000.0));
    }

    @Test
    void cambiarEstado_retornaPedidoConNuevoEstado() throws Exception {
        PedidoResponseDTO dto = new PedidoResponseDTO();
        dto.setId(1L);
        dto.setTotal(3000.0);
        dto.setEstado(EstadoPedido.ENTREGADO);

        when(pedidoService.cambiarEstado(eq(1L), eq(EstadoPedido.ENTREGADO))).thenReturn(dto);

        mockMvc.perform(patch("/pedidos/1/estado")
                        .param("estado", "ENTREGADO"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("ENTREGADO"));
    }
}
