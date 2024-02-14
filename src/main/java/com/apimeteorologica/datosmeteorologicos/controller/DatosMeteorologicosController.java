package com.apimeteorologica.datosmeteorologicos.controller;

import com.apimeteorologica.datosmeteorologicos.dto.ClimaDto;
import com.apimeteorologica.datosmeteorologicos.dto.ContaminacionAireDto;
import com.apimeteorologica.datosmeteorologicos.dto.PronosticoDto;
import com.apimeteorologica.datosmeteorologicos.exception.ClimaCiudadException;
import com.apimeteorologica.datosmeteorologicos.security.service.UserDetailsImpl;
import com.apimeteorologica.datosmeteorologicos.service.AuditoriaService;
import com.apimeteorologica.datosmeteorologicos.service.DatosMeteorologicosService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;

/**
 *
 * @author Jan Carlo
 */
@Tag(name = "Datos Meteorológicos", description = "Endpoints para consultar el clima actual, un pronostico de 5 dias y la contaminación de una ciudad. NOTA: Se requiere autenticación para poder obtener pruebas exitosas")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/datosmeteorologicos")
public class DatosMeteorologicosController {

    private Logger logger = LoggerFactory.getLogger(DatosMeteorologicosController.class);

    @Autowired
    private final DatosMeteorologicosService datosMeteorologicosService;

    @Autowired
    private AuditoriaService auditoriaService;

    Authentication authentication;
    UserDetailsImpl userDetails;
    ObjectMapper mapper;

    @Autowired
    public DatosMeteorologicosController(DatosMeteorologicosService datosMeteorologicosService) {
        this.datosMeteorologicosService = datosMeteorologicosService;
    }

