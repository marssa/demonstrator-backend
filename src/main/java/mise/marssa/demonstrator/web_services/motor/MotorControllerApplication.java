package mise.marssa.demonstrator.web_services.motor;

import java.util.ArrayList;

import mise.marssa.demonstrator.constants.Constants;
import mise.marssa.demonstrator.control.electrical_motor.MotorController;
import mise.marssa.footprint.datatypes.decimal.MDecimal;
import mise.marssa.footprint.exceptions.ConfigurationError;
import mise.marssa.footprint.exceptions.NoConnection;
import mise.marssa.footprint.exceptions.OutOfRange;

import org.restlet.Application;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.data.CacheDirective;
import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.routing.Router;

public class MotorControllerApplication extends Application {
	
	private ArrayList<CacheDirective> cacheDirectives;
	private MotorController motorController;
	
	public MotorControllerApplication(ArrayList<CacheDirective> cacheDirectives, MotorController motorController) {
		this.cacheDirectives = cacheDirectives;
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
        		response.setCacheDirectives(cacheDirectives);
        		try {
        			double value = Float.parseFloat(request.getAttributes().get("speed").toString());
        			motorController.rampTo(new MDecimal(value));
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
            }
        };
        
        // Create the increase motor speed control handler
        Restlet increaseSpeed = new Restlet() {
        	@Override
            public void handle(Request request, Response response) {
        		response.setCacheDirectives(cacheDirectives);
        		try {
        			motorController.increase(Constants.MOTOR.STEP_SIZE);
    				response.setEntity("Increasing motor speed by " + Constants.MOTOR.STEP_SIZE + "%", MediaType.TEXT_PLAIN);
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
				} catch (NoConnection e) {
					response.setStatus(Status.SERVER_ERROR_INTERNAL, "No connection error has been returned");
					e.printStackTrace();
				}
            }
        };
        
        // Create the decrease motor speed control handler
        Restlet decreaseSpeed = new Restlet() {
        	@Override
            public void handle(Request request, Response response) {
        		response.setCacheDirectives(cacheDirectives);
        		try {
					motorController.decrease(Constants.MOTOR.STEP_SIZE);
    				response.setEntity("Decreasing motor speed by " + Constants.MOTOR.STEP_SIZE + "%", MediaType.TEXT_PLAIN);
        		} catch (NumberFormatException e) {
        			response.setStatus(Status.CLIENT_ERROR_BAD_REQUEST, "The value of the speed resource has an incorrect format");
        		} catch (InterruptedException e) {
        			response.setStatus(Status.INFO_PROCESSING, "The ramping routinee has been interrupted");
        			e.printStackTrace();
				} catch (ConfigurationError e) {
					response.setStatus(Status.SERVER_ERROR_INTERNAL, "The request has returned a ConfigurationError");
					e.printStackTrace();
				} catch (OutOfRange e) {
					response.setStatus(Status.SERVER_ERROR_INTERNAL, "The specified value is out of range");
					e.printStackTrace();
				} catch (NoConnection e) {
					response.setStatus(Status.SERVER_ERROR_INTERNAL, "No connection error has been returned");
					e.printStackTrace();
				}
            }
        };
        
        // Create the motor speed monitoring handler
        Restlet speedMonitor = new Restlet() {
        	@Override
            public void handle(Request request, Response response) {
        		response.setCacheDirectives(cacheDirectives);
    			response.setEntity(motorController.getValue().toString(), MediaType.TEXT_PLAIN);
            }
        };
        
        router.attach("/speed/{speed}", speedControl);
        router.attach("/increaseSpeed", increaseSpeed);
        router.attach("/decreaseSpeed", decreaseSpeed);
        router.attach("/speed", speedMonitor);
        
        return router;
    }
}