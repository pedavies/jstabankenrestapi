package se.jsta;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

import javax.ws.rs.core.Response;

import javafx.fxml.LoadException;

public class DBHelper {
	private static String dbConnectionName = "jdbc:sqlite:jstabanken.db";

	public static void initDB() throws TimeoutException{
		createTableIfNotExist();
	}
	
	public static void executeUpdate(String sql) throws TimeoutException{
		Connection c = null;
	    Statement stmt = null;
	    try {
	      Class.forName("org.sqlite.JDBC");
	      c = DriverManager.getConnection(dbConnectionName);
	      c.setAutoCommit(false);
	      System.out.println("Opened database successfully");
	      stmt = c.createStatement();
	      stmt.executeUpdate(sql);
	      stmt.close();
	      c.commit();
	      c.close();
	    } catch ( Exception e ) {
	      System.err.println( e.getClass().getName() + ":" + e.getMessage() );
	      try{
	    	  c.close();
	      }catch(Exception ee){
	    	  
	      }finally{
	    	  throw new TimeoutException("Databasen hinner inte med");
	      }
	    }
	    System.out.println("SQL executed created successfully");
	}
	
	private static void createTableIfNotExist() throws TimeoutException{
		if(!isTableAlreadyCreted()){
			  Connection c = null;
			    Statement stmt = null;
			    try {
			      Class.forName("org.sqlite.JDBC");
			      c = DriverManager.getConnection(dbConnectionName);
			      System.out.println("Opened database successfully");

			      stmt = c.createStatement();
			      String sql = "CREATE TABLE CUSTOMERS " +
			                   "(ID INT PRIMARY KEY     NOT NULL," +
			                   " NAME           TEXT    NOT NULL, " + 
			                   " BALANCE            REAL     NOT NULL)"; 
			      stmt.executeUpdate(sql);
			      stmt.close();
			      c.close();
			      
			      executeUpdate("INSERT INTO CUSTOMERS (ID,NAME,BALANCE) " +
		                   "VALUES (1, 'Saad', 20000.2);");
			      executeUpdate("INSERT INTO CUSTOMERS (ID,NAME,BALANCE) " +
		                   "VALUES (2, 'Rickard', 20055.2);");
			    } catch ( Exception e ) {
			      System.err.println( e.getClass().getName() + ":" + e.getMessage() );
			      try{
			    	  c.close();
			      }catch(Exception ee){
			    	  
			      }finally{
			    	  throw new TimeoutException("Databasen hinner inte med");
			      }
			    }
			    System.out.println("Table created successfully");
		}
	}
	
	
	  private static boolean isTableAlreadyCreted() throws TimeoutException
	  {
	    Connection c = null;
	    Statement stmt = null;
	    try {
	      Class.forName("org.sqlite.JDBC");
	      c = DriverManager.getConnection(dbConnectionName);
	      c.setAutoCommit(false);
	      System.out.println("Opened database successfully");

	      stmt = c.createStatement();
	      ResultSet rs = stmt.executeQuery( "SELECT name FROM sqlite_master WHERE type='table' AND name='CUSTOMERS';" );
	      boolean exist = false;
	      while ( rs.next() ) {
	    	  exist = true;
	      }
	      rs.close();
	      stmt.close();
	      c.close();
	      return exist;
	    } catch ( Exception e ) {
	      System.err.println( e.getClass().getName() + ":" + e.getMessage() );
	      try{
	    	  c.close();
	      }catch(Exception ee){
	    	  
	      }finally{
	    	  throw new TimeoutException("Databasen hinner inte med");
	      }
	    }
	  }
	  
	  private static int getNextId() throws TimeoutException{
	      System.out.println("Getting the next id");

		   Connection c = null;
		    Statement stmt = null;
		    try {
		      Class.forName("org.sqlite.JDBC");
		      c = DriverManager.getConnection(dbConnectionName);
		      c.setAutoCommit(false);
		      System.out.println("Opened database successfully");

		      stmt = c.createStatement();
		      ResultSet rs = stmt.executeQuery( "SELECT * FROM CUSTOMERS" );
		      int lastIndex = 0;
		      while ( rs.next() ) {
		    	  lastIndex = rs.getInt(1);
		      }
		      rs.close();
		      stmt.close();
		      c.close();
		      return lastIndex + 1;
		    } catch ( Exception e ) {
		      System.err.println( e.getClass().getName() + ":" + e.getMessage() );
		      try{
		    	  c.close();
		      }catch(Exception ee){
		    	  
		      }finally{
		    	  throw new TimeoutException("Databasen hinner inte med");
		      }
		    }
	  }
	  
