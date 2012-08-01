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
package org.marssa.demonstrator.tests.control;

import org.marssa.footprint.datatypes.decimal.MDecimal;
import org.marssa.footprint.exceptions.ConfigurationError;
import org.marssa.footprint.exceptions.NoConnection;
import org.marssa.footprint.exceptions.OutOfRange;
import org.marssa.footprint.interfaces.control.motor.IMotorController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Clayton Tabone
 * 
 */
public class SternDriveMotorControllerTest implements IMotorController {

	private static final Logger logger = LoggerFactory
			.getLogger(SternDriveMotorControllerTest.class.getName());

	int ordinal;
	// ------------ - - - - - + + + + +
	int[] speed = { -16, -9, -8, -6, -4, 0, 4, 6, 8, 9, 16 };
	private int arrayValue;

	/**
	 * @throws ConfigurationError
	 * @throws OutOfRange
	 * @throws NoConnection
	 * 
	 */
	public SternDriveMotorControllerTest() {
		arrayValue = 5;
		System.out.print("Neutral");
	}

	private void labJackOutput(int speed) throws NoConnection {
		System.out.print("Current Speed" + speed);
		speed = Math.abs(speed);
		boolean m = false;
		int p = 0;

		// check polarity
		// modulus
		for (int f = 1; f <= 16; f = (f << 1)) {
			int r = speed & f;
			if (r > 0) {
				m = true;
			}
			System.out.print(6001 + p);
			System.out.println(m ? true : false);
			// lj.write(new MInteger(6000 + p), new MBoolean(m ? true : false));
			m = false;
			p++;
		}

	}

	@Override
	public MDecimal getSpeed() {
		int currentSpeed = 0;
		for (int y = 0; y < 11; y++) {
			if (speed[y] == speed[arrayValue])
				currentSpeed = y - 5;
		}
		return new MDecimal(currentSpeed);
	}

	@Override
	public void stop() throws NoConnection {
		labJackOutput(speed[5]);
	}

	@Override
	public void increase() throws InterruptedException, ConfigurationError,
			OutOfRange, NoConnection {
		if (arrayValue == 5) {
			logger.info("DPDT relay1----6006 false ------ increase");
			logger.info("DPDT relay2----6007 false ------ increase");
		}
		if (arrayValue == 10) {
			labJackOutput(speed[arrayValue]);
		} else {
			arrayValue++;
			labJackOutput(speed[arrayValue]);

		}
	}

	@Override
	public void decrease() throws InterruptedException, ConfigurationError,
			OutOfRange, NoConnection {
		if (arrayValue == 5) {
			logger.info("DPDT relay2----6006 true ------ decrease");
			logger.info("DPDT relay2----6007 true ------ decrease");
		}
		if (arrayValue == 0) {
			labJackOutput(speed[arrayValue]);
		} else {
			arrayValue--;
			labJackOutput(speed[arrayValue]);
		}
	}
}