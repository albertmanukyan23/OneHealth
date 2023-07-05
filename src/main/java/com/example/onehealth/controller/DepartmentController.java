package com.example.onehealth.controller;
import com.example.onehealth.entity.Department;
import com.example.onehealth.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;
@Controller
@RequiredArgsConstructor
@RequestMapping("/departments")
public class DepartmentController {

    private final DepartmentService departmentService;

    @GetMapping("/open-page")
    public String departmentPage(ModelMap modelMap,
                                 @RequestParam("page") Optional<Integer> page,
                                 @RequestParam("size") Optional<Integer> size
    ) {
        Page<Department> result = departmentService.getDepartmentPageData(page, size);
        List<Integer> pageNumbers = departmentService.getPageNumbers(result.getTotalPages());
        modelMap.addAttribute("pageNumbers", pageNumbers);
        modelMap.addAttribute("departments", result);
        return "department";
    }

    @GetMapping("/page-to-add")
    public String addDepartmentPage() {
        return "addDepartment";
    }

    @GetMapping("/edit-page")
    public String updateDepartmentPage(ModelMap modelMap, @ModelAttribute Department department) {
        modelMap.addAttribute("departments", department);
        return "updateDepartment";
    }

    @PostMapping("/add")
    public String addDepartment(@ModelAttribute Department department) {
        departmentService.addDepartment(department);
        return "redirect:/departments/open-page";
    }

    @PostMapping("/modify")
    public String updateDepartment(@ModelAttribute Department department) {
        departmentService.update(department);
        return "redirect:/departments/open-page";
    }

    @GetMapping("/remove")
    public String deleteDepartment(int id) {
        departmentService.deleteDepartment(id);
        return "redirect:/departments/open-page";
    }
}
