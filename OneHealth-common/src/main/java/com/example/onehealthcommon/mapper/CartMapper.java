package com.example.onehealthcommon.mapper;

import com.example.onehealthcommon.dto.CreatCartDto;
import com.example.onehealthcommon.entity.Cart;
import com.example.onehealthcommon.entity.MedServ;
import com.example.onehealthcommon.entity.User;
import com.example.onehealthcommon.repository.MedServRepository;
import com.example.onehealthcommon.repository.UserRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
@Mapper(componentModel = "spring", uses = {MedServMapper.class, UserMapper.class})
public abstract class CartMapper {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MedServRepository medServRepository;

    @Mapping(target = "medServSet", source = "medServSet", qualifiedByName = "getMedServ")
    @Mapping(target = "user", source = "userId", qualifiedByName = "getUserFromDb")
    public abstract Cart mapDto(CreatCartDto dto);

    @Mapping(target = "medServSet", source = "medServSet")
    @Mapping(target = "userId", source = "user.id")
    public abstract CreatCartDto map(Cart entity);

    @Named("getUserFromDb")
    protected User getUserFromDb(int userId) {
        return userRepository.findById(userId).orElse(null);
    }

    @Named("getMedServ")
    protected Set<MedServ> getMedServ(Set<MedServ> medServSet) {
        List<MedServ> all = medServRepository.findAll();
        medServSet.addAll(all);
        return medServSet;
    }
}