    @Operation(summary = "Se utiliza para poder consultar el clima de una ciudad, pasando como parámetro el nombre de la misma.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Peticion exitosa.",
                content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ClimaDto.class))}),
        @ApiResponse(responseCode = "404", description = "Datos no encontrados",
                content = @Content),
        @ApiResponse(responseCode = "403", description = "El usuario no se encuentra autenticado.",
                content = @Content),
        @ApiResponse(responseCode = "429", description = "Se ha sobrepasado la cuota de peticiones disponibles.",
                content = @Content),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                content = @Content)})
    @PreAuthorize("hasRole('USER')")
    @Cacheable("climaCache")
    @GetMapping("/clima/{ciudad}")
    public ResponseEntity<?> obtenerClima(@PathVariable String ciudad) {
        if (ciudad == null || ciudad.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Se necesita colocar un valor para el parámetro de ciudad para poder realizar la consulta");
        }

        try {
            authentication = SecurityContextHolder.getContext().getAuthentication();
            userDetails = (UserDetailsImpl) authentication.getPrincipal();

            // Realizar la solicitud para obtener el clima
            ClimaDto clima = datosMeteorologicosService.obtenerClimaPorCiudad(ciudad);

            // Convertir el objeto ClimaDto a JSON para almacenarlo en la auditoría
            mapper = new ObjectMapper();
            String respuesta = mapper.writeValueAsString(clima);

            // Registrar la auditoría con la solicitud y la respuesta
            auditoriaService.registrarAuditoria(userDetails, "GET /datosmeteorologicos/clima/" + ciudad, respuesta);

            return ResponseEntity.ok(clima);
        } catch (ClimaCiudadException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al obtener datos del clima desde el controlador: " + ex.getMessage());
        } catch (RestClientException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al llamar a la API externa desde el controlador:");
        } catch (Exception ex) {
            logger.error("Error interno en el servidor desde el controlador:", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno en el servidor desde el controlador: " + ex.getMessage());
        }
    }

    @Operation(summary = "Se utiliza para poder consultar el pronóstico del tiempo de los proximos 5 dias de una ciudad, pasando como parámetro el nombre de la misma.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Peticion exitosa.",
                content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PronosticoDto.class))}),
        @ApiResponse(responseCode = "404", description = "Datos no encontrados",
                content = @Content),
        @ApiResponse(responseCode = "403", description = "El usuario no se encuentra autenticado.",
                content = @Content),
        @ApiResponse(responseCode = "429", description = "Se ha sobrepasado la cuota de peticiones disponibles.",
                content = @Content),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                content = @Content)})
    @PreAuthorize("hasRole('USER')")
    @Cacheable("pronosticoCache")
    @GetMapping("/pronostico/{ciudad}")
    public ResponseEntity<?> obtenerPronostico(@PathVariable String ciudad) {

        if (ciudad == null || ciudad.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Se necesita colocar un valor para el parámetro de ciudad para poder realizar la consulta");
        }

        try {
            authentication = SecurityContextHolder.getContext().getAuthentication();
            userDetails = (UserDetailsImpl) authentication.getPrincipal();

            // Realizar la solicitud para obtener el pronostico
            PronosticoDto pronostico = datosMeteorologicosService.obtenerPronostico5diasPorCiudad(ciudad);

            // Convertir el objeto PronosticoDto a JSON para almacenarlo en la auditoría
            mapper = new ObjectMapper();
            String respuesta = mapper.writeValueAsString(pronostico);

            // Registrar la auditoría con la solicitud y la respuesta
            auditoriaService.registrarAuditoria(userDetails, "GET /datosmeteorologicos/pronostico/" + ciudad, respuesta);

            return ResponseEntity.ok(pronostico);
        } catch (ClimaCiudadException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al obtener datos del pronostico desde el controlador: " + ex.getMessage());
        } catch (RestClientException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al llamar a la API externa de pronostico desde el controlador:");
        } catch (Exception ex) {
            logger.error("Error interno en el servidor desde el controlador:", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno en el servidor desde el controlador: " + ex.getMessage());
        }

    }

    @Operation(summary = "Se utiliza para poder consultar la contaminación que existe en ciudad, pasando como parámetro el nombre de la misma.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Peticion exitosa.",
                content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ContaminacionAireDto.class))}),
        @ApiResponse(responseCode = "404", description = "Datos no encontrados",
                content = @Content),
        @ApiResponse(responseCode = "403", description = "El usuario no se encuentra autenticado.",
                content = @Content),
        @ApiResponse(responseCode = "429", description = "Se ha sobrepasado la cuota de peticiones disponibles.",
                content = @Content),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                content = @Content)})
    @PreAuthorize("hasRole('USER')")
    @Cacheable("contaminacionCache")
    @GetMapping("/contaminacion/{ciudad}")
    public ResponseEntity<?> obtenerContaminacion(@PathVariable String ciudad) {

        if (ciudad == null || ciudad.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Se necesita colocar un valor para el parámetro de ciudad para poder realizar la consulta");
        }

        try {
            // Obtener detalles del usuario autenticado
            authentication = SecurityContextHolder.getContext().getAuthentication();
            userDetails = (UserDetailsImpl) authentication.getPrincipal();

            // Obtener datos de contaminación del aire para la ciudad especificada
            ContaminacionAireDto contaminacionAire = datosMeteorologicosService.obtenerContaminacionAirePorCiudad(ciudad);

            // Convertir el objeto ContaminacionAireDto a JSON para almacenarlo en la auditoría
            mapper = new ObjectMapper();
            String respuesta = mapper.writeValueAsString(contaminacionAire);

            // Registrar la auditoría con la solicitud y la respuesta
            auditoriaService.registrarAuditoria(userDetails, "GET /datosmeteorologicos/contaminacion/" + ciudad, respuesta);

            return ResponseEntity.ok(contaminacionAire);
        } catch (ClimaCiudadException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al obtener datos de contaminación desde el controlador: " + ex.getMessage());
        } catch (RestClientException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al llamar a la API externa para contaminación desde el controlador:");
        } catch (Exception ex) {
            logger.error("Error interno en el servidor desde el controlador:", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno en el servidor desde el controlador: " + ex.getMessage());
        }
    }

}
