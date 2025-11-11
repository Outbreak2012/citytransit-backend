package com.citytransit.resolver;

import com.citytransit.model.dto.request.LoginRequest;
import com.citytransit.model.dto.request.RegisterRequest;
import com.citytransit.model.dto.response.AuthResponse;
import com.citytransit.model.entity.Usuario;
import com.citytransit.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class AuthResolver {

    private final AuthService authService;

    @MutationMapping
    public AuthResponse login(@Argument("input") LoginRequest input) {
        return authService.login(input);
    }

    @MutationMapping
    public AuthResponse register(@Argument("input") RegisterRequest input) {
        return authService.register(input);
    }

    @QueryMapping
    public Usuario me(@AuthenticationPrincipal Usuario usuario) {
        return usuario;
    }
}
