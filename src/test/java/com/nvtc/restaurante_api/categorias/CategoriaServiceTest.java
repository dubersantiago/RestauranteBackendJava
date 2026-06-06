package com.nvtc.restaurante_api.categorias;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.nvtc.restaurante_api.categorias.dtos.CategoriaResponseDTO;
import com.nvtc.restaurante_api.categorias.model.Categoria;
import com.nvtc.restaurante_api.categorias.repository.CategoriaRepository;
import com.nvtc.restaurante_api.categorias.services.CategoriaService;

@ExtendWith(MockitoExtension.class)
class CategoriaServiceTest {

    @Mock
    private CategoriaRepository categoriaRepository;

    @InjectMocks
    private CategoriaService categoriaService;

    @Test
    void toDTO_mapsCategoriaCorrectamente() {
        Categoria categoria = new Categoria();
        categoria.setId(1L);
        categoria.setNombre("Bebidas");

        CategoriaResponseDTO dto = categoriaService.toDTO(categoria);

        assertEquals(1L, dto.getId());
        assertEquals("Bebidas", dto.getNombre());
    }

    @Test
    void listar_retornaListaDTOs() {
        Categoria c1 = new Categoria();
        c1.setId(1L);
        c1.setNombre("Entradas");

        Categoria c2 = new Categoria();
        c2.setId(2L);
        c2.setNombre("Postres");

        when(categoriaRepository.findAll()).thenReturn(List.of(c1, c2));

        List<CategoriaResponseDTO> resultado = categoriaService.Listar();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("Entradas", resultado.get(0).getNombre());
        assertEquals("Postres", resultado.get(1).getNombre());
    }
}
