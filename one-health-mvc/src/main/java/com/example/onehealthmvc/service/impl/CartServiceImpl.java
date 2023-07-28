package com.example.onehealthmvc.service.impl;

import com.example.onehealthcommon.entity.*;
import com.example.onehealthcommon.repository.CartRepository;
import com.example.onehealthcommon.repository.DepartmentRepository;
import com.example.onehealthcommon.repository.MedServRepository;
import com.example.onehealthcommon.repository.OrderRepository;
import com.example.onehealthmvc.security.CurrentUser;
import com.example.onehealthmvc.service.CartService;
import com.example.onehealthmvc.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final MedServRepository medServRepository;

    @Override
    public Optional<Cart> findCartByUserId(int id) {
        return cartRepository.findCartByUserId(id);
    }

    @Override
    public void addOrderByMedical(CurrentUser currentUser) {
        User user = currentUser.getUser();
        Optional<Cart> cartByUserId = cartRepository.findCartByUserId(user.getId());
        if (cartByUserId.isPresent()) {
            Cart cart = cartByUserId.get();
            Order order = Order.builder()
                    .user(user)
                    .dateTime(new Date())
                    .medServSet(new HashSet<>(cart.getMedServSet()))
                    .build();
            orderRepository.save(order);
            cartRepository.save(cart);
        }
    }


    @Override
    public double countPrice(Set<MedServ> medServSet) {
        int sum = 0;
        for (MedServ medServ : medServSet) {
            sum += medServ.getPrice();
        }
        return sum;
    }

    @Override
    public Set<MedServ> deleteById(Set<MedServ> medServSet, int id) {
        medServSet.removeIf(medServ -> medServ.getId() == id);
        return medServSet;
    }

    @Override
    public void deleteByIdMedServ(CurrentUser currentUser, int medServId) {
        User user = currentUser.getUser();
        Optional<Cart> cartByUserId = cartRepository.findCartByUserId(user.getId());
        if (cartByUserId.isPresent()) {
            Cart cart = cartByUserId.get();
            Set<MedServ> updateMedSer = deleteById(cart.getMedServSet(), medServId);
            cart.setMedServSet(updateMedSer);
            cartRepository.save(cart);
        }
    }

    @Override
    public void addCartByMedical(CurrentUser currentUser, int medicalId) {
        Cart cart;
        User user = currentUser.getUser();
        Optional<Cart> cartByUserId = cartRepository.findCartByUserId(user.getId());
        Optional<MedServ> byId = medServRepository.findById(medicalId);
        if (cartByUserId.isEmpty()) {
            cart = Cart.builder()
                    .user(user)
                    .build();
            cartRepository.save(cart);
        } else {
            cart = cartByUserId.get();
        }
        cart.getMedServSet().add(byId.get());
        cartRepository.save(cart);
    }
}