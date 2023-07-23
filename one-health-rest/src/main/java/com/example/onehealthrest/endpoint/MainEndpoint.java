package com.example.onehealthrest.endpoint;
import com.example.onehealthcommon.entity.UserType;

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

@Controller
public class MainEndpoint {

    @Value("${hospital.upload.image.path}")
    private String imageUploadPath;

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

}
