package se.jsta;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeoutException;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import javafx.fxml.LoadException;
import se.jsta.DBHelper;



/**
 * Root resource (exposed at "jstabanken" path)
 */
@Path("jstabanken")
public class JSTABanken {
    
    @POST
    @Path("createcustomer")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
	public Response createCustomer(@QueryParam("name") String name)  {
    	if(null == name || name.isEmpty()){
			return Response.status(Response.Status.NOT_ACCEPTABLE).entity("You have to provide a name").build();
    	}
    	try{
    		DBHelper.initDB();
    		return DBHelper.createCustomer(name);
    	}catch(TimeoutException le){
    		return Response.status(Response.Status.REQUEST_TIMEOUT).entity("Databasen hinner inte med").build();
    	}

	}

    @POST
    @Path("insertmoney")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
	public Response insertMoney(@QueryParam("name") String name , @QueryParam("amount") float amount){
    	if(null == name || name.isEmpty()){
			return Response.status(Response.Status.NOT_ACCEPTABLE).entity("You have to provide a name").build();
    	}
    	if(0 == amount){
			return Response.status(Response.Status.NOT_ACCEPTABLE).entity("You have to provide an amount").build();
    	}
    	try{
    		DBHelper.initDB();
    		float balance = DBHelper.getBalance(name);
    		
    		if(balance == -1){
    			return Response.status(Response.Status.NOT_FOUND).entity("Kunde inte hitta kund med namn:" + name).build();

    		}
    		if (amount <= 0) {
    			return Response.status(Response.Status.NOT_ACCEPTABLE).entity("Beloppet är negativt:" + amount).build();
    		}

    		return DBHelper.setBalance(name, balance + amount);
    	}catch(TimeoutException le){
    		return Response.status(Response.Status.REQUEST_TIMEOUT).entity("Databasen hinner inte med").build();
    	}

	}

    @POST
    @Path("withdrawmoney")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
	public Response withdrawMoney(@QueryParam("name") String name , @QueryParam("amount") float amount) {
    	if(null == name || name.isEmpty()){
			return Response.status(Response.Status.NOT_ACCEPTABLE).entity("You have to provide a name").build();
    	}
    	if(0 == amount){
			return Response.status(Response.Status.NOT_ACCEPTABLE).entity("You have to provide an amount").build();
    	}
    	try{
        	DBHelper.initDB();
    		float balance = DBHelper.getBalance(name);
    		
    		if(balance == -1){
    			return Response.status(Response.Status.NOT_FOUND).entity("Kunde inte hitta kund med namn:" + name).build();

    		}
    		
    		if (amount <= 0) {
    			return Response.status(Response.Status.NOT_ACCEPTABLE).entity("Beloppet är negativt:" + amount).build();
    		}

    		
    		if (balance - amount < 0) {
    			return Response.status(Response.Status.NOT_ACCEPTABLE).entity("Det finns för lite pengar:" + amount).build();
    		}
    		return DBHelper.setBalance(name, balance - amount);
    	}catch(TimeoutException le){
    		return Response.status(Response.Status.REQUEST_TIMEOUT).entity("Databasen hinner inte med").build();
    	}


	}

    @GET
    @Path("getbalance")
    @Produces(MediaType.APPLICATION_JSON)
	public Response getBalance(@QueryParam("name") String name)  {
    	if(null == name || name.isEmpty()){
			return Response.status(Response.Status.NOT_ACCEPTABLE).entity("You have to provide a name").build();
    	}
    	try{
    		DBHelper.initDB();
    		float balance = DBHelper.getBalance(name);
    		if(balance == -1){
    			return Response.status(Response.Status.NOT_FOUND).entity("Kunde inte hitta kund med namn:" + name).build();
    				
    		}else{
    			return Response.ok("{\"Name\":\"" + name + "\", \"Balance\":\"" + balance +"\"}").build();
    		}
    	}catch(TimeoutException le){
    		return Response.status(Response.Status.REQUEST_TIMEOUT).entity("Databasen hinner inte med").build();
    	}

	}
	
    @GET
    @Path("getcustomers")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCusomers() {
    	try{
    		DBHelper.initDB();
    		return  DBHelper.getCustomers();
    	}catch(TimeoutException le){
    		return Response.status(Response.Status.REQUEST_TIMEOUT).entity("Databasen hinner inte med").build();
    	}

    }
    

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("robthebank")
    @Produces(MediaType.APPLICATION_JSON)
    public String robTheBank(){
    	return "{\"Resultat\":\"Polisen skjuter dig och du fortsätter testa manuellt i nästa liv\"}";
    }
    
}
