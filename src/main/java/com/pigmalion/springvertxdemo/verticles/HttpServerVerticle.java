package com.pigmalion.springvertxdemo.verticles;

import com.pigmalion.springvertxdemo.model.User;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

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

        vertx.eventBus().request("GET_USERS", new JsonObject(), reply -> {
           if (reply.succeeded()) {
               String stringUsers = reply.result().body().toString();

               routingContext
                       .response()
                       .end(stringUsers);
           } else {
               routingContext
                       .response()
                       .end(("---- event bus reply getUsers error ------"));
               System.out.println("---- event bus reply getUsers error ------" + reply.cause().toString());
           }
        });

    }
}
