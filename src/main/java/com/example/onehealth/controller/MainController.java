package com.example.onehealth.controller;


import com.example.onehealth.entity.UserType;
import com.example.onehealth.security.CurrentUser;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@Controller
public class MainController {
    @Value("${hospital.doctor.upload.image.path}")
    private String imageUploadPath;
    @GetMapping("/")
    public String indexPage() {
        return "index";
    }

    @GetMapping("customLogin")
    public String customLogin() {
        return "customLogin";

    }

    @GetMapping("/customSuccessLogIn")
    public String customSuccessLogIn(@AuthenticationPrincipal CurrentUser currentUser) {
        if (currentUser != null) {
            if (currentUser.getUser().getUserType() == UserType.PATIENT) {
                return "redirect:/";
            } else if (currentUser.getUser().getUserType() == UserType.ADMIN) {
                return "redirect:user/admin";
            } else {
                return "redirect:/customLogin";
            }
        }
        return "/customLogin";

    }
    @GetMapping(value = "/getImage",
            produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody byte[] getImage(@RequestParam("imageName") String imageName) throws IOException {
        File file = new File(imageUploadPath + imageName);
        if (file.exists()) {
            FileInputStream fis = new FileInputStream(file);
            return IOUtils.toByteArray(fis);
        }
        return null;
    }
    @GetMapping("/admin")
    public String showAdminPage() {
        return "admin";
    }

}
