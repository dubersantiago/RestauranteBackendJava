package com.nvtc.restaurante_api.productos.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.nvtc.restaurante_api.categorias.model.Categoria;
import com.nvtc.restaurante_api.categorias.repository.CategoriaRepository;
import com.nvtc.restaurante_api.productos.dtos.CreateProductRequest;
import com.nvtc.restaurante_api.productos.dtos.ProductoResponseDTO;
import com.nvtc.restaurante_api.productos.model.Producto;
import com.nvtc.restaurante_api.productos.repository.ProductoRepository;

import jakarta.transaction.Transactional;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;

    public ProductoService(ProductoRepository repository,CategoriaRepository categoriaRepository) {
        this.productoRepository = repository;
        this.categoriaRepository = categoriaRepository;
    }

    public List<ProductoResponseDTO> listar() {
        return productoRepository.findByStockGreaterThanAndActivoTrue(0)
            .stream()
            .map(this::mapToProductResponse)
            .toList();
    }
    
    @Transactional
    public ProductoResponseDTO guardar(CreateProductRequest productoDto) {
        Categoria categoria = categoriaRepository.findById(productoDto.getCategoriaId())
        .orElseThrow(() -> new RuntimeException("Categoria no encontrada"));

        Producto producto = new Producto();
        producto.setNombre(productoDto.getNombre());
        producto.setPrecio(productoDto.getPrecio());
        producto.setStock(productoDto.getStock());
        producto.setFechaVencimiento(productoDto.getFechaVencimiento());
        producto.setCategoria(categoria);

        Producto productoGuardado=productoRepository.save(producto);
        return mapToProductResponse(productoGuardado);
    }

    @Transactional
    public ProductoResponseDTO editar(Long id, CreateProductRequest productoDto) {
        Producto producto = productoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        Categoria categoria = categoriaRepository.findById(productoDto.getCategoriaId())
            .orElseThrow(() -> new RuntimeException("Categoria no encontrada"));

        producto.setNombre(productoDto.getNombre());
        producto.setPrecio(productoDto.getPrecio());
        producto.setStock(productoDto.getStock());
        producto.setFechaVencimiento(productoDto.getFechaVencimiento());
        producto.setCategoria(categoria);

        return mapToProductResponse(productoRepository.save(producto));
    }

    private ProductoResponseDTO mapToProductResponse(Producto producto){
        ProductoResponseDTO response = new ProductoResponseDTO();
        response.setId(producto.getId());
        response.setNombre(producto.getNombre());
        response.setPrecio(producto.getPrecio());
        response.setStock(producto.getStock());
        response.setCategoria(producto.getCategoria().getNombre());
        response.setDisponible(producto.getStock() != null && producto.getStock() > 0 && producto.getActivo());

        return response;
    }
}
