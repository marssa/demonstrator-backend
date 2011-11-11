package mise.marssa.demonstrator.web_services.motionControlPage;

import java.util.ArrayList;

import mise.marssa.demonstrator.control.electrical_motor.MotorController;
import mise.marssa.demonstrator.control.rudder.RudderController;
import mise.marssa.footprint.data_types.float_datatypes.MFloat;
import mise.marssa.footprint.exceptions.NoConnection;
import org.restlet.Application;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.data.CacheDirective;
import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.routing.Router;

public class MotionControlPageApplication extends Application {
	
	private ArrayList<CacheDirective> cacheDirectives;
	private MotorController motorController = null;
	private RudderController rudderController = null;
	
	public MotionControlPageApplication(ArrayList<CacheDirective> cacheDirectives, MotorController motorController, RudderController rudderController) {
		this.cacheDirectives = cacheDirectives;
		this.motorController = motorController;
		this.rudderController = rudderController;
	}

    /**
     * Creates a root Restlet that will receive all incoming calls.
     */
    @Override
    public synchronized Restlet createInboundRoot() {
        Router router = new Router(getContext());
        
        // Create the navigation lights state handler
        Restlet rudderAndSpeedState = new Restlet() {
        	@Override
            public void handle(Request request, Response response) {
        		response.setCacheDirectives(cacheDirectives);
				try {
					MFloat motorSpeed = motorController.getValue();
					MFloat rudderAngle = rudderController.getAngle();
					response.setEntity("{\"motor\":" + motorSpeed.toJSON().getContents() + ",\"rudder\":" + rudderAngle.toJSON().getContents() + "}", MediaType.APPLICATION_JSON);
				} catch (NoConnection e) {
					response.setStatus(Status.SERVER_ERROR_INTERNAL, "No connection error has been returned");
					e.printStackTrace();
				}
    			
            }
        };
        
        router.attach("/rudderAndSpeed", rudderAndSpeedState);
        
        return router;
    }
}