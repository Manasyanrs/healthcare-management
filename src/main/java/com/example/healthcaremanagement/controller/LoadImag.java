package com.example.healthcaremanagement.controller;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@Controller
public class LoadImag {
    @Value("${healthcare.management.doctors.avatars.path}")
    private String doctorsAvatarsPath;

    @Value("${healthcare.management.default.avatars.path}")
    private String defaultAvatar;

    @GetMapping(value = "/getImage",
            produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody byte[] getImage(@RequestParam("avatar") String avatar) throws IOException {
        File file = new File(doctorsAvatarsPath + avatar);
        FileInputStream fis;
        if (!avatar.equals("")) {
            fis = new FileInputStream(file);
            return IOUtils.toByteArray(fis);
        } else {
            file = new File(defaultAvatar);
            fis = new FileInputStream(file);
        }
        return IOUtils.toByteArray(fis);
    }

}
