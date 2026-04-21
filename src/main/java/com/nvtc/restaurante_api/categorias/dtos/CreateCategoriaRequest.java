package com.nvtc.restaurante_api.categorias.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class CreateCategoriaRequest {
    private String nombre;
}
