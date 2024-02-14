// codigo RateLimitingConfig 
package com.apimeteorologica.datosmeteorologicos.config;

import com.apimeteorologica.datosmeteorologicos.filter.RateLimitingFilter;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import jakarta.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.util.function.Function;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 *
 * @author Jan Carlo
 */
@Configuration
public class RateLimitingConfig implements WebMvcConfigurer {

    @Bean
    public Bucket createBucket() {
        // Definicion de cuantos tokens tendra de inicio y cuantos se van a rellenar despues de cierto tiempo
        return Bucket.builder()
                .addLimit(Bandwidth.classic(5, Refill.greedy(2, Duration.ofMinutes(1))))
                .build();
    }

    @Bean
    public Function<HttpServletRequest, String> createKeyFunction() {
        // Crea una función que recibe un objeto HttpServletRequest y devuelve una cadena que identifica al usuario de la petición por su token Bearer
        return request -> request.getHeader("Authorization");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        
        RateLimitingFilter rateLimitingFilter = new RateLimitingFilter(createBucket(), request -> request.getHeader("Authorization"));

        // Registra el filtro para todas las peticiones de la API
        registry.addInterceptor(rateLimitingFilter).addPathPatterns("/datosmeteorologicos/**");
    }

}
