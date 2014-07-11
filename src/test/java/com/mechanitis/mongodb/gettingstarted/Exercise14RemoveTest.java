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
import org.mongodb.WriteResult;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

public class Exercise14RemoveTest {
    private MongoDatabase database;
    private MongoCollection<Document> collection;

    @Test
    public void shouldDeleteOnlyCharlieFromTheDatabase() {
        // Given
        Person bob = new Person("bob", "Bob The Amazing", new Address("123 Fake St", "LondonTown", 1234567890), asList(27464, 747854));
        collection.insert(PersonAdaptor.toDocument(bob));

        Person charlie = new Person("charlie", "Charles", new Address("74 That Place", "LondonTown", 1234567890), asList(1, 74));
        collection.insert(PersonAdaptor.toDocument(charlie));

        Person emily = new Person("emily", "Emily", new Address("5", "Some Town", 646383), Collections.<Integer>emptyList());
        collection.insert(PersonAdaptor.toDocument(emily));

        // When
        Document query = new Document("_id", "charlie");
        WriteResult resultOfRemove = collection.find(query).remove();

        // Then
        assertThat(resultOfRemove.getCount(), is(1));

        ArrayList<Document> remainingPeople = collection.find().into(new ArrayList<Document>());
        assertThat(remainingPeople.size(), is(2));

        for (final Document remainingPerson : remainingPeople) {
            assertThat((String) remainingPerson.get("_id"), is(not(charlie.getId())));
        }
   }

    @Test
    public void shouldDeletePeopleWhoLiveInLondon() {
        // Given
        Person bob = new Person("bob", "Bob The Amazing", new Address("123 Fake St", "LondonTown", 1234567890), asList(27464, 747854));
        collection.insert(PersonAdaptor.toDocument(bob));

        Person charlie = new Person("charlie", "Charles", new Address("74 That Place", "LondonTown", 1234567890), asList(1, 74));
        collection.insert(PersonAdaptor.toDocument(charlie));

        Person emily = new Person("emily", "Emily", new Address("5", "Some Town", 646383), Collections.<Integer>emptyList());
        collection.insert(PersonAdaptor.toDocument(emily));

        // When
        Document query = new Document("address.city", "LondonTown");
        WriteResult resultOfRemove = collection.find(query).remove();

        // Then
        assertThat(resultOfRemove.getCount(), is(2));

        ArrayList<Document> remainingPeople = collection.find().into(new ArrayList<Document>());
        assertThat(remainingPeople.size(), is(1));

        assertThat((String) remainingPeople.get(0).get("_id"), is(emily.getId()));
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
