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
public class PronosticoDto implements Serializable {

    private List<DatosMeteorologicos> list;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DatosMeteorologicos {

        private long dt;
        @JsonProperty("main")
        private Principal principal;
        @JsonProperty("weather")
        private List<Clima> clima;
        @JsonProperty("clouds")
        private Nubes nubes;
        @JsonProperty("wind")
        private Viento viento;
        @JsonProperty("visibility")
        private int visibilidad;
        @JsonProperty("pop")
        private int pop;
        @JsonProperty("dt_txt")
        private String dtTxt;

        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Principal {

            @JsonProperty("temp")
            private double temperatura;
            @JsonProperty("temp_min")
            private double temperaturaMinima;
            @JsonProperty("temp_max")
            private double temperaturaMaxima;
            @JsonProperty("pressure")
            private int presion;
            @JsonProperty("sea_level")
            private int nivelDelMar;
            @JsonProperty("grnd_level")
            private int nivelDeTierra;
            @JsonProperty("humidity")
            private int humedad;

        }

        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Clima {

            @JsonProperty("main")
            private String main;
            @JsonProperty("description")
            private String descripcion;
        }

        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Nubes {

            @JsonProperty("all")
            private int all;
        }

        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Viento {

            @JsonProperty("speed")
            private double velocidad;
            @JsonProperty("deg")
            private int deg;
        }

    }
}
