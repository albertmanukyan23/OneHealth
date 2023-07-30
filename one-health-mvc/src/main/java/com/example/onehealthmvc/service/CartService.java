package com.example.onehealthmvc.service;

import com.example.onehealthcommon.entity.Cart;
import com.example.onehealthcommon.entity.MedServ;
import com.example.onehealthmvc.security.CurrentUser;

import java.util.Optional;
import java.util.Set;

public interface CartService {
    Optional<Cart> findCartByUserId(int id);

    void addOrder(CurrentUser currentUser);


    double countPrice(Set<MedServ> medServSet);

    Set<MedServ> deleteById(Set<MedServ> medServSet, int id);

    void deleteByIdMedServ(CurrentUser currentUser, int medServId);

    void addCartByMedicalId(CurrentUser currentUser, int medicalId);
}
