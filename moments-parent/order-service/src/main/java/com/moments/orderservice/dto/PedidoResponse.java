package com.moments.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PedidoResponse {

    private Long id;
    private Date fechaEntrega;
    private String lugarEntrega;
    private String estatus;
    private Float total;
    private Date fechaRegistro;
    private Date fechaActualizacion;
    private ClienteResponse cliente;
}
