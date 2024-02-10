/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.apimeteorologica.datosmeteorologicos.service;

import com.apimeteorologica.datosmeteorologicos.entity.Auditoria;
import com.apimeteorologica.datosmeteorologicos.repository.AuditoriaRepository;
import com.apimeteorologica.datosmeteorologicos.security.entity.User;
import com.apimeteorologica.datosmeteorologicos.security.repository.UserRepository;
import com.apimeteorologica.datosmeteorologicos.security.service.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 *
 * @author Jan Carlo
 */
@Service
public class AuditoriaServiceImpl implements AuditoriaService {

    @Autowired
    private AuditoriaRepository auditoriaRepository;

    @Autowired
    private UserRepository userRepository;
    
    @Override
    public void registrarAuditoria(UserDetailsImpl userDetails, String peticion, String respuesta){
        String username = userDetails.getUsername();
        User user = userRepository.findByUsername(username)
                        .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con el nombre de usuario: " + username));

        Auditoria auditoria = new Auditoria();
        auditoria.setUser(user);
        auditoria.setPeticion(peticion);
        auditoria.setRespuesta(respuesta);
        
        auditoriaRepository.save(auditoria);
    }

}
