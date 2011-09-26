package mise.demonstrator.web_service.motor;

import mise.demonstrator.control.electrical_motor.MotorController;
import mise.marssa.data_types.float_datatypes.MFloat;
import mise.marssa.exceptions.ConfigurationError;
import mise.marssa.exceptions.OutOfRange;

import org.restlet.Application;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.routing.Router;

public class MotorControllerApplication extends Application {
	
	MotorController motorController = null;
	
	public MotorControllerApplication(MotorController motorController) {
		this.motorController = motorController;
	}

    /**
     * Creates a root Restlet that will receive all incoming calls.
     */
    @Override
    public synchronized Restlet createInboundRoot() {
        Router router = new Router(getContext());
        
        // Create the motor speed control handler
        Restlet speedControl = new Restlet() {
        	@Override
            public void handle(Request request, Response response) {
        		try {
        			float value = Float.parseFloat(request.getAttributes().get("speed").toString());
        			motorController.rampTo(new MFloat(value));
        			response.setEntity("Ramping motor speed to " + value + "%", MediaType.TEXT_PLAIN);
        		} catch (NumberFormatException e) {
        			response.setStatus(Status.CLIENT_ERROR_BAD_REQUEST, "The value of the speed resource has an incorrect format");
        		} catch (InterruptedException e) {
        			response.setStatus(Status.INFO_PROCESSING, "The ramping algorithm has been interrupted");
        			e.printStackTrace();
				} catch (ConfigurationError e) {
					response.setStatus(Status.SERVER_ERROR_INTERNAL, "The request has returned a ConfigurationError");
					e.printStackTrace();
				} catch (OutOfRange e) {
					response.setStatus(Status.SERVER_ERROR_INTERNAL, "The specified value is out of range");
					e.printStackTrace();
				}
        		/*
        		String message = "Resource URI  : "
                        + request.getResourceRef() + '\n'
                        + "Root URI      : " + request.getRootRef()
                        + '\n' + "Routed part   : "
                        + request.getResourceRef().getBaseRef() + '\n'
                        + "Remaining part: "
                        + request.getResourceRef().getRemainingPart();
        		*/
            }
        };
        
     // Create the motor speed monitoring handler
        Restlet speedMonitor = new Restlet() {
        	@Override
            public void handle(Request request, Response response) {
    			response.setEntity(motorController.getValue().toString(), MediaType.TEXT_PLAIN);
            }
        };
        
        router.attach("/speed/{speed}", speedControl);
        router.attach("/speed", speedMonitor);
        
        return router;
    }
}