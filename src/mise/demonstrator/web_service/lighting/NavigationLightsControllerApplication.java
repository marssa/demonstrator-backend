package mise.demonstrator.web_service.lighting;

import mise.demonstrator.control.lighting.NavigationLightsController;
import mise.marssa.data_types.MBoolean;
import org.restlet.Application;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.data.MediaType;
import org.restlet.routing.Router;

public class NavigationLightsControllerApplication extends Application {
	
	NavigationLightsController navLightsController = null;
	
	public NavigationLightsControllerApplication(NavigationLightsController navLightsController) {
		this.navLightsController = navLightsController;
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
        
        router.attach("/navigationLights/{state}", navLights);
        router.attach("/navigationLights", navLightsState);
        
        return router;
    }
}