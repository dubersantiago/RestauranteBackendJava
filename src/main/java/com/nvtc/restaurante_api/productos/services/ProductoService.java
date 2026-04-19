package com.nvtc.restaurante_api.productos.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.nvtc.restaurante_api.productos.model.Producto;
import com.nvtc.restaurante_api.productos.repository.ProductoRepository;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository repository) {
        this.productoRepository = repository;
    }

    public List<Producto> listar() {
        return productoRepository.findAll();
    }
    
    public Producto guardar(Producto producto) {
        return productoRepository.save(producto);
    }
}
