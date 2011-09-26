package mise.demonstrator.web_service.lightControlPage;

import mise.demonstrator.control.electrical_motor.MotorController;
import mise.demonstrator.control.lighting.NavigationLightsController;
import mise.demonstrator.control.lighting.UnderwaterLightsController;
import mise.demonstrator.control.rudder.RudderController;
import mise.marssa.data_types.MBoolean;
import mise.marssa.data_types.float_datatypes.MFloat;
import mise.marssa.exceptions.NoConnection;

import org.restlet.Application;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.routing.Router;

public class LightControlPageApplication extends Application {
	
	NavigationLightsController navLightsController = null;
	UnderwaterLightsController underwaterLightsController = null;
	
	public LightControlPageApplication(NavigationLightsController navLightsController, UnderwaterLightsController underwaterLightsController) {
		this.navLightsController = navLightsController;
		this.underwaterLightsController = underwaterLightsController;
	}

    /**
     * Creates a root Restlet that will receive all incoming calls.
     */
    @Override
    public synchronized Restlet createInboundRoot() {
        Router router = new Router(getContext());
        
        // Create the navigation lights state handler
        Restlet lightState = new Restlet() {
        	@Override
            public void handle(Request request, Response response) {
				MBoolean navLightsState = navLightsController.getNavigationLightState();
				MBoolean underWaterLightsState = underwaterLightsController.getUnderwaterLightState();
				response.setEntity("{\"navLights\":\"" + navLightsState + "\",\"underwaterLights\":\"" + underWaterLightsState + "\"}", MediaType.APPLICATION_JSON);
            }
        };
        
        router.attach("/statusAll", lightState);
        
        return router;
    }
}