/**
 * 
 */
package mise.demonstrator.control;

import mise.marssa.interfaces.control.IController;
import mise.marssa.interfaces.control.IRamping;
import mise.marssa.data_types.MBoolean;
import mise.marssa.data_types.integer_datatypes.MInteger;
import mise.marssa.data_types.float_datatypes.MFloat;
import mise.marssa.exceptions.ConfigurationError;
import mise.marssa.exceptions.OutOfRange;

/**
 * @author Clayton Tabone
 *
 */
public class Ramping implements IRamping {
	int stepDelay;
	float currentValue, stepSize;
	IController controller;
	
	// true means positive ramping
	boolean direction = false;
	
	public Ramping(MInteger stepDelay, MFloat stepSize, IController controller) throws ConfigurationError, OutOfRange {
		this.stepDelay = stepDelay.getValue();
		this.stepSize = stepSize.getValue();
		this.controller = controller;
		this.currentValue = 0;
		controller.outputValue(new MFloat(this.currentValue));
	}
	
	public Ramping(MInteger stepDelay, MFloat stepSize, IController controller, MFloat initialValue) throws ConfigurationError, OutOfRange {
		this.stepDelay = stepDelay.getValue();
		this.stepSize = stepSize.getValue();
		this.controller = controller;
		this.currentValue = initialValue.getValue();
		controller.outputValue(new MFloat(this.currentValue));
	}
	
	/* (non-Javadoc)
	 * @see mise.marssa.interfaces.electrical_motor_control.IRamping#rampTo(mise.marssa.data_types.float_datatypes.MFloat)
	 */
	@Override
	public void rampTo(MFloat desiredValue) throws InterruptedException, ConfigurationError, OutOfRange {
		float difference = desiredValue.getValue() - currentValue;
		direction = (difference > 0);
		while(true) {
			if(difference == 0) {
				// Do nothing. The desired value is the same as the current value.
			} else if(direction) {
				if(currentValue == stepSize)
					controller.setPolaritySignal(IController.Polarity.POSITIVE);
				currentValue += stepSize;
			} else {
				if(currentValue == -stepSize)
					controller.setPolaritySignal(IController.Polarity.NEGATIVE);
				currentValue -= stepSize;
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
	public void increase(MFloat incrementValue) throws InterruptedException, ConfigurationError, OutOfRange {
		rampTo(new MFloat(currentValue + incrementValue.getValue()));
	}
	
	/* (non-Javadoc)
	 * @see mise.marssa.interfaces.electrical_motor_control.IRamping#decrease(mise.marssa.data_types.float_datatypes.MFloat)
	 */
	@Override
	public void decrease(MFloat decrementValue) throws InterruptedException, ConfigurationError, OutOfRange {
		rampTo(new MFloat(currentValue - decrementValue.getValue()));
	}

	@Override
	public MFloat getCurrentValue() {
		return new MFloat(currentValue);
	}
}
