package com.example.onehealth.controller;

import com.example.onehealth.entity.Department;
import com.example.onehealth.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;


@Controller
@RequiredArgsConstructor
@RequestMapping("/department")
public class DepartmentController {
    private final DepartmentService departmentService;

    @GetMapping("/open-page")
    public String departmentPage(ModelMap modelMap) {
        List<Department> department = departmentService.getDepartment();
        modelMap.addAttribute("departments", department);
        return "department";
    }

    @GetMapping("/add")
    public String addDepartmentPage() {
        return "addDepartment";
    }

    @GetMapping("/update")
    public String updateDepartmentPage(ModelMap modelMap, @ModelAttribute Department department) {
        modelMap.addAttribute("departments", department);
        return "updateDepartment";
    }

    @PostMapping("/add")
    public String addDepartment(@ModelAttribute Department department) {
        departmentService.addDepartment(department);
        return "redirect:/department/open-page";
    }

    @PostMapping("/update")
    public String updateDepartment(@ModelAttribute Department department) {
        departmentService.update(department);
        return "redirect:/department/open-page";
    }

    @GetMapping("/remove")
    public String deleteDepartment(int id) {
        departmentService.deleteDepartment(id);
        return "redirect:/department/open-page";
    }
}
