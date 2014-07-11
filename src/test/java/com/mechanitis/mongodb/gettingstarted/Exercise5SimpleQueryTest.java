package com.mechanitis.mongodb.com.mechanitis.mongodb.gettingstarted;

import com.mechanitis.mongodb.gettingstarted.person.Address;
import com.mechanitis.mongodb.gettingstarted.person.Person;
import com.mechanitis.mongodb.gettingstarted.person.PersonAdaptor;
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

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class Exercise5SimpleQueryTest {
    private MongoDatabase database;
    private MongoCollection<Document> collection;

    @Test
    public void shouldFindAllDBObjectsWithTheNameCharles() {
        // Given
        Person charlie = new Person("charlie", "Charles", new Address("74 That Place", "LondonTown", 1234567890), asList(1, 74));
        collection.insert(PersonAdaptor.toDocument(charlie));

        Person bob = new Person("bob", "Bob The Amazing", new Address("123 Fake St", "LondonTown", 1234567890), asList(27464, 747854));
        collection.insert(PersonAdaptor.toDocument(bob));

        // When
        Document query = new Document("name", "Charles");
        List<Document> results = collection.find(query).into(new ArrayList<Document>());

        // Then
        assertThat(results.size(), is(1));
        assertThat((String) results.get(0).get("_id"), is(charlie.getId()));
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
