// codigo 	RateLimitingFilter

package com.apimeteorologica.datosmeteorologicos.filter;

import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.function.Function;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 *
 * @author Jan Carlo
 */
@Component
public class RateLimitingFilter implements HandlerInterceptor {

    private Bucket bucket;
    private Function<HttpServletRequest, String> keyFunction;

    public RateLimitingFilter(Bucket bucket, Function<HttpServletRequest, String> keyFunction) {
        this.bucket = bucket;
        this.keyFunction = keyFunction;
    }

     @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // Obtiene la clave para identificar al usuario de la petición
        String key = keyFunction.apply(request);

        // Intenta consumir un token del bucket
        ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(1);

        // Si el bucket tiene tokens disponibles, acepta la petición
        if (probe.isConsumed()) {
            // Añade las cabeceras con la información del bucket
            response.addHeader("X-Rate-Limit-Peticiones-Restantes", String.valueOf(probe.getRemainingTokens()));
            response.addHeader("X-Rate-Limit-Reintentar-Despues-De-Pasados-Segundos", String.valueOf(probe.getNanosToWaitForRefill() / 1_000_000_000));
            return true;
        } // Si el bucket está vacío, rechaza la petición
        else {
            // Añade las cabeceras con la información del bucket
            response.addHeader("X-Rate-Limit-Reintentar-Despues-De-Pasados-Segundos", String.valueOf(probe.getNanosToWaitForRefill() / 1_000_000_000));
            // Devuelve un código de estado 429
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            return false;
        }
    }

}
