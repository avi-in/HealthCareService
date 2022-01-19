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

   // @Autowired
   // ApplicationUserRepository userRepository;


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
        String email="";
        Optional<ApplicationUser> au=uRepository.findById(u.getUser_name());
           if(au.isPresent()) {
               email = au.get().getUser_email();
               System.out.println("Email Id for existing user: "+email);
           }
           else {
               email = "";
           }
           if( u.getUser_email().equals(email))
                  return ResponseEntity.status(422).body("Password or username policy failed");
           us.saveUser(u);
           return ResponseEntity.ok().body("Registration successfully");
       // return ResponseEntity.status(422).body("Password or username policy failed");
    }


    @PostMapping("/signin")
    public /*ResponseEntity<JSONObject> */ Map<String, String> authenticate(@RequestBody ApplicationUser u) throws Exception {
        HashMap<String, String> map = new HashMap<>();
        final UserDetails userDetails = userAuthService.loadUserByUsername(u.getUser_name());
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userDetails, u.getPassword(),null ));
            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (BadCredentialsException e) {
            map.put("message","Invalid username or password");
            return map;
           // throw new Exception("INVALID_CREDENTIALS", e);
        }

        final String token = jwtUtils.generateToken(userDetails);
/*
        JSONObject js=new JSONObject();
        js.put("message","Registration successfully");
        js.put("token",token);
        return ResponseEntity.ok(js);

 */
       // HashMap<String, String> map = new HashMap<>();
        map.put("message", "Authentication successful!");
        map.put("token", token);
        map.put("id",u.getUser_name());
       return map;
    }


    @GetMapping("/viewprofile/{userId}")
    public ResponseEntity<?> getUserById(@PathVariable(value = "userId") String userId){
        Optional<ApplicationUser> u = uRepository.findById(userId);
        if(u.isPresent())
           return ResponseEntity.ok().body(u.get());
        else
            return ResponseEntity.status(500).body("User not exists");
    }

   @GetMapping("/editprofile/{userId}")
    public ResponseEntity<?> editUserById(@PathVariable(value = "userId") String userId,  @RequestBody ApplicationUser userDetails){
        Optional<ApplicationUser> u1 = uRepository.findById(userId);
        if(u1.isPresent()) {
            ApplicationUser u = u1.get();
            u.setUser_email(userDetails.getUser_email());
            u.setUser_name(userDetails.getUser_name());
            u.setLocation(userDetails.getLocation());
            u.setUser_mobile(userDetails.getUser_mobile());
            final ApplicationUser updatedUser = us.saveUser(u);//uRepository.save(u);
            return ResponseEntity.ok(updatedUser);
        }
        else {
            return ResponseEntity.status(500).body("user not exists with given Id");
        }
    }







}
