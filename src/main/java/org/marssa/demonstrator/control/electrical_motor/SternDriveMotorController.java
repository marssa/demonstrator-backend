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
package org.marssa.demonstrator.control.electrical_motor;

import org.marssa.footprint.datatypes.MBoolean;
import org.marssa.footprint.datatypes.decimal.MDecimal;
import org.marssa.footprint.datatypes.integer.MInteger;
import org.marssa.footprint.exceptions.ConfigurationError;
import org.marssa.footprint.exceptions.NoConnection;
import org.marssa.footprint.exceptions.OutOfRange;
import org.marssa.footprint.interfaces.control.motor.IMotorController;
import org.marssa.services.control.Ramping;
import org.marssa.services.control.Ramping.RampingType;
import org.marssa.services.diagnostics.daq.LabJack;
import org.marssa.services.diagnostics.daq.LabJackUE9;

/**
 * @author Clayton Tabone
 * 
 */
public class SternDriveMotorController implements IMotorController {
	private final MInteger MOTOR_0_DIRECTION = LabJack.FIO6_ADDR;
	private final MInteger MOTOR_1_DIRECTION = LabJack.FIO7_ADDR;
	private final MInteger STEP_DELAY = new MInteger(20);
	private final MDecimal STEP_SIZE = new MDecimal(20.0f);
	private LabJackUE9 lj;
	private Ramping ramping;

	public static enum MotorSpeed {
		FORWARD_5(100), FORWARD_4(80), FORWARD_3(60), FORWARD_2(40), FORWARD_1(
				20), OFF(0), REVERSE_1(-20), REVERSE_2(-40), REVERSE_3(-60), REVERSE_4(
				-80), REVERSE_5(-100);

		private MotorSpeed(int value) {
		}
		
		public int getValue() {
			return this.ordinal();
		}
	}

	/**
	 * @throws ConfigurationError
	 * @throws OutOfRange
	 * @throws NoConnection
	 * 
	 */
	public SternDriveMotorController(LabJackUE9 lj) throws ConfigurationError,
			OutOfRange, NoConnection {
		this.lj = lj;
		// TODO set MotorSpeed to OFF (0 0 0 0 0)
		this.ramping = new Ramping(STEP_DELAY, STEP_SIZE, this,
				RampingType.ACCELERATED);
	}

	public void outputValue(MDecimal motorSpeed) throws ConfigurationError,
			OutOfRange, NoConnection {
		System.out.println(motorSpeed);
		/*
		// R Y W RY RW
		switch (motorSpeed.intValue()) {
		// 1 0 0 0 0
		case FORWARD_5:
			BitString switches = new BitString(5);
			switches.clearAll();
			switches.set(0); // Set the first bit
			setSpeedCoil(switches);
			break;
		// 0 1 0 0 1
		case FORWARD_4:

			break;
		// 0 1 0 0 0
		case FORWARD_3:

			break;
		// 0 0 1 1 0
		case FORWARD_2:

			break;
		// 0 0 1 0 0
		case FORWARD_1:

			break;
		// 0 0 0 0 0
		case OFF:

			break;
		case REVERSE_1:

			break;
		case REVERSE_2:

			break;
		case REVERSE_3:

			break;
		case REVERSE_4:

			break;
		case REVERSE_5:

			break;
		}
		*/
	}
	
	private void setSpeedCoil(/* net.sf.javabdd.BitString.BitString switches */) {
		// TODO See http://javabdd.sourceforge.net/apidocs/net/sf/javabdd/BitString.html
		// iterate over BitString and set registers
	}

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

	public MDecimal getValue() {
		return ramping.getCurrentValue();
	}

	public void rampTo(MotorSpeed desiredValue) throws InterruptedException,
			ConfigurationError, OutOfRange {
		ramping.rampTo(new MDecimal(desiredValue.getValue()));
	}

	public void increase() throws InterruptedException,
			ConfigurationError, OutOfRange, NoConnection {
		// Check if we are already at max forward speed
		if (this.ramping.getCurrentValue().intValue() != MotorSpeed.FORWARD_5.getValue())
			this.ramping.increase(STEP_SIZE);
	}

	public void decrease() throws InterruptedException,
			ConfigurationError, OutOfRange, NoConnection {
		// Check if we are already at max reverse speed
		if (this.ramping.getCurrentValue().intValue() != MotorSpeed.REVERSE_5.getValue())
			this.ramping.decrease(STEP_SIZE);
	}
}