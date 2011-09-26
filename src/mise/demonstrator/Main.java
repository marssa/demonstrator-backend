package mise.demonstrator;

import org.restlet.resource.ServerResource;
import mise.demonstrator.constants.Constants;
import mise.demonstrator.control.LabJack;
import mise.demonstrator.control.LabJack.TimerConfigMode;
import mise.demonstrator.control.LabJack.Timers;
import mise.demonstrator.control.LabJack.TimersEnabled;
import mise.demonstrator.control.electrical_motor.MotorController;
import mise.demonstrator.control.rudder.RudderController;
import mise.demonstrator.control.lighting.NavigationLightsController;
import mise.demonstrator.control.lighting.UnderwaterLightsController;
import mise.demonstrator.navigation_equipment.GpsReceiver;
import mise.demonstrator.web_service.WebServices;
import mise.marssa.data_types.integer_datatypes.MInteger;
import mise.marssa.data_types.integer_datatypes.MLong;
import mise.marssa.exceptions.ConfigurationError;
import mise.marssa.exceptions.OutOfRange;

/** @author Clayton Tabone
 *
 */
public class Main extends ServerResource {
		
	/**
	 * @param args the args
	 * @throws Exception 
	 */
	public static void main(java.lang.String[] args) {
		LabJack labJack = null;
		NavigationLightsController navLightsController;
		UnderwaterLightsController underwaterLightsController;
		MotorController motorController;
		RudderController rudderController;
		
		// Initialise LabJack
		try {
			labJack = LabJack.getInstance(Constants.LABJACK.HOST, Constants.LABJACK.PORT, TimersEnabled.TWO);
		} catch (Exception e) {
			System.err.println("Cannot connect to " + Constants.LABJACK.HOST + ":" + Constants.LABJACK.PORT);
			e.printStackTrace();
			System.exit(1);
		}
		
		// Initialise Controllers and Receivers
		try {
			navLightsController = new NavigationLightsController(labJack);
			underwaterLightsController = new UnderwaterLightsController(labJack);
			motorController = new MotorController(labJack);
			rudderController = new RudderController(labJack);
			// TODO attach physical GPSReceiver
			GpsReceiver gpsReceiver = null;
			//GpsReceiver gpsReceiver = new GpsReceiver(Constants.GPS.HOST, Constants.GPS.PORT);
			new WebServices(navLightsController, underwaterLightsController, motorController, rudderController, gpsReceiver);
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