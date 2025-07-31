package com.example.demo.repository;

import com.example.demo.entity.OurEngineers;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface OurEngineerRepository extends JpaRepository<OurEngineers, Integer> {
    Optional<OurEngineers> findByEmail(String email);
}