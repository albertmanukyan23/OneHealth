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

    /**
     * Deletes a medical service with the specified ID from the user's cart.
     * The method fetches the user's cart and removes the medical service with the given {@code medServId}.
     * If the cart and medical service exist, the cart will be updated and saved to the repository.
     *
     * @param currentUser The current user performing the action.
     * @param medServId   The ID of the medical service to be deleted from the cart.
     * @return {@code true} if the medical service was successfully deleted from the cart, {@code false} otherwise.
     */

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

    /**
     * Adds a medical service to the user's cart and returns the updated cart information as a DTO.
     * If the user's cart does not exist, a new cart will be created and associated with the user.
     * The method fetches the medical service based on the provided {@code medicalId} and adds it to the cart's set of
     * medical services. If the cart already exists, it will be updated and saved to the repository.
     *
     */

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