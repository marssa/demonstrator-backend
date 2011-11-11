package mise.marssa.demonstrator;

import org.restlet.resource.ServerResource;
import mise.marssa.apps.demonstrator.control.lighting.NavigationLightsController;
import mise.marssa.apps.demonstrator.control.lighting.UnderwaterLightsController;
import mise.marssa.apps.demonstrator.control.rudder.RudderController;
import mise.marssa.apps.demonstrator.navigation_equipment.GpsReceiver;
import mise.marssa.apps.demonstrator.web_services.WebServices;
import mise.marssa.apss.demonstrator.constants.Constants;
import mise.marssa.apss.demonstrator.control.LabJack;
import mise.marssa.apss.demonstrator.control.LabJack.TimersEnabled;
import mise.marssa.apss.demonstrator.control.electrical_motor.MotorController;
import mise.marssa.exceptions.ConfigurationError;
import mise.marssa.exceptions.OutOfRange;

/** @author Clayton Tabone
 *
 */
public class Main extends ServerResource {
		
	/**
	 * @param args the args 
	 */
	public static void main(java.lang.String[] args) {
		LabJack labJack = null;
		NavigationLightsController navLightsController;
		UnderwaterLightsController underwaterLightsController;
		MotorController motorController;
		RudderController rudderController;
		GpsReceiver gpsReceiver;
		WebServices webServices;
		
		// Initialise LabJack
		try {
			System.out.print("Initialising LabJack ... ");
			labJack = LabJack.getInstance(Constants.LABJACK.HOST, Constants.LABJACK.PORT, TimersEnabled.TWO);
			System.out.println("success!");
		} catch (Exception e) {
			System.out.println("failure!");
			System.err.println("Cannot connect to " + Constants.LABJACK.HOST + ":" + Constants.LABJACK.PORT);
			e.printStackTrace();
			System.exit(1);
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
			System.exit(1);
		} catch (OutOfRange e) {
			e.printStackTrace();
			System.exit(1);
		} catch (Exception e1) {
			e1.printStackTrace();
			System.exit(1);
		}
		/*
		// NavigationLights Tests
		System.out.println(navigationLights.getNavigationLightState());
		Percentage desiredValue = new Percentage(10f);
		
		// MotorControl Tests
		try {
			motorController.rampTo(desiredValue);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		// Rudder Tests
		try {
			rudderController.rotate(new MBoolean (true));
			rudderController.rotate(new MBoolean (false));
			rudderController.rotate(new MBoolean (true));
			rudderController.rotate(new MBoolean (false));
			rudderController.rotate(new MBoolean (false));
			rudderController.rotate(new MBoolean (true));
			rudderController.rotate(new MBoolean (true));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		// GPSReceiver Tests
		try {
			System.out.println("The GPS coordinates are " + gpsReceiver.getCoordinate());
			System.out.println("Altitude is " + gpsReceiver.getElevation());
			System.out.println("Course over ground " + gpsReceiver.getCOG());
			System.out.println("Speed over ground " + gpsReceiver.getSOG());
			//System.out.println("EPT " + gps.getEPT());      ///have to find the EPT
			System.out.println("Time " + gpsReceiver.getDate());
		} catch (NoConnection e) {
			e.printStackTrace();
		} catch (NoValue e) {
			e.printStackTrace();
		}
		*/
	}
}