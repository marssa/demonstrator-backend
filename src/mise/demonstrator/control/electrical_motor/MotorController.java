/**
 * 
 */
package mise.demonstrator.control.electrical_motor;

import mise.demonstrator.control.LabJack;
import mise.demonstrator.control.Ramping;
import mise.demonstrator.control.LabJack.TimerConfigMode;
import mise.demonstrator.control.LabJack.Timers;
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
	 * @throws ConfigurationError 
	 * @throws OutOfRange 
	 * 
	 */
	public MotorController(LabJack lj) throws ConfigurationError, OutOfRange {
		this.lj = lj;
		lj.setTimerMode(Timers.TIMER_0, TimerConfigMode.PWM_OUTPUT_16BIT);
		lj.setTimerMode(Timers.TIMER_1, TimerConfigMode.PWM_OUTPUT_16BIT);
		lj.setTimerBaseClock(LabJack.TimerBaseClock.CLOCK_4_MHZ_DIVISOR);
		lj.setTimerClockDivisor(new MLong(2));
		lj.setTimerValue(Timers.TIMER_0, new MLong((long) Math.pow(2, 32) - 1));
		lj.setTimerValue(Timers.TIMER_1, new MLong((long) Math.pow(2, 32) - 1));
		this.ramping = new Ramping(STEP_DELAY, STEP_SIZE, this);
	}

	/* (non-Javadoc)
	 * @see mise.marssa.interfaces.electrical_motor_control.IMotorController#outputMotorSpeed(mise.marssa.data_types.float_datatypes.speed.PercentSpeed)
	 */
	@Override
	public void outputValue(MFloat motorSpeed) throws ConfigurationError, OutOfRange {
		System.out.println(motorSpeed);
		MLong actualValue = new MLong((long) ((Math.pow(2, 32) - 1) * Math.abs(motorSpeed.getValue()) / 100.0));
		lj.setTimerValue(LabJack.Timers.TIMER_0, actualValue);
	}
	
	@Override
	public MFloat getValue() {
		return ramping.getCurrentValue();
	}
	
	public void rampTo(MFloat desiredValue) throws InterruptedException, ConfigurationError, OutOfRange {
		this.ramping.rampTo(desiredValue);
	}
}
