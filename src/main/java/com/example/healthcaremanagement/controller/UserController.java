package com.example.healthcaremanagement.controller;

import com.example.healthcaremanagement.entity.Address;
import com.example.healthcaremanagement.entity.User;
import com.example.healthcaremanagement.repository.AddressRepository;
import com.example.healthcaremanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/user")

public class UserController {

    @Value("${healthcare.management.users.avatars.path}")
    private String userAvatar;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    @GetMapping("/register")
    public String getUserRegisterPage() {
        return "user/registerUserPage";
    }

    @PostMapping("/register")
    public String postUserRegister(@ModelAttribute User user, @ModelAttribute Address address,
                                   @RequestParam("profile_pic") MultipartFile multipartFile,
                                   RedirectAttributes redirectAttributes) throws IOException {

        Optional<User> byEmail = userRepository.findByEmail(user.getEmail());
        if (byEmail.isEmpty()){
            Address addressObj = addressRepository.save(address);

            if (multipartFile != null && !multipartFile.isEmpty()){
                String uploadFileName = multipartFile.getOriginalFilename();
                String avatarName = System.nanoTime() + "_" + user.getId() +
                        uploadFileName.substring(uploadFileName.length() - 4);

                Path fileNameAndPath = Paths.get(userAvatar, avatarName);
                Files.write(fileNameAndPath, multipartFile.getBytes());

                user.setAvatar(avatarName);
            }
            user.setAddress(addressObj);
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(user);
            return "redirect:/";
        }
        redirectAttributes.addFlashAttribute("msg", "Username or password is exist");
        return "redirect:/user/register";

    }

    @GetMapping("/users")
    public String showUsers(ModelMap modelMap){
        List<User> all = userRepository.findAll();
        modelMap.addAttribute("users", all);
        return "user/showUsers";
    }

    @PostMapping("/delete")
    public String deleteUser(@RequestParam("id") int id) {
        userRepository.deleteById(id);
        return "redirect:/user/users";
    }

}
