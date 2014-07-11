package com.mechanitis.mongodb.com.mechanitis.mongodb.gettingstarted;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mongodb.Document;
import org.mongodb.Index;
import org.mongodb.MongoClient;
import org.mongodb.MongoClientURI;
import org.mongodb.MongoClients;
import org.mongodb.MongoCollection;
import org.mongodb.MongoDatabase;

import java.net.UnknownHostException;
import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class Exercise9IndexTest {
    private MongoDatabase database;
    private MongoCollection<Document> collection;

    @Test
    public void shouldCreateAnAscendingIndex() {
        // given
        collection.insert(new Document("fieldToIndex", "Bob"));
        
        // when
        collection.tools().createIndexes(asList(Index.builder().addKey("fieldToIndex").build()));

        // then
        List<Document> indexes = collection.tools().getIndexes();
        assertThat((String) indexes.get(1).get("name"), is("fieldToIndex_1"));
    }

    @Before
    public void setUp() throws UnknownHostException {
        MongoClient mongoClient = MongoClients.create(new MongoClientURI("mongodb://localhost:27017"));
        database = mongoClient.getDatabase("Examples");
        collection = database.getCollection("people");
    }

    @After
    public void tearDown() {
        database.tools().drop();
    }
}
