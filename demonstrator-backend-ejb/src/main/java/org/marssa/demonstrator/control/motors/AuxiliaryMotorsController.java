/**
 * Copyright 2012 MARSEC-XL International Limited
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.marssa.demonstrator.control.motors;

import org.marssa.demonstrator.constants.Constants;
import org.marssa.footprint.datatypes.MBoolean;
import org.marssa.footprint.datatypes.decimal.MDecimal;
import org.marssa.footprint.datatypes.integer.MInteger;
import org.marssa.footprint.exceptions.ConfigurationError;
import org.marssa.footprint.exceptions.NoConnection;
import org.marssa.footprint.exceptions.OutOfRange;
import org.marssa.footprint.interfaces.control.IController;
import org.marssa.footprint.interfaces.control.motor.IMotorController;
import org.marssa.services.control.Ramping;
import org.marssa.services.control.Ramping.RampingType;
import org.marssa.services.diagnostics.daq.LabJack;
import org.marssa.services.diagnostics.daq.LabJackU3;
import org.marssa.services.diagnostics.daq.LabJackU3.TimerConfigModeU3;

/**
 * @author Clayton Tabone
 * 
 */
public class AuxiliaryMotorsController implements IMotorController, IController {
	private final MInteger MOTOR_0_DIRECTION = LabJack.FIO6_ADDR;
	private final MInteger MOTOR_1_DIRECTION = LabJack.FIO7_ADDR;
	private final MInteger STEP_DELAY = new MInteger(20);
	private final MDecimal STEP_SIZE = new MDecimal(1.0f);
	private final LabJackU3 lj;
	private final Ramping ramping;

	/**
	 * @throws ConfigurationError
	 * @throws OutOfRange
	 * @throws NoConnection
	 * 
	 */
	public AuxiliaryMotorsController(LabJackU3 lj) throws ConfigurationError,
			OutOfRange, NoConnection {
		this.lj = lj;
		lj.setTimerMode(LabJackU3.TimerU3.TIMER_0,
				TimerConfigModeU3.PWM_OUTPUT_16BIT);
		lj.setTimerMode(LabJackU3.TimerU3.TIMER_1,
				TimerConfigModeU3.PWM_OUTPUT_16BIT);
		lj.setTimerBaseClock(LabJackU3.TimerBaseClockU3.CLOCK_4_MHZ_DIVISOR);
		lj.setTimerClockDivisor(new MInteger(2));
		lj.setTimerValue(LabJackU3.TimerU3.TIMER_0,
				new MInteger((int) Math.pow(2, 32) - 1));
		lj.setTimerValue(LabJackU3.TimerU3.TIMER_1,
				new MInteger((int) Math.pow(2, 32) - 1));
		this.ramping = new Ramping(STEP_DELAY, STEP_SIZE, this,
				RampingType.ACCELERATED);
	}

	@Override
	public void outputValue(MDecimal motorSpeed) throws ConfigurationError,
			OutOfRange, NoConnection {
		System.out.println(motorSpeed);
		MInteger actualValue = new MInteger((int) ((Math.pow(2, 32) - 1)
				* motorSpeed.abs().doubleValue() / 100.0));
		/*
		 * The Motor connected to TIMER_0 is slightly faster. Hence it is being
		 * scaled down to 90% of the requested value.
		 */
		lj.setTimerValue(LabJackU3.TimerU3.TIMER_0, new MInteger(
				(int) (actualValue.doubleValue() * 0.9)));
		lj.setTimerValue(LabJackU3.TimerU3.TIMER_1, actualValue);
	}

	@Override
	public void stop() {
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
	public MDecimal getValue() {
		return ramping.getCurrentValue();
	}

	public void rampTo(MDecimal desiredValue) throws InterruptedException,
			ConfigurationError, OutOfRange {
		ramping.rampTo(desiredValue);
	}

	@Override
	public void increase() throws InterruptedException, ConfigurationError,
			OutOfRange, NoConnection {
		double currentValue = this.ramping.getCurrentValue().doubleValue();
		if ((currentValue + Constants.MOTOR.STEP_SIZE.doubleValue()) > Constants.MOTOR.MAX_VALUE
				.doubleValue())
			this.rampTo(Constants.MOTOR.MAX_VALUE);
		else
			this.ramping.increase(Constants.MOTOR.STEP_SIZE);
	}

	@Override
	public void decrease() throws InterruptedException, ConfigurationError,
			OutOfRange, NoConnection {
		double currentValue = this.ramping.getCurrentValue().doubleValue();
		if ((currentValue - Constants.MOTOR.STEP_SIZE.doubleValue()) < Constants.MOTOR.MIN_VALUE
				.doubleValue())
			this.rampTo(Constants.MOTOR.MIN_VALUE);
		else
			this.ramping.decrease(Constants.MOTOR.STEP_SIZE);
	}

	@Override
	public MDecimal getSpeed() {
		return ramping.getCurrentValue();
	}

}