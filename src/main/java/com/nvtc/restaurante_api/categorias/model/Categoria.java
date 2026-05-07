package com.nvtc.restaurante_api.categorias.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.nvtc.restaurante_api.productos.model.Producto;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Data
@Entity
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    public String nombre;
    
    @JsonManagedReference
    @OneToMany(mappedBy = "categoria")
    private List<Producto> productos;
}
