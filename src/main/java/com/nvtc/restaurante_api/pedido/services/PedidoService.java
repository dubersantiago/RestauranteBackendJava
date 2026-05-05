package com.nvtc.restaurante_api.pedido.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.nvtc.restaurante_api.pedido.dtos.CreatePedidoDTO;
import com.nvtc.restaurante_api.pedido.dtos.DetalleRequest;
import com.nvtc.restaurante_api.pedido.dtos.DetalleResponse;
import com.nvtc.restaurante_api.pedido.dtos.PedidoResponseDTO;
import com.nvtc.restaurante_api.pedido.model.DetallePedido;
import com.nvtc.restaurante_api.pedido.model.EstadoPedido;
import com.nvtc.restaurante_api.pedido.model.Pedido;
import com.nvtc.restaurante_api.pedido.repository.PedidoRepository;
import com.nvtc.restaurante_api.productos.model.Producto;
import com.nvtc.restaurante_api.productos.repository.ProductoRepository;

import jakarta.transaction.Transactional;

@Service
public class PedidoService {
    private final PedidoRepository pedidoRepository;
    private final ProductoRepository productoRepository;

    public PedidoService(PedidoRepository pedidoRepository,ProductoRepository productoRepository){
        this.pedidoRepository=pedidoRepository;
        this.productoRepository=productoRepository;
    }

    @Transactional
    public PedidoResponseDTO crearPedido(CreatePedidoDTO request){
        if (request.getDetalles() == null || request.getDetalles().isEmpty()) {
            throw new RuntimeException("El pedido debe tener al menos un producto");
        }

        Pedido pedido = new Pedido();
        pedido.setFecha(LocalDateTime.now());
        pedido.setEstado(EstadoPedido.PENDIENTE);

        List<DetallePedido> detalles = new ArrayList<>();
        double total = 0;

        for(DetalleRequest item: request.getDetalles()){
            Producto producto = productoRepository.findById(item.getProductoId())
            .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

            if(producto.getStock()<item.getCantidad()){
                throw new RuntimeException("Cantidad excede el stock");
            }

            DetallePedido detalle = new DetallePedido();
            detalle.setProducto(producto);
            detalle.setCantidad(item.getCantidad());
            detalle.setPrecioUnitario(producto.getPrecio());
            detalle.setPedido(pedido);

            total += producto.getPrecio() * item.getCantidad();

            producto.setStock(producto.getStock()-item.getCantidad());

            detalles.add(detalle);
        }

        pedido.setTotal(total);
        pedido.setDetalles(detalles);

        Pedido guardado = pedidoRepository.save(pedido);

        return mapToResponseDTO(guardado);
    }

    public List<PedidoResponseDTO> listar(){
        List<Pedido> pedidos = pedidoRepository.findAll();
        List<PedidoResponseDTO> dto = new ArrayList<>();

        for(Pedido p:pedidos){
            dto.add(mapToResponseDTO(p));
        }

        return dto;
    }

    public PedidoResponseDTO obtenerPorId(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        return mapToResponseDTO(pedido);
    }

    private PedidoResponseDTO mapToResponseDTO(Pedido pedido){
        PedidoResponseDTO response = new PedidoResponseDTO();

        response.setId(pedido.getId());
        response.setFecha(pedido.getFecha());
        response.setTotal(pedido.getTotal());
        response.setEstado(pedido.getEstado());

        List<DetalleResponse> lista = new ArrayList<>();

        for(DetallePedido d:pedido.getDetalles()){
            DetalleResponse dto = new DetalleResponse();
            dto.setProductoId(d.getProducto().getId());
            dto.setNombreProducto(d.getProducto().getNombre());
            dto.setCantidad(d.getCantidad());
            dto.setPrecioUnitario(d.getPrecioUnitario());

            lista.add(dto);
        }

        response.setDetalles(lista);

        return response;
    }


}
