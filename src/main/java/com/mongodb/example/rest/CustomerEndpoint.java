package com.mongodb.example.rest;

import com.mongodb.example.dao.CustomerDAO;
import com.mongodb.example.model.Customer;

import javax.ejb.Stateless;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;
import java.util.List;
import java.util.logging.Logger;


/**
 * 
 */
@Stateless
@Path("/customers")
public class CustomerEndpoint {

	private static Logger LOG = Logger.getLogger(CustomerEndpoint.class.getName());

    private CustomerDAO customerDAO = new CustomerDAO();

    /**
     *
     * @param customer
     * @return
     */
	@POST
	@Consumes("application/json")
	public Response create(Customer customer) {

	    LOG.info("Create customer for fullName: " + customer.getFullName());

		customerDAO.insertOne(customer);

		return Response.created(
				UriBuilder.fromResource(CustomerEndpoint.class)
						.path(String.valueOf(customer.getId())).build()).build();
	}

    /**
     *
     * @param id
     * @return
     */
	@DELETE
	@Path("/{id:[0-9a-fA-F]{24}}")
	public Response deleteById(@PathParam("id") String id) {

	    LOG.info("Delete by id for id: " + id);

	    long deletedCount = customerDAO.deleteOne(id);

	    if(deletedCount == 0) {
            return Response.status(Status.NOT_FOUND).build();
        }

		return Response.noContent().build();
	}

    /**
     *
     * @param id
     * @return
     */
	@GET
	@Path("/{id:[0-9a-fA-F]{24}}")
	@Produces("application/json")
	public Response findById(@PathParam("id") String id) {

	    LOG.info("Find by id for id: " + id);

	    Customer customer = customerDAO.findOne(id);

	    if(customer != null) {
            return Response.ok(customer).build();
        }

        return Response.status(Status.NOT_FOUND).build();

	}

    /**
     *
     * @param startPosition
     * @param maxResult
     * @return
     */
	@GET
	@Produces("application/json")
	public List<Customer> listAll(@QueryParam("start") Integer startPosition,
			@QueryParam("max") Integer maxResult) {

	    LOG.info("List all customers");

		final List<Customer> results = customerDAO.findAll();

		return results;
	}

    /**
     *
     * @param id
     * @param customer
     * @return
     */
	@PUT
	@Path("/{id:[0-9a-fA-F]{24}}")
	@Consumes("application/json")
	public Response update(@PathParam("id") String id, Customer customer) {

        LOG.info("Update customer for id: " + id);

	    System.out.println("WS - update customer for id " + id);
		if (customer == null) {
			return Response.status(Status.BAD_REQUEST).build();
		}
		if (id == null) {
			return Response.status(Status.BAD_REQUEST).build();
		}
		if (!id.equals(customer.getId())) {
			return Response.status(Status.CONFLICT).entity(customer).build();
		}
		if (customerDAO.findOne(id) == null) {
			return Response.status(Status.NOT_FOUND).build();
		}

		customerDAO.updateOne(customer);

		return Response.noContent().build();
	}
}
