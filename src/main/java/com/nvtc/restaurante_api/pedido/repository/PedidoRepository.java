package com.nvtc.restaurante_api.pedido.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nvtc.restaurante_api.pedido.model.Pedido;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {}


