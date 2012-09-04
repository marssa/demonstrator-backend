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

import org.marssa.footprint.datatypes.MBoolean;
import org.marssa.footprint.datatypes.decimal.MDecimal;
import org.marssa.footprint.datatypes.integer.MInteger;
import org.marssa.footprint.exceptions.NoConnection;
import org.marssa.footprint.interfaces.control.rudder.IRudderController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RudderControllerTest implements IRudderController {
	private static final Logger logger = LoggerFactory
			.getLogger(RudderControllerTest.class.getName());

	private double rudderAngle = 0.0;

	/**
	 * The rotateMultiple is used to use the rotate method multiple times
	 */
	@Override
	public synchronized void rotateMultiple(MInteger multiple,
			MBoolean direction) throws InterruptedException, NoConnection {
		if (direction.getValue()) {
			logger.info("Rotating Rudder Right by {}", multiple);
			rudderAngle += multiple.intValue();
		} else {
			logger.info("Rotating Rudder Left by {}", multiple);
			rudderAngle -= multiple.intValue();
		}
		logger.info("New rudder angle: {}", rudderAngle);
	}

	@Override
	public void rotateToCentre() throws NoConnection, InterruptedException {
		logger.info("Rotating Rudder to the Centre");
		rudderAngle = 0;
	}

	/**
	 * The rotate is used to rotate the stepper motor by one step in either left
	 * or right direction The MBoolean direction false means that the rudder has
	 * negative angle (turns the boat to the left direction) The MBoolean
	 * direction true means that the rudder has positive angle (turns the boat
	 * to the right direction)
	 */
	@Override
	public void rotate(MBoolean direction) throws NoConnection,
			InterruptedException {
		if (direction.getValue()) {
			logger.info("Rotating Rudder Right");
			rudderAngle += 1;
		} else {
			logger.info("Rotating Rudder Left");
			rudderAngle -= 1;
		}
	}

	/**
	 * The getAngle returns the actual angle of the rudder
	 */
	@Override
	public MDecimal getAngle() throws NoConnection {
		return new MDecimal(rudderAngle);
	}

	@Override
	public void rotateExtreme(MBoolean direction) throws InterruptedException,
			NoConnection {
		if (direction.getValue()) {
			logger.info("Rotating Rudder to the extreme Right");
			rudderAngle += 30;
		} else {
			logger.info("Rotating Rudder to the extreme Left");
			rudderAngle -= 30;
		}
	}

}
