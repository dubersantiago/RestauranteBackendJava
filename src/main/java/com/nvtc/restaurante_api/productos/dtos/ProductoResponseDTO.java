package com.nvtc.restaurante_api.productos.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class ProductoResponseDTO{
    private Long id;
    private String nombre;
    private Double precio;
    private Integer stock;
    private String categoria;
}
