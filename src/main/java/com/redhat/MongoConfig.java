package com.redhat;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

@ApplicationScoped
public class MongoConfig {

	@Named
	public MongoClient mongoClient() {
		return new MongoClient(new ServerAddress("localhost"), new MongoClientOptions.Builder().build());
	}
}
