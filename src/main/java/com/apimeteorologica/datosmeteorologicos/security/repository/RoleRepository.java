package com.apimeteorologica.datosmeteorologicos.security.repository;

import com.apimeteorologica.datosmeteorologicos.security.entity.Role;
import com.apimeteorologica.datosmeteorologicos.security.util.RoleEnum;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Jan Carlo
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
  Optional<Role> findByName(RoleEnum name);
}
