package com.example.onehealthrest.service;

import com.example.onehealthcommon.dto.CreatCartDto;
import com.example.onehealthcommon.dto.OrderDto;
import com.example.onehealthcommon.entity.MedServ;
import com.example.onehealthrest.security.CurrentUser;

import java.util.Optional;
import java.util.Set;

public interface CartService {
    Optional<OrderDto> addOrderByMedical(CurrentUser currentUser, OrderDto orderDto);

    Set<MedServ> deleteById(Set<MedServ> medServSet, int id);

    boolean deleteByIdMedServ(CurrentUser currentUser, int medServId);

    Optional<CreatCartDto> addCartByMedical(CurrentUser currentUser, CreatCartDto dtoCart, int medicalId);


}
