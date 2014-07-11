package com.mechanitis.mongodb.com.mechanitis.mongodb.gettingstarted;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mongodb.Document;
import org.mongodb.MongoClient;
import org.mongodb.MongoClientURI;
import org.mongodb.MongoClients;
import org.mongodb.MongoCollection;
import org.mongodb.MongoDatabase;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mongodb.Sort.ascending;

public class Exercise8SkipAndLimitTest {
    private MongoDatabase database;
    private MongoCollection<Document> collection;

    @Test
    public void shouldReturnDBObjects3to9Of20DBObjectsUsingSkipAndLimit() {
        // Given
        for (int i = 0; i < 20; i++) {
            collection.insert(new Document("name", "person" + i).append("someIntValue", i));
        }

        // When
        List<Document> results = collection.find()
                                           .sort(ascending("someIntValue"))
                                           .skip(3)
                                           .limit(7)
                                           .into(new ArrayList<Document>());

        // Then
        assertThat(results.size(), is(7));
        assertThat((int) results.get(0).get("someIntValue"), is(3));
        assertThat((int) results.get(1).get("someIntValue"), is(4));
        assertThat((int) results.get(2).get("someIntValue"), is(5));
        assertThat((int) results.get(3).get("someIntValue"), is(6));
        assertThat((int) results.get(4).get("someIntValue"), is(7));
        assertThat((int) results.get(5).get("someIntValue"), is(8));
        assertThat((int) results.get(6).get("someIntValue"), is(9));
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
