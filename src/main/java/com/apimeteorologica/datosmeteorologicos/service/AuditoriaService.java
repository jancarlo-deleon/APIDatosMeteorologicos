/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.apimeteorologica.datosmeteorologicos.service;

import com.apimeteorologica.datosmeteorologicos.entity.Auditoria;
import com.apimeteorologica.datosmeteorologicos.repository.AuditoriaRepository;
import com.apimeteorologica.datosmeteorologicos.security.entity.User;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Jan Carlo
 */
public class AuditoriaService {
    
    @Autowired
    private AuditoriaRepository auditoriaRepository;
    
    public void registrarAuditoria(User user, String peticion, String respuesta){
        Auditoria auditoria = new Auditoria();
        
        auditoria.setUser(user);
        auditoria.setPeticion(peticion);
        auditoria.setRespuesta(respuesta);
        
        auditoriaRepository.save(auditoria);
        
    }
    
}
