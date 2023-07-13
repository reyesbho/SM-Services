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
public class ProductoPedidoRequest {


    private Long idProducto;
    private Long idSabor;
    private Long idTipoProducto;
    private String texto;
    private String comentarios;
}
