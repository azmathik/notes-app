package com.teletronics.notes.cli;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class MongoCheckCommandLineRunner implements CommandLineRunner {
    @Autowired
    private MongoClient mongoClient;
    @Override
    public void run(String... args) throws Exception {
        MongoDatabase database = mongoClient.getDatabase("note-app-db");
        if (database == null) {
            throw new RuntimeException("Database note-app-db does not exist!");
        } else {
            System.out.println("Database note-app-db exists.");
        }
    }
}
