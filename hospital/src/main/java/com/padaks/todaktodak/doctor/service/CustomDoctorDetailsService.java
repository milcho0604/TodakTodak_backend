package com.padaks.todaktodak.doctor.service;

import com.padaks.todaktodak.doctor.domain.Doctor;
import com.padaks.todaktodak.doctor.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CustomDoctorDetailsService implements UserDetailsService {

    @Autowired
    private DoctorRepository doctorRepository;

    @Override
    public UserDetails loadUserByUsername(String doctorEmail) throws UsernameNotFoundException {
        Doctor doctor = doctorRepository.findByDoctorEmail(doctorEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + doctorEmail));

        return new org.springframework.security.core.userdetails.User(doctor.getDoctorEmail(), doctor.getPassword(), new ArrayList<>());
    }
}
