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

import java.util.ArrayList;
import java.util.List;

import org.marssa.footprint.datatypes.MBoolean;
import org.marssa.footprint.datatypes.decimal.MDecimal;
import org.marssa.footprint.datatypes.integer.MInteger;
import org.marssa.footprint.exceptions.ConfigurationError;
import org.marssa.footprint.exceptions.NoConnection;
import org.marssa.footprint.exceptions.OutOfRange;
import org.marssa.footprint.interfaces.control.motor.IMotorController;
import org.marssa.services.diagnostics.daq.LabJack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Clayton Tabone
 * 
 */
public class SternDriveMotorController implements IMotorController {

	private static final Logger logger = LoggerFactory
			.getLogger(SternDriveMotorController.class);
	// ------------ - - - - - + + + + +
	private static final int[] speed = { -16, -9, -8, -6, -4, 0, 4, 6, 8, 9, 16 };

	private final LabJack lj;
	private final ArrayList<MInteger> ports = new ArrayList<MInteger>();
	private int arrayValue;

	/**
	 * @throws ConfigurationError
	 * @throws OutOfRange
	 * @throws NoConnection
	 * 
	 */
	public SternDriveMotorController(LabJack lj, List<MInteger> ports)
			throws ConfigurationError, OutOfRange, NoConnection {
		this.lj = lj;
		this.ports.addAll(ports);
		arrayValue = 5;
		labJackOutput(speed[arrayValue]);
	}

	private void labJackOutput(int speed) throws NoConnection {
		logger.info("Get speed {}", getSpeed());
		speed = Math.abs(speed);
		boolean m = false;
		int p = 0;
		for (int f = 1; f <= 16; f = (f << 1)) {
			int r = speed & f;
			if (r > 0) {
				m = true;
			}
			logger.info("Port: " + ports.get(p) + " " + m);
			lj.write(ports.get(p), new MBoolean(m));
			m = false;
			p++;
		}

	}

	@Override
	public void stop() throws NoConnection {
		labJackOutput(speed[5]);
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
	public void increase() throws InterruptedException, ConfigurationError,
			OutOfRange, NoConnection {
		if (arrayValue == 5) {
			lj.write(ports.get(ports.size() - 1), new MBoolean(false));
			logger.info("DPDT relay1----" + ports.get(ports.size() - 1)
					+ " false ------ increase");
			Thread.sleep(1000);
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
			lj.write(ports.get(ports.size() - 1), new MBoolean(true));
			logger.info("DPDT relay2----" + ports.get(ports.size() - 1)
					+ " true ------ decrease");
			Thread.sleep(1000);
		}
		if (arrayValue == 0) {
			labJackOutput(speed[arrayValue]);
		} else {
			arrayValue--;
			labJackOutput(speed[arrayValue]);
		}
	}
}