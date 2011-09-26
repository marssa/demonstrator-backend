package mise.demonstrator.web_service.lighting;

import mise.demonstrator.control.lighting.NavigationLightsController;
import mise.demonstrator.control.lighting.UnderwaterLightsController;
import mise.marssa.data_types.MBoolean;
import org.restlet.Application;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.data.MediaType;
import org.restlet.routing.Router;

public class LightControllerApplication extends Application {
	
	NavigationLightsController navLightsController = null;
	UnderwaterLightsController underwaterLightsController = null;
	
	public LightControllerApplication(NavigationLightsController navLightsController, UnderwaterLightsController underwaterLightsController) {
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
        Restlet navLights = new Restlet() {
        	@Override
            public void handle(Request request, Response response) {
        		//TODO Handle parseException since parseBoolean doesn't check for and raise this exception
    			boolean state = Boolean.parseBoolean(request.getAttributes().get("state").toString());
    			navLightsController.setNavigationLightState(new MBoolean(state));
    			response.setEntity("Setting navigation lights state to " + (state ? "on" : "off"), MediaType.TEXT_PLAIN);
            }
        };
        
        // Create the navigation lights state handler
        Restlet navLightsState = new Restlet() {
        	@Override
            public void handle(Request request, Response response) {
        		//TODO Handle parseException since parseBoolean doesn't check for and raise this exception
    			response.setEntity(navLightsController.getNavigationLightState().toString(), MediaType.TEXT_PLAIN);
            }
        };
        
        // Create the underwater lights state handler
        Restlet underwaterLights = new Restlet() {
        	@Override
            public void handle(Request request, Response response) {
        		//TODO Handle parseException since parseBoolean doesn't check for and raise this exception
    			boolean state = Boolean.parseBoolean(request.getAttributes().get("state").toString());
    			underwaterLightsController.setUnderwaterLightState(new MBoolean(state));
    			response.setEntity("Setting navigation lights state to " + (state ? "on" : "off"), MediaType.TEXT_PLAIN);
            }
        };
        
        // Create the underwater lights state handler
        Restlet underwaterLightsState = new Restlet() {
        	@Override
            public void handle(Request request, Response response) {
        		//TODO Handle parseException since parseBoolean doesn't check for and raise this exception
    			response.setEntity(underwaterLightsController.getUnderwaterLightState().toString(), MediaType.TEXT_PLAIN);
            }
        };
        
        router.attach("/navigationLights/{state}", navLights);
        router.attach("/navigationLights", navLightsState);
        router.attach("/underwaterLights/{state}", underwaterLights);
        router.attach("/underwaterLights", underwaterLightsState);
        
        return router;
    }
}