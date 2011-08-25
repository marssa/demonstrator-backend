/**
 * 
 */
package mise.demonstrator.rudder_control;

import mise.marssa.data_types.float_datatypes.MFloat;

import mise.marssa.rudder_control.IRudderAngle;

/**
 * @author Warren Zahra
 *
 */
public class RudderAngle implements IRudderAngle {

private double voltageDifference;
	/* (non-Javadoc)
	 * @see mise.marssa.rudder_control.IRudderAngle#angle()
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
		// TODO Auto-generated method stub
		return new MFloat(0);
	}


	

	

}
