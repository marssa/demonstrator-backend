/**
 * 
 */
package mise.marssa.demonstrator;

import org.restlet.Component;
import mise.marssa.footprint.exceptions.ConfigurationError;
import mise.marssa.footprint.exceptions.OutOfRange;
import mise.marssa.services.navigation.GpsReceiver;
import mise.marssa.services.diagnostics.daq.LabJack;
import mise.marssa.services.diagnostics.daq.LabJack.TimersEnabled;
import mise.marssa.apps.demonstrator.control.lighting.NavigationLightsController;
import mise.marssa.apps.demonstrator.control.lighting.UnderwaterLightsController;
import mise.marssa.apps.demonstrator.control.rudder.RudderController;
import mise.marssa.apps.demonstrator.web_services.WebServices;
import mise.marssa.apss.demonstrator.constants.Constants;
import mise.marssa.apss.demonstrator.control.electrical_motor.MotorController;
import org.apache.commons.daemon.*;

/**
 * @author Clayton Tabone
 *
 */
public class JSVC implements Daemon {
	LabJack labJack;
	NavigationLightsController navLightsController;
	UnderwaterLightsController underwaterLightsController;
	MotorController motorController;
	RudderController rudderController;
	GpsReceiver gpsReceiver;
	WebServices webServices;
	
	Component component = new Component();
	
	/**
	 * Open configuration files, create a trace file, create ServerSockets, Threads
	 * @param context
	 */
	@Override
	public void init(DaemonContext context) throws DaemonInitException, Exception {
		// Initialise LabJack
		try {
			System.out.print("Initialising LabJack ... ");
			labJack = LabJack.getInstance(Constants.LABJACK.HOST, Constants.LABJACK.PORT, TimersEnabled.TWO);
			System.out.println("success!");
		} catch (Exception e) {
			System.out.println("failure!");
			System.err.println("Cannot connect to " + Constants.LABJACK.HOST + ":" + Constants.LABJACK.PORT);
			e.printStackTrace();
			stop();
		}
		
		// Initialise Controllers and Receivers
		try {
			System.out.print("Initialising LabJack ... ");
			navLightsController = new NavigationLightsController(labJack);
			System.out.println("success!");
			
			System.out.print("Initialising lights controller ... ");
			underwaterLightsController = new UnderwaterLightsController(labJack);
			System.out.println("success!");
			
			System.out.print("Initialising motor controller ... ");
			motorController = new MotorController(labJack);
			System.out.println("success!");
			
			System.out.print("Initialising rudder controller ... ");
			rudderController = new RudderController(labJack);
			System.out.println("success!");
			
			System.out.print("Initialising GPS receiver ... ");
			gpsReceiver = new GpsReceiver(Constants.GPS.HOST, Constants.GPS.PORT);
			System.out.println("success!");
			
			System.out.print("Initialising web services ... ");
			webServices = new WebServices(navLightsController, underwaterLightsController, motorController, rudderController, gpsReceiver);
			System.out.println("success!");
			
			System.out.print("Starting restlet web servicves ... ");
			webServices.start();
			System.out.println("success!");
		} catch (ConfigurationError e) {
			e.printStackTrace();
			stop();
		} catch (OutOfRange e) {
			e.printStackTrace();
			stop();
		}
	}
	
	/**
	 * Start the Thread, accept incoming connections
	 */
	@Override
	public void start() throws Exception {
		System.out.print("Starting web services ... ");
		webServices.start();
		System.out.println("success!");
	}
	
	/**
	 * Inform the Thread to terminate the run(), close the ServerSockets
	 */
	@Override
	public void stop() throws Exception {
		try {
			System.out.print("Stopping web services ... ");
			webServices.stop();
			System.out.println("success!");
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Destroy any object created in init()
	 */
	@Override
	public void destroy() {
		labJack = null;
		navLightsController = null;
		underwaterLightsController = null;
		motorController = null;
		rudderController = null;
		gpsReceiver = null;
		webServices = null;
	}
}
