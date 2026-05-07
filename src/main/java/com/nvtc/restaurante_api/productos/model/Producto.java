package com.nvtc.restaurante_api.productos.model;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.nvtc.restaurante_api.categorias.model.Categoria;
import com.nvtc.restaurante_api.pedido.model.DetallePedido;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Data
@Entity
public class Producto{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String nombre;
    private double precio;
    private Integer stock;

    private LocalDate fechaVencimiento;
    private Boolean activo = true;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "categoria_id",nullable = false)
    private Categoria categoria;

    @OneToMany(mappedBy = "producto")
    private List<DetallePedido> detalles;
}