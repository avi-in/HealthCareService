package com.example.project.controller;

import com.example.project.repository.ApplicationUserRepository;
import com.example.project.security.JwtUtil;
import com.example.project.service.UserAuthService;
//import jdk.nashorn.internal.scripts.JS;
//import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import com.example.project.Model.ApplicationUser;
import com.example.project.service.ApplicationUserService;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin("*")
public class ApplicationUserController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    ApplicationUserRepository userRepository;


    @Autowired
    JwtUtil jwtUtils;
    @Autowired
    private UserAuthService userAuthService;
    @Autowired
    private ApplicationUserService us;

    @Autowired
    ApplicationUserRepository uRepository;


    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody ApplicationUser u) {
        us.saveUser(u);
        //uRepository.save(u);
        return ResponseEntity.ok().body("Registration successfully");
    }


    @PostMapping("/signin")
    public /*ResponseEntity<JSONObject> */ Map<String, String> authenticate(@RequestBody ApplicationUser u) throws Exception {

        final UserDetails userDetails = userAuthService.loadUserByUsername(u.getUser_name());
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userDetails, u.getPassword(),null ));
            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }

        final String token = jwtUtils.generateToken(userDetails);
/*
        JSONObject js=new JSONObject();
        js.put("message","Registration successfully");
        js.put("token",token);
        return ResponseEntity.ok(js);

 */
        HashMap<String, String> map = new HashMap<>();
        map.put("message", "Registration successfully");
        map.put("token", token);
       return map;
    }


    @GetMapping("/viewprofile/{userId}")
    public ResponseEntity<ApplicationUser> getUserById(@PathVariable(value = "userId") String userId){
        Optional<ApplicationUser> u = uRepository.findById(userId);
        return ResponseEntity.ok().body(u.get());
    }

   @GetMapping("/editprofile/{userId}")
    public ResponseEntity<ApplicationUser> editUserById(@PathVariable(value = "userId") String userId,  @RequestBody ApplicationUser userDetails){
        Optional<ApplicationUser> u1 = uRepository.findById(userId);
        ApplicationUser u=u1.get();
        u.setUser_email(userDetails.getUser_email());
        u.setUser_name(userDetails.getUser_name());
        u.setLocation(userDetails.getLocation());
        u.setUser_mobile(userDetails.getUser_mobile());
        final ApplicationUser updatedUser= uRepository.save(u);
        return ResponseEntity.ok(updatedUser);

    }







}
