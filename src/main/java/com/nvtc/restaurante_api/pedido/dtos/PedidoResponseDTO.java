package com.nvtc.restaurante_api.pedido.dtos;

import java.time.LocalDateTime;
import java.util.List;

import com.nvtc.restaurante_api.pedido.model.EstadoPedido;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class PedidoResponseDTO {
    private long id;
    private Double total;
    private LocalDateTime fecha;
    private EstadoPedido estado;
    private List<DetalleResponse> detalles;

}
