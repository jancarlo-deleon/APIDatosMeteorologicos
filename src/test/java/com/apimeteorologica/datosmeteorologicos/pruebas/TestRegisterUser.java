package com.apimeteorologica.datosmeteorologicos.pruebas;

import com.apimeteorologica.datosmeteorologicos.payload.request.SignupRequest;
import com.apimeteorologica.datosmeteorologicos.security.controller.AuthController;
import com.apimeteorologica.datosmeteorologicos.security.entity.Role;
import com.apimeteorologica.datosmeteorologicos.security.entity.User;
import com.apimeteorologica.datosmeteorologicos.security.jwt.JwtUtils;
import com.apimeteorologica.datosmeteorologicos.security.repository.RoleRepository;
import com.apimeteorologica.datosmeteorologicos.security.repository.UserRepository;
import com.apimeteorologica.datosmeteorologicos.security.util.RoleEnum;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
public class TestRegisterUser {

    // Inyectar un objeto MockMvc con la anotación @Autowired
    @Autowired
    private MockMvc mockMvc;

    // Crear un objeto ObjectMapper para convertir los objetos a JSON
    private ObjectMapper objectMapper = new ObjectMapper();

    // Crear objetos simulados con la anotación @MockBean para los servicios y repositorios que usa el controlador
    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private RoleRepository roleRepository;

    @MockBean
    private PasswordEncoder encoder;

    @MockBean
    private JwtUtils jwtUtils;

    // Crear un método de prueba con la anotación @Test
    @Test
    public void testRegisterUser() throws Exception {
        SignupRequest signUpRequest = new SignupRequest();
        signUpRequest.setUsername("test");
        signUpRequest.setEmail("test@example.com");
        signUpRequest.setPassword("test1234");
        Set<String> roles = new HashSet<>();
        roles.add("ROLE_USER");
        signUpRequest.setRole(roles);

        User user = new User();
        user.setUsername("test");
        user.setEmail("test@example.com");
        user.setPassword(encoder.encode("test1234"));
        Set<Role> userRoles = new HashSet<>();
        Role userRole = new Role(RoleEnum.ROLE_USER);
        userRoles.add(userRole);
        user.setRoles(userRoles);

        Mockito.when(userRepository.existsByUsername(anyString())).thenReturn(false);
        Mockito.when(userRepository.existsByEmail(anyString())).thenReturn(false);
        Mockito.when(roleRepository.findByName(any(RoleEnum.class))).thenReturn(java.util.Optional.of(userRole));
        Mockito.when(userRepository.save(any(User.class))).thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/signup")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signUpRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print());

    }
}
