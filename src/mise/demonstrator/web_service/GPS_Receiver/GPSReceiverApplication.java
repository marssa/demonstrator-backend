package mise.demonstrator.web_service.GPS_Receiver;

import mise.demonstrator.control.lighting.NavigationLightsController;
import mise.demonstrator.control.lighting.UnderwaterLightsController;
import mise.demonstrator.navigation_equipment.GpsReceiver;
import mise.marssa.data_types.MBoolean;
import mise.marssa.data_types.float_datatypes.MFloat;
import mise.marssa.exceptions.NoConnection;
import mise.marssa.exceptions.NoValue;
import mise.marssa.exceptions.OutOfRange;

import org.restlet.Application;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.routing.Router;

public class GPSReceiverApplication extends Application {
	
	GpsReceiver gpsReceiver = null;
	
	public GPSReceiverApplication(GpsReceiver gpsReceiver) {
		this.gpsReceiver = gpsReceiver;
	}

    /**
     * Creates a root Restlet that will receive all incoming calls.
     */
    @Override
    public synchronized Restlet createInboundRoot() {
        Router router = new Router(getContext());
        
        // Create the navigation lights state handler
        Restlet coordinates = new Restlet() {
        	@Override
            public void handle(Request request, Response response) {
        		//TODO Handle parseException since parseBoolean doesn't check for and raise this exception
        		try {
					response.setEntity( gpsReceiver.getCoordinate().toJSON().getContents() , MediaType.APPLICATION_JSON);
				} catch (NoConnection e) {
					response.setStatus(Status.SERVER_ERROR_INTERNAL, "No connection error has been returned");
					e.printStackTrace();
				} catch (NoValue e) {
					response.setStatus(Status.CLIENT_ERROR_REQUESTED_RANGE_NOT_SATISFIABLE, "No connection error has been returned");
					e.printStackTrace();
				} catch (OutOfRange e) {
					response.setStatus(Status.CLIENT_ERROR_REQUESTED_RANGE_NOT_SATISFIABLE, "No connection error has been returned");
					e.printStackTrace();
				}
            }
        };
        
       
        router.attach("/coordinates/",coordinates);
        
        return router;
    }
}