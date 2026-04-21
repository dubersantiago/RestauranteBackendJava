package com.nvtc.restaurante_api.productos.dtos;

import java.time.LocalDate;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class CreateProductRequest {
    private String nombre;
    private double precio;
    private Integer stock;
    private LocalDate fechaVencimiento;
    private Long categoriaId;
}
