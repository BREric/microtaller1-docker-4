package co.org.uniquindio.retosmicroservicios;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import io.jsonwebtoken.Jwts;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Date;

public class LoginHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("POST".equals(exchange.getRequestMethod())) {
            InputStream requestBody = exchange.getRequestBody();
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, String> requestMap = objectMapper.readValue(requestBody, Map.class);

            String usuario = requestMap.get("usuario");
            String clave = requestMap.get("clave");

            // Ya no validamos usuario y clave, simplemente generamos el token
            if (usuario != null && clave != null) {
                String jwt = generateJwt(usuario);
                String response = "{\"token\":\"" + jwt + "\"}";
                exchange.getResponseHeaders().add("Content-Type", "application/json");
                exchange.sendResponseHeaders(200, response.getBytes(StandardCharsets.UTF_8).length);
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes(StandardCharsets.UTF_8));
                os.close();
            } else {
                String response = "Solicitud no válida: Los atributos 'usuario' y 'clave' son obligatorios";
                exchange.sendResponseHeaders(400, response.getBytes(StandardCharsets.UTF_8).length);
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes(StandardCharsets.UTF_8));
                os.close();
            }
        } else {
            String response = "Método no permitido";
            exchange.sendResponseHeaders(405, response.getBytes(StandardCharsets.UTF_8).length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes(StandardCharsets.UTF_8));
            os.close();
        }
    }

    private String generateJwt(String usuario) {
        long nowMillis = System.currentTimeMillis();
        long expMillis = nowMillis + 3600000; // 1 hora en milisegundos
        Date now = new Date(nowMillis);
        Date exp = new Date(expMillis);

        return Jwts.builder()
                .setSubject(usuario)
                .setHeaderParam("type", "JWT")
                .setIssuer("ingesis.uniquindio.edu.co")
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(JwtConfig.SECRET_KEY)
                .compact();
    }
}
