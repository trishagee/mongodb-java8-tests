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
import java.util.Collections;
import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mongodb.Sort.ascending;

public class Exercise12UpdateMultipleDocumentsTest {
    private MongoDatabase database;
    private MongoCollection<Document> collection;

    @Test
    public void shouldOnlyUpdateTheFirstDBObjectMatchingTheQuery() {
        // Given
        Person bob = new Person("bob", "Bob The Amazing", new Address("123 Fake St", "LondonTown", 1234567890), asList(27464, 747854));
        collection.insert(PersonAdaptor.toDocument(bob));

        Person charlie = new Person("charlie", "Charles", new Address("74 That Place", "LondonTown", 1234567890), asList(1, 74));
        collection.insert(PersonAdaptor.toDocument(charlie));

        Person emily = new Person("emily", "Emily", new Address("5", "Some Town", 646383), Collections.<Integer>emptyList());
        collection.insert(PersonAdaptor.toDocument(emily));

        // When
        Document findLondoners = new Document("address.city", "LondonTown");
        assertThat(collection.find(findLondoners).count(), is(2L));

        collection.find(findLondoners)
                  .sort(ascending("_id"))
                  .updateOne(new Document("$set", new Document("wasUpdated", true)));

        // Then
        List<Document> londoners = collection.find(findLondoners).sort(ascending("_id")).into(new ArrayList<Document>());
        assertThat(londoners.size(), is(2));

        assertThat((String) londoners.get(0).get("name"), is(bob.getName()));
        assertThat((boolean) londoners.get(0).get("wasUpdated"), is(true));

        assertThat((String) londoners.get(1).get("name"), is(charlie.getName()));
        assertThat(londoners.get(1).get("wasUpdated"), is(nullValue()));
    }

    @Test
    public void shouldUpdateEveryoneLivingInLondon() {
        // Given
        Person bob = new Person("bob", "Bob The Amazing", new Address("123 Fake St", "LondonTown", 1234567890), asList(27464, 747854));
        collection.insert(PersonAdaptor.toDocument(bob));

        Person charlie = new Person("charlie", "Charles", new Address("74 That Place", "LondonTown", 1234567890), asList(1, 74));
        collection.insert(PersonAdaptor.toDocument(charlie));

        Person emily = new Person("emily", "Emily", new Address("5", "Some Town", 646383), Collections.<Integer>emptyList());
        collection.insert(PersonAdaptor.toDocument(emily));

        // When
        Document findLondoners = new Document("address.city", "LondonTown");
        assertThat(collection.find(findLondoners).count(), is(2L));

        collection.find(findLondoners)
                  .sort(ascending("_id"))
                  .update(new Document("$set", new Document("wasUpdated", true)));

        // Then
        List<Document> londoners = collection.find(findLondoners).sort(ascending("_id")).into(new ArrayList<Document>());
        assertThat(londoners.size(), is(2));

        Document firstLondoner = londoners.get(0);
        assertThat((String) firstLondoner.get("name"), is(bob.getName()));
        assertThat((boolean) firstLondoner.get("wasUpdated"), is(true));

        Document secondLondoner = londoners.get(1);
        assertThat((String) secondLondoner.get("name"), is(charlie.getName()));
        assertThat((boolean) secondLondoner.get("wasUpdated"), is(true));
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
