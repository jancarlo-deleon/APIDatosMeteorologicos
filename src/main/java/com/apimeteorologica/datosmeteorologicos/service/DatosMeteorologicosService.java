package com.apimeteorologica.datosmeteorologicos.service;

import com.apimeteorologica.datosmeteorologicos.dto.ClimaDto;

/**
 *
 * @author Jan Carlo
 */
public interface DatosMeteorologicosService {

    ClimaDto obtenerClimaPorCiudad(String ciudad);

}
