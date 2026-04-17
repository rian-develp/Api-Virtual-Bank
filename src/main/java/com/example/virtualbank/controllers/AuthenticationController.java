package com.example.virtualbank.controllers;


import com.example.virtualbank.auth.request.AuthRequestSignUpDTO;
import com.example.virtualbank.auth.response.AuthResponseDTO;
import com.example.virtualbank.repositories.CustomerRepository;
import com.example.virtualbank.responsebody.ResponseBody;
import com.example.virtualbank.services.auth.AuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final CustomerRepository repository;
    private final PasswordEncoder encoder;
    private final AuthenticationService service;

    public AuthenticationController(CustomerRepository repository, PasswordEncoder encoder, AuthenticationService service) {
        this.repository = repository;
        this.encoder = encoder;
        this.service = service;
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseBody<String>> login(@RequestBody AuthRequestSignUpDTO dto){
        var response = service.login(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new ResponseBody<>(HttpStatus.CREATED.value(), response)
        );
    }

    @PostMapping("/signup")
    public ResponseEntity<ResponseBody<AuthResponseDTO>> signup(@RequestBody AuthRequestSignUpDTO dto){
        AuthResponseDTO response = service.signup(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new ResponseBody<>(HttpStatus.CREATED.value(), response)
        );
    }
}
