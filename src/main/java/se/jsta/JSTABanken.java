package se.jsta;

import java.util.Arrays;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import se.jsta.DBHelper;



/**
 * Root resource (exposed at "jstabanken" path)
 */
@Path("jstabanken")
public class JSTABanken {
    
    @GET
    @Path("createcustomer")
    @Produces(MediaType.APPLICATION_JSON)
	public Response createCustomer(@QueryParam("name") String name)  {
    	if(null == name){
			return Response.status(Response.Status.NOT_ACCEPTABLE).entity("You have to provide a name").build();
    	}
		DBHelper.initDB();
		return DBHelper.createCustomer(name);
	}

    @GET
    @Path("insertmoney")
    @Produces(MediaType.APPLICATION_JSON)
	public Response insertMoney(@QueryParam("name") String name , @QueryParam("amount") float amount){
    	if(null == name){
			return Response.status(Response.Status.NOT_ACCEPTABLE).entity("You have to provide a name").build();
    	}
    	if(0 == amount){
			return Response.status(Response.Status.NOT_ACCEPTABLE).entity("You have to provide an amount").build();
    	}
		DBHelper.initDB();
		float balance = DBHelper.getBalance(name);
		
		if(balance == -1){
			return Response.status(Response.Status.NOT_FOUND).entity("Kunde inte hitta kund med namn: " + name).build();

		}
		if (amount <= 0) {
			return Response.status(Response.Status.NOT_ACCEPTABLE).entity("Beloppet är negativt: " + amount).build();
		}

		return DBHelper.setBalance(name, balance + amount);
	}

    @GET
    @Path("withdrawmoney")
    @Produces(MediaType.APPLICATION_JSON)
	public Response withdrawMoney(@QueryParam("name") String name , @QueryParam("amount") float amount) {
    	if(null == name){
			return Response.status(Response.Status.NOT_ACCEPTABLE).entity("You have to provide a name").build();
    	}
    	if(0 == amount){
			return Response.status(Response.Status.NOT_ACCEPTABLE).entity("You have to provide an amount").build();
    	}
    	DBHelper.initDB();
		float balance = DBHelper.getBalance(name);
		
		if(balance == -1){
			return Response.status(Response.Status.NOT_FOUND).entity("Kunde inte hitta kund med namn: " + name).build();

		}
		
		if (amount <= 0) {
			return Response.status(Response.Status.NOT_ACCEPTABLE).entity("Beloppet är negativt: " + amount).build();
		}

		
		if (balance - amount < 0) {
			return Response.status(Response.Status.NOT_ACCEPTABLE).entity("Det finns för lite pengar: " + amount).build();
		}
		return DBHelper.setBalance(name, balance - amount);

	}

    @GET
    @Path("getbalance")
    @Produces(MediaType.APPLICATION_JSON)
	public Response getBalance(@QueryParam("name") String name)  {
    	if(null == name){
			return Response.status(Response.Status.NOT_ACCEPTABLE).entity("You have to provide a name").build();
    	}
		DBHelper.initDB();
		float balance = DBHelper.getBalance(name);
		if(balance == -1){
			return Response.status(Response.Status.NOT_FOUND).entity("Kunde inte hitta kund med namn: " + name).build();
				
		}else{
			return Response.ok("{\"Name\":\"" + name + "\"Balance\":\"" + balance +"\"}").build();
		}
	}
	
    @GET
    @Path("getcustomers")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCusomers() {
		DBHelper.initDB();
		return  DBHelper.getCustomers();
    }
    

    @GET
    @Path("robthebank")
    @Produces(MediaType.APPLICATION_JSON)
    public String robTheBank(){
    	return "{\"Resultat\":\"Polisen skjuter dig och du fortsätter testa manuellt i nästa liv\"}";
    }
    
}
