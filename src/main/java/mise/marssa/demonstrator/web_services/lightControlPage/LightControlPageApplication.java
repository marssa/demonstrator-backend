package mise.marssa.demonstrator.web_services.lightControlPage;

import java.util.ArrayList;

import mise.marssa.demonstrator.control.lighting.NavigationLightsController;
import mise.marssa.demonstrator.control.lighting.UnderwaterLightsController;
import mise.marssa.footprint.data_types.MBoolean;
import org.restlet.Application;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.data.CacheDirective;
import org.restlet.data.MediaType;
import org.restlet.routing.Router;

public class LightControlPageApplication extends Application {
	
	private ArrayList<CacheDirective> cacheDirectives;
	private NavigationLightsController navLightsController;
	private UnderwaterLightsController underwaterLightsController;
	
	public LightControlPageApplication(ArrayList<CacheDirective> cacheDirectives, NavigationLightsController navLightsController, UnderwaterLightsController underwaterLightsController) {
		this.cacheDirectives = cacheDirectives;
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
        		response.setCacheDirectives(cacheDirectives);
				MBoolean navLightsState = navLightsController.getNavigationLightState();
				MBoolean underWaterLightsState = underwaterLightsController.getUnderwaterLightState();
				response.setEntity("{\"navLights\":" + navLightsState.toJSON().getContents() + ",\"underwaterLights\":" + underWaterLightsState.toJSON().getContents() + "}", MediaType.APPLICATION_JSON);
            }
        };
        
        router.attach("/statusAll", lightState);
        
        return router;
    }
}