package com.example.onehealthcommon.mapper;

import com.example.onehealthcommon.dto.CreatDoctorRequestDto;
import com.example.onehealthcommon.dto.DoctorDtoResponse;
import com.example.onehealthcommon.entity.Doctor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class DoctorMapper {

    @Value("http://localhost:8080")
    public String siteUrl;

    public abstract Doctor mapDto(CreatDoctorRequestDto creatDoctorRequestDto);

    @Mapping(target = "picName", expression = "java(doctor.getPicName() != null ? siteUrl + \"/users/getImage?picName=\" + doctor.getPicName() : null)")
    public abstract DoctorDtoResponse map(Doctor doctor);

   public abstract List<DoctorDtoResponse> mapListDto(List<Doctor> doctorList);

}
