package com.apimeteorologica.datosmeteorologicos.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Data;

/**
 *
 * @author Jan Carlo
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ClimaDto {

    @JsonProperty("name")
    private String ciudad;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Clima {
        @JsonProperty("description")
        private String descripcion;
    }
    
    @JsonProperty("weather")
    private List<Clima> clima;

    
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Viento {
        @JsonProperty("speed")
    private double velocidadViento;
    }
    
    @JsonProperty("wind")
    private Viento viento;

    @Data
    public static class Coordenadas {

        @JsonProperty("lon")
        private double longitud;

        @JsonProperty("lat")
        private double latitud;
    }

    @JsonProperty("coord")
    private Coordenadas coordenadas;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Principal {

        @JsonProperty("temp")
        private double temperatura;

        @JsonProperty("pressure")
        private int presion;

        @JsonProperty("humidity")
        private int humedad;
    }

    @JsonProperty("main")
    private Principal main;

}
