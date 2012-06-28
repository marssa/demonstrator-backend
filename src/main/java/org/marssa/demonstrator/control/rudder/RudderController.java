package org.marssa.demonstrator.control.rudder;


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

import java.io.IOException;

import org.marssa.footprint.datatypes.MBoolean;
import org.marssa.footprint.datatypes.decimal.MDecimal;
import org.marssa.footprint.datatypes.integer.MInteger;
import org.marssa.footprint.exceptions.NoConnection;
import org.marssa.footprint.interfaces.control.rudder.IRudderController;
import org.marssa.services.diagnostics.daq.LabJackUE9;





/**
 * @author Warren Zahra
 * 
 */
public class RudderController implements IRudderController {

	private final MInteger STEPPER1 = new MInteger(6000);
	private final MInteger STEPPER2 = new MInteger(6001);
	private final MInteger STEPPER3 = new MInteger(6002);
	private final MInteger STEPPER4 = new MInteger(6003);
	private final MBoolean HIGH = new MBoolean(true);
	private final MBoolean LOW = new MBoolean(false);

	private int stepRight = 0;
	private int stepLeft = 0;
	private double voltageDifference = 0;
	private double angleDifference = 0;
	private static MDecimal angle = new MDecimal(0);

	private LabJackUE9 lj;

	public RudderController(LabJackUE9 lj) throws NoConnection,
			InterruptedException {
		this.lj = lj;
		rotate(new MBoolean(false));
		rotate(new MBoolean(true));
	}

	/**
	 * The rotateMultiple is used to use the rotate method multiple times
	 */
	public synchronized void rotateMultiple(MInteger multiple,
			MBoolean direction) throws InterruptedException, NoConnection {
		for (int x = 0; x < multiple.intValue(); x++) {
			if (angle.doubleValue() > 30 && direction.getValue() == true) {
				break;
			} else if (angle.doubleValue() < -30
					&& direction.getValue() == false) {
				break;
			}
			rotate(direction);
		}

	}

	/**
	 * The rotateExtreme is used to rotate to the extreme possible angles
	 */
	public synchronized void rotateExtreme(MBoolean direction)
			throws InterruptedException, NoConnection {

		while (angle.doubleValue() < 32 && direction.getValue() == true) {
			rotate(direction);
		}

		while (angle.doubleValue() > -32 && direction.getValue() == false) {
			rotate(direction);
		}

	}

	/**
	 * The rotateToCentre is used to rotate the rudder to approximate its centre
	 * position
	 */
	public void rotateToCentre() throws NoConnection, InterruptedException {
		while (angle.doubleValue() > 5) {
			rotate(new MBoolean(false));
		}
		while (angle.doubleValue() < -5) {
			rotate(new MBoolean(true));
		}
	}

	/**
	 * The rotate is used to rotate the stepper motor by one step in either left
	 * or right direction The MBoolean direction false means that the rudder has
	 * negative angle (turns the boat to the left direction) The MBoolean
	 * direction true means that the rudder has positive angle (turns the boat
	 * to the right direction)
	 */
	public void rotate(MBoolean direction) throws NoConnection,
			InterruptedException {
		if ((stepLeft == 0 && direction.getValue())
				|| (stepRight == 3 && direction.getValue() == false)) {
			lj.write(STEPPER1, HIGH);
			lj.write(STEPPER2, HIGH);
			lj.write(STEPPER3, LOW);
			lj.write(STEPPER4, LOW);
			stepLeft = 1;
			stepRight = 0;
			Thread.sleep(50);
			return;
		}
		if ((stepLeft == 1 && direction.getValue())
				|| (stepRight == 2 && direction.getValue() == false)) {
			lj.write(STEPPER1, LOW);
			lj.write(STEPPER2, HIGH);
			lj.write(STEPPER3, HIGH);
			lj.write(STEPPER4, LOW);
			stepLeft = 2;
			stepRight = 3;
			Thread.sleep(50);
			return;
		}
		if ((stepLeft == 2 && direction.getValue())
				|| (stepRight == 1 && direction.getValue() == false)) {
			lj.write(STEPPER1, LOW);
			lj.write(STEPPER2, LOW);
			lj.write(STEPPER3, HIGH);
			lj.write(STEPPER4, HIGH);
			stepLeft = 3;
			stepRight = 2;
			Thread.sleep(50);
			return;
		}
		if ((stepLeft == 3 && direction.getValue())
				|| (stepRight == 0 && direction.getValue() == false)) {
			lj.write(STEPPER1, HIGH);
			lj.write(STEPPER2, LOW);
			lj.write(STEPPER3, LOW);
			lj.write(STEPPER4, HIGH);
			stepLeft = 0;
			stepRight = 1;
			Thread.sleep(50);
			return;
		}
	}

