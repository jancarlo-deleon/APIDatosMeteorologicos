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
public class ContaminacionAireDto implements Serializable {

    @JsonProperty("coord")
    private Coordenadas coordenadas;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Coordenadas {

        @JsonProperty("lon")
        private double longitud;

        @JsonProperty("lat")
        private double latitud;
    }

    @JsonProperty("list")
    private List<AirpollutionData> list;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class AirpollutionData {

        @JsonProperty("main")
        private Principal main;

        @JsonProperty("components")
        private Componente components;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Principal {

        @JsonProperty("aqi")
        private int indiceCalidadAire;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Componente {

        @JsonProperty("co")
        private double monoxidoDeCarbon;

        @JsonProperty("no")
        private double monoxidoDeNitrogeno;

        @JsonProperty("no2")
        private double dioxidoDeNitrogeno;

        @JsonProperty("o3")
        private double ozono;

        @JsonProperty("so2")
        private double dioxidoDeSulfuro;

        @JsonProperty("pm2_5")
        private double pm2_5;

        @JsonProperty("pm10")
        private double pm10;

        @JsonProperty("nh3")
        private double nh3;

    }

}
