package com.example.project.controller;

import java.util.*;

import com.example.project.repository.AppointmentRepository;
//import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.project.Model.Appointment;
//import com.example.project.service.AppointmentService;

@RestController
public class AppointmentController {
    @Autowired
    private AppointmentRepository aRepository;



    @PostMapping("/appointment/register")
    public ResponseEntity<String> registerAppointment(@RequestBody Appointment a) {
        aRepository.save(a);
        return ResponseEntity.ok().body("Booking successfully");
    }

    @GetMapping("/appointment/list")
    public List<Appointment> getAllAppointment() {
        return aRepository.findAll();
    }

    @GetMapping("/appointment/view/{id}")
    public ResponseEntity<Appointment> getAppointmentById(@PathVariable(value = "id") Long Id){
        Optional<Appointment> p = aRepository.findById(String.valueOf(Id));
        if(p.isPresent())
            p.get();
            return ResponseEntity.ok().body(p.get());
    }


    @DeleteMapping("/appointment/delete/{id}")
    public Map<String, Boolean> deleteAppointment(@PathVariable(value = "id") Long Id){
        Optional<Appointment> p = aRepository.findById(String.valueOf(Id));
        aRepository.delete(p.get());
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }


}
