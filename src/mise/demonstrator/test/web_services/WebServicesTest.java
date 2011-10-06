package mise.demonstrator.test.web_services;

import org.restlet.Application;
import org.restlet.Component;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.data.MediaType;
import org.restlet.data.Protocol;
import org.restlet.data.Status;
import org.restlet.routing.Router;
import mise.demonstrator.constants.Constants;
import mise.demonstrator.control.Ramping;
import mise.marssa.data_types.float_datatypes.MFloat;
import mise.marssa.data_types.integer_datatypes.MInteger;
import mise.marssa.exceptions.ConfigurationError;
import mise.marssa.exceptions.NoConnection;
import mise.marssa.exceptions.OutOfRange;
import mise.marssa.interfaces.control.IController;

public class WebServicesTest {
	private static class WebServicesTestApplication extends Application {
		String value = "";
	    /**
	     * Creates a root Restlet that will receive all incoming calls.
	     */
	    @Override
	    public synchronized Restlet createInboundRoot() {
	        Router router = new Router(getContext());
	        
	        // Create the echo handler
	        Restlet echo = new Restlet() {
	        	@Override
	            public void handle(Request request, Response response) {
	        		String value = request.getAttributes().get("value").toString();
        			setValue(value);
        			response.setEntity("You entered the following parameter:\n" + value, MediaType.TEXT_PLAIN);
	            }
	        };
	        
	        // Create the get state handler
	        Restlet state = new Restlet() {
	        	@Override
	            public void handle(Request request, Response response) {
	    			response.setEntity("The state of the last parameter you have entered is:\n", MediaType.TEXT_PLAIN);
	            }
	        };
	        
	        router.attach("/navigationLights", state);
	        router.attach("/navigationLights/{value}", echo);
	        
	        return router;
	    }
	    
	    public void setValue(String newValue) {
			this.value = newValue;
		}
	}
	
	/**
	 * @param args the args 
	 */
	public static void main(java.lang.String[] args) {
		// Create a new Component
		Component component = new Component();
		System.out.println("Starting Web Services on " + Constants.WEB_SERVICES.HOST.getContents() + ":" + Constants.WEB_SERVICES.PORT.toString() + " ...");
		
	    // Add a new HTTP server listening on the given port
	    component.getServers().add(Protocol.HTTP, Constants.WEB_SERVICES.HOST.getContents(), Constants.WEB_SERVICES.PORT.getValue());

		// Attach the motion control feedback application
	    component.getDefaultHost().attach("/lighting", new WebServicesTestApplication());
	    
	    // Start the component
	    try {
			component.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
