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

import java.text.ParseException;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/appointments")
public class AppointmentController {
    private String chosenDate;
    private String msg;

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
        return "appointmentPage";
    }

    @GetMapping("/chosenDate")
    public String chosenDatePage(ModelMap modelMap) {
        modelMap.addAttribute("currentDate", LocalDate.now().toString());
        modelMap.addAttribute("msg", msg);
        msg = null;
        return "chosenDatePage";
    }

    @PostMapping("/chosenDate")
    public String chosenDate(@RequestParam("chosen") String chosen, ModelMap modelMap) {
        modelMap.addAttribute("chosenDate", chosen);
        chosenDate = chosen;
        List<String> registerDate = appointmentRepository.findAllByDateTimeLike(LocalDate.parse(chosenDate));
        List<String> receptionSlots = DateUtils.getFreeTime(registerDate);

        if (receptionSlots.isEmpty()) {
            msg = "Please chose another day.";
            return "redirect:/appointments/chosenDate";
        }
        return "redirect:/appointments/add";
    }

    @GetMapping("/add")
    public String addAppointmentPage(ModelMap modelMap) {
        List<String> registerDate = appointmentRepository.findAllByDateTimeLike(LocalDate.parse(chosenDate));
        List<String> receptionSlots = DateUtils.getFreeTime(registerDate);

        List<Patient> patientList = patientRepository.findAll();
        List<Doctor> doctorList = doctorRepository.findAll();

        modelMap.addAttribute("chosenDate", chosenDate);
        modelMap.addAttribute("patientList", patientList);
        modelMap.addAttribute("doctorList", doctorList);
        modelMap.addAttribute("receptionSlots", receptionSlots);

        return "addAppointmentPage";
    }

    @PostMapping("/add")
    public String addAppointment(@RequestParam(name = "time") String time,
                                 @RequestParam(name = "patientId") String patientId,
                                 @RequestParam(name = "doctorId") String doctorId) throws ParseException {

        Date chosenDateAndTime = DateUtils.parsToFormatDate(chosenDate + " " + time);

        appointmentRepository.save(
                Appointment.builder()
                        .dateTime(chosenDateAndTime)
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
