/**
 * 
 */
package mise.demonstrator.electrical_motor_control;

import mise.marssa.data_types.integer_datatypes.MInteger;
import mise.marssa.data_types.float_datatypes.MFloat;
import mise.marssa.data_types.float_datatypes.Percentage;
import mise.marssa.interfaces.control.electrical_motor.IMotorController;

/**
 * @author Clayton Tabone
 *
 */
public class MotorController implements IMotorController {

	private final MInteger  STEP_DELAY = new MInteger(50);
	private final MFloat STEP_SIZE = new MFloat(1.0f);
	//private final Float INITIAL_VALUE = new Float(50f);
	Ramping ramping;
	/**
	 * 
	 */
	public MotorController() {
		this.ramping = new Ramping(STEP_DELAY, STEP_SIZE, this);
		//this.ramping = new Ramping(STEP_DELAY, STEP_SIZE, this, INITIAL_VALUE);
	}

	/* (non-Javadoc)
	 * @see mise.marssa.interfaces.electrical_motor_control.IMotorController#outputMotorSpeed(mise.marssa.data_types.float_datatypes.speed.PercentSpeed)
	 */
	@Override
	public void outputValue(MFloat motorSpeed) {
		System.out.println(motorSpeed);
	}
	
	public void rampTo(Percentage desiredValue) throws InterruptedException {
		this.ramping.rampTo(desiredValue);
	}
}
