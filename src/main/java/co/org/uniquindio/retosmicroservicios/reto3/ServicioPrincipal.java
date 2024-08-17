package co.org.uniquindio.retosmicroservicios.reto3;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class ServicioPrincipal {
    public static void main(String[] args) {
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(8082), 0);
            server.createContext("/saludo", new RequestHandler());
            server.createContext("/login", new LoginHandler());
            server.setExecutor(null);
            System.out.println("Servidor iniciado en el puerto 80...");
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
