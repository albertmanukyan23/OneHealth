package com.example.onehealth.controller;

import com.example.onehealth.entity.Department;
import com.example.onehealth.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequiredArgsConstructor
@RequestMapping("/department")
public class DepartmentController {

    private final DepartmentService departmentService;

    @GetMapping("/open-page")
    public String departmentPage(ModelMap modelMap,
                                 @RequestParam("page") Optional<Integer> page,
                                 @RequestParam("size") Optional<Integer> size
    ) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(5);
        Pageable pageable = PageRequest.of(currentPage - 1, pageSize);
        Page<Department> result = departmentService.getDepartment(pageable);
        int totalPages = result.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            modelMap.addAttribute("pageNumbers", pageNumbers);
        }
        modelMap.addAttribute("departments", result);
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