	  private static boolean isCustomerExist(String name) throws TimeoutException{
	      System.out.println("Checking if customer exist");
		  Connection c = null;
		    Statement stmt = null;
		    try {
		      Class.forName("org.sqlite.JDBC");
		      c = DriverManager.getConnection(dbConnectionName);
		      c.setAutoCommit(false);
		      System.out.println("Opened database successfully");

		      stmt = c.createStatement();
		      ResultSet rs = stmt.executeQuery( "SELECT * FROM CUSTOMERS WHERE name='"+name+"';" );
		      boolean exist = false;
		      while ( rs.next() ) {
			      System.out.println("Found customer in database");
			      System.out.println("Name" + rs.getString("name"));
			      System.out.println("Balance" + rs.getString("balance"));
			      exist = true;
		      }
		      rs.close();
		      stmt.close();
		      c.close();
		      if(exist == false){
			      System.out.println("Found no customer");
		      }
		      return exist;
		    } catch ( Exception e ) {
		      System.err.println( e.getClass().getName() + ":" + e.getMessage() );
		      try{
		    	  c.close();
		      }catch(Exception ee){
		    	  
		      }finally{
		    	  throw new TimeoutException("Databasen hinner inte med");
		      }
		    }
	  }
	  
	  
	  public static Response createCustomer(String name) throws TimeoutException {
		  if(isCustomerExist(name)){
			  return Response.status(Response.Status.CONFLICT).entity( "A Customer with name "+ name +" already exist").build();
		  }
		  int index = getNextId();
	      executeUpdate("INSERT INTO CUSTOMERS (ID,NAME,BALANCE) " +
                  "VALUES ("+ index +", '"+ name +"', 0);");
	      return Response.ok("{\"Name\":\"" + name + "\", \"Balance\":\"0\"}").build();
	  }
	  
	  public static float getBalance(String name) throws TimeoutException {
	      System.out.println("Getting balance for " + name);

		  if(!isCustomerExist(name)){
		      return -1;
		  }
		  Connection c = null;
		    Statement stmt = null;
		    try {
		      Class.forName("org.sqlite.JDBC");
		      c = DriverManager.getConnection(dbConnectionName);
		      c.setAutoCommit(false);
		      System.out.println("Opened database successfully");

		      stmt = c.createStatement();
		      ResultSet rs = stmt.executeQuery( "SELECT * FROM CUSTOMERS WHERE name='"+name+"';" );
		      float balance = -1;
		      while ( rs.next() ) {
	    	 
		    	  balance = rs.getFloat("balance");
		      }
		      rs.close();
		      stmt.close();
		      c.close();
		      return  balance;
		    } catch ( Exception e ) {
		      System.err.println( e.getClass().getName() + ":" + e.getMessage() );
		      try{
		    	  c.close();
		      }catch(Exception ee){
		    	  
		      }finally{
		    	  throw new TimeoutException("Databasen hinner inte med");
		      }
		    }
	  }
	
	 public static Response setBalance(String name, float balance) throws TimeoutException{
		 if(!isCustomerExist(name)){
				return Response.status(Response.Status.NOT_FOUND).entity("Kunde inte hitta kund med namn:" + name).build();
			 }
		 
		 executeUpdate("UPDATE CUSTOMERS set balance = "+ balance + " where NAME = '"+name+"';");
		 return Response.ok("{\"Name\":\"" + name + "\", \"Balance\":\"" + balance + "\"}").build();
	 }
	 
	 public static Response getCustomers() throws TimeoutException
	 {
	      System.out.println("Getting all customers");
		  Connection c = null;
		    Statement stmt = null;
		    try {
		      Class.forName("org.sqlite.JDBC");
		      c = DriverManager.getConnection(dbConnectionName);
		      c.setAutoCommit(false);
		      System.out.println("Opened database successfully");

		      stmt = c.createStatement();
		      ResultSet rs = stmt.executeQuery( "SELECT * FROM CUSTOMERS order by id desc;" );
		      String customers = "{\"customers\":[";
		      while ( rs.next() ) {
		    	 String name = rs.getString("name");
		    	 float balance = rs.getFloat("balance");
		    	 customers += "{\"name\":\""+ name + "\", \"Balance\":\"" + balance + "\"},";
		      }
		      rs.close();
		      stmt.close();
		      c.close();
		      customers = (customers + "]}").replace("},]}","}]}");
		      return Response.ok(customers).build();
		    } catch ( Exception e ) {
		      System.err.println( e.getClass().getName() + ":" + e.getMessage() );
		      try{
		    	  c.close();
		      }catch(Exception ee){
		    	  
		      }finally{
		    	  throw new TimeoutException("Databasen hinner inte med");
		      }
		    }
	 }

	
}
