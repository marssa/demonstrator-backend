/**
 * 
 */
package mise.demonstrator.control.electrical_motor;

import mise.demonstrator.control.LabJack;
import mise.demonstrator.control.Ramping;
import mise.marssa.data_types.integer_datatypes.MInteger;
import mise.marssa.data_types.float_datatypes.MFloat;
import mise.marssa.interfaces.control.electrical_motor.IMotorController;

/**
 * @author Clayton Tabone
 *
 */
public class MotorController implements IMotorController {

	private final MInteger  STEP_DELAY = new MInteger(50);
	private final MFloat STEP_SIZE = new MFloat(1.0f);
	private LabJack lj;
	private Ramping ramping;
	
	/**
	 * 
	 */
	public MotorController(LabJack lj) {
		this.lj = lj;
		this.ramping = new Ramping(STEP_DELAY, STEP_SIZE, this);
	}

	/* (non-Javadoc)
	 * @see mise.marssa.interfaces.electrical_motor_control.IMotorController#outputMotorSpeed(mise.marssa.data_types.float_datatypes.speed.PercentSpeed)
	 */
	@Override
	public void outputValue(MFloat motorSpeed) {
		System.out.println(motorSpeed);
		
	}
	
	public void rampTo(MFloat desiredValue) throws InterruptedException {
		this.ramping.rampTo(desiredValue);
	}
}
