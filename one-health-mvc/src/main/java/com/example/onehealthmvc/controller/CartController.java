package com.example.onehealthmvc.controller;

import com.example.onehealthcommon.entity.Cart;
import com.example.onehealthcommon.entity.MedServ;
import com.example.onehealthcommon.entity.User;
import com.example.onehealthmvc.security.CurrentUser;
import com.example.onehealthmvc.service.CartService;
import com.example.onehealthmvc.service.MedServService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import java.util.*;
@Controller
@RequiredArgsConstructor
@RequestMapping("/cart")
public class CartController {
    private final CartService cartService;
    private final MedServService medServService;

    @GetMapping("/online-payments-page")
    public String onlinePaymentsPage(ModelMap modelMap) {
        List<MedServ> medServIes = medServService.findAll();
        modelMap.addAttribute("medServ", medServIes);
        return "onlinePayments";
    }

    @GetMapping("/see/context")
    public String order(@AuthenticationPrincipal CurrentUser currentUser
            , ModelMap modelMap) {
        User user = currentUser.getUser();
        Optional<Cart> cartByUserId = cartService.findCartByUserId(user.getId());
        Set<MedServ> medServSet = cartByUserId.get().getMedServSet();
        modelMap.addAttribute("totalSum", cartService.countPrice(medServSet));
        modelMap.addAttribute("medServList", medServSet);
        return "orders";
    }

    @GetMapping("do/order")
    public String order(@AuthenticationPrincipal CurrentUser currentUser) {
        cartService.addOrderByMedical(currentUser);
        return "redirect:/cart/online-payments-page";
    }

    @GetMapping("/add")
    public String addMedicalByCart(@AuthenticationPrincipal CurrentUser currentUser,
                                   @RequestParam("medicalId") int medicalId) {
        cartService.addCartByMedical(currentUser, medicalId);
        return "redirect:/cart/see/context";
    }


    @PostMapping("/remove/{medServId}")
    public String delete(@PathVariable("medServId") int medServId,
                         @AuthenticationPrincipal CurrentUser currentUser) {
        cartService.deleteByIdMedServ(currentUser, medServId);
        return "redirect:/cart/see/context";
    }
}
