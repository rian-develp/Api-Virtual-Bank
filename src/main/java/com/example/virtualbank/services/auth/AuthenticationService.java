package com.example.virtualbank.services.auth;

import com.example.virtualbank.auth.request.AuthRequestDTO;
import com.example.virtualbank.auth.response.AuthResponseDTO;
import org.springframework.stereotype.Service;

@Service
public interface AuthenticationService {

    String login(AuthRequestDTO dto);
    AuthResponseDTO signup(AuthRequestDTO dto);
}
