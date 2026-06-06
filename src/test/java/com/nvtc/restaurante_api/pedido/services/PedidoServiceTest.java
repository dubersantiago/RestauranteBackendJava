package com.nvtc.restaurante_api.pedido.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.nvtc.restaurante_api.exceptions.ItemAgotadoException;
import com.nvtc.restaurante_api.pedido.dtos.CreatePedidoDTO;
import com.nvtc.restaurante_api.pedido.dtos.DetalleRequest;
import com.nvtc.restaurante_api.pedido.dtos.PedidoResponseDTO;
import com.nvtc.restaurante_api.pedido.model.EstadoPedido;
import com.nvtc.restaurante_api.pedido.model.Pedido;
import com.nvtc.restaurante_api.pedido.repository.PedidoRepository;
import com.nvtc.restaurante_api.productos.model.Producto;
import com.nvtc.restaurante_api.productos.repository.ProductoRepository;

@ExtendWith(MockitoExtension.class)
class PedidoServiceTest {

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private PedidoService pedidoService;

    @Test
    void crearPedido_conDetallesValidos_retornaPedido() {
        Producto producto = new Producto();
        producto.setId(1L);
        producto.setNombre("Coca Cola");
        producto.setPrecio(1500.0);
        producto.setStock(10);

        DetalleRequest detalleReq = new DetalleRequest();
        detalleReq.setProductoId(1L);
        detalleReq.setCantidad(2);

        CreatePedidoDTO request = new CreatePedidoDTO();
        request.setDetalles(List.of(detalleReq));

        Pedido pedidoGuardado = new Pedido();
        pedidoGuardado.setId(1L);
        pedidoGuardado.setTotal(3000.0);
        pedidoGuardado.setEstado(EstadoPedido.PENDIENTE);

        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedidoGuardado);

        PedidoResponseDTO resultado = pedidoService.crearPedido(request);

        assertNotNull(resultado);
        assertEquals(3000.0, resultado.getTotal());
        assertEquals(EstadoPedido.PENDIENTE, resultado.getEstado());
    }

    @Test
    void crearPedido_sinDetalles_lanzaExcepcion() {
        CreatePedidoDTO request = new CreatePedidoDTO();
        request.setDetalles(List.of());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            pedidoService.crearPedido(request);
        });

        assertEquals("El pedido debe tener al menos un producto", ex.getMessage());
        verify(pedidoRepository, never()).save(any());
    }

    @Test
    void crearPedido_conDetallesNulos_lanzaExcepcion() {
        CreatePedidoDTO request = new CreatePedidoDTO();
        request.setDetalles(null);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            pedidoService.crearPedido(request);
        });

        assertEquals("El pedido debe tener al menos un producto", ex.getMessage());
        verify(pedidoRepository, never()).save(any());
    }

    @Test
    void crearPedido_productoAgotado_lanzaItemAgotadoException() {
        Producto producto = new Producto();
        producto.setId(1L);
        producto.setNombre("Pizza");
        producto.setPrecio(5000.0);
        producto.setStock(0);

        DetalleRequest detalleReq = new DetalleRequest();
        detalleReq.setProductoId(1L);
        detalleReq.setCantidad(1);

        CreatePedidoDTO request = new CreatePedidoDTO();
        request.setDetalles(List.of(detalleReq));

        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));

        ItemAgotadoException ex = assertThrows(ItemAgotadoException.class, () -> {
            pedidoService.crearPedido(request);
        });

        assertEquals("El producto 'Pizza' se ha agotado y no puede ser añadido.", ex.getMessage());
    }

    @Test
    void crearPedido_stockInsuficiente_lanzaItemAgotadoException() {
        Producto producto = new Producto();
        producto.setId(1L);
        producto.setNombre("Pizza");
        producto.setPrecio(5000.0);
        producto.setStock(2);

        DetalleRequest detalleReq = new DetalleRequest();
        detalleReq.setProductoId(1L);
        detalleReq.setCantidad(5);

        CreatePedidoDTO request = new CreatePedidoDTO();
        request.setDetalles(List.of(detalleReq));

        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));

        ItemAgotadoException ex = assertThrows(ItemAgotadoException.class, () -> {
            pedidoService.crearPedido(request);
        });

        assertEquals("Stock insuficiente para 'Pizza'. Stock disponible: 2", ex.getMessage());
    }

    @Test
    void crearPedido_productoNoExistente_lanzaExcepcion() {
        DetalleRequest detalleReq = new DetalleRequest();
        detalleReq.setProductoId(999L);
        detalleReq.setCantidad(1);

        CreatePedidoDTO request = new CreatePedidoDTO();
        request.setDetalles(List.of(detalleReq));

        when(productoRepository.findById(999L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            pedidoService.crearPedido(request);
        });

        assertEquals("Producto no encontrado", ex.getMessage());
    }

    @Test
    void obtenerPorId_conIdValido_retornaPedido() {
        Pedido pedido = new Pedido();
        pedido.setId(1L);
        pedido.setTotal(5000.0);
        pedido.setEstado(EstadoPedido.PENDIENTE);

        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));

        PedidoResponseDTO resultado = pedidoService.obtenerPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals(5000.0, resultado.getTotal());
    }

    @Test
    void obtenerPorId_conIdInvalido_lanzaExcepcion() {
        when(pedidoRepository.findById(999L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            pedidoService.obtenerPorId(999L);
        });

        assertEquals("Pedido no encontrado", ex.getMessage());
    }

    @Test
    void cambiarEstado_actualizaEstadoCorrectamente() {
        Pedido pedido = new Pedido();
        pedido.setId(1L);
        pedido.setEstado(EstadoPedido.PENDIENTE);

        Pedido pedidoActualizado = new Pedido();
        pedidoActualizado.setId(1L);
        pedidoActualizado.setEstado(EstadoPedido.ENTREGADO);

        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedidoActualizado);

        PedidoResponseDTO resultado = pedidoService.cambiarEstado(1L, EstadoPedido.ENTREGADO);

        assertNotNull(resultado);
        assertEquals(EstadoPedido.ENTREGADO, resultado.getEstado());
    }

    @Test
    void cambiarEstado_conIdInvalido_lanzaExcepcion() {
        when(pedidoRepository.findById(999L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            pedidoService.cambiarEstado(999L, EstadoPedido.ENTREGADO);
        });

        assertEquals("Pedido no encontrado", ex.getMessage());
    }
}
