/**
 * 
 */
package mise.demonstrator;

import mise.demonstrator.constants.Constants;
import mise.demonstrator.control.LabJack;
import mise.demonstrator.control.LabJack.TimersEnabled;
import mise.demonstrator.control.electrical_motor.MotorController;
import mise.demonstrator.control.lighting.NavigationLightsController;
import mise.demonstrator.control.lighting.UnderwaterLightsController;
import mise.demonstrator.control.rudder.RudderController;
import mise.demonstrator.navigation_equipment.GpsReceiver;
import mise.demonstrator.web_service.WebServices;
import mise.marssa.exceptions.ConfigurationError;
import mise.marssa.exceptions.OutOfRange;

/**
 * @author Clayton Tabone
 *
 */
public class JSVC {
	LabJack labJack;
	NavigationLightsController navLightsController;
	UnderwaterLightsController underwaterLightsController;
	MotorController motorController;
	RudderController rudderController;
	GpsReceiver gpsReceiver;
	WebServices webServices;
	
	/**
	 * Open configuration files, create a trace file, create ServerSockets, Threads
	 * @param arguments
	 */
	public void init(String[] arguments) {
		// Initialise LabJack
		try {
			labJack = LabJack.getInstance(Constants.LABJACK.HOST, Constants.LABJACK.PORT, TimersEnabled.TWO);
		} catch (Exception e) {
			System.err.println("Cannot connect to " + Constants.LABJACK.HOST + ":" + Constants.LABJACK.PORT);
			e.printStackTrace();
			stop();
		}
		
		// Initialise Controllers and Receivers
		try {
			navLightsController = new NavigationLightsController(labJack);
			underwaterLightsController = new UnderwaterLightsController(labJack);
			motorController = new MotorController(labJack);
			rudderController = new RudderController(labJack);
			gpsReceiver = new GpsReceiver(Constants.GPS.HOST, Constants.GPS.PORT);
			webServices = new WebServices(navLightsController, underwaterLightsController, motorController, rudderController, gpsReceiver);
		} catch (ConfigurationError e) {
			e.printStackTrace();
			stop();
		} catch (OutOfRange e) {
			e.printStackTrace();
			stop();
		} catch (Exception e1) {
			e1.printStackTrace();
			stop();
		}
	}

	/**
	 * Start the Thread, accept incoming connections
	 */
	void start() {
		try {
			webServices.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Inform the Thread to terminate the run(), close the ServerSockets
	 */
	void stop() {
		try {
			webServices.stop();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Destroy any object created in init()
	 */
	void destroy() {
		labJack = null;
		navLightsController = null;
		underwaterLightsController = null;
		motorController = null;
		rudderController = null;
		gpsReceiver = null;
		webServices = null;
	}
}
