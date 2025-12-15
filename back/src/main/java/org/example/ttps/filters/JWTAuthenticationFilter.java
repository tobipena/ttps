package org.example.ttps.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.ttps.services.TokenService;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Order(2) // Se ejecuta después del CorsFilter
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    private final TokenService tokenService;

    public JWTAuthenticationFilter(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        String contextPath = request.getContextPath();
        String method = request.getMethod();
        
        if (path.startsWith(contextPath)) {
            path = path.substring(contextPath.length());
        }
        
        // Siempre permitir OPTIONS (CORS preflight)
        if (HttpMethod.OPTIONS.matches(method)) {
            return true;
        }
        
        // Rutas siempre públicas
        if ("/auth/login".equals(path) 
            || "/auth/register".equals(path)
            || path.startsWith("/swagger-ui")
            || path.startsWith("/v3/api-docs")
            || path.startsWith("/swagger-resources")
            || path.startsWith("/webjars")) {
            return true;
        }
        
        // TEMPORALMENTE: Hacer públicos todos los endpoints de la API para desarrollo
        if (path.startsWith("/desapariciones") 
            || path.startsWith("/avistamientos")
            || path.startsWith("/mascotas")
            || path.startsWith("/usuarios")) {
            return true;
        }
        
        // Todo lo demás requiere autenticación
        return false;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (token == null || !tokenService.validateToken(token)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Token inválido o expirado");
            return;
        }

        String email = tokenService.getEmailFromToken(token);
        request.setAttribute("userEmail", email);

        chain.doFilter(request, response);
    }
}
