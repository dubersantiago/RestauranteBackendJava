package com.nvtc.restaurante_api.pedido.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class DetalleRequest {
    private Long productoId;
    private Integer cantidad;
}
