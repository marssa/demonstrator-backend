/**
 * 
 */
package mise.demonstrator.electrical_motor_control;

import mise.marssa.data_types.integer_datatypes.Integer;
import mise.marssa.data_types.float_datatypes.Float;
import mise.marssa.interfaces.control.electrical_motor.IMotorController;

/**
 * @author Clayton Tabone
 *
 */
public class MotorController implements IMotorController {

	private final Integer  STEP_DELAY = new Integer(50);
	private final Float STEP_SIZE = new Float(1.0f);
	Ramping ramping;
	/**
	 * 
	 */
	public MotorController() {
		this.ramping = new Ramping(STEP_DELAY, STEP_SIZE, this);
	}

	/* (non-Javadoc)
	 * @see mise.marssa.interfaces.electrical_motor_control.IMotorController#outputMotorSpeed(mise.marssa.data_types.float_datatypes.speed.PercentSpeed)
	 */
	@Override
	public void outputValue(Float motorSpeed) {
		System.out.println(motorSpeed);
	}
	
	public void rampTo(Float desiredValue) throws InterruptedException {
		this.ramping.rampTo(desiredValue);
	}
}
