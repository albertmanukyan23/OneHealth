package com.example.onehealthcommon.mapper;

import com.example.onehealthcommon.dto.CreatDoctorRequestDto;
import com.example.onehealthcommon.dto.DoctorDtoResponse;
import com.example.onehealthcommon.entity.Department;
import com.example.onehealthcommon.entity.Doctor;
import com.example.onehealthcommon.repository.DepartmentRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper(componentModel = "spring", uses = {DepartmentMapper.class})
@Component
public abstract class DoctorMapper {
    @Autowired
    private DepartmentRepository departmentRepository;

    @Value("http://localhost:8080")
    public String siteUrl;

    @Mapping(target = "department", source = "departmentId", qualifiedByName = "getDepartmentDb")
    public abstract Doctor mapDto(CreatDoctorRequestDto creatDoctorRequestDto);

    @Mapping(target = "picName", expression = "java(doctor.getPicName() != null ? siteUrl + \"/users/getImage?picName=\" + doctor.getPicName() : null)")
    @Mapping(target = "departmentDto", source = "department")
    public abstract DoctorDtoResponse map(Doctor doctor);

    public abstract List<DoctorDtoResponse> mapListDto(List<Doctor> doctorList);

    @Named("getDepartmentDb")
    protected Department getDepartmentDb(int departmentId) {
        return departmentRepository.findById(departmentId).orElse(null);
    }

}
