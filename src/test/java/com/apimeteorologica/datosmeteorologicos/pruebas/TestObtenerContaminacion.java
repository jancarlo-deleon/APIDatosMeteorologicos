package com.apimeteorologica.datosmeteorologicos.pruebas;

import com.apimeteorologica.datosmeteorologicos.controller.DatosMeteorologicosController;
import com.apimeteorologica.datosmeteorologicos.dto.ClimaDto;
import com.apimeteorologica.datosmeteorologicos.dto.ContaminacionAireDto;
import com.apimeteorologica.datosmeteorologicos.security.jwt.JwtUtils;
import com.apimeteorologica.datosmeteorologicos.security.service.UserDetailsImpl;
import com.apimeteorologica.datosmeteorologicos.service.AuditoriaService;
import com.apimeteorologica.datosmeteorologicos.service.DatosMeteorologicosService;
import java.util.Arrays;
import java.util.Collections;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
@WebMvcTest(DatosMeteorologicosController.class)
public class TestObtenerContaminacion {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DatosMeteorologicosService datosMeteorologicosService;

    @MockBean
    private AuditoriaService auditoriaService;


    @Test
    public void testObtenerContaminacion() throws Exception {

        // Creacion de UserDetailsImpl y SecurityContext de forma manual en vez de usar  MockUser para poder ejecutar la prueba 
        UserDetails userDetails = new UserDetailsImpl(1L, "prueba", "prueba@gmail.com", "12345678", Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()));
        SecurityContextHolder.setContext(securityContext);

        String ciudad = "Madrid";
         ContaminacionAireDto contaminacionAire = new ContaminacionAireDto(
            new ContaminacionAireDto.Coordenadas(-3.7038, 40.4168),
            Collections.singletonList(
                new ContaminacionAireDto.AirpollutionData(
                    new ContaminacionAireDto.Principal(100),
                    new ContaminacionAireDto.Componente(1.2, 2.3, 3.4, 4.5, 5.6, 6.7, 7.8, 8.9)
                )
            )
        );

        Mockito.when(datosMeteorologicosService.obtenerContaminacionAirePorCiudad(ciudad)).thenReturn(contaminacionAire);
        Mockito.doNothing().when(auditoriaService).registrarAuditoria(Mockito.any(), Mockito.any(), Mockito.any());
        mockMvc.perform(MockMvcRequestBuilders.get("/datosmeteorologicos/contaminacion/" + ciudad)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());

    }

}
