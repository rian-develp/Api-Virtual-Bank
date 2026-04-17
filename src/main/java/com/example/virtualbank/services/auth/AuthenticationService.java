package com.example.virtualbank.services.auth;

import com.example.virtualbank.auth.request.AuthRequestLoginDTO;
import com.example.virtualbank.auth.request.AuthRequestSignUpDTO;
import com.example.virtualbank.auth.response.AuthResponseDTO;
import org.springframework.stereotype.Service;

@Service
public interface AuthenticationService {

    String login(AuthRequestLoginDTO dto);
    AuthResponseDTO signup(AuthRequestSignUpDTO dto);
}
