package com.mechanitis.mongodb.gettingstarted;

import org.junit.Test;
import org.mongodb.MongoClient;
import org.mongodb.MongoClientURI;
import org.mongodb.MongoClients;
import org.mongodb.connection.ServerAddress;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

public class Exercise1ConnectingTest {
    @Test
    public void shouldCreateANewMongoClientConnectedToLocalhost() throws Exception {
        //when: 
        MongoClient mongoClient = MongoClients.create(new ServerAddress());

        //then:
        assertThat(mongoClient, is(notNullValue()));
    }

    @Test
    public void shouldBeAbleToConnectViaURI() throws Exception {
        //when:
        MongoClient mongoClient = MongoClients.create(new MongoClientURI("mongodb://localhost:27017"));

        //then:
        assertThat(mongoClient, is(notNullValue()));
    }

}
