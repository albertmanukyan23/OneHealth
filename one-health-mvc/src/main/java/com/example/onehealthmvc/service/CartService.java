package com.example.onehealthmvc.service;

import com.example.onehealthcommon.entity.Cart;
import com.example.onehealthcommon.entity.Department;
import com.example.onehealthcommon.entity.MedServ;
import com.example.onehealthmvc.security.CurrentUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface CartService {
    Optional<Cart> findCartByUserId(int id);
    void addOrderByMedical(CurrentUser currentUser);


    double countPrice(Set<MedServ> medServSet);
    Set<MedServ> deleteById(Set<MedServ> medServSet,int id);

    void deleteByIdMedServ(CurrentUser currentUser,int medServId);

    void addCartByMedical(CurrentUser currentUser, int medicalId);
}
