package mise.demonstrator.web_service.rudder;

import mise.demonstrator.control.rudder.RudderController;
import mise.marssa.data_types.MBoolean;
import org.restlet.Application;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.data.MediaType;
import org.restlet.routing.Router;

public class RudderControllerApplication extends Application {
	
	RudderController rudderController = null;
	
	public RudderControllerApplication(RudderController rudderController) {
		this.rudderController = rudderController;
	}

    /**
     * Creates a root Restlet that will receive all incoming calls.
     */
    @Override
    public synchronized Restlet createInboundRoot() {
        Router router = new Router(getContext());
        
        // Create the rotation handler
        Restlet rotate = new Restlet() {
        	@Override
            public void handle(Request request, Response response) {
        		try {
        			//TODO Handle parseException since parseBoolean doesn't check for and raise this exception
        			boolean direction = Boolean.parseBoolean(request.getAttributes().get("direction").toString());
        			rudderController.rotate(new MBoolean(direction));
        			response.setEntity("Rotating the rudder in the direction set by direction = " + direction, MediaType.TEXT_PLAIN);
        		} catch (InterruptedException e) {
        			// TODO Auto-generated catch block
        			e.printStackTrace();
				}
            }
        };
        
        router.attach("/rotate/{direction}", rotate);
        
        return router;
    }
}