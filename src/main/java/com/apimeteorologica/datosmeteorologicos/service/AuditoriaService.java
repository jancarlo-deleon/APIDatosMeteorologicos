package com.apimeteorologica.datosmeteorologicos.service;

import com.apimeteorologica.datosmeteorologicos.security.entity.User;
import com.apimeteorologica.datosmeteorologicos.security.service.UserDetailsImpl;


/**
 *
 * @author Jan Carlo
 */
public interface AuditoriaService {
   public void registrarAuditoria(UserDetailsImpl user, String peticion, String respuesta); 
}
