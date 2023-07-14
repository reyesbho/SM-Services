package com.moments.orderservice.service;

import com.moments.orderservice.dto.PedidoRequest;
import com.moments.orderservice.dto.PedidoResponse;
import com.moments.orderservice.dto.ProductoPedidoResponse;
import com.moments.orderservice.dto.SaborResponse;
import com.moments.orderservice.exception.EntityNotFoundException;
import com.moments.orderservice.model.*;
import com.moments.orderservice.repository.*;
import com.moments.orderservice.util.EstatusEnum;
import com.moments.orderservice.util.MapObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class PedidoService {
    private PedidoRepository pedidoRepository;
    private PedidoProductoRepository pedidoProductoRepository;
    private ClienteRepository clienteRepository;
    private SaborRepository saborRepository;
    private ProductoTipoRepository productoTipoRepository;
    private ProductoRepository productoRepository;

    public PedidoService(PedidoRepository pedidoRepository,PedidoProductoRepository pedidoProductoRepository,
                         ClienteRepository clienteRepository, SaborRepository saborRepository,
                         ProductoTipoRepository productoTipoRepository, ProductoRepository productoRepository){
        this.pedidoRepository = pedidoRepository;
        this.pedidoProductoRepository = pedidoProductoRepository;
        this.clienteRepository = clienteRepository;
        this.saborRepository = saborRepository;
        this.productoRepository = productoRepository;
        this.productoTipoRepository = productoTipoRepository;
    }

    public Page<PedidoResponse> getPedidos(Optional<String> estatus, Pageable pageable){
        Page<Pedido> pagePedidos = null;
        if(estatus.isPresent()){
            pagePedidos = this.pedidoRepository.findByEstatus(estatus.get(), pageable);
        }else {
            pagePedidos = this.pedidoRepository.findAll(pageable);
        }

        return new PageImpl<>(pagePedidos.getContent().stream().map(MapObject::mapToPedidoResponse).toList(),
                pageable, pagePedidos.getTotalElements());
    }

    public PedidoResponse getPedido(Long idPedido){
            Pedido pedido = this.pedidoRepository.findById(idPedido).orElse(null);
            return MapObject.mapToPedidoResponse(pedido);
    }


    public List<ProductoPedidoResponse> getProductosByPedido(Long idPedido){
        List<ProductoPedido> productos = this.pedidoProductoRepository.findByIdPedido(idPedido);
        return productos.stream().map(MapObject::mapToPedidoProductoResponse).toList();
    }


    @Transactional
    public void updateStatePedido(Long idPedido, String status) throws EntityNotFoundException {
        //validar que exista el pedido
        Optional<Pedido> pedidoOptional = this.pedidoRepository.findById(idPedido);
        if (pedidoOptional.isEmpty()){
            throw new EntityNotFoundException("El pedido no existe");
        }

        Pedido pedidoEntity = pedidoOptional.get();
        pedidoEntity.setEstatus(EstatusEnum.getStatusEnum(status).getValue());
        pedidoEntity.setFechaActualizacion(new Date());

        this.pedidoRepository.save(pedidoEntity);
    }

    @Transactional
    public void addPedido(PedidoRequest newPedido) throws EntityNotFoundException {
        //validar el cliente
        Cliente cliente = null;
        Optional<Cliente> clienteOptional = this.clienteRepository.findById(newPedido.getCliente().getId());
        if(clienteOptional.isEmpty()){
            cliente = Cliente.builder()
                    .nombre(newPedido.getCliente().getNombre())
                    .apellidoPaterno(newPedido.getCliente().getApellidoPaterno())
                    .apellidoMaterno(newPedido.getCliente().getApellidoMaterno())
                    .direccion(newPedido.getCliente().getDireccion())
                    .build();
            this.clienteRepository.saveAndFlush(cliente);
        }else{
            cliente = clienteOptional.get();
        }

        Pedido pedidoEntity = Pedido.builder()
                .fechaEntrega(newPedido.getFechaEntrega())
                .lugarEntrega(newPedido.getLugarEntrega())
                .estatus(EstatusEnum.BACKLOG.getValue())
                .total(newPedido.getTotal())
                .fechaRegistro(new Date())
                .fechaActualizacion(null)
                .cliente(cliente)
                .build();
        this.pedidoRepository.saveAndFlush(pedidoEntity);

        List<ProductoPedido> productosEntity = new ArrayList<>();
        newPedido.getProductos().forEach(productoDto -> {
            Producto productoEntity = this.productoRepository.findById(productoDto.getIdProducto()).orElse(new Producto(productoDto.getIdProducto()));
            Sabor saborEntity = null;
            if(Objects.nonNull(productoDto.getIdSabor())){
                saborEntity = this.saborRepository.findById(productoDto.getIdSabor()).orElse(null);
            }
            ProductoTipo productoTipoEntity = this.productoTipoRepository.findById(productoDto.getIdTipoProducto()).orElse(new ProductoTipo(productoDto.getIdTipoProducto()));

            ProductoPedido producto=ProductoPedido.builder()
                    .idPedido(pedidoEntity.getId())
                    .producto(productoEntity)
                    .sabor(saborEntity)
                    .tipoProducto(productoTipoEntity)
                    .texto(productoDto.getTexto())
                    .comentarios(productoDto.getComentarios())
                    .fechaRegistro(new Date())
                    .fechaActualizacion(null)
                    .build();
                    productosEntity.add(producto);
        });

        this.pedidoProductoRepository.saveAll(productosEntity);
    }
}
