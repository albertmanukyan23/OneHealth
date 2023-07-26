package com.example.onehealthcommon.util;

import com.example.onehealthcommon.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
@Slf4j
public class ImageDownloader {

    @Value("${hospital.upload.image.path}")
    private String imageUploadPath;

    public void saveProfilePicture(MultipartFile multipartFile, User user) throws IOException {
        if (multipartFile != null && !multipartFile.isEmpty()) {
            String fileName = System.nanoTime() + "_" + multipartFile.getOriginalFilename();
            File file = new File(imageUploadPath + fileName);
            multipartFile.transferTo(file);
            user.setPicName(fileName);
            log.info("saveProfilePicture() worked for the user with " + user.getId() + " id");
        }
    }

    public void deleteProfilePicture(String filename) throws IOException
    {
        if (filename != null && !filename.equalsIgnoreCase("null")) {
            Path path = Paths.get(imageUploadPath + filename);
            Files.delete(path);
            log.info("deleteProfilePicture() worked ");

        }
    }
}
