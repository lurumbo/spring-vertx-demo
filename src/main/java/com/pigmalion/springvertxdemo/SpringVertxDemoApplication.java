package com.pigmalion.springvertxdemo;

import com.pigmalion.springvertxdemo.verticles.HttpServerVerticle;
import io.vertx.core.Vertx;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class SpringVertxDemoApplication {

	@Autowired
	private HttpServerVerticle httpServerVerticle;

	public static void main(String[] args) {
		SpringApplication.run(SpringVertxDemoApplication.class, args);
	}

	@PostConstruct
	public void deployVerticles () {
		Vertx vertx = Vertx.vertx();
		vertx.deployVerticle(httpServerVerticle);
	}

}
