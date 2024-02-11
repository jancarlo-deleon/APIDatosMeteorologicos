package com.apimeteorologica.datosmeteorologicos.service;

import com.apimeteorologica.datosmeteorologicos.dto.ClimaDto;
import com.apimeteorologica.datosmeteorologicos.dto.PronosticoDto;
import com.apimeteorologica.datosmeteorologicos.exception.ClimaCiudadException;
import com.apimeteorologica.datosmeteorologicos.exception.PronosticoCiudadException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author Jan Carlo
 */
@Service
public class DatosMeteorologicosServiceImpl implements DatosMeteorologicosService {

    @Value("${datosmeteorologicos.app.apikey}")
    private String apiKey;

    private static final String APIURL_CLIMA_CIUDAD = "https://api.openweathermap.org/data/2.5/weather";
    private static final String APIURL_PRONOSTICO_5_DIAS_CIUDAD = "https://api.openweathermap.org/data/2.5/forecast";

     private final ObjectMapper objectMapper = new ObjectMapper();
    
    @Autowired
    private RestTemplate restTemplate;

     @Override
    public ClimaDto obtenerClimaPorCiudad(String ciudad) {
        try {
            String url = APIURL_CLIMA_CIUDAD + "?q=" + ciudad + "&appid=" + apiKey + "&mode=json&lang=es";
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                ClimaDto climaDTO = objectMapper.readValue(response.getBody(), ClimaDto.class);
                return climaDTO;
            } else {
                throw new ClimaCiudadException("Error al obtener datos del clima desde el serviceImpl: " + response.getStatusCode());
            }
        } catch (RestClientException | IOException ex) {
            throw new ClimaCiudadException("Error al llamar a la API externa de clima desde el serviceImpl: " + ex.getMessage());
        }
    }

    @Override
    public PronosticoDto obtenerPronostico5diasPorCiudad(String ciudad) {
         try {
            String url = APIURL_PRONOSTICO_5_DIAS_CIUDAD + "?q=" + ciudad + "&appid=" + apiKey + "&mode=json&lang=es";
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                PronosticoDto pronosticoDTO = objectMapper.readValue(response.getBody(), PronosticoDto.class);
                return pronosticoDTO;
            } else {
                throw new PronosticoCiudadException("Error al obtener datos del pronostico del clima de 5 dias desde el serviceImpl: " + response.getStatusCode());
            }
        } catch (RestClientException | IOException ex) {
            throw new PronosticoCiudadException("Error al llamar a la API externa de pronostico desde el serviceImpl: " + ex.getMessage());
        }
        
    }

}
