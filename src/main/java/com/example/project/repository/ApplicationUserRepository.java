package com.example.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.project.Model.ApplicationUser;

import java.util.Optional;


public interface ApplicationUserRepository  extends JpaRepository<ApplicationUser, String>{

    Optional<ApplicationUser> findById(String user_name);
    //Optional<ApplicationUser> findByuser_email(String user_email);
    //boolean existsByuser_email(String user_email);
}
