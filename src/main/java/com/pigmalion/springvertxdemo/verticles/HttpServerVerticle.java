package com.pigmalion.springvertxdemo.verticles;

import io.vertx.core.AbstractVerticle;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import org.springframework.stereotype.Component;

@Component
public class HttpServerVerticle extends AbstractVerticle {

    @Override
    public void start () {

        Router router = Router.router(vertx);
        router.get("/users").handler(this::getUsers);

        vertx
            .createHttpServer()
            .requestHandler(router)
            .listen(8080, result -> {
                if (result.succeeded())
                    System.out.println("----- Vertx HTTP Server running on port 8080 -----");
                else
                    System.out.println("----- Vertx HTTP Server failed at start -----");
            });

    }

    private void getUsers(RoutingContext routingContext) {
        routingContext
                .response()
                .end("Get users coming soon ..");
    }
}
