package com.example.onehealthmvc.service.impl;

import com.example.onehealthcommon.entity.Cart;
import com.example.onehealthcommon.entity.MedServ;
import com.example.onehealthcommon.entity.Order;
import com.example.onehealthcommon.entity.User;
import com.example.onehealthcommon.repository.CartRepository;
import com.example.onehealthcommon.repository.MedServRepository;
import com.example.onehealthcommon.repository.OrderRepository;
import com.example.onehealthmvc.security.CurrentUser;
import com.example.onehealthmvc.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final MedServRepository medServRepository;

    @Override
    public Optional<Cart> findCartByUserId(int id) {
        return cartRepository.findCartByUserId(id);
    }

    //Creates an order for the current user based on the items in their cart.
    @Override
    public void addOrder(CurrentUser currentUser) {
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
            log.info("Cart and Order have been created for user " + currentUser.getUser().getId());
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
            log.info("deleteByIdMedServ() method has been worked successfully for user" + currentUser.getUser().getId());
        }
    }

    //Adds a medical service to the cart for the current user

    @Override
    public void addCartByMedicalId(CurrentUser currentUser, int medicalId) {
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