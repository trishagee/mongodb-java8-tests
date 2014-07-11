package com.mechanitis.mongodb.com.mechanitis.mongodb.gettingstarted;

import com.mechanitis.mongodb.gettingstarted.person.Address;
import com.mechanitis.mongodb.gettingstarted.person.Person;
import com.mechanitis.mongodb.gettingstarted.person.PersonAdaptor;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
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
import static org.junit.Assert.assertThat;

public class Exercise13UpsertTest {
    private MongoDatabase database;
    private MongoCollection<Document> collection;

    @Test
    @Ignore("Some problem with _id")
    public void shouldOnlyInsertDBObjectIfItDidNotExistWhenUpsertIsTrue() {
        Person bob = new Person("bob", "Bob The Amazing", new Address("123 Fake St", "LondonTown", 1234567890), asList(27464, 747854));
        collection.insert(PersonAdaptor.toDocument(bob));

        Person charlie = new Person("charlie", "Charles", new Address("74 That Place", "LondonTown", 1234567890), asList(1, 74));
        collection.insert(PersonAdaptor.toDocument(charlie));

        // new person not in the database yet
        Person claire = new Person("claire", "Claire", new Address("1", "Town", 836558493), Collections.<Integer>emptyList());

        // When
        Document findClaire = new Document("_id", claire.getId());
        WriteResult resultOfUpdate = collection.find(findClaire)
                                               .updateOne(PersonAdaptor.toDocument(claire));

        // Then
        assertThat(resultOfUpdate.getCount(), is(0));
        // without upsert this should not have been inserted
        assertThat(collection.find(findClaire).count(), is(0L));


        // When
        WriteResult resultOfUpsert = collection.find(findClaire)
                                               .upsert()
                                               .updateOne(PersonAdaptor.toDocument(claire));

        // Then
        assertThat(resultOfUpsert.getCount(), is(1));

        Document newClaireDocument = collection.find(findClaire).into(new ArrayList<Document>()).get(0);
        // all values should have been updated to the new object values
        assertThat((String) newClaireDocument.get("_id"), is(claire.getId()));
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
