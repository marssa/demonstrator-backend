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
import org.marssa.footprint.datatypes.MString;
import org.marssa.footprint.datatypes.integer.MInteger;
import org.marssa.footprint.exceptions.NoConnection;
import org.marssa.footprint.interfaces.control.lighting.ILightToggle;
import org.marssa.footprint.logger.MMarker;
import org.marssa.services.diagnostics.daq.LabJackU3;
import org.marssa.services.diagnostics.daq.LabJackUE9;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;

/**
 * @author Warren Zahra
 * 
 */
public class UnderwaterLightsController implements ILightToggle {

	static Logger underwaterLightLogger = (Logger) LoggerFactory
			.getLogger("UnderwaterLightsController");

	private boolean lightState;
	private LabJackUE9 lj;
	private String switched;
	private MInteger underLights;
	private Object[] poho = { lj.getHost(), lj.getPort() };

	public UnderwaterLightsController(MString host, MInteger port,
			MInteger underLights) throws UnknownHostException, NoConnection {
		underwaterLightLogger
				.info("An instance of Navigation light controller was instantiated with labjack host {}, and port {}.",
						host, port);
		this.lightState = false;
		this.underLights = underLights;
		this.lj = LabJackUE9.getInstance(host, port);
	}

	public UnderwaterLightsController(MString host, MInteger port,
			MInteger navLights, MBoolean newState) throws NoConnection,
			UnknownHostException {
		this.lj = LabJackUE9.getInstance(host, port);
		underwaterLightLogger
				.info("An instance of UnderWater light controller was instantiated with labjack host: {}, and port: {}, with state set to: {}",
						poho, newState.getValue());
		lightState = newState.getValue();
		lj.write(navLights, newState);
	}

	public void toggleLight() throws NoConnection {
		lightState = !lightState;
		if (lightState)
			switched = "ON";
		else
			switched = "OFF";
		underwaterLightLogger
				.trace("UnderwaterLights from labjack with host: {} and port: {} have been switched {} .",
						poho, switched);
		lj.write(underLights, new MBoolean(lightState));
	}

	public MBoolean getUnderwaterLightState() {
		underwaterLightLogger
				.trace(MMarker.GETTER,
						"Returning LightState {}, from labjack with host: {} and port: {} .",
						new MBoolean(lightState), poho);
		return new MBoolean(lightState);
	}

	public void setUnderwaterLightState(MBoolean newState) throws NoConnection {
		this.lightState = newState.getValue();
		if (newState.getValue())
			switched = "ON";
		else
			switched = "OFF";
		underwaterLightLogger
				.trace(MMarker.SETTER,
						"Switching UnderWaterlight: {} ,from labjack with host: {} and port: {}",
						switched, poho);
		lj.write(underLights, newState);
	}
}
