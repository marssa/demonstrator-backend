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
import mise.demonstrator.control.lighting.UnderwaterLightsController;
import mise.demonstrator.control.rudder.RudderController;
import mise.demonstrator.navigation_equipment.GpsReceiver;
import mise.demonstrator.web_service.GPS_Receiver.GPSReceiverApplication;
import mise.demonstrator.web_service.lightControlPage.LightControlPageApplication;
import mise.demonstrator.web_service.lighting.LightControllerApplication;
import mise.demonstrator.web_service.motionControlPage.MotionControlPageApplication;
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
	public WebServices(NavigationLightsController navLightsController, UnderwaterLightsController underwaterLightsController, MotorController motorController, RudderController rudderController, GpsReceiver gpsReceiver) throws Exception {
	    // Add a new HTTP server listening on the given port
	    component.getServers().add(Protocol.HTTP, Constants.WEB_SERVICES.HOST.getContents(), Constants.WEB_SERVICES.PORT.getValue());
	    
	    // Add new client connector for the FILE protocol
	    component.getClients().add(Protocol.FILE);
	    
	    // Attach the static file server application
	    component.getDefaultHost().attach("", new StaticFileServerApplication());

	    // Attach the light control application
	    component.getDefaultHost().attach("/lighting", new LightControllerApplication(navLightsController, underwaterLightsController));
	    
	    // Attach the motor control application
	    component.getDefaultHost().attach("/motor", new MotorControllerApplication(motorController));

	    // Attach the rudder control application
	    component.getDefaultHost().attach("/rudder", new RudderControllerApplication(rudderController));
	    
	    // Attach the GPS receiver application
	    component.getDefaultHost().attach("/gps", new GPSReceiverApplication(gpsReceiver));
	    
	    // Attach the motion control feedback application 
	    component.getDefaultHost().attach("/motionControlPage", new MotionControlPageApplication(motorController, rudderController));
	    
	    // Attach the motion control feedback application
	    component.getDefaultHost().attach("/lightControlPage", new LightControlPageApplication(navLightsController, underwaterLightsController));
	}
	
	public void start() throws Exception {
		// Start the component
	    component.start();
	}

	public void stop() throws Throwable {
		finalize();
	}
	
	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		component.stop();
	}
}
