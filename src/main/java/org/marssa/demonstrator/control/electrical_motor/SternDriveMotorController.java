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

import org.marssa.demonstrator.JSVC;
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
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;

/**
 * @author Clayton Tabone
 * 
 */
public class SternDriveMotorController implements IMotorController{
	
	private LabJackUE9 lj;
	int ordinal;
	//------------  -   -  -  -  -   + + + +  +
	int[] speed = {-16,-9,-8,-6,-4,0,4,6,8,9,16};
	private int arrayValue;
	private static final Logger logger = (Logger) LoggerFactory
			.getLogger(JSVC.class);

	/**
	 * @throws ConfigurationError
	 * @throws OutOfRange
	 * @throws NoConnection
	 * 
	 */
	public SternDriveMotorController(LabJackUE9 lj) throws ConfigurationError,
			OutOfRange, NoConnection {
		this.lj = lj;
		arrayValue=5;
		labJackOutput(speed[arrayValue]);
	}

	private void labJackOutput(int speed) throws NoConnection {
		logger.info("Current Speed "+speed);
		speed = Math.abs(speed);
		boolean m = false;
		int p = 0;
		for (int f = 1; f <= 16; f = (f << 1)) {
			int r = speed&f;
			if (r > 0) {
				m = true;
			}
			logger.info("Port: "+(6008 + p)+" "+(m ? true : false));
			lj.write(new MInteger(6008 + p), new MBoolean(m ? true : false));
			m = false;
			p++;
		}

	}

@Override
	public void stop() throws NoConnection{
		labJackOutput(speed[5]);
	}

	public MDecimal getValue(){
		int speedValue = 0;
		boolean rotation;
		if (speed[arrayValue]>0)
			rotation = true;
		else 
			rotation=false;
		
		switch(Math.abs(speed[arrayValue])){
		case 0:
			speedValue = 0;
		case 4:
			speedValue = 1;
		case 6:
			speedValue = 2;
		case 8:
			speedValue = 3;
		case 9:
			speedValue = 4;
		case 16:
			speedValue = 5;
		break;
		}
		if (rotation ==true)
		return new MDecimal(speedValue);
		else
			return new MDecimal(-speedValue);
	}
	public void increase() throws InterruptedException, ConfigurationError,
			OutOfRange, NoConnection {
		if (arrayValue == 5){
			lj.write(new MInteger(6000), new MBoolean (false));
			logger.info("DPDT relay1----60013 " + " false ------ increase");
			Thread.sleep(1000);
		}
		if(arrayValue == 10){
			labJackOutput(speed[arrayValue]);
		}else{
			arrayValue++;
		labJackOutput(speed[arrayValue]);
		
		}
	}

	public void decrease() throws InterruptedException, ConfigurationError,
			OutOfRange, NoConnection {
		if (arrayValue == 5){
			lj.write(new MInteger(6000), new MBoolean (false));
			logger.info("DPDT relay2----6013 " + " true ------ decrease");
			Thread.sleep(1000);
		}
		if(arrayValue == 0){
			labJackOutput(speed[arrayValue]);
		}else{
			arrayValue--;
		labJackOutput(speed[arrayValue]);
		}
	}

	@Override
	public void outputValue(MDecimal value) throws ConfigurationError,
			OutOfRange, NoConnection {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPolaritySignal(Polarity polarity) throws NoConnection {
		// TODO Auto-generated method stub
		
	}
}