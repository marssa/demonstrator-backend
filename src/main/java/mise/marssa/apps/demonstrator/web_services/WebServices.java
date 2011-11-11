/**
 * 
 */
package mise.marssa.apps.demonstrator.web_services;

import java.util.ArrayList;
import org.restlet.Component;
import org.restlet.Server;
import org.restlet.data.CacheDirective;
import org.restlet.data.Protocol;
import org.restlet.resource.ServerResource;
import mise.marssa.apps.demonstrator.control.lighting.NavigationLightsController;
import mise.marssa.apps.demonstrator.control.lighting.UnderwaterLightsController;
import mise.marssa.apps.demonstrator.control.rudder.RudderController;
import mise.marssa.apps.demonstrator.navigation_equipment.GpsReceiver;
import mise.marssa.apps.demonstrator.web_service.rudder.RudderControllerApplication;
import mise.marssa.apps.demonstrator.web_services.GPS_Receiver.GPSReceiverApplication;
import mise.marssa.apps.demonstrator.web_services.lightControlPage.LightControlPageApplication;
import mise.marssa.apps.demonstrator.web_services.lighting.LightControllerApplication;
import mise.marssa.apps.demonstrator.web_services.motionControlPage.MotionControlPageApplication;
import mise.marssa.apps.demonstrator.web_services.motor.MotorControllerApplication;
import mise.marssa.apss.demonstrator.constants.Constants;
import mise.marssa.apss.demonstrator.control.electrical_motor.MotorController;

/**
 * @author Clayton Tabone
 *
 */
public class WebServices extends ServerResource {

	private final ArrayList<CacheDirective> cacheDirectives = new ArrayList<CacheDirective>();
	
	// Create a new Component
	private Component component = new Component();
	
	/**
	 * @throws Exception 
	 * 
	 */
	public WebServices(NavigationLightsController navLightsController, UnderwaterLightsController underwaterLightsController, MotorController motorController, RudderController rudderController, GpsReceiver gpsReceiver) throws Exception {
		// Set caching directives to noCache and noStore
		cacheDirectives.add(CacheDirective.noCache());
		cacheDirectives.add(CacheDirective.noStore());
		
	    // Add a new HTTP server listening on the given port
		Server server = component.getServers().add(Protocol.HTTP, Constants.WEB_SERVICES.HOST.getContents(), Constants.WEB_SERVICES.PORT.getValue());
		server.getContext().getParameters().add("maxTotalConnections", Constants.WEB_SERVICES.MAX_TOTAL_CONNECTIONS.toString());
	    
	    // Add new client connector for the FILE protocol
	    component.getClients().add(Protocol.FILE);
	    
	    // Attach the static file server application
	    component.getDefaultHost().attach("", new StaticFileServerApplication());

	    // Attach the light control application
	    component.getDefaultHost().attach("/lighting", new LightControllerApplication(cacheDirectives, navLightsController, underwaterLightsController));
	    
	    // Attach the motor control application
	    component.getDefaultHost().attach("/motor", new MotorControllerApplication(cacheDirectives, motorController));

	    // Attach the rudder control application
	    component.getDefaultHost().attach("/rudder", new RudderControllerApplication(cacheDirectives, rudderController));
	    
	    // Attach the GPS receiver application
	    component.getDefaultHost().attach("/gps", new GPSReceiverApplication(cacheDirectives, gpsReceiver));
	    
	    // Attach the motion control feedback application 
	    component.getDefaultHost().attach("/motionControlPage", new MotionControlPageApplication(cacheDirectives, motorController, rudderController));
	    
	    // Attach the motion control feedback application
	    component.getDefaultHost().attach("/lightControlPage", new LightControlPageApplication(cacheDirectives, navLightsController, underwaterLightsController));
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
