package com.example.healthcaremanagement.controller;

import com.example.healthcaremanagement.entity.Doctor;
import com.example.healthcaremanagement.repository.DoctorRepository;
import com.example.healthcaremanagement.util.DoctorProfession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Controller
@RequestMapping("/doctors")
public class DoctorController {

    @Value("${healthcare.management.users.avatars.path}")
    private String doctorsAvatarsPath;

    @Autowired
    private DoctorRepository doctorRepository;

    @GetMapping()
    public String getDoctorPage(ModelMap modelMap) {
        List<Doctor> all = doctorRepository.findAll();
        modelMap.addAttribute("doctorsList", all);
        return "doctor/doctorsPage";
    }

    @GetMapping("/add")
    public String getAddDoctorPage(ModelMap modelMap) {
        DoctorProfession[] values = DoctorProfession.values();
        modelMap.addAttribute("professions", values);
        return "doctor/addDoctor";
    }

    @PostMapping("/add")
    public String saveDoctor(@ModelAttribute Doctor doctor,
                             @RequestParam("profile_pic") MultipartFile multipartFile) throws IOException {
        if (multipartFile != null && !multipartFile.isEmpty()) {
            String uploadFileName = multipartFile.getOriginalFilename();
            String avatarName = System.nanoTime() + "_" + doctor.getId() +
                    uploadFileName.substring(uploadFileName.length() - 4);

            Path fileNameAndPath = Paths.get(doctorsAvatarsPath, avatarName);
            Files.write(fileNameAndPath, multipartFile.getBytes());

            doctor.setAvatar(avatarName);

        }
        doctorRepository.save(doctor);
        return "redirect:/doctors";
    }

    @PostMapping("/delete")
    public String deleteDoctor(@RequestParam("id") int id) {
        doctorRepository.deleteById(id);
        return "redirect:/doctors";
    }
}
