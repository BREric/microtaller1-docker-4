package co.org.uniquindio.retosmicroservicios;

import com.sun.net.httpserver.HttpServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.net.InetSocketAddress;

@SpringBootApplication
public class RetosMicroserviciosApplication {

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