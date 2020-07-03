package com.pigmalion.springvertxdemo.verticles;

import com.pigmalion.springvertxdemo.model.User;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.pgclient.PgConnectOptions;
import io.vertx.pgclient.PgPool;
import io.vertx.sqlclient.PoolOptions;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DBVerticle extends AbstractVerticle {

    private PgPool client;

    @Override
    public void start (Promise<Void> promise) throws Exception {

        PgConnectOptions connectOptions = new PgConnectOptions()
                .setPort(5432)
                .setHost("localhost")
                .setDatabase("usersdb")
                .setUser("postgres")
                .setPassword("1111");

        PoolOptions poolOptions = new PoolOptions().setMaxSize(5);

        client = PgPool.pool(vertx, connectOptions, poolOptions);

        client.getConnection(ar -> {
            if (ar.succeeded()) {
                vertx.eventBus().consumer("GET_USERS", this::getUsers);
                System.out.println("------ Postgres getConnection success ----------");
                promise.complete();
            } else {
                System.out.println("------ Postgres getConnection error ---------");
                promise.fail(ar.cause());
            }
        });

    }

    private void getUsers(Message<JsonObject> message) {
        client
            .query("SELECT * FROM USERS")
            .execute( res -> {
                if (res.succeeded()) {
                    RowSet<Row> rows = res.result();
                    ArrayList<User> userArrayList = new ArrayList<>();
                    for (Row row : rows) {
                        Long id = row.getLong("id");
                        String name = row.getString("name");
                        userArrayList.add(new User(id, name));
                    }
                    message.reply(userArrayList.toString());
                } else {
                    System.out.println("------ getUsers message error ------ " + res.cause().toString());
                }
        });
    }


}
