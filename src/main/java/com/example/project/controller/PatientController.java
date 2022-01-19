package com.example.project.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.example.project.repository.PatientRepository;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.project.Model.Patient;
import com.example.project.service.PatientService;
@RestController
public class PatientController {



    @Autowired
    private PatientRepository pRepository;


    @PostMapping("/patients/register")
    public ResponseEntity<String> registerPatient(@RequestBody Patient patient) {
        pRepository.save(patient);
        return ResponseEntity.ok().body("Registration successfully");
    }

    @GetMapping("/patients/list")
    public List<Patient> getAllPatients() {
        return pRepository.findAll();
    }

    @GetMapping("/patients/view/{id}")
    public ResponseEntity<Patient> getPatientById(@PathVariable(value = "id") String Id){
        Optional<Patient> p = pRepository.findById(Id);
        return ResponseEntity.ok().body(p.get());
    }


    @DeleteMapping("/patients/delete/{id}")
    public Map<String, Boolean> deletePatientById(@PathVariable(value = "id") String Id){
        Optional<Patient> p = pRepository.findById(Id);
        pRepository.delete(p.get());
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }


}
