package com.example.onehealthcommon.mapper;

import com.example.onehealthcommon.dto.PatientDto;
import com.example.onehealthcommon.dto.PatientRegisterDto;
import com.example.onehealthcommon.entity.Patient;
import com.example.onehealthcommon.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

@Mapper(componentModel = "spring", uses = {User.class})
public abstract class PatientMapper {

    @Value("http://localhost:8080")
    public String siteUrl;

    public abstract Patient map(PatientRegisterDto patientRegisterDto);

    @Mapping(target = "picName", expression = "java(patient.getPicName() != null ? siteUrl + \"/users/getImage?picName=\" + patient.getPicName() : null)")
    public abstract PatientDto map(Patient patient);

    public abstract List<PatientDto> mapListToDtos(List<Patient> books);

}
