package com.nvtc.restaurante_api.pedido.dtos;

import java.time.LocalDateTime;
import java.util.List;


public class PedidoResponseDTO {
    private long id;
    private Double total;
    private LocalDateTime fecha;
    private List<DetalleResponse> productos;

    public static class DetalleResponse  {
        private Long productoId;
        private String nombreProducto;
        private Integer cantidad;
        private Double precioUnitario;
    }
}
