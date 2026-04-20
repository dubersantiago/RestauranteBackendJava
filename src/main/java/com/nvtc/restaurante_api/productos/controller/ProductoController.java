package com.nvtc.restaurante_api.productos.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nvtc.restaurante_api.productos.dtos.CreateProdcutRequest;
import com.nvtc.restaurante_api.productos.model.Producto;
import com.nvtc.restaurante_api.productos.services.ProductoService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping("/productos")
public class ProductoController {
    private final ProductoService productoService;

    public ProductoController(ProductoService service){
        this.productoService=service;
    }

    @GetMapping
    public List<Producto> listar() {
        return productoService.listar();
    }

    @PostMapping
    public Producto crear(@RequestBody CreateProdcutRequest producto) {
        System.out.println("productos nombre recibido desde el dto");
        System.out.println(producto.getNombre());
        return productoService.guardar(producto);
    }
}
