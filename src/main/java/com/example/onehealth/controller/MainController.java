package com.example.onehealth.controller;


import com.example.onehealth.entity.User;
import com.example.onehealth.entity.UserType;
import com.example.onehealth.security.CurrentUser;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static org.apache.commons.io.IOUtils.close;

@Controller
public class MainController {

    @Value("${hospital.upload.image.path}")
    private String imageUploadPath;

    @GetMapping("/")
    public String indexPage() {
        return "index";
    }

    @GetMapping("customLogin")
    public String customLogin(@RequestParam(value = "error", required = false) String error, ModelMap modelMap) {
        if (error != null) {
            modelMap.addAttribute("errorMessage", "Invalid username or password");
        }
        return "customLogin";

    }

    @GetMapping("/customSuccessLogIn")
    public String customSuccessLogIn(@AuthenticationPrincipal CurrentUser currentUser) {
        if (currentUser != null) {
            if (currentUser.getUser().getUserType() == UserType.PATIENT) {
                return "redirect:/patients/singlePage";
            } else if (currentUser.getUser().getUserType() == UserType.ADMIN) {
                return "redirect:/admin";
            } else {
                return "redirect:/doctor/singlePage";
            }
        }
        return "/customLogin";

    }


    @GetMapping(value = "/getImage",
            produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody byte[] getImage(@RequestParam("imageName") String imageName) throws IOException {
        File file = new File(imageUploadPath + imageName);
        if (file.exists()) {
            try (FileInputStream fis = new FileInputStream(file)) {
                return IOUtils.toByteArray(fis);
            }
        }
        return null;
    }

    @GetMapping("/admin")
    public String showAdminPage() {
        return "admin";
    }

}
