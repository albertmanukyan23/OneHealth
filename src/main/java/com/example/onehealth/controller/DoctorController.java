package com.example.onehealth.controller;

import com.example.onehealth.entity.*;
import com.example.onehealth.security.CurrentUser;
import com.example.onehealth.service.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;


@Controller
@RequiredArgsConstructor
@RequestMapping("/doctor")
//todo doctors
public class DoctorController {

    private final UserService userService;
    private final DoctorService doctorService;
    private final AppointmentService appointmentService;
    private final DepartmentService departmentService;
    private final CommentService commentService;



    @GetMapping
    public String getDoctors(ModelMap modelMap,
                             @RequestParam("page") Optional<Integer> page,
                             @RequestParam("size") Optional<Integer> size
    ) {
        List<Department> departmentList = departmentService.getDepartmentList();
        Page<Doctor> result = doctorService.getDoctorPageData(page, size);
        List<Integer> pageNumbers = doctorService.getNumbersPage(result.getTotalPages());
        modelMap.addAttribute("pageNumbers", pageNumbers);
        modelMap.addAttribute("doctors", result);
        modelMap.addAttribute("departments", departmentList);
        return "doctors";
    }

    @GetMapping("/singlePage")
    //todo single-page
    public String singlePage(@AuthenticationPrincipal CurrentUser currentUser, ModelMap modelMap) {
        modelMap.addAttribute("doctor", currentUser.getUser());
        return "doctorSinglePage";

    }

    @GetMapping("/appointments")
    public String getDoctorPersonalAppointments(@AuthenticationPrincipal CurrentUser currentUser,
                                                ModelMap modelMap) {
        User user = currentUser.getUser();
        List<Appointment> doctorAppointments = appointmentService.getDoctorAppointments(user.getId());
        modelMap.addAttribute("appointments", doctorAppointments);
        return "doctorAppointments";
    }

    @GetMapping("/add")
    public String addDoctor(@ModelAttribute("doctor") Doctor doctor, ModelMap modelMap
    ) {
        List<Department> departmentList = departmentService.getDepartmentList();
        modelMap.addAttribute("departments", departmentList);
        return "addDoctor";
    }

    @PostMapping("/add")
    public String addDoctor(@ModelAttribute("doctor") @Valid Doctor doctor, BindingResult bindingResult,
                            @RequestParam("image") MultipartFile multipartFile) throws IOException {
        if (bindingResult.hasErrors()) {
            return "addDoctor";
        }
        doctorService.registerDoctor(doctor, multipartFile);
        return "redirect:/doctor";
    }

    @GetMapping("/update")
    public String updateDoctor(@ModelAttribute("doctor") Doctor doctor, ModelMap modelMap) {
        List<Department> departmentList = departmentService.getDepartmentList();
        Optional<Doctor> doctorById = doctorService.findDoctorById(doctor.getId());
        modelMap.addAttribute("departments", departmentList);
        doctorById.ifPresent(doctorFromDb -> modelMap.addAttribute("doctor", doctorFromDb));
        return "updateDoctor";
    }

    @PostMapping("/update")
    public String updateDoctor(@ModelAttribute("doctor") @Valid Doctor doctor, BindingResult bindingResult,
                               @RequestParam("image") MultipartFile multipartFile) throws IOException {
        if (bindingResult.hasErrors()) {
            return "updateDoctor";
        }
        doctorService.update(doctor, multipartFile);
        return "redirect:/doctor";
    }

    @GetMapping("/remove")
    public String removeDoctor(@RequestParam("id") int id) throws IOException {
        userService.deleteUser(id);
        return "redirect:/doctor";
    }

    @GetMapping("/search")
    public String doctorSearch(@RequestParam("searchText") String searchText, ModelMap modelMap) {
        List<Doctor> doctors = doctorService.searchDoctorsByKey(searchText);
        modelMap.addAttribute("doctors", doctors);
        return "searchDoctors";
    }

    @GetMapping("/details/{id}")
    public String doctorDetails(@PathVariable("id") int doctorId, ModelMap modelMap,
                                @AuthenticationPrincipal CurrentUser currentUser) {
        List<Comment> comments = commentService.findCommentByDoctorId(doctorId);
        Optional<Doctor> doctorById = doctorService.findDoctorById(doctorId);
        doctorById.ifPresent(doctor -> modelMap.addAttribute("doctor", doctor));
        modelMap.addAttribute("comments",comments);
        modelMap.addAttribute("currentUser",currentUser.getUser());
        return "doctorDetails";
    }

}
