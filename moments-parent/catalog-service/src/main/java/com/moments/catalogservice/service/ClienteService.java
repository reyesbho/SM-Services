package com.moments.catalogservice.service;

import com.moments.catalogservice.dto.ClienteResponse;
import com.moments.catalogservice.model.Cliente;
import com.moments.catalogservice.repository.ClienteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClienteService {
    private ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository){
        this.clienteRepository = clienteRepository;
    }

    public List<ClienteResponse> getClientes(){
        List<Cliente> clienteList = this.clienteRepository.findAll();
        return  clienteList.stream().map(this::mapToClienteResponse).collect(Collectors.toList());
    }

    public List<ClienteResponse> getClientes(String search){
        List<Cliente> clienteList = this.clienteRepository.findByNombreContainingIgnoreCaseOrApellidoPaternoContainingIgnoreCase(search, search);
        return  clienteList.stream().map(this::mapToClienteResponse).collect(Collectors.toList());
    }

    private ClienteResponse mapToClienteResponse(Cliente cliente){
        return ClienteResponse.builder().id(cliente.getId())
                .nombre(cliente.getNombre())
                .apellidoPaterno(cliente.getApellidoPaterno())
                .apellidoMaterno(cliente.getApellidoMaterno())
                .direccion(cliente.getDireccion()).build();
    }
}
