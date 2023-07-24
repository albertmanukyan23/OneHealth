package com.example.onehealthcommon.mapper;

import com.example.onehealthcommon.dto.PatientDto;
import com.example.onehealthcommon.dto.PatientRegisterDto;
import com.example.onehealthcommon.entity.Patient;
import com.example.onehealthcommon.entity.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {User.class})
public interface PatientMapper {

    Patient map(PatientRegisterDto patientRegisterDto);

    PatientDto map(Patient patient);

    public List<PatientDto> mapListToDtos(List<Patient> books);

}
