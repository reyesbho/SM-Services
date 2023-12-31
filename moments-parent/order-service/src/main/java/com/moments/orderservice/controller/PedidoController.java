package com.moments.orderservice.controller;

import com.moments.orderservice.dto.PedidoRequest;
import com.moments.orderservice.dto.PedidoResponse;
import com.moments.orderservice.exception.EntityNotFoundException;
import com.moments.orderservice.service.PedidoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/pedido")
public class PedidoController {

    private PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService){
        this.pedidoService = pedidoService;
    }

    @GetMapping("")
    public ResponseEntity<Page<PedidoResponse>> getPedidos(@RequestParam(name = "estatus", required = false) Optional<String> estatus,
                                     @RequestParam(name = "page", defaultValue = "0", required = false) int page,
                                     @RequestParam(name = "size",  defaultValue = "10", required = false) int size){
        Pageable pageRequest = PageRequest.of(page, size);
        Page<PedidoResponse> pedidoResponseList = this.pedidoService.getPedidos(estatus, pageRequest);
        return new ResponseEntity<>(pedidoResponseList, HttpStatus.OK);
    }

    @GetMapping("/{idPedido}")
    public ResponseEntity<PedidoResponse> getPedidos(@PathVariable("idPedido") Long idPedido){
        PedidoResponse pedidoResponse = this.pedidoService.getPedido(idPedido);
        return new ResponseEntity(pedidoResponse, HttpStatus.OK);
    }

    @GetMapping("/{idPedido}/producto")
    public ResponseEntity getProductosByPedido(@PathVariable("idPedido") Long idPedido){
        return new ResponseEntity(this.pedidoService.getProductosByPedido(idPedido), HttpStatus.OK);
    }

    @PutMapping("/{id}/{estatus}")
    @ResponseStatus(HttpStatus.OK)
    public void updateEstatusPedido(@PathVariable("id") Long id, @PathVariable("estatus") String estatus) throws EntityNotFoundException {
        this.pedidoService.updateStatePedido(id, estatus);
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public void addPedido(@RequestBody PedidoRequest pedidoRequest){
        this.pedidoService.addPedido(pedidoRequest);
    }
}
