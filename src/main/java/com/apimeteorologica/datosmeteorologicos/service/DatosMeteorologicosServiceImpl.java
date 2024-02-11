package com.apimeteorologica.datosmeteorologicos.service;

import com.apimeteorologica.datosmeteorologicos.dto.ClimaDto;
import com.apimeteorologica.datosmeteorologicos.dto.ContaminacionAireDto;
import com.apimeteorologica.datosmeteorologicos.dto.GeocodingDto;
import com.apimeteorologica.datosmeteorologicos.dto.PronosticoDto;
import com.apimeteorologica.datosmeteorologicos.exception.ClimaCiudadException;
import com.apimeteorologica.datosmeteorologicos.exception.GeocodingException;
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
    private static final String APIURL_CONTAMINACION_CIUDAD = "http://api.openweathermap.org/data/2.5/air_pollution";
    private static final String APIURL_GEOCODING = "http://api.openweathermap.org/geo/1.0/direct";

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

    @Override
    public ContaminacionAireDto obtenerContaminacionAirePorCiudad(String ciudad) {
        try {
            String geocodingUrl = APIURL_GEOCODING + "?q=" + ciudad + "&appid=" + apiKey + "&mode=json&lang=es";
            ResponseEntity<GeocodingDto[]> geocodingResponse = restTemplate.getForEntity(geocodingUrl, GeocodingDto[].class);

            if (geocodingResponse.getStatusCode() == HttpStatus.OK) {
                GeocodingDto[] geocoding = geocodingResponse.getBody();
                if (geocoding != null && geocoding.length > 0) {
                    double lat = geocoding[0].getLatitud();
                    double lon = geocoding[0].getLongitud();

                    String contaminacionUrl = APIURL_CONTAMINACION_CIUDAD + "?lat=" + lat + "&lon=" + lon + "&appid=" + apiKey + "&mode=json&lang=es";
                    ResponseEntity<String> contaminacionResponse = restTemplate.getForEntity(contaminacionUrl, String.class);

                    if (contaminacionResponse.getStatusCode() == HttpStatus.OK) {
                        ContaminacionAireDto contaminacionAireDto = objectMapper.readValue(contaminacionResponse.getBody(), ContaminacionAireDto.class);
                        return contaminacionAireDto;
                    } else {
                        throw new ClimaCiudadException("Error al obtener datos de contaminacion desde la serviceImpl: " + contaminacionResponse.getStatusCode());
                    }
                } else {
                    throw new GeocodingException("No se encontraron datos de geocodificación para la ciudad: " + ciudad);
                }
            } else {
                throw new GeocodingException("Error al obtener datos de Geocoding desde la serviceImpl: " + geocodingResponse.getStatusCode());
            }
        } catch (RestClientException | IOException | GeocodingException | ClimaCiudadException ex) {
            throw new ClimaCiudadException("Error al llamar a la API externa desde el serviceImpl: " + ex.getMessage());
        }
    }

}
