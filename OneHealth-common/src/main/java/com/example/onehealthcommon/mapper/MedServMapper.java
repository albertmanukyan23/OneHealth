package com.example.onehealthcommon.mapper;

import com.example.onehealthcommon.dto.MedServDto;
import com.example.onehealthcommon.entity.MedServ;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MedServMapper {

    MedServDto map(MedServ medServ);
    MedServ map(MedServDto medServ);

}
