package com.nvtc.restaurante_api.pedido.dtos;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class CreatePedidoDTO {
    private List<DetalleRequest> detalles;
}
