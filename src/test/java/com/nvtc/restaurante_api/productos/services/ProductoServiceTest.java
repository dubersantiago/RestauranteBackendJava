package com.nvtc.restaurante_api.productos.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.nvtc.restaurante_api.categorias.model.Categoria;
import com.nvtc.restaurante_api.categorias.repository.CategoriaRepository;
import com.nvtc.restaurante_api.productos.dtos.CreateProductRequest;
import com.nvtc.restaurante_api.productos.dtos.ProductoResponseDTO;
import com.nvtc.restaurante_api.productos.model.Producto;
import com.nvtc.restaurante_api.productos.repository.ProductoRepository;

@ExtendWith(MockitoExtension.class)
class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository;

    @Mock
    private CategoriaRepository categoriaRepository;

    @InjectMocks
    private ProductoService productoService;

    @Test
    void listar_retornaListaDeProductos() {
        Categoria categoria = new Categoria();
        categoria.setId(1L);
        categoria.setNombre("Bebidas");

        Producto producto = new Producto();
        producto.setId(1L);
        producto.setNombre("Coca Cola");
        producto.setPrecio(1500.0);
        producto.setStock(10);
        producto.setCategoria(categoria);

        when(productoRepository.findAll()).thenReturn(List.of(producto));

        List<ProductoResponseDTO> resultado = productoService.listar();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Coca Cola", resultado.get(0).getNombre());
        assertEquals("Bebidas", resultado.get(0).getCategoria());
    }

    @Test
    void guardar_creaProductoCorrectamente() {
        Categoria categoria = new Categoria();
        categoria.setId(1L);
        categoria.setNombre("Bebidas");

        CreateProductRequest request = new CreateProductRequest();
        request.setNombre("Coca Cola");
        request.setPrecio(1500.0);
        request.setStock(10);
        request.setCategoriaId(1L);

        Producto productoGuardado = new Producto();
        productoGuardado.setId(1L);
        productoGuardado.setNombre("Coca Cola");
        productoGuardado.setPrecio(1500.0);
        productoGuardado.setStock(10);
        productoGuardado.setCategoria(categoria);

        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));
        when(productoRepository.save(any(Producto.class))).thenReturn(productoGuardado);

        ProductoResponseDTO resultado = productoService.guardar(request);

        assertNotNull(resultado);
        assertEquals("Coca Cola", resultado.getNombre());
        assertEquals(1500.0, resultado.getPrecio());
        assertEquals(10, resultado.getStock());
        assertEquals("Bebidas", resultado.getCategoria());
    }

    @Test
    void guardar_lanzaExcepcion_cuandoCategoriaNoExiste() {
        CreateProductRequest request = new CreateProductRequest();
        request.setNombre("Producto");
        request.setCategoriaId(999L);

        when(categoriaRepository.findById(999L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            productoService.guardar(request);
        });

        assertEquals("Categoria no encontrada", ex.getMessage());
        verify(productoRepository, never()).save(any());
    }

    @Test
    void editar_actualizaProductoCorrectamente() {
        Categoria categoria = new Categoria();
        categoria.setId(1L);
        categoria.setNombre("Bebidas");

        Producto productoExistente = new Producto();
        productoExistente.setId(1L);
        productoExistente.setNombre("Coca Cola");
        productoExistente.setPrecio(1500.0);
        productoExistente.setStock(10);
        productoExistente.setCategoria(categoria);

        CreateProductRequest request = new CreateProductRequest();
        request.setNombre("Coca Cola Actualizada");
        request.setPrecio(2000.0);
        request.setStock(5);
        request.setCategoriaId(1L);

        Producto productoActualizado = new Producto();
        productoActualizado.setId(1L);
        productoActualizado.setNombre("Coca Cola Actualizada");
        productoActualizado.setPrecio(2000.0);
        productoActualizado.setStock(5);
        productoActualizado.setCategoria(categoria);

        when(productoRepository.findById(1L)).thenReturn(Optional.of(productoExistente));
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));
        when(productoRepository.save(any(Producto.class))).thenReturn(productoActualizado);

        ProductoResponseDTO resultado = productoService.editar(1L, request);

        assertNotNull(resultado);
        assertEquals("Coca Cola Actualizada", resultado.getNombre());
        assertEquals(2000.0, resultado.getPrecio());
        assertEquals(5, resultado.getStock());
    }

    @Test
    void editar_lanzaExcepcion_cuandoProductoNoExiste() {
        CreateProductRequest request = new CreateProductRequest();
        request.setNombre("Producto");

        when(productoRepository.findById(999L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            productoService.editar(999L, request);
        });

        assertEquals("Producto no encontrado", ex.getMessage());
        verify(productoRepository, never()).save(any());
    }

    @Test
    void editar_lanzaExcepcion_cuandoCategoriaNoExiste() {
        Producto productoExistente = new Producto();
        productoExistente.setId(1L);
        productoExistente.setNombre("Producto");

        CreateProductRequest request = new CreateProductRequest();
        request.setNombre("Producto Editado");
        request.setCategoriaId(999L);

        when(productoRepository.findById(1L)).thenReturn(Optional.of(productoExistente));
        when(categoriaRepository.findById(999L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            productoService.editar(1L, request);
        });

        assertEquals("Categoria no encontrada", ex.getMessage());
    }
}
