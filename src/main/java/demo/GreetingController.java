package demo;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Component;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

@Component
public class GreetingController extends AbstractVerticle {

	private static final String template = "Hello, %s!";
	private final AtomicLong counter = new AtomicLong();

	@Override
	public void start() throws Exception {
		Router router = Router.router(vertx);

		router.route().handler(BodyHandler.create());
		router.get("/vertx/greeting").handler(routingContext -> {

			HttpServerResponse response = routingContext.response();
			response.putHeader("content-type", "text/plain");

			// Write to the response and end it
			String name = routingContext.request().getParam("name");
			if (name == null) {
				name = "World";
			}
			response.end(Json.encodePrettily(
					new Greeting(counter.incrementAndGet(), String.format(template, name) + " <Vert.X>")));
		});

		vertx.createHttpServer().requestHandler(router::accept).listen(8080);

	}

}
