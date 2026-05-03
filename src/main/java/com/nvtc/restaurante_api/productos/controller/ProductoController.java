package com.nvtc.restaurante_api.productos.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nvtc.restaurante_api.productos.dtos.CreateProductRequest;
import com.nvtc.restaurante_api.productos.dtos.ProductoResponseDTO;
import com.nvtc.restaurante_api.productos.model.Producto;
import com.nvtc.restaurante_api.productos.services.ProductoService;

import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/productos")
public class ProductoController {
    private final ProductoService productoService;

    public ProductoController(ProductoService service){
        this.productoService=service;
    }

    @GetMapping
    public List<ProductoResponseDTO> listar() {
        return productoService.listar();
    }

     @GetMapping("/{id}")
    public String probar(@PathVariable Long id) {
        return "app funcionando id ="+id;
    }

    @PostMapping(consumes = "application/json")
    public ProductoResponseDTO crear(@RequestBody CreateProductRequest producto) {
        return productoService.guardar(producto);
    }
}
