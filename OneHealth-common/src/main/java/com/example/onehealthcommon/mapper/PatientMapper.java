package com.example.onehealthcommon.mapper;

import com.example.onehealthcommon.dto.PatientDto;
import com.example.onehealthcommon.dto.PatientRegisterDto;
import com.example.onehealthcommon.entity.Patient;
import com.example.onehealthcommon.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring" ,uses = {User.class})
public interface PatientMapper {
//    @Mapping(source = "patientRegisterDto.name", target = "name")
//    @Mapping(source = "patientRegisterDto.surname", target = "surname")
//    @Mapping(source = "patientRegisterDto.email", target = "email")
//    @Mapping(source = "patientRegisterDto.password", target = "password")
//    @Mapping(source = "patientRegisterDto.birthDate", target = "birthDate")
//    @Mapping(source = "patientRegisterDto.region", target = "region")
//    @Mapping(source = "patientRegisterDto.address", target = "address")
//    @Mapping(source = "patientRegisterDto.nation", target = "nation")
    Patient map(PatientRegisterDto patientRegisterDto);
    PatientDto map(Patient patient);
}
