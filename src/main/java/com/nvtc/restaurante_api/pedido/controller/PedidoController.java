package com.nvtc.restaurante_api.pedido.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nvtc.restaurante_api.pedido.dtos.CreatePedidoDTO;
import com.nvtc.restaurante_api.pedido.dtos.PedidoResponseDTO;
import com.nvtc.restaurante_api.pedido.services.PedidoService;

import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {
    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService){
        this.pedidoService=pedidoService;
    }

    @PostMapping(consumes = "application/json")
    public PedidoResponseDTO crearPedido(@RequestBody CreatePedidoDTO request){
        System.out.println("DETALLES: " + request.getDetalles());
        return pedidoService.crearPedido(request);
    }

    @GetMapping
    public List<PedidoResponseDTO> listarPedidos(){
        return pedidoService.listar();
    }

    @GetMapping("/{id}")
    public PedidoResponseDTO buscarPedido(@PathVariable Long id){
        return pedidoService.obtenerPorId(id);
    }
}
