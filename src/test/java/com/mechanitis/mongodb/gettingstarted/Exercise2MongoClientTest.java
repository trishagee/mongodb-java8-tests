package com.mechanitis.mongodb.gettingstarted;

import org.junit.Test;
import org.mongodb.Document;
import org.mongodb.MongoClient;
import org.mongodb.MongoClients;
import org.mongodb.MongoCollection;
import org.mongodb.MongoDatabase;
import org.mongodb.connection.ServerAddress;

import java.net.UnknownHostException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

public class Exercise2MongoClientTest {
    @Test
    public void shouldGetADatabaseFromTheMongoClient() throws Exception {
        //given:
        MongoClient mongoClient = MongoClients.create(new ServerAddress());

        //when:
        MongoDatabase database = mongoClient.getDatabase("TheDatabaseName");

        //then:
        assertThat(database, is(notNullValue()));
    }

    @Test
    public void shouldGetACollectionFromTheDatabase() throws Exception {
        //given:
        MongoClient mongoClient = MongoClients.create(new ServerAddress());
        MongoDatabase database = mongoClient.getDatabase("TheDatabaseName");

        //when:
        MongoCollection collection = database.getCollection("TheCollectionName");

        //then:
        assertThat(collection, is(notNullValue()));
    }

    @Test(expected = IllegalStateException.class)
    public void shouldNotBeAbleToUseMongoClientAfterItHasBeenClosed() throws UnknownHostException {
        //given:
        MongoClient mongoClient = MongoClients.create(new ServerAddress());

        //when:
        mongoClient.close();
        mongoClient.getDatabase("SomeDatabase").getCollection("coll").insert(new Document("field", "value"));
    }

}
