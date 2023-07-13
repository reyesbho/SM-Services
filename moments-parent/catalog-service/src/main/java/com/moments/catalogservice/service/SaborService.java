package com.moments.catalogservice.service;

import com.moments.catalogservice.dto.SaborResponse;
import com.moments.catalogservice.model.Sabor;
import com.moments.catalogservice.repository.SaborRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SaborService {

    private SaborRepository saborRepository;

    public SaborService(SaborRepository saborRepository){
        this.saborRepository = saborRepository;
    }

    public List<SaborResponse> getSabores(){
        List<Sabor> saborList = this.saborRepository.findAll();
        return  saborList.stream().map(this::mapToSaborResponse).collect(Collectors.toList());
    }

    private SaborResponse mapToSaborResponse(Sabor sabor){
        return SaborResponse.builder().id(sabor.getId())
                .clave(sabor.getClave())
                .descripcion(sabor.getDescripcion())
                .estatus(sabor.getEstatus())
                .build();
    }
}
