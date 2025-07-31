package com.example.demo.controller;

import com.example.demo.dto.ReqResDto;
import com.example.demo.entity.OurEngineers;
import com.example.demo.service.EngineersManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
public class EngineerManagementController {
    @Autowired
    private EngineersManagementService engineersManagementService;

    @PostMapping("/auth/register")
    public ResponseEntity<ReqResDto> register(@RequestBody ReqResDto reqResDto) {
        return ResponseEntity.ok(engineersManagementService.registerNewUser(reqResDto));
    }

    @PostMapping("/auth/login")
    public ResponseEntity<ReqResDto> login(@RequestBody ReqResDto reqResDto) {
       return ResponseEntity.ok(engineersManagementService.loginUser(reqResDto));
    }

    @GetMapping("/admin/get-all-engineers")
    public ResponseEntity<ReqResDto> getAllEngineers() {
        return ResponseEntity.ok(engineersManagementService.getAllEngineers());
    }

    @GetMapping("/admin/get-engineers/{engineerId}")
    public ResponseEntity<ReqResDto> getEngineerById(@PathVariable Integer engineerId) {
        return ResponseEntity.ok(engineersManagementService.getEngineersById(engineerId));
    }

    @GetMapping("/admin/update/{engineerId}")
    public ResponseEntity<ReqResDto> updateEngineerWithId(
            @PathVariable Integer engineerId, @RequestBody OurEngineers reqRes
    ) {
        return ResponseEntity.ok(engineersManagementService.updateEngineer(engineerId, reqRes));
    }

    @GetMapping("/enginner/get-profile")
    public ResponseEntity<ReqResDto> getProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        ReqResDto response = engineersManagementService.getMyInfo(email);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/admin/update/{engineerId}")
    public ResponseEntity<ReqResDto> deleteEngineerWithId(@PathVariable Integer engineerId) {
        return ResponseEntity.ok(engineersManagementService.deleteEngineer(engineerId));
    }
}
