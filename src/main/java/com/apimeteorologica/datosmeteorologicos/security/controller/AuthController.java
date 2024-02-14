package com.apimeteorologica.datosmeteorologicos.security.controller;

import com.apimeteorologica.datosmeteorologicos.payload.request.LoginRequest;
import com.apimeteorologica.datosmeteorologicos.payload.request.SignupRequest;
import com.apimeteorologica.datosmeteorologicos.payload.response.MessageResponse;
import com.apimeteorologica.datosmeteorologicos.payload.response.JwtResponse;
import com.apimeteorologica.datosmeteorologicos.security.entity.Role;
import com.apimeteorologica.datosmeteorologicos.security.entity.User;
import com.apimeteorologica.datosmeteorologicos.security.jwt.JwtUtils;
import com.apimeteorologica.datosmeteorologicos.security.repository.RoleRepository;
import com.apimeteorologica.datosmeteorologicos.security.repository.UserRepository;
import com.apimeteorologica.datosmeteorologicos.security.service.UserDetailsImpl;
import com.apimeteorologica.datosmeteorologicos.security.util.RoleEnum;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Jan Carlo
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Registro y autenticación", description = "Endpoints para registro y autenticacion de usuarios.")
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @Operation(summary = "Se utiliza para poder realizar una autenticación de usuario y obtener el token a utilizar para hacer consultas.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Inicio de Sesión exitoso",
                content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = JwtResponse.class))}),
        @ApiResponse(responseCode = "400", description = "Peticion invalida",
                content =  { @Content(schema = @Schema())}),
        @ApiResponse(responseCode = "401", description = "Credenciales incorrectas",
                content = @Content),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado",
                content = @Content),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                content = @Content)})
    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles));
    }

    @Operation(summary = "Se utiliza para poder realizar el registro de nuevos usuarios.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario creado exitosamente.",
                content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class))}),
        @ApiResponse(responseCode = "400", description = "Peticion invalida.",
                content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal Server Error.",
                content = @Content)})
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: El username ya se encuentra en uso"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: El email ya se encuentra en uso"));
        }

        // Create new user's account
        User user = new User(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));

        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(RoleEnum.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role no encontrado"));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
//                    case "admin":
//                        Role adminRole = roleRepository.findByName(RoleEnum.ROLE_ADMIN)
//                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
//                        roles.add(adminRole);
//
//                        break;
                    default:
                        Role userRole = roleRepository.findByName(RoleEnum.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role no encontrado"));
                        roles.add(userRole);
                }
            });
        }

        user.setRoles(roles);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("Usuario registrado con exito."));
    }
}
