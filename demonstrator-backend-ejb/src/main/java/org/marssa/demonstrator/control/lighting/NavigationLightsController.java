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

public class NavigationLightsController implements ILightToggle {

	static Logger logger = LoggerFactory
			.getLogger(NavigationLightsController.class.getName());

	private boolean lightState;
	private LabJack lj;
	private MInteger navLights;
	private String address = "";

	/**
	 * 
	 * @param lj
	 * @param navLights
	 * @throws UnknownHostException
	 * @throws NoConnection
	 */
	public NavigationLightsController(LabJack lj, MInteger navLights)
			throws UnknownHostException, NoConnection {
		address = lj.getHost().getContents() + ":" + lj.getPort().intValue();
		logger.info(
				"An instance of navigation lights controller was instantiated with LabJack connected at {}",
				address);
		this.lj = lj;
		this.lightState = false;
		this.navLights = navLights;

		// TODO There must be something wrong here. FIO4 is operating in output
		// mode, regardless of FIO4-dir

		// Set direction for FIO4 port
		// lj.write(LabJack.FIO4_DIR_ADDR, new MBoolean(true));

		// Set output to false
		lj.write(navLights, new MBoolean(false));

	}

	/**
	 * 
	 * @param lj
	 * @param navLights
	 * @param newState
	 * @throws NoConnection
	 * @throws UnknownHostException
	 */
	public NavigationLightsController(LabJack lj, MInteger navLights,
			MBoolean newState) throws NoConnection, UnknownHostException {
		address = lj.getHost().getContents() + ":" + lj.getPort().intValue();
		logger.info(
				"An instance of navigation lights controller was instantiated with LabJack connected at {} with light state set to {}",
				new Object[] { address, newState.getValue() });
		this.lightState = newState.getValue();

		// TODO There must be something wrong here. FIO4 is operating in output
		// mode, regardless of FIO4-dir

		// Set direction for FIO4 port
		// lj.write(LabJack.FIO4_DIR_ADDR, new MBoolean(true));

		// Set outout to new state
		lj.write(navLights, newState);
	}

	@Override
	public void toggleLight() throws NoConnection {

		lightState = !lightState;
		logger.trace(
				MMarker.SETTER,
				"Navigation lights from LabJack connected at {} have been switched to {}",
				address, lightState ? "ON" : "OFF");
		lj.write(navLights, new MBoolean(lightState));
	}

	public MBoolean getNavigationLightState() {
		logger.trace(MMarker.GETTER,
				"Returning light state {}, from LabJack connected at {}",
				new MBoolean(lightState), address);
		return new MBoolean(lightState);
	}

	public void setNavigationLightState(MBoolean newState) throws NoConnection {
		this.lightState = newState.getValue();
		logger.trace(
				MMarker.SETTER,
				"Navigation lights from LabJack connected at {} have been switched to {}",
				address, lightState ? "ON" : "OFF");
		lj.write(navLights, newState);
	}
}
