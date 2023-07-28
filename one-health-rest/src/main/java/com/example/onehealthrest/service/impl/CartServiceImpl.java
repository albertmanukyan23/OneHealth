package com.example.onehealthrest.service.impl;

import com.example.onehealthcommon.dto.CreatCartDto;
import com.example.onehealthcommon.dto.OrderDto;
import com.example.onehealthcommon.entity.Cart;
import com.example.onehealthcommon.entity.MedServ;
import com.example.onehealthcommon.entity.Order;
import com.example.onehealthcommon.entity.User;
import com.example.onehealthcommon.mapper.CartMapper;
import com.example.onehealthcommon.mapper.OrderMapper;
import com.example.onehealthcommon.repository.CartRepository;
import com.example.onehealthcommon.repository.MedServRepository;
import com.example.onehealthcommon.repository.OrderRepository;
import com.example.onehealthrest.security.CurrentUser;
import com.example.onehealthrest.service.CartService;
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
    private final CartMapper cartMapper;
    private final OrderMapper orderMapper;

    @Override
    public Optional<OrderDto> addOrderByMedical(CurrentUser currentUser, OrderDto orderDto) {
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
           return Optional.of( orderMapper.map(order));
        }
        log.info("add method save() did not work ");
        return Optional.empty();
    }

    @Override
    public Set<MedServ> deleteById(Set<MedServ> medServSet, int id) {
        medServSet.removeIf(medServ -> medServ.getId() == id);
        return medServSet;
    }

    @Override
    public boolean deleteByIdMedServ(CurrentUser currentUser, int medServId) {
        boolean isDeleted = false;
        User user = currentUser.getUser();
        Optional<Cart> cartByUserId = cartRepository.findCartByUserId(user.getId());
        if (cartByUserId.isPresent()) {
            Cart cart = cartByUserId.get();
            Set<MedServ> updateMedSer = deleteById(cart.getMedServSet(), medServId);
            cart.setMedServSet(updateMedSer);
            cartRepository.save(cart);
             isDeleted = true;
        }
        log.info("add method delete() did not work ");
        return isDeleted;
    }

    @Override
    public Optional<CreatCartDto> addCartByMedical(CurrentUser currentUser, CreatCartDto dtoCart, int medicalId) {
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
        return Optional.of(cartMapper.map(cart));
    }
}