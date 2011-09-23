/**
 * 
 */
package mise.demonstrator.control.electrical_motor;

import mise.demonstrator.control.LabJack;
import mise.demonstrator.control.Ramping;
import mise.marssa.data_types.integer_datatypes.MInteger;
import mise.marssa.data_types.integer_datatypes.MLong;
import mise.marssa.data_types.float_datatypes.MFloat;
import mise.marssa.exceptions.ConfigurationError;
import mise.marssa.exceptions.OutOfRange;
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
	public void outputValue(MFloat motorSpeed) throws ConfigurationError, OutOfRange {
		System.out.println(motorSpeed);
		MLong actualValue = new MLong((long) (Math.pow(2, 32) * motorSpeed.getValue() / 100.0));
		lj.setTimerValue(LabJack.Timers.TIMER_0, actualValue);
	}
	
	public void rampTo(MFloat desiredValue) throws InterruptedException, ConfigurationError, OutOfRange {
		this.ramping.rampTo(desiredValue);
	}
}
