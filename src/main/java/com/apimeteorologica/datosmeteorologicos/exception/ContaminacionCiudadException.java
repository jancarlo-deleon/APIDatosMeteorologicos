/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.apimeteorologica.datosmeteorologicos.exception;

/**
 *
 * @author Jan Carlo
 */
public class ContaminacionCiudadException extends RuntimeException{
     public ContaminacionCiudadException(String message) {
        super(message);
    }

    public ContaminacionCiudadException(String message, Throwable cause) {
        super(message, cause);
    }
}
