package se.jsta;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.io.IOException;
import java.net.URI;
import java.util.Map;

/**
 * Main class.
 *
 */
public class Main {


    /**
     * Main method.
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
    	String portString = (String) System.getProperty("port");
    	int portNumber = 8081;
    	try{
    		portNumber = Integer.valueOf(portString);
    	}catch(Exception e ){
    		
    	}
    	
    	String BASE_URI = "http://localhost:"+ portNumber +"/jstabanken-rest-api/";
         
        final ResourceConfig rc = new ResourceConfig().packages("se.jsta");

        // create and start a new instance of grizzly http server
        // exposing the Jersey application at BASE_URI
        final HttpServer server = GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
        System.out.println(String.format("Jersey app started with WADL available at "
                + "%sapplication.wadl\nHit enter to stop it...", BASE_URI));
        System.in.read();
        server.stop();
    }
}

