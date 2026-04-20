package com.nvtc.restaurante_api.productos.dtos;

import java.time.LocalDate;
import lombok.Data;

@Data
public class CreateProdcutRequest {
    private String nombre;
    private double precio;
    private Integer stock;
    private LocalDate fechaVencimiento;
}
