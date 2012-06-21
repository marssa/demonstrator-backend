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
import org.marssa.footprint.datatypes.integer.MInteger;
import org.marssa.footprint.exceptions.ConfigurationError;
import org.marssa.footprint.exceptions.NoConnection;
import org.marssa.footprint.exceptions.OutOfRange;
import org.marssa.footprint.interfaces.control.IController;
import org.marssa.services.control.Ramping;
import org.marssa.services.control.Ramping.RampingType;

public class TestController implements IController {
	private Ramping ramping;
	
	public TestController() {
		try {
			ramping = new Ramping(new MInteger(50), new MDecimal(1.0f), this, RampingType.ACCELERATED);
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
	
	public void rampTo(MDecimal desiredValue) throws InterruptedException, ConfigurationError, OutOfRange {
		ramping.rampTo(desiredValue);
	}
	
	public void increase(MDecimal incrementValue) throws InterruptedException, ConfigurationError, OutOfRange {
		try {
			ramping.increase(incrementValue);
		} catch (NoConnection e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void decrease(MDecimal decrementValue) throws InterruptedException, ConfigurationError, OutOfRange {
		try {
			ramping.decrease(decrementValue);
		} catch (NoConnection e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void outputValue(MDecimal value) throws ConfigurationError,
			OutOfRange, NoConnection {
		// TODO Auto-generated method stub
		System.out.println(value);
	}

	public void setPolaritySignal(Polarity polarity)
			throws NoConnection {
		// TODO Auto-generated method stub
		
	}

	public MDecimal getValue() {
		// TODO Auto-generated method stub
		return ramping.getCurrentValue();
	}
}
