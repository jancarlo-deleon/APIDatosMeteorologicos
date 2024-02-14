package com.apimeteorologica.datosmeteorologicos.pruebas;

import com.apimeteorologica.datosmeteorologicos.payload.request.LoginRequest;
import com.apimeteorologica.datosmeteorologicos.security.controller.AuthController;
import com.apimeteorologica.datosmeteorologicos.security.jwt.JwtUtils;
import com.apimeteorologica.datosmeteorologicos.security.repository.RoleRepository;
import com.apimeteorologica.datosmeteorologicos.security.repository.UserRepository;
import com.apimeteorologica.datosmeteorologicos.security.service.UserDetailsImpl;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

/**
 *
 * @author Jan Carlo
 */
@ExtendWith(SpringExtension.class)
@WebMvcTest(AuthController.class)
public class TestAuthenticateUser {

    @Autowired
    private MockMvc mockMvc;

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

    @Test
    @WithMockUser(username = "prueba", password = "12345678", roles = {"USER"})
    public void testAuthenticateUser() throws Exception {
        LoginRequest loginRequest = new LoginRequest("prueba", "12345678");
        Authentication authentication = Mockito.mock(Authentication.class);
        UserDetailsImpl userDetails = Mockito.mock(UserDetailsImpl.class);
        List<String> roles = Arrays.asList("ROLE_USER");
        List<GrantedAuthority> authorities = roles.stream()
                .map(role -> new SimpleGrantedAuthority(role))
                .collect(Collectors.toList());
        String jwt = "token-de-test";

        Mockito.when(authenticationManager.authenticate(Mockito.any())).thenReturn(authentication);
        Mockito.when(authentication.getPrincipal()).thenReturn(userDetails);
        Mockito.when(userDetails.getId()).thenReturn(1L);
        Mockito.when(userDetails.getUsername()).thenReturn("prueba");
        Mockito.when(userDetails.getEmail()).thenReturn("pruebatest@example.com");
        Mockito.when(jwtUtils.generateJwtToken(Mockito.any())).thenReturn(jwt);

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/signin")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"prueba\",\"password\":\"12345678\"}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

}
