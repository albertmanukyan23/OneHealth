package com.example.onehealthrest.endpoint;

import com.example.onehealthcommon.dto.CreatCartDto;
import com.example.onehealthcommon.dto.OrderDto;
import com.example.onehealthcommon.mapper.CartMapper;
import com.example.onehealthrest.security.CurrentUser;
import com.example.onehealthrest.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/cart")
@Slf4j
public class CartEndpoint {
    private final CartService cartService;
    private final CartMapper cartMapper;

    @PostMapping("/create/{medicalId}")
    public ResponseEntity<CreatCartDto> addMedicalByCart(@AuthenticationPrincipal CurrentUser currentUser,
                                                         @PathVariable("medicalId") int medicalId,
                                                         @RequestBody CreatCartDto dtoCart) {
        Optional<CreatCartDto> creatCartDto = cartService.addCartByMedical(currentUser, dtoCart, medicalId);
        return creatCartDto.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.CONFLICT).build());

    }
    @PostMapping
    public ResponseEntity<OrderDto> addMedicalByOrder(@AuthenticationPrincipal CurrentUser currentUser,
                                                      @RequestBody OrderDto orderDto) {
        Optional<OrderDto> dto = cartService.addOrderByMedical(currentUser, orderDto);
        return dto.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.CONFLICT).build());
    }

    @PostMapping("/remove/{medServId}")
    public ResponseEntity<CreatCartDto> delete(@PathVariable("medServId") int medServId,
                                               @AuthenticationPrincipal CurrentUser currentUser) {
        return cartService.deleteByIdMedServ(currentUser, medServId) ?
                ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
