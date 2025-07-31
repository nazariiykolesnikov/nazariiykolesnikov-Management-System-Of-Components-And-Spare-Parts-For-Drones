package com.example.demo.service;

import com.example.demo.dto.ReqResDto;
import com.example.demo.entity.OurEngineers;
import com.example.demo.repository.OurEngineerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class EngineersManagementService {
    @Autowired
    private OurEngineerRepository ourEngineerRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public ReqResDto registerNewUser(ReqResDto registrationRequest) {
        ReqResDto response = new ReqResDto();

        try {
            OurEngineers ourEngineers = new OurEngineers();
            ourEngineers.setEmail(registrationRequest.getEmail());
            ourEngineers.setPhoneNumber(registrationRequest.getPhoneNumber());
            ourEngineers.setAddress(registrationRequest.getAddress());
            ourEngineers.setRole(registrationRequest.getRole());
            ourEngineers.setName(registrationRequest.getName());
            ourEngineers.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));

            OurEngineers savedEngineers = ourEngineerRepository.save(ourEngineers);
            if (savedEngineers.getEngineerId() != null) {
                response.setOurEngineers(savedEngineers);
                response.setMessage("Користувачі були успішно зареєстровані !");
                response.setStatusCode(200);
            }
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setError(e.getMessage());
        }
        return response;
    }

    public ReqResDto loginUser(ReqResDto loginRequest) {
        ReqResDto response = new ReqResDto();

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginRequest.getEmail(), loginRequest.getPassword()));
            var user = ourEngineerRepository.findByEmail(loginRequest.getEmail()).orElseThrow();
            var jwt = jwtUtils.generateToken(user);
            var refreshToken = jwtUtils.generateRefreshToken(new HashMap<>(), user);
            response.setStatusCode(200);
            response.setToken(jwt);
            response.setRole(user.getRole());
            response.setRefreshToken(refreshToken);
            response.setExpirationTime("24Hrs");
            response.setMessage("Успішний вхід в систему !");
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setError(e.getMessage());
        }
        return response;
    }

    public ReqResDto refreshToken(ReqResDto refreshTokenRequest) {
        ReqResDto response = new ReqResDto();

        try {
            String ourEmail = jwtUtils.extractUsername(refreshTokenRequest.getToken());
            OurEngineers engineers = ourEngineerRepository.findByEmail(ourEmail).orElseThrow();

            if (jwtUtils.isTokenValid(refreshTokenRequest.getToken(), engineers)) {
                var jwt = jwtUtils.generateToken(engineers);
                var refreshToken = jwtUtils.generateRefreshToken(new HashMap<>(), engineers);
                response.setStatusCode(200);
                response.setToken(jwt);
                response.setRefreshToken(refreshToken);
                response.setExpirationTime("24Hrs");
                response.setMessage("Успішно оновлено токени !");
            }
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setError(e.getMessage());
        }
        return response;
    }

    public ReqResDto getAllEngineers() {
        ReqResDto responce = new ReqResDto();

        try {
            List<OurEngineers> result = ourEngineerRepository.findAll();

            if (!result.isEmpty()) {
                responce.setOurEngineersList(result);
                responce.setStatusCode(200);
                responce.setMessage("Успішно отримано список усіх інженерів ! :)");
            } else {
                responce.setStatusCode(404);
                responce.setMessage("Жодного інженера не було знайдено ! :(");
            }
            return responce;
        } catch(Exception e) {
            responce.setStatusCode(500);
            responce.setMessage("Виникла помилка :" + e.getMessage());
            return responce;
        }
    }

    public ReqResDto getEngineersById(Integer engineerId) {
        ReqResDto responce = new ReqResDto();

        try {
            OurEngineers engineersById = ourEngineerRepository
                    .findById(engineerId)
                    .orElseThrow(() -> new RuntimeException("Інженера з даним ID не було знайдено :("));
            responce.setOurEngineers(engineersById);
            responce.setStatusCode(200);
            responce.setMessage("Інженера з ID " + engineerId + " було успіщно знайдено :)");
        } catch(Exception e) {
            responce.setStatusCode(500);
            responce.setMessage("Виникла помилка :" + e.getMessage());
        }
        return responce;
    }

    public ReqResDto deleteEngineer(Integer engineerId) {
        ReqResDto reqRes = new ReqResDto();

        try {
            Optional<OurEngineers> engineersOptional = ourEngineerRepository.findById(engineerId);

            if (engineersOptional.isPresent()) {
                ourEngineerRepository.deleteById(engineerId);
                reqRes.setStatusCode(200);
                reqRes.setMessage("Інженера з ID " + engineerId + " було успішно видалено !");
            } else {
                reqRes.setStatusCode(404);
                reqRes.setMessage("Іженера даним ID не було знайдено");
            }
        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setMessage("Під час видалення інженера виникла помилка :" + e.getMessage());
        }
        return reqRes;
    }

    public ReqResDto updateEngineer(Integer engineerId, OurEngineers updatedEngineer) {
        ReqResDto reqRes = new ReqResDto();

        try {
            Optional<OurEngineers> engineerOptional = ourEngineerRepository.findById(engineerId);

            if (engineerOptional.isPresent()) {
                OurEngineers availableEngineers = engineerOptional.get();
                availableEngineers.setName(updatedEngineer.getName());
                availableEngineers.setEmail(updatedEngineer.getEmail());
                availableEngineers.setPhoneNumber(updatedEngineer.getPhoneNumber());
                availableEngineers.setAddress(updatedEngineer.getAddress());
                availableEngineers.setRole(updatedEngineer.getRole());

                if (updatedEngineer.getPassword() != null && !updatedEngineer.getPassword().isEmpty()) {
                    availableEngineers.setPassword(passwordEncoder.encode(updatedEngineer.getPassword()));
                }

                OurEngineers savedEngineer = ourEngineerRepository.save(availableEngineers);
                reqRes.setOurEngineers(savedEngineer);
                reqRes.setStatusCode(200);
                reqRes.setMessage("Користувач з Id: " + engineerId + " був успішно оновлений :)");
            } else {
                reqRes.setStatusCode(404);
                reqRes.setMessage("Користувача з Id: " + engineerId + " не було знайдено для оновлення");
            }
        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setMessage("Виникла помилка під час отримання інформації про інженера з ID "
                    + engineerId + " :(" + e.getMessage());
        }
        return reqRes;
    }

    public ReqResDto getMyInfo(String email) {
        ReqResDto reqRes = new ReqResDto();
        try {
            Optional<OurEngineers> userOptional = ourEngineerRepository.findByEmail(email);
            if (userOptional.isPresent()) {
                reqRes.setOurEngineers(userOptional.get());
                reqRes.setStatusCode(200);
                reqRes.setMessage("Інформація про інженера з E-mail " + email + " була успішно знайдена :)");
            } else {
                reqRes.setStatusCode(404);
                reqRes.setMessage("Інформацію про даного інженера не було знайдено :(");
            }
        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setMessage("Виникла помилка під час знаходження інформації про інженера: "
                    + e.getMessage());
        }
        return reqRes;
    }
}
