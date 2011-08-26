package mise.demonstrator;

import mise.demonstrator.control.electrical_motor.MotorController;
import mise.demonstrator.light_controller.LightToggle;
import mise.demonstrator.control.rudder.Rudder;
import mise.marssa.data_types.float_datatypes.Percentage;
import mise.marssa.data_types.integer_datatypes.MInteger;
import mise.marssa.exceptions.OutOfRange;
import mise.marssa.data_types.MBoolean;
/**
 * @author Clayton Tabone
 *
 */
public class Main {
		
	/**
	 * @param args the args
	 * @throws OutOfRange 
	 * @throws InterruptedException 
	 */
	public static void main(java.lang.String[] args) throws OutOfRange, InterruptedException {
		/*
		MotorController mc = new MotorController();
		LightToggle navigationLights = new LightToggle();
		
		System.out.println(navigationLights.getLightState());
		Percentage desiredValue = new Percentage(10f);
		
		try {
			mc.rampTo(desiredValue);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		 Timestamp ts = new Timestamp(0);
		 System.out.println(ts);
		 
		 System.out.println(ts.getTime());
		 Date d=new Date();
		 System.out.println("Today date is "+ d);
		 */
		 
		MInteger delay = new MInteger(1000);
		 Rudder myRudder = new Rudder(delay);
		 myRudder.rotate(new MBoolean (true));
		 myRudder.rotate(new MBoolean (false));
		 myRudder.rotate(new MBoolean (true));
		 myRudder.rotate(new MBoolean (false));
		 myRudder.rotate(new MBoolean (false));
		 myRudder.rotate(new MBoolean (true));
		 myRudder.rotate(new MBoolean (true));
		 
		 /*
		 
		MString host = new MString("192.168.1.1");
		MInteger port = new MInteger(2947);
		
		GpsReceiver gps = new GpsReceiver(host, port);
		try {
			System.out.println("The GPS coordinates are " +gps.getCoordinate());
			System.out.println("Altitude is " +gps.getElevation());
			System.out.println("Course over ground "+gps.getCOG());
			System.out.println("Speed over ground "+gps.getSOG());
			//System.out.println("EPT "+gps.getEPT());      ///have to find the EPT
			System.out.println("Time "+gps.getDate());
		} catch (NoConnection e) {
			e.printStackTrace();
		} catch (NoValue e) {
			e.printStackTrace();
		}
	*/	
	}
	
}
