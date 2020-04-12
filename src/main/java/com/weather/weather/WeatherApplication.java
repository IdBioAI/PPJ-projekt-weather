package com.weather.weather;

import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import com.weather.rest.PostAdd;
import com.weather.rest.PostDelete;
import com.weather.rest.PostEdit;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

@SpringBootApplication
public class WeatherApplication {

    public static void main(String[] args) {


      //  SpringApplication.run(WeatherApplication.class, args);
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(8500), 0);
            HttpContext context = server.createContext("/");
            server.createContext("/add", new PostAdd());
            server.createContext("/delete", new PostDelete());
            server.createContext("/edit", new PostEdit());
            context.setHandler(WeatherApplication::handleRequest);
            server.start();
        }catch (Exception ex){

        }

    }

    private static void handleRequest(HttpExchange exchange) throws IOException {
        String response = "Hi there!";
        exchange.sendResponseHeaders(200, response.getBytes().length);//response code and length
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

}
