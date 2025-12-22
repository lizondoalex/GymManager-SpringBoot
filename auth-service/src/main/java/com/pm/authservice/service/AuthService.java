package com.pm.authservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pm.authservice.dto.LoginRequestDTO;
import com.pm.authservice.dto.RegisterRequestDTO;
import com.pm.authservice.model.User;
import com.pm.authservice.util.JwtUtil;
import io.jsonwebtoken.JwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserService userService, PasswordEncoder passwordEncoder, JwtUtil jwtUtil){
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public Optional<String> authenticate(LoginRequestDTO loginRequestDTO){
        Optional<String> token = userService.findByEmail(loginRequestDTO.getEmail())
                .filter(u -> passwordEncoder.matches(loginRequestDTO.getPassword(), u.getPassword()))
                .map( u -> jwtUtil.generateToken(u.getEmail(), u.getRole()));

        return token;
    }

    public boolean validateToken(String token){
        try{
            jwtUtil.validateToken(token);
            return true;
        } catch (JwtException ex){
            return false;
        }
    }

    public boolean validateEmail(String email){
        Optional<User> user = userService.findByEmail(email);
        if(user.isEmpty()){
            return false;
        }
        return true;
    }

    public Optional<String> register(RegisterRequestDTO registerRequestDTO){

        LocalDate currentTime = LocalDate.now();
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> data = new HashMap<>();
        data.put("name", registerRequestDTO.getName());
        data.put("email", registerRequestDTO.getEmail());
        data.put("address", registerRequestDTO.getAddress());
        data.put("dateOfBirth", registerRequestDTO.getDateOfBirth().toString());
        data.put("registerDate", currentTime.toString());

        String json;
        try {
            json = mapper.writeValueAsString(data);

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        String url = "http://member-service:4000/members";

        WebClient webClient = WebClient.create();

        String result =
                webClient.post()
                .uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(json)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        log.info("Results of the post {}", result);



        String hashedPassword = passwordEncoder.encode(registerRequestDTO.getPassword());
        User user = new User();
        user.setPassword(hashedPassword);
        user.setEmail(registerRequestDTO.getEmail());
        user.setRole("USER");
        userService.addUser(user);

        LoginRequestDTO loginRequestDTO = new LoginRequestDTO();
        loginRequestDTO.setEmail(registerRequestDTO.getEmail());
        loginRequestDTO.setPassword(registerRequestDTO.getPassword());

        Optional<String> tokenOptional = authenticate(loginRequestDTO);

        return tokenOptional;
    }
}