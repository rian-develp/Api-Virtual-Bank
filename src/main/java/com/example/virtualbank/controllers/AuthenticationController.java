package com.example.virtualbank.controllers;


import com.example.virtualbank.auth.request.AuthRequestLoginDTO;
import com.example.virtualbank.auth.request.AuthRequestSignUpDTO;
import com.example.virtualbank.auth.response.AuthResponseDTO;
import com.example.virtualbank.repositories.CustomerRepository;
import com.example.virtualbank.responsebody.ResponseBody;
import com.example.virtualbank.services.auth.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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

    private final PasswordEncoder encoder;
    private final AuthenticationService service;

    public AuthenticationController(PasswordEncoder encoder, AuthenticationService service) {
        this.encoder = encoder;
        this.service = service;
    }

    @Operation(
            summary = "Does login",
            description = "Does login, generate token"
    )
    @ApiResponse(responseCode = "200", description = "Success")
    @PostMapping("/login")
    public ResponseEntity<ResponseBody<String>> login(@RequestBody AuthRequestLoginDTO dto){
        var response = service.login(dto);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseBody<>(HttpStatus.OK.value(), response)
        );
    }

    @Operation(
            summary = "Does signup",
            description = "Register user in database"
    )
    @ApiResponse(responseCode = "201", description = "Success")
    @PostMapping("/signup")
    public ResponseEntity<ResponseBody<AuthResponseDTO>> signup(@RequestBody AuthRequestSignUpDTO dto){
        AuthResponseDTO response = service.signup(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new ResponseBody<>(HttpStatus.CREATED.value(), response)
        );
    }
}
