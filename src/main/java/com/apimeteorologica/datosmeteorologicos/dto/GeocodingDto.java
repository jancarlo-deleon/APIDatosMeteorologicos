package com.apimeteorologica.datosmeteorologicos.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
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
public class GeocodingDto implements Serializable {
    
    @JsonProperty("name")
    private String nombre;
    @JsonProperty("lat")
    private double latitud;
    @JsonProperty("lon")
    private double longitud;
    @JsonProperty("country")
    private String pais;
    @JsonProperty("state")
    private String estado;

}
