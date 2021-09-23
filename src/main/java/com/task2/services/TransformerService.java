package com.task2.services;

import com.task2.DB.DTO;
import com.task2.DB.Model;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;

@Service
@AllArgsConstructor
public class TransformerService {
    @Autowired
    public ModelMapper modelMapper;

    public DTO EntityToDto(Model object, Class DtoClass){
        return modelMapper.map(object, (Type) DtoClass);
    }

    public Model DtoToEntity(DTO object, Class ModelClass) {
        return modelMapper.map(object, (Type) ModelClass);
    }
}
