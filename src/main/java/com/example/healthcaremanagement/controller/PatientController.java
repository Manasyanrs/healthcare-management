package com.example.healthcaremanagement.controller;

import com.example.healthcaremanagement.entity.Patient;
import com.example.healthcaremanagement.repository.PatientRepository;
import com.example.healthcaremanagement.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/patients")
public class PatientController {

    @Autowired
    private PatientRepository patientRepository;

    @GetMapping()
    public String getPatientsPage(ModelMap modelMap) {
        List<Patient> all = patientRepository.findAll();
        modelMap.addAttribute("patientList", all);
        return "patient/patientsPage";
    }

    @GetMapping("/add")
    public String getAddPatientPage(ModelMap modelMap) {
        String dateNow = LocalDate.now().toString();
        modelMap.addAttribute("date", dateNow);
        return "patient/addPatient";
    }

    @PostMapping("/add")
    public String savePatient(@RequestParam("name") String name,
                              @RequestParam("surname") String surname,
                              @RequestParam("dateOfBirthday") String dateOfBirthday) throws ParseException {

        patientRepository.save(
                Patient.builder()
                        .name(name)
                        .surname(surname)
                        .dateOfBirthday(DateUtils.parseToDate(dateOfBirthday))
                        .build()
        );
        return "redirect:/patients";
    }

    @PostMapping("/delete")
    public String deletePatient(@RequestParam("id") int id) {
        patientRepository.deleteById(id);
        return "redirect:/patients";
    }

}
