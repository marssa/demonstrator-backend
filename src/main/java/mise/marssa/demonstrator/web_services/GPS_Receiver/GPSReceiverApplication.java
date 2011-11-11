package mise.marssa.demonstrator.web_services.GPS_Receiver;

import java.util.ArrayList;

import mise.marssa.services.navigation.GpsReceiver;
import mise.marssa.footprint.exceptions.NoConnection;
import mise.marssa.footprint.exceptions.NoValue;
import mise.marssa.footprint.exceptions.OutOfRange;
import org.restlet.Application;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.data.CacheDirective;
import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.routing.Router;

public class GPSReceiverApplication extends Application {
	
	private ArrayList<CacheDirective> cacheDirectives;
	private GpsReceiver gpsReceiver;
	
	public GPSReceiverApplication(ArrayList<CacheDirective> cacheDirectives, GpsReceiver gpsReceiver) {
		this.cacheDirectives = cacheDirectives;
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
        		response.setCacheDirectives(cacheDirectives);
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
        
       
        router.attach("/coordinates", coordinates);
        
        return router;
    }
}