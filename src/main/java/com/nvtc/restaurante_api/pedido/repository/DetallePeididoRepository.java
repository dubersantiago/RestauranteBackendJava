package com.nvtc.restaurante_api.pedido.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nvtc.restaurante_api.pedido.model.DetallePedido;

public interface DetallePeididoRepository extends JpaRepository<DetallePedido,Long> {}
