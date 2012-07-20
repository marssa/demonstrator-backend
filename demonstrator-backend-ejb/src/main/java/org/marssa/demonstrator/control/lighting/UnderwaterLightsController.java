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
package org.marssa.demonstrator.control.lighting;

import java.net.UnknownHostException;

import org.marssa.footprint.datatypes.MBoolean;
import org.marssa.footprint.datatypes.integer.MInteger;
import org.marssa.footprint.exceptions.NoConnection;
import org.marssa.footprint.interfaces.control.lighting.ILightToggle;
import org.marssa.footprint.logger.MMarker;
import org.marssa.services.diagnostics.daq.LabJack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Warren Zahra
 * 
 */
public class UnderwaterLightsController implements ILightToggle {

	static Logger logger = LoggerFactory
			.getLogger("UnderwaterLightsController");

	private boolean lightState;
	private LabJack lj;
	private MInteger underLights;
	private String address = "";

	/**
	 * 
	 * @param lj
	 * @param underLights
	 * @throws UnknownHostException
	 * @throws NoConnection
	 */
	public UnderwaterLightsController(LabJack lj, MInteger underLights)
			throws UnknownHostException, NoConnection {
		address = lj.getHost().getContents() + ":" + lj.getPort().intValue();
		logger.info(
				"An instance of underwater lights controller was instantiated with LabJack connected at {}",
				address);
		this.lj = lj;
		this.lightState = false;
		this.underLights = underLights;
	}

	/**
	 * 
	 * @param lj
	 * @param navLights
	 * @param newState
	 * @throws NoConnection
	 * @throws UnknownHostException
	 */
	public UnderwaterLightsController(LabJack lj, MInteger navLights,
			MBoolean newState) throws NoConnection, UnknownHostException {
		address = lj.getHost().getContents() + ":" + lj.getPort().intValue();
		logger.info(
				"An instance of underwater lights controller was instantiated with LabJack connected at {} with light state set to {}",
				new Object[] { address, newState.getValue() });
		lightState = newState.getValue();
		lj.write(navLights, newState);
	}

	@Override
	public void toggleLight() throws NoConnection {
		lightState = !lightState;
		logger.trace(
				MMarker.SETTER,
				"Underwater lights from LabJack connected at {} have been switched to {}",
				address, lightState ? "ON" : "OFF");
		lj.write(underLights, new MBoolean(lightState));
	}

	public MBoolean getUnderwaterLightState() {
		logger.trace(
				MMarker.GETTER,
				"Returning light state {}, from LabJack with host: {} and port: {} .",
				new MBoolean(lightState), address);
		return new MBoolean(lightState);
	}

	public void setUnderwaterLightState(MBoolean newState) throws NoConnection {
		this.lightState = newState.getValue();
		logger.trace(
				MMarker.SETTER,
				"Underwater lights from LabJack connected at {} have been switched to {}",
				address, lightState ? "ON" : "OFF");
		lj.write(underLights, newState);
	}
}
