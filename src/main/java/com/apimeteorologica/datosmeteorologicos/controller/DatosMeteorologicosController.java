package com.apimeteorologica.datosmeteorologicos.controller;

import com.apimeteorologica.datosmeteorologicos.dto.ClimaDto;
import com.apimeteorologica.datosmeteorologicos.exception.ClimaCiudadException;
import com.apimeteorologica.datosmeteorologicos.service.DatosMeteorologicosService;
import com.apimeteorologica.datosmeteorologicos.service.DatosMeteorologicosServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;

/**
 *
 * @author Jan Carlo
 */
@RestController
@RequestMapping("/datosmeteorologicos")
public class DatosMeteorologicosController {
    @Autowired
    private final DatosMeteorologicosServiceImpl datosMeteorologicosService;

    @Autowired
    public DatosMeteorologicosController(DatosMeteorologicosServiceImpl datosMeteorologicosService) {
        this.datosMeteorologicosService = datosMeteorologicosService;
    }

    @GetMapping("/clima/{ciudad}")
    public ResponseEntity<?> obtenerClima(@PathVariable String ciudad) {
        if (ciudad == null || ciudad.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Se necesita colocar un valor para el parámetro de ciudad para poder realizar la consulta");
        }

        try {
            ClimaDto clima = datosMeteorologicosService.obtenerClimaPorCiudad(ciudad);
            return ResponseEntity.ok(clima);
        } catch (ClimaCiudadException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al obtener datos del clima desde el controlador: " + ex.getMessage());
        } catch (RestClientException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al llamar a la API externa desde el controlador:");
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno en el servidor desde el controlador:");
        }
    }
}
