package com.nvtc.restaurante_api.exceptions;

public class ItemAgotadoException extends RuntimeException {
    public ItemAgotadoException(String mensaje) {
        super(mensaje);
    }
}