/**
 * 
 */
package mise.demonstrator.control.rudder;

import mise.marssa.data_types.float_datatypes.MFloat;
import mise.marssa.interfaces.control.rudder.IRudderAngle;

/**
 * @author Warren Zahra
 *
 */
public class RudderAngle implements IRudderAngle {

private double voltageDifference;
	/* (non-Javadoc)
	 * @see mise.marssa.control.rudder.IRudderAngle#angle()
	 */
	public RudderAngle()
	{
		
	}
	
	
	public MFloat getRudderAngle(MFloat voltage) {
		if (voltage.getValue() <  2.5){
			voltageDifference = voltage.getValue() - 2.5;
			
			System.out.println("The rudder is in the left position");
		}
		else if (voltage.getValue() > 2.5){
			System.out.println("The rudder is in the right position");
		}
		else if (voltage.getValue() == 0){
			System.out.println("The rudder is in the centre position");
		}
		return new MFloat(0);
	}
}
