/**
 * 
 */
package mise.demonstrator.electrical_motor_control;

import mise.marssa.interfaces.control.IController;
import mise.marssa.interfaces.control.electrical_motor.IRamping;
import mise.marssa.data_types.integer_datatypes.Integer;
import mise.marssa.data_types.float_datatypes.Float;

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
	
	public Ramping(Integer stepDelay, Float stepSize, IController controller) {
		this.stepDelay = stepDelay.getValue();
		this.stepSize = stepSize.getValue();
		this.controller = controller;
		this.currentValue = 0;
	}
	
	public Ramping(Integer stepDelay, Float stepSize, IController controller, Float initialValue) {
		this.stepDelay = stepDelay.getValue();
		this.stepSize = stepSize.getValue();
		this.controller = controller;
		this.currentValue = initialValue.getValue();
	}
	
	/* (non-Javadoc)
	 * @see mise.marssa.interfaces.electrical_motor_control.IRamping#rampTo(mise.marssa.data_types.float_datatypes.Percentage)
	 */
	@Override
	public void rampTo(Float desiredValue) throws InterruptedException {
		float difference = desiredValue.getValue() - currentValue;
		direction = (difference > 0);
		while(true) {
			if(direction) {
				currentValue += stepSize;
			} else {
				currentValue -= stepSize;
			}
			controller.outputValue(new Float(currentValue));
            Thread.sleep(stepDelay);
            if(direction && (currentValue >= desiredValue.getValue())) {
            	break;
            } else if(!direction && (currentValue <= desiredValue.getValue())) {
            	break;
            }
        }
	}

}
