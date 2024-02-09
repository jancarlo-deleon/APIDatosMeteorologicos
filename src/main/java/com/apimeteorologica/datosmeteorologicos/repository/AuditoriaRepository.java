/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.apimeteorologica.datosmeteorologicos.repository;

import com.apimeteorologica.datosmeteorologicos.entity.Auditoria;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Jan Carlo
 */
public interface AuditoriaRepository extends JpaRepository<Auditoria,Long>{
    
}
