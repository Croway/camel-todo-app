package com.redhat;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.client.model.Filters;

import javax.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@ApplicationScoped
public class TodoAppRoute extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		restConfiguration().bindingMode(RestBindingMode.json);

		rest("api")
				.get().produces("application/json").to("direct:findAll")
				.post().produces("application/json").type(Todo.class).to("direct:create").to("direct:create")
				.patch("{id}").produces("application/json").to("direct:updateById")
				.delete().to("direct:deleteCompleted")
				.delete("{id}").to("direct:deleteById");

		from("direct:deleteCompleted")
				.log("delete completed")
				.process(exchange -> {
					Bson id = Filters.eq("completed", true);

					exchange.getMessage().setBody(id);
				})
				.to("mongodb:mongoClient?database=todo&collection=todos&operation=remove")
				.endRest();

		from("direct:updateById")
				.log("update by Id ${header.id}")
				.to("mongodb:mongoClient?database=todo&collection=todos&operation=save")
				.endRest();

		from("direct:deleteById")
				.log("delete by Id ${header.id}")
				.process(exchange -> {
					Bson id = Filters.eq("_id", exchange.getMessage().getHeader("id"));

					exchange.getMessage().setBody(id);
				})
				.to("mongodb:mongoClient?database=todo&collection=todos&operation=remove").setBody().body()
				.endRest();

		from("direct:create")
				.log("pre process body ${body}")
				.process(exchange -> {
					Todo todo = exchange.getMessage().getBody(Todo.class);

					todo._id = UUID.randomUUID().toString();

					exchange.getMessage().setBody(todo);
				})
				.log("direct create ${body}")
				.to("mongodb:mongoClient?database=todo&collection=todos&operation=insert").setBody().body().endRest();

		from("direct:findAll")
				.to("mongodb:myDb?database=todo&collection=todos&operation=findAll&outputType=DocumentList")
				.process(exchange -> {
					List<Document> body = exchange.getMessage().getBody(List.class);
					List<Todo> todos = body.stream().map(document -> {
						Todo todo = new Todo();

						todo._id = document.get("_id").toString();
						todo.id = document.get("_id").toString();
						todo.url = document.get("url", String.class);
						todo.title = document.get("title", String.class);
						todo.order = document.get("order", Integer.class);
						todo.completed = document.get("completed", Boolean.class);

						return todo;
					}).collect(Collectors.toList());

					exchange.getMessage().setBody(todos);
				}).endRest();
	}
}
