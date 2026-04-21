package com.nvtc.restaurante_api.productos.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.nvtc.restaurante_api.categorias.model.Categoria;
import com.nvtc.restaurante_api.categorias.repository.CategoriaRepository;
import com.nvtc.restaurante_api.productos.dtos.CreateProductRequest;
import com.nvtc.restaurante_api.productos.model.Producto;
import com.nvtc.restaurante_api.productos.repository.ProductoRepository;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;

    public ProductoService(ProductoRepository repository,CategoriaRepository categoriaRepository) {
        this.productoRepository = repository;
        this.categoriaRepository = categoriaRepository;
    }

    public List<Producto> listar() {
        return productoRepository.findAll();
    }
    
    public Producto guardar(CreateProductRequest productoDto) {
        Categoria categoria = categoriaRepository.findById(productoDto.getCategoriaId())
        .orElseThrow(() -> new RuntimeException("Categoria no encontrada"));

        Producto producto = new Producto();
        producto.setNombre(productoDto.getNombre());
        producto.setPrecio(productoDto.getPrecio());
        producto.setStock(productoDto.getStock());
        producto.setFechaVencimiento(productoDto.getFechaVencimiento());
        producto.setCategoria(categoria);

        return productoRepository.save(producto);
    }
}
