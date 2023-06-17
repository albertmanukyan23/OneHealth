package com.example.onehealth.controller;

import com.example.onehealth.entity.Department;
import com.example.onehealth.service.DepartmentService;
import com.example.onehealth.service.DoctorService;
import com.example.onehealth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
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
        return "addDepartment.html";
    }
}
