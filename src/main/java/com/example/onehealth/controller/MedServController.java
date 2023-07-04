package com.example.onehealth.controller;

import com.example.onehealth.entity.MedServ;
import com.example.onehealth.security.CurrentUser;
import com.example.onehealth.service.MedServService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/medicalServices")
public class MedServController {

    private final MedServService medServService;
    @GetMapping
    public String getPriceList(ModelMap modelMap, @AuthenticationPrincipal CurrentUser currentUser) {
        List<MedServ> priceList = medServService.getPriceList();
        modelMap.addAttribute("priceList", priceList);
        modelMap.addAttribute("currentUser",currentUser);
        return "priceListView";
    }

    @GetMapping("/create")
    public String createMedicalService() {
        return "createMedicalService";
    }

    @PostMapping("/create")
    public String createMedicalService(@ModelAttribute MedServ medServ) {
        medServService.save(medServ);
        return "redirect:/medicalServices";
    }

    @GetMapping("/remove")
    public String deleteMedicalService(@RequestParam("id") int id) {
        medServService.delete(id);
        return "redirect:/medicalServices";
    }
}