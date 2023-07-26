package com.example.onehealthcommon.mapper;

import com.example.onehealthcommon.dto.CreateMedServDto;
import com.example.onehealthcommon.dto.MedServDto;
import com.example.onehealthcommon.entity.MedServ;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MedServMapper {

    CreateMedServDto map(MedServ medServ);

    MedServ map(CreateMedServDto medServ);

    MedServDto mapTo(MedServ medServ);
    List<MedServDto> map(List<MedServ> medServ);

}
