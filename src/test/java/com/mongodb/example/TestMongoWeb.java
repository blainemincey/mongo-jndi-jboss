package com.mongodb.example;


import com.mongodb.MongoClient;
import com.mongodb.example.dao.CustomerDAO;
import com.mongodb.example.model.Customer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.logging.Logger;

/**
 * Created by bmincey on 5/15/17.
 */
public class TestMongoWeb {

    private static Logger LOG = Logger.getLogger(TestMongoWeb.class.getName());

    private CustomerDAO customerDAO = null;

    /**
     * Set up MongoDB for testing
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {

        LOG.info("Setup test class...");

        MongoClient mongoClient = new MongoClient("localhost", 27017);

        this.customerDAO = new CustomerDAO(mongoClient);
    }

    /**
     *
     */
    @Test
    public void runCRUDTests() {

        // Test create
        LOG.info("Test insert one...");

        Customer customer = new Customer();
        customer.setFullName("Blaine Mincey");
        customer.setAge(21);

        this.customerDAO.insertOne(customer);

        LOG.info("Id: " + customer.getId());

        Assert.assertNotNull(customer.getId());

        // Test find
        LOG.info("Test find one with id: " + customer.getId());

        Customer findCustomer =
                this.customerDAO.findOne(customer.getId());

        Assert.assertNotNull(findCustomer);

        // Test update
        LOG.info("Test update with id: " + customer.getId());
        customer.setFullName("Randy Blaine Mincey");

        this.customerDAO.updateOne(customer);

        // Test delete
        LOG.info("Test delete with id: " + customer.getId());

        long deletedCount = this.customerDAO.deleteOne(customer.getId());

        Assert.assertEquals(1, deletedCount);

    }
}
