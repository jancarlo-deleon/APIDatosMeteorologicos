package com.apimeteorologica.datosmeteorologicos.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author Jan Carlo
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ClimaDto implements Serializable {

    @JsonProperty("name")
    private String ciudad;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Clima {

        @JsonProperty("description")
        private String descripcion;
    }

    @JsonProperty("weather")
    private List<Clima> clima;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Viento {

        @JsonProperty("speed")
        private double velocidadViento;
    }

    @JsonProperty("wind")
    private Viento viento;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Coordenadas {

        @JsonProperty("lon")
        private double longitud;

        @JsonProperty("lat")
        private double latitud;
    }

    @JsonProperty("coord")
    private Coordenadas coordenadas;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
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
