package mise.demonstrator;

import mise.demonstrator.electrical_motor_control.MotorController;
import mise.marssa.data_types.String;
import mise.marssa.data_types.float_datatypes.Float;
import mise.marssa.data_types.integer_datatypes.Integer;
import mise.marssa.exceptions.NoConnection;
import mise.marssa.exceptions.NoValue;

/**
 * @author Clayton Tabone
 *
 */
public class Main {
		
	/**
	 * @param args the args
	 */
	public static void main(java.lang.String[] args) {
		
		MotorController mc = new MotorController();
		Float desiredValue = new Float(100f);
		try {
			mc.rampTo(desiredValue);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*String host = new String("dreamplug.mise");
		Integer port = new Integer(2947);
		GpsReceiver gps = new GpsReceiver(host, port);
		try {
			System.out.println(gps.getCoordinate());
			System.out.println(gps.getCOG());
			System.out.println(gps.getDate());
		} catch (NoConnection e) {
			e.printStackTrace();
		} catch (NoValue e) {
			e.printStackTrace();
		}*/
	}
}
