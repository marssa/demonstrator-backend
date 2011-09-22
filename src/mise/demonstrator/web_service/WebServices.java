/**
 * 
 */
package mise.demonstrator.web_service;

import org.restlet.Component;
import org.restlet.data.Protocol;
import org.restlet.resource.ServerResource;
import mise.demonstrator.constants.Constants;
import mise.demonstrator.control.electrical_motor.MotorController;
import mise.demonstrator.control.lighting.NavigationLightsController;
import mise.demonstrator.control.rudder.RudderController;
import mise.demonstrator.navigation_equipment.GpsReceiver;
import mise.demonstrator.web_service.lighting.NavigationLightsControllerApplication;
import mise.demonstrator.web_service.motor.MotorControllerApplication;
import mise.demonstrator.web_service.rudder.RudderControllerApplication;

/**
 * @author Clayton Tabone
 *
 */
public class WebServices extends ServerResource {

	// Create a new Component
	private Component component = new Component();
	
	/**
	 * @throws Exception 
	 * 
	 */
	public WebServices(NavigationLightsController navLightsController, MotorController motorController, RudderController rudderController, GpsReceiver gpsReceiver) throws Exception {
		/*
		Restlet restlet = new Restlet() {
            @Override
            public void handle(Request request, Response response) {
                response.setEntity("Hello World!", MediaType.TEXT_PLAIN);
            }
        };

        // Create the HTTP server and listen on port 8182
        new Server(Protocol.HTTP, 8182, restlet).start();
        */
		
		///*
	    // Add a new HTTP server listening on the given port
	    component.getServers().add(Protocol.HTTP, Constants.WEB_SERVICES.PORT.getValue());

	    // Attach the navigation lights control application
	    component.getDefaultHost().attach("/lighting", new NavigationLightsControllerApplication(navLightsController));
	    
	    // Attach the motor control application
	    component.getDefaultHost().attach("/motor", new MotorControllerApplication(motorController));

	    // Attach the rudder control application
	    component.getDefaultHost().attach("/rudder", new RudderControllerApplication(rudderController));
	    
	    // Start the component
	    component.start();
	    //*/
	}
}