	/**
	 * The getAngle returns the actual angle of the rudder
	 */
	public MDecimal getAngle() throws NoConnection {
		try {
			double voltageValue = lj.read(new MInteger(0), new MInteger(8),
					new MInteger(1)).doubleValue(); // value that needs to be
													// read from the labjack
			if (voltageValue < 2.45) {
				voltageDifference = 2.45 - voltageValue;
				angleDifference = voltageDifference * 57.14;
				angle = new MDecimal(angleDifference);
			}
			if (voltageValue > 2.45) {
				voltageDifference = voltageValue - 2.45;
				angleDifference = voltageDifference * 57.14;
				angle = new MDecimal(-angleDifference);
			}
			if (voltageValue == 2.45) {
				angle = new MDecimal(0);
			}
		} catch (IOException e) {
			throw new NoConnection("Cannot read from LabJack\n"
					+ e.getMessage(), e.getCause());
		}
		return angle;
	}

	
}
///**
// * Copyright 2012 MARSEC-XL International Limited
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *     http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//package org.marssa.demonstrator.control.rudder;
//
//import java.io.IOException;
//
//import org.marssa.demonstrator.constants.Constants;
//import org.marssa.footprint.datatypes.MBoolean;
//import org.marssa.footprint.datatypes.decimal.MDecimal;
//import org.marssa.footprint.datatypes.integer.MInteger;
//import org.marssa.footprint.exceptions.NoConnection;
//import org.marssa.footprint.interfaces.control.rudder.IRudderController;
//import org.marssa.services.diagnostics.daq.LabJack;
//
//
///**
// * @author Warren Zahra
// * 
// */
//public class RudderController implements IRudderController {
//
//	private final MInteger STEPPER1 = LabJack.FIO8_ADDR;
//	private final MInteger STEPPER2 = LabJack.FIO9_ADDR;
//	private final MInteger STEPPER3 = LabJack.FIO10_ADDR;
//	private final MInteger STEPPER4 = LabJack.FIO11_ADDR;
//	private final MBoolean HIGH = new MBoolean(true);
//	private final MBoolean LOW = new MBoolean(false);
//
//	private int stepRight = 0;
//	private int stepLeft = 0;
//	private double voltageDifference = 0;
//	private double angleDifference = 0;
//	private static MDecimal angle;
//
//	private LabJack lj;
//
//	public RudderController(LabJack lj) throws NoConnection,
//			InterruptedException {
//		this.lj = lj;
//		rotate(new MBoolean(false));
//		rotate(new MBoolean(true));
//	}
//
//	/**
//	 * The rotateMultiple is used to use the rotate method multiple times
//	 */
//	public synchronized void rotateMultiple(MInteger multiple,
//			MBoolean direction) throws InterruptedException, NoConnection {
//		for (int x = 0; x < multiple.intValue(); x++) {
//			if (angle.doubleValue() > 30 && direction.getValue() == true) {
//				break;
//			} else if (angle.doubleValue() < -30
//					&& direction.getValue() == false) {
//				break;
//			}
//			rotate(direction);
//		}
//
//	}
//
//	/**
//	 * The rotateExtreme is used to rotate to the extreme possible angles
//	 */
//	public synchronized void rotateExtreme(MBoolean direction)
//			throws InterruptedException, NoConnection {
//
//		while (angle.doubleValue() < 32 && direction.getValue() == true) {
//			rotate(direction);
//		}
//
//		while (angle.doubleValue() > -32 && direction.getValue() == false) {
//			rotate(direction);
//		}
//
//	}
//
//	/**
//	 * The rotateToCentre is used to rotate the rudder to approximate its centre
//	 * position
//	 */
//	public void rotateToCentre() throws NoConnection, InterruptedException {
//		while (angle.doubleValue() > 5) {
//			rotate(new MBoolean(false));
//		}
//		while (angle.doubleValue() < -5) {
//			rotate(new MBoolean(true));
//		}
//	}
//
//	/**
//	 * The rotate is used to rotate the stepper motor by one step in either left
//	 * or right direction The MBoolean direction false means that the rudder has
//	 * negative angle (turns the boat to the left direction) The MBoolean
//	 * direction true means that the rudder has positive angle (turns the boat
//	 * to the right direction)
//	 */
//	public void rotate(MBoolean direction) throws NoConnection,
//			InterruptedException {
//		if ((stepLeft == 0 && direction.getValue())
//				|| (stepRight == 3 && direction.getValue() == false)) {
//			lj.write(STEPPER1, HIGH);
//			lj.write(STEPPER2, HIGH);
//			lj.write(STEPPER3, LOW);
//			lj.write(STEPPER4, LOW);
//			stepLeft = 1;
//			stepRight = 0;
//			Thread.sleep(Constants.RUDDER.RUDDER_DELAY.intValue());
//			return;
//		}
//		if ((stepLeft == 1 && direction.getValue())
//				|| (stepRight == 2 && direction.getValue() == false)) {
//			lj.write(STEPPER1, LOW);
//			lj.write(STEPPER2, HIGH);
//			lj.write(STEPPER3, HIGH);
//			lj.write(STEPPER4, LOW);
//			stepLeft = 2;
//			stepRight = 3;
//			Thread.sleep(Constants.RUDDER.RUDDER_DELAY.intValue());
//			return;
//		}
//		if ((stepLeft == 2 && direction.getValue())
//				|| (stepRight == 1 && direction.getValue() == false)) {
//			lj.write(STEPPER1, LOW);
//			lj.write(STEPPER2, LOW);
//			lj.write(STEPPER3, HIGH);
//			lj.write(STEPPER4, HIGH);
//			stepLeft = 3;
//			stepRight = 2;
//			Thread.sleep(Constants.RUDDER.RUDDER_DELAY.intValue());
//			return;
//		}
//		if ((stepLeft == 3 && direction.getValue())
//				|| (stepRight == 0 && direction.getValue() == false)) {
//			lj.write(STEPPER1, HIGH);
//			lj.write(STEPPER2, LOW);
//			lj.write(STEPPER3, LOW);
//			lj.write(STEPPER4, HIGH);
//			stepLeft = 0;
//			stepRight = 1;
//			Thread.sleep(Constants.RUDDER.RUDDER_DELAY.intValue());
//			return;
//		}
//	}
//
//	/**
//	 * The getAngle returns the actual angle of the rudder
//	 */
//	public MDecimal getAngle() throws NoConnection {
//		try {
//			double voltageValue = lj.read(new MInteger(0), new MInteger(8),
//					new MInteger(1)).doubleValue(); // value that needs to be
//													// read from the labjack
//			if (voltageValue < 2.45) {
//				voltageDifference = 2.45 - voltageValue;
//				angleDifference = voltageDifference * 57.14;
//				angle = new MDecimal(angleDifference);
//			}
//			if (voltageValue > 2.45) {
//				voltageDifference = voltageValue - 2.45;
//				angleDifference = voltageDifference * 57.14;
//				angle = new MDecimal(-angleDifference);
//			}
//			if (voltageValue == 2.45) {
//				angle = new MDecimal(0);
//			}
//		} catch (IOException e) {
//			throw new NoConnection("Cannot read from LabJack\n"
//					+ e.getMessage(), e.getCause());
//		}
//		return angle;
//	}
//}
