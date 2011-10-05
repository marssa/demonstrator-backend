/**
 * 
 */
package mise.demonstrator.control;

import mise.marssa.interfaces.control.IController;
import mise.marssa.interfaces.control.IRamping;
import mise.marssa.data_types.integer_datatypes.MInteger;
import mise.marssa.data_types.float_datatypes.MFloat;
import mise.marssa.exceptions.ConfigurationError;
import mise.marssa.exceptions.NoConnection;
import mise.marssa.exceptions.OutOfRange;

/**
 * @author Clayton Tabone
 *
 */
public class Ramping implements IRamping {
	int stepDelay;
	float currentValue, stepSize;
	private IController controller;
	private RampingType rampType;
	// true means positive ramping
	boolean direction = false;
	private IController.Polarity polarity;
	
	public enum RampingType{
		DEFAULT(0),
		ACCELERATED(1);
		
		private RampingType(int rampingType) { }
	};
	
	public Ramping(MInteger stepDelay, MFloat stepSize, IController controller, RampingType rampType) throws ConfigurationError, OutOfRange, NoConnection {
		this.stepDelay = stepDelay.getValue();
		this.stepSize = stepSize.getValue();
		this.controller = controller;
		this.currentValue = 0;
		this.rampType  = rampType;
		controller.outputValue(new MFloat(this.currentValue));
	}
	
	public Ramping(MInteger stepDelay, MFloat stepSize, IController controller, MFloat initialValue, RampingType rampType) throws ConfigurationError, OutOfRange, NoConnection {
		this.stepDelay = stepDelay.getValue();
		this.stepSize = stepSize.getValue();
		this.controller = controller;
		this.currentValue = initialValue.getValue();
		this.rampType  = rampType;
		controller.outputValue(new MFloat(this.currentValue));
	}
	
	/* (non-Javadoc)
	 * @see mise.marssa.interfaces.electrical_motor_control.IRamping#rampTo(mise.marssa.data_types.float_datatypes.MFloat)
	 */
	@Override
	public void rampTo(MFloat desiredValue) throws InterruptedException, ConfigurationError, OutOfRange, NoConnection {
		float difference = desiredValue.getValue() - currentValue;
		
		direction = (difference > 0);
		while(true) {
			
			if(difference == 0) {break;
				// Do nothing. The desired value is the same as the current value.
			}
						
			if(direction) {
				if(currentValue == stepSize) {
					this.polarity = IController.Polarity.POSITIVE;
					controller.setPolaritySignal(this.polarity);
				}
				currentValue += stepSize;
			}
			else {
				if(currentValue == -stepSize) {
					this.polarity = IController.Polarity.NEGATIVE;
					controller.setPolaritySignal(this.polarity);
				 }
				currentValue -= stepSize;
			}
			
			if (rampType == RampingType.ACCELERATED){
				
				if(this.polarity  == IController.Polarity.POSITIVE && direction == false){
					if (desiredValue.getValue() > 0)
						currentValue = desiredValue.getValue();
					else if (desiredValue.getValue() <0)
						currentValue = -1;				
					else
					currentValue=0;
					
				}
				else if(this.polarity  == IController.Polarity.NEGATIVE && direction == true){
					if (desiredValue.getValue() > 0)
						currentValue = 1;
					else if (desiredValue.getValue() <0)
						currentValue = desiredValue.getValue(); 
					else
						currentValue=0;
					
					 }
				
	            }
			
			
			controller.outputValue(new MFloat(currentValue));
            Thread.sleep(stepDelay);
            if((currentValue == desiredValue.getValue())) {
            	break;
            }
        }
	}
	
	/* (non-Javadoc)
	 * @see mise.marssa.interfaces.electrical_motor_control.IRamping#increase(mise.marssa.data_types.float_datatypes.MFloat)
	 */
	@Override
	public void increase(MFloat incrementValue) throws InterruptedException, ConfigurationError, OutOfRange, NoConnection {
		rampTo(new MFloat(currentValue + incrementValue.getValue()));
	}
	
	/* (non-Javadoc)
	 * @see mise.marssa.interfaces.electrical_motor_control.IRamping#decrease(mise.marssa.data_types.float_datatypes.MFloat)
	 */
	@Override
	public void decrease(MFloat decrementValue) throws InterruptedException, ConfigurationError, OutOfRange, NoConnection {
		rampTo(new MFloat(currentValue - decrementValue.getValue()));
	}

	@Override
	public MFloat getCurrentValue() {
		return new MFloat(currentValue);
	}
}
