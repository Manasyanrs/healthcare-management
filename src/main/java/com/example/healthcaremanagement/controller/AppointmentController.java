package com.example.healthcaremanagement.controller;

import com.example.healthcaremanagement.entity.Appointment;
import com.example.healthcaremanagement.entity.Doctor;
import com.example.healthcaremanagement.entity.Patient;
import com.example.healthcaremanagement.repository.AppointmentRepository;
import com.example.healthcaremanagement.repository.DoctorRepository;
import com.example.healthcaremanagement.repository.PatientRepository;
import com.example.healthcaremanagement.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/appointments")
public class AppointmentController {

    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @GetMapping()
    public String appointmentsPage(ModelMap modelMap) {
        List<Appointment> all = appointmentRepository.findAll();
        modelMap.addAttribute("appointmentList", all);
        return "appointment/appointmentPage";
    }

    @GetMapping("/chosenDate")
    public String chosenDatePage(ModelMap modelMap) {
        modelMap.addAttribute("currentDate", LocalDate.now().toString());
        return "appointment/chosenDatePage";
    }

    @GetMapping("/add")
    public String addAppointmentPage(@RequestParam("chosen") String chosen, ModelMap modelMap,
                                     RedirectAttributes redirectAttributes) {
        modelMap.addAttribute("chosenDate", chosen);

        List<String> registerDate = appointmentRepository.findAllByDateTimeLike(LocalDate.parse(chosen));
        List<String> receptionSlots = DateUtils.getFreeTime(registerDate);

        if (receptionSlots.isEmpty()) {
            redirectAttributes.addFlashAttribute("msg", "Please chose another day.");
            return "redirect:/appointments/chosenDate";
        }

        List<Patient> patientList = patientRepository.findAll();
        List<Doctor> doctorList = doctorRepository.findAll();

        modelMap.addAttribute("chosenDate", chosen);
        modelMap.addAttribute("patientList", patientList);
        modelMap.addAttribute("doctorList", doctorList);
        modelMap.addAttribute("receptionSlots", receptionSlots);

        return "appointment/addAppointmentPage";
    }

    @PostMapping("/add")
    public String addAppointment(@RequestParam("dateTime") String dateTime,
                                 @RequestParam(name = "patientId") String patientId,
                                 @RequestParam(name = "doctorId") String doctorId) {

        Date date = DateUtils.parsToFormatDate(dateTime);

        appointmentRepository.save(
                Appointment.builder()
                        .dateTime(date)
                        .doctor(doctorRepository.findById(Integer.parseInt(doctorId)).get())
                        .patient(patientRepository.findById(Integer.parseInt(patientId)).get())
                        .build()
        );

        return "redirect:/appointments";
    }

    @PostMapping("/delete")
    public String deleteAppointment(@RequestParam("id") int id) {
        appointmentRepository.deleteById(id);
        return "redirect:/appointments";
    }
}
