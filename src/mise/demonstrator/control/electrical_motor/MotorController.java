/**
 * 
 */
package mise.demonstrator.control.electrical_motor;

import mise.demonstrator.constants.Constants;
import mise.demonstrator.control.LabJack;
import mise.demonstrator.control.Ramping;
import mise.demonstrator.control.LabJack.TimerConfigMode;
import mise.demonstrator.control.LabJack.Timers;
import mise.demonstrator.control.Ramping.RampingType;
import mise.marssa.data_types.MBoolean;
import mise.marssa.data_types.integer_datatypes.MInteger;
import mise.marssa.data_types.integer_datatypes.MLong;
import mise.marssa.data_types.float_datatypes.MFloat;
import mise.marssa.exceptions.ConfigurationError;
import mise.marssa.exceptions.NoConnection;
import mise.marssa.exceptions.OutOfRange;
import mise.marssa.interfaces.control.electrical_motor.IMotorController;

/**
 * @author Clayton Tabone
 *
 */
public class MotorController implements IMotorController {

	private final MInteger MOTOR_0_DIRECTION = LabJack.FIO6_ADDR;
	private final MInteger MOTOR_1_DIRECTION = LabJack.FIO7_ADDR;
	private final MInteger STEP_DELAY = new MInteger(20);
	private final MFloat STEP_SIZE = new MFloat(1.0f);
	private LabJack lj;
	private Ramping ramping;
	private Thread rampingThread;
	private RampingTask rampingTask = null;
	
	private class RampingTask implements Runnable {
		private MotorController mc;
		MFloat desiredValue;
		
		public RampingTask(MotorController mc, MFloat desiredValue) {
			this.mc = mc;
			this.desiredValue = desiredValue;
		}
		
		// TODO These exceptions have to be properly handled
		@Override
		public void run() {
			try {
				synchronized(this) {
					if(desiredValue.getValue() > Constants.MOTOR.MAX_VALUE.getValue())
						this.mc.ramping.rampTo(Constants.MOTOR.MAX_VALUE);
					else if(desiredValue.getValue() < Constants.MOTOR.MIN_VALUE.getValue())
						this.mc.ramping.rampTo(Constants.MOTOR.MAX_VALUE);
					else
						this.mc.ramping.rampTo(desiredValue);
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ConfigurationError e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (OutOfRange e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoConnection e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * @throws ConfigurationError 
	 * @throws OutOfRange 
	 * @throws NoConnection 
	 * 
	 */
	public MotorController(LabJack lj) throws ConfigurationError, OutOfRange, NoConnection {
		this.lj = lj;
		lj.setTimerMode(Timers.TIMER_0, TimerConfigMode.PWM_OUTPUT_16BIT);
		lj.setTimerMode(Timers.TIMER_1, TimerConfigMode.PWM_OUTPUT_16BIT);
		lj.setTimerBaseClock(LabJack.TimerBaseClock.CLOCK_4_MHZ_DIVISOR);
		lj.setTimerClockDivisor(new MLong(2));
		lj.setTimerValue(Timers.TIMER_0, new MLong((long) Math.pow(2, 32) - 1));
		lj.setTimerValue(Timers.TIMER_1, new MLong((long) Math.pow(2, 32) - 1));
		this.ramping = new Ramping(STEP_DELAY, STEP_SIZE, this, RampingType.ACCELERATED );
	}

	/* (non-Javadoc)
	 * @see mise.marssa.interfaces.electrical_motor_control.IMotorController#outputMotorSpeed(mise.marssa.data_types.float_datatypes.speed.PercentSpeed)
	 */
	@Override
	public void outputValue(MFloat motorSpeed) throws ConfigurationError, OutOfRange, NoConnection {
		System.out.println(motorSpeed);
		MLong actualValue = new MLong((long) ((Math.pow(2, 32) - 1) * Math.abs(motorSpeed.getValue()) / 100.0));
		/*
		 * The Motor connected to TIMER_0 is slightly faster.
		 * Hence it is being scaled down to 90% of the requested value.
		 */
		lj.setTimerValue(LabJack.Timers.TIMER_0, new MLong((long) (actualValue.getValue() * 0.9)));
		lj.setTimerValue(LabJack.Timers.TIMER_1, actualValue);
	}
	
	@Override
	public void setPolaritySignal(Polarity polarity) throws NoConnection {
		switch (polarity) {
		case POSITIVE:
			lj.write(MOTOR_0_DIRECTION, new MBoolean(true));
			lj.write(MOTOR_1_DIRECTION, new MBoolean(true));
			break;
		case NEGATIVE:
			lj.write(MOTOR_0_DIRECTION, new MBoolean(false));
			lj.write(MOTOR_1_DIRECTION, new MBoolean(false));
			break;
		case OFF:
			// TODO handle the case to switch off the motors
			break;
		}
	}
	
	@Override
	public MFloat getValue() {
		return ramping.getCurrentValue();
	}
	
	public void rampTo(MFloat desiredValue) throws InterruptedException, ConfigurationError, OutOfRange {
	 
		if(this.rampingTask != null) {
			if(this.rampingThread.isAlive()) {
				this.rampingThread.interrupt();
				this.rampingThread.join();
			}
		}
		synchronized (this) {
			this.rampingTask = new RampingTask(this, desiredValue);
			this.rampingThread = new Thread(rampingTask);
			rampingThread.start();
		}
	}
	
	public void increase(MFloat incrementValue) throws InterruptedException, ConfigurationError, OutOfRange, NoConnection {
		float currentValue = this.ramping.getCurrentValue().getValue();
		if((currentValue + Constants.MOTOR.STEP_SIZE.getValue()) > Constants.MOTOR.MAX_VALUE.getValue())
			this.rampTo(Constants.MOTOR.MAX_VALUE);
		else
			this.ramping.increase(incrementValue);
	}
	
	public void decrease(MFloat decrementValue) throws InterruptedException, ConfigurationError, OutOfRange, NoConnection {
		float currentValue = this.ramping.getCurrentValue().getValue();
		if((currentValue - Constants.MOTOR.STEP_SIZE.getValue()) < Constants.MOTOR.MIN_VALUE.getValue())
			this.rampTo(Constants.MOTOR.MIN_VALUE);
		else
			this.ramping.decrease(decrementValue);
	}
}
