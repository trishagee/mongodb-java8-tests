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
import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class Exercise10UpdateByReplacementTest {
    private MongoDatabase database;
    private MongoCollection<Document> collection;

    @Test
    @Ignore("Some problem with _id")
    public void shouldReplaceWholeDocumentWithNewOne() {
        // Given
        Person bob = new Person("bob", "Bob The Amazing", new Address("123 Fake St", "LondonTown", 1234567890), asList(27464, 747854));
        collection.insert(PersonAdaptor.toDocument(bob));

        Person charlie = new Person("charlie", "Charles", new Address("74 That Place", "LondonTown", 1234567890), asList(1, 74));
        collection.insert(PersonAdaptor.toDocument(charlie));

        // When
        Person updatedCharlieObject = new Person("charlie", "Charles the Suave", new Address("A new street", "GreatCity", 7654321),
                                                 Collections.<Integer>emptyList());
        Document findCharlie = new Document("_id", charlie.getId());
        WriteResult resultOfUpdate = collection.find(findCharlie)
                                               .updateOne(PersonAdaptor.toDocument(updatedCharlieObject));

        // Then
//        assertThat(resultOfUpdate.getCount(), is(1));

        Document newCharlieDocument = collection.find(findCharlie).into(new ArrayList<Document>()).get(0);
        // all values should have been updated to the new object values
        assertThat((String) newCharlieDocument.get("_id"), is(updatedCharlieObject.getId()));
        assertThat((String) newCharlieDocument.get("name"), is(updatedCharlieObject.getName()));
        assertThat((List<Integer>) newCharlieDocument.get("books"), is(updatedCharlieObject.getBookIds()));
        Document address = (Document) newCharlieDocument.get("address");
        assertThat((String) address.get("street"), is(updatedCharlieObject.getAddress().getStreet()));
        assertThat((String) address.get("city"), is(updatedCharlieObject.getAddress().getTown()));
        assertThat((int) address.get("phone"), is(updatedCharlieObject.getAddress().getPhone()));
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
