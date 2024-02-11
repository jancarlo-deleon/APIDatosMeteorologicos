/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.apimeteorologica.datosmeteorologicos.exception;

/**
 *
 * @author Jan Carlo
 */
public class PronosticoCiudadException extends RuntimeException {
    public PronosticoCiudadException(String message) {
        super(message);
    }

    public PronosticoCiudadException(String message, Throwable cause) {
        super(message, cause);
    }
}
