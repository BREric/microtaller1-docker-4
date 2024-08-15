package co.org.uniquindio.retosmicroservicios.reto3;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class RequestHandler implements HttpHandler {
    private static final String SECRET_KEY = "mySecretKey"; // Debe ser la misma clave usada para firmar el JWT

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String response;
        int statusCode;

        if ("GET".equals(exchange.getRequestMethod())) {
            String authHeader = exchange.getRequestHeaders().getFirst("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                response = "Unauthorized: No token provided";
                statusCode = 401;
            } else {
                String token = authHeader.substring(7);
                try {
                    Claims claims = validateJwt(token);
                    String query = exchange.getRequestURI().getQuery();
                    Map<String, String> queryParams = parseQueryParams(query);

                    if (queryParams.containsKey("nombre")) {
                        String nombre = queryParams.get("nombre");

                        if (nombre.equals(claims.getSubject())) {
                            response = "Hola " + nombre;
                            statusCode = 200;
                        } else {
                            response = "Unauthorized: Nombre en parámetro no coincide con el token";
                            statusCode = 401;
                        }
                    } else {
                        response = "Solicitud no valida: El nombre es obligatorio";
                        statusCode = 400;
                    }
                } catch (SignatureException e) {
                    response = "Unauthorized: Token inválido";
                    statusCode = 401;
                }
            }
        } else {
            response = "Recurso no encontrado";
            statusCode = 404;
        }

        exchange.sendResponseHeaders(statusCode, response.getBytes(StandardCharsets.UTF_8).length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes(StandardCharsets.UTF_8));
        os.close();
    }

    private Claims validateJwt(String jwt) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(jwt)
                .getBody();
    }

    private Map<String, String> parseQueryParams(String query) {
        // El código existente para analizar los parámetros de la query
        return Map.of(); // Implementa tu código para analizar la query como antes
    }
}
