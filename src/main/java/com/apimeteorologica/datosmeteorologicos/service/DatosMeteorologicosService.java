package com.apimeteorologica.datosmeteorologicos.service;

import com.apimeteorologica.datosmeteorologicos.dto.ClimaDto;
import com.apimeteorologica.datosmeteorologicos.dto.ContaminacionAireDto;
import com.apimeteorologica.datosmeteorologicos.dto.PronosticoDto;

/**
 *
 * @author Jan Carlo
 */
public interface DatosMeteorologicosService {

    ClimaDto obtenerClimaPorCiudad(String ciudad);
    
    PronosticoDto obtenerPronostico5diasPorCiudad(String ciudad);
    
    ContaminacionAireDto obtenerContaminacionAirePorCiudad(String ciudad);
    
}
