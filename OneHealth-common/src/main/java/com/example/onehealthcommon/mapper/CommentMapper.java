package com.example.onehealthcommon.mapper;

import com.example.onehealthcommon.dto.CommentDto;
import com.example.onehealthcommon.dto.CreatCommentDto;
import com.example.onehealthcommon.entity.Comment;
import com.example.onehealthcommon.entity.Doctor;
import com.example.onehealthcommon.entity.User;
import com.example.onehealthcommon.repository.DoctorRepository;
import com.example.onehealthcommon.repository.UserRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;

@Mapper(componentModel = "spring", uses = {DoctorMapper.class, UserMapper.class})
@Component
public abstract class CommentMapper {
    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private UserRepository userRepository;

    @Mapping(target = "doctor", source = "doctorId", qualifiedByName = "getDoctorFromDb")
    @Mapping(target = "user", source = "userId", qualifiedByName = "getUserFromDb")
    public abstract Comment mapDto(CreatCommentDto dto);

    @Mapping(target = "doctorDtoResponse", source = "doctor")
    @Mapping(target = "userDto", source = "user")
    public abstract CommentDto map(Comment entity);

    @Named("getDoctorFromDb")
    protected Doctor getDoctorFromDb(int doctorId) {
        return doctorRepository.findById(doctorId).orElse(null);
    }

    @Named("getUserFromDb")
    protected User getUserFromDb(int userId) {
        return userRepository.findById(userId).orElse(null);
    }
    public abstract List<CommentDto> mapListDto(List<Comment> content);

}
