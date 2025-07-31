package com.example.demo.service;

import com.example.demo.repository.OurEngineerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class OurUserDetailsService implements UserDetailsService {
    @Autowired
    private OurEngineerRepository ourEngineerRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return ourEngineerRepository
                .findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Інженера з email " + username + " не знайдено"));
    }
}