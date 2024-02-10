package com.apimeteorologica.datosmeteorologicos.exception;

/**
 *
 * @author Jan Carlo
 */
public class ClimaCiudadException extends RuntimeException {

    public ClimaCiudadException(String message) {
        super(message);
    }

    public ClimaCiudadException(String message, Throwable cause) {
        super(message, cause);
    }
}
