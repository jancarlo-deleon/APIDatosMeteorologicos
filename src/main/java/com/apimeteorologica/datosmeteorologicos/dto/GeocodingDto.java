package com.apimeteorologica.datosmeteorologicos.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import lombok.Data;

/**
 *
 * @author Jan Carlo
 */
@Data
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
