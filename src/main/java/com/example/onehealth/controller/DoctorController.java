package com.example.onehealth.controller;

import com.example.onehealth.entity.Department;
import com.example.onehealth.entity.Doctor;
import com.example.onehealth.entity.UserType;
import com.example.onehealth.service.DepartmentService;
import com.example.onehealth.service.DoctorService;
import com.example.onehealth.service.UserService;
import com.example.onehealth.service.impl.DepartmentServiceImpl;
import com.example.onehealth.util.UserUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


@Controller
@RequiredArgsConstructor
@RequestMapping("/doctor")
public class DoctorController {
    private final UserService userService;
    private final DoctorService doctorService;
    private final DepartmentService departmentService;
    private final UserUtil userUtil;

    @GetMapping
    public String getDoctors(ModelMap modelMap,
                             @RequestParam("page") Optional<Integer> page,
                             @RequestParam("size") Optional<Integer> size
    ) {
        List<Department> departmentList = departmentService.getDepartmentList();
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(5);
        Pageable pageable = PageRequest.of(currentPage - 1, pageSize);
        Page<Doctor> result= doctorService.getDoctorPage(pageable);
        int totalPages = result.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            modelMap.addAttribute("pageNumbers", pageNumbers);
        }
        modelMap.addAttribute("doctors", result);
        modelMap.addAttribute("departments", departmentList);
        return "doctors";
    }

    @GetMapping("/add")
    public String addDoctor(@ModelAttribute("doctor") Doctor doctor, ModelMap modelMap,
                            Department department
                            ) {
        modelMap.addAttribute("departments",department);
        return "addDoctor";
    }

    @PostMapping("/add")
    public String addDoctor(@ModelAttribute("doctor") @Valid Doctor doctor, BindingResult bindingResult,
                            @RequestParam("image") MultipartFile multipartFile) throws IOException {
        if (bindingResult.hasErrors()) {
            return "addDoctor";
        }
        userUtil.saveProfilePicture(multipartFile, doctor);
        doctor.setRegisDate(new Date());
        doctor.setUserType(UserType.DOCTOR);
        userService.registerUser(doctor);
        return "redirect:/doctor";
    }

    @GetMapping("/update")
    public String updateDoctor( @ModelAttribute("doctor") Doctor doctor,ModelMap modelMap) {
        List<Department> departmentList = departmentService.getDepartmentList();
        Optional<Doctor> doctorById = doctorService.findDoctorById(doctor.getId());
        modelMap.addAttribute("departments",departmentList);
        doctorById.ifPresent(doctorFromDb-> modelMap.addAttribute("doctor", doctorFromDb));
        return "updateDoctor";
    }

    @PostMapping("/update")
    public String updateDoctor(@ModelAttribute("doctor") @Valid Doctor doctor, BindingResult bindingResult,
                               @RequestParam("image") MultipartFile multipartFile) throws IOException {
        if (bindingResult.hasErrors()) {
            return "updateDoctor";
        }
        userUtil.saveProfilePicture(multipartFile, doctor);
        doctorService.update(doctor);
        return "redirect:/doctor";
    }

    @GetMapping("/remove")
    public String removeUser(@RequestParam("id") int id) {
        userService.deleteUser(id);
        return "redirect:/doctor";
    }
}
