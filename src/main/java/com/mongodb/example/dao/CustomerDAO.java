package com.mongodb.example.dao;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.mongodb.example.model.Customer;
import org.bson.Document;
import org.bson.types.ObjectId;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static com.mongodb.client.model.Filters.eq;

/**
 * Created by bmincey on 4/18/17.
 */
public class CustomerDAO {

    private static Logger LOG = Logger.getLogger(CustomerDAO.class.getName());

    private MongoClient mongoClient;

    MongoCollection<Document> myCollection;

    /**
     *
     */
    public CustomerDAO() {
        LOG.info("Initializing via JNDI lookup...");

        try {

            InitialContext initialContext = new InitialContext();
            mongoClient = (MongoClient) initialContext.lookup("java:global/MyMongoClient");

            this.initializeCollection();

        }
        catch (NamingException ne) {
            LOG.severe(ne.toString());

            ne.printStackTrace();
        }
    }


    /**
     *
     * @param mongoClient
     */
    public CustomerDAO(MongoClient mongoClient) {
        LOG.info("Initializing via passed in MongoClient...");
        this.mongoClient = mongoClient;
        this.initializeCollection();
    }

    /**
     *
     */
    private void initializeCollection() {
        LOG.info("Initializing Collection...");

        myCollection
                = mongoClient.getDatabase("myJavaDatabase").getCollection("myJavaCollection");

        LOG.info("Initialization of Collection Complete...");
    }


    /**
     *
     * @param customer
     */
    public void insertOne(Customer customer) {

        LOG.info("Inserting customer for fullName: " + customer.getFullName());

        Document document = new Document("fullName", customer.getFullName())
                                 .append("age", customer.getAge());

        myCollection.insertOne(document);

        customer.setId(document.getObjectId("_id").toString());

        LOG.info("Assigning id: " + customer.getId());
    }

    /**
     *
     * @param id
     * @return
     */
    public long deleteOne(String id) {

        LOG.info("Deleting customer for id: " + id);

        DeleteResult deleteResult = myCollection.deleteOne(eq("_id", new ObjectId(id)));
        long deletedCount = deleteResult.getDeletedCount();

        LOG.info("Num deleted: " + deletedCount);

        return deletedCount;
    }

    /**
     *
     * @param id
     * @return
     */
    public Customer findOne(String id) {

        LOG.info("Find customer for id: " + id);

        Customer customer = null;

        Document document = myCollection.find(eq("_id", new ObjectId(id))).first();

        if(document != null && document.getObjectId("_id") != null) {

            LOG.info("Customer found for id: " + id);

            customer = new Customer();

            customer.setId(document.getObjectId("_id").toString());
            customer.setFullName(document.getString("fullName"));
            customer.setAge(document.getInteger("age"));
        }

        return customer;
    }

    /**
     *
     * @return
     */
    public List<Customer> findAll() {

        LOG.info("Find all customers");

        List<Customer> customerList = null;

        FindIterable<Document> resultList = myCollection.find();

        if(resultList != null) {

            customerList = new ArrayList<Customer>();

            for( Document document : resultList ) {

                Customer customer = new Customer();
                customer.setId(document.getObjectId("_id").toString());
                customer.setFullName(document.getString("fullName"));
                customer.setAge(document.getInteger("age"));

                customerList.add(customer);

            }
        }

        return customerList;
    }

    /**
     *
     * @param customer
     */
    public void updateOne(Customer customer) {

        LOG.info("Updating customer for id: " + customer.getId());

        UpdateResult update1 = myCollection.updateOne(eq("_id",new ObjectId(customer.getId())),
                new Document("$set", new Document("fullName", customer.getFullName())));

        UpdateResult update2 = myCollection.updateOne(eq("_id",new ObjectId(customer.getId())),
                new Document("$set", new Document("age", customer.getAge())));



        if(update1.getModifiedCount() > 0){
            LOG.info("Full Name updated to: " + customer.getFullName() );
        }

        if(update2.getModifiedCount() > 0){
            LOG.info("Age updated to: " + customer.getAge());
        }
    }

}
