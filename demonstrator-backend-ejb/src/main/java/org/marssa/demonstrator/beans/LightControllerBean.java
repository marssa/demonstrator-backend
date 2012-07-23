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
package org.marssa.demonstrator.beans;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.UnknownHostException;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.marssa.demonstrator.control.lighting.NavigationLightsController;
import org.marssa.demonstrator.control.lighting.UnderwaterLightsController;
import org.marssa.demonstrator.daq.DAQType;
import org.marssa.demonstrator.lights.LightType;
import org.marssa.demonstrator.network.AddressType;
import org.marssa.demonstrator.settings.Settings;
import org.marssa.footprint.datatypes.MString;
import org.marssa.footprint.datatypes.integer.MInteger;
import org.marssa.footprint.exceptions.ConfigurationError;
import org.marssa.footprint.exceptions.NoConnection;
import org.marssa.services.diagnostics.daq.LabJack;
import org.marssa.services.diagnostics.daq.LabJackU3;
import org.marssa.services.diagnostics.daq.LabJackUE9;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Clayton Tabone
 * 
 */
@Singleton
@Startup
public class LightControllerBean {

	private static final Logger logger = LoggerFactory
			.getLogger(LightControllerBean.class.getName());

	private NavigationLightsController navLightsController;
	private UnderwaterLightsController underwaterLightsController;

	/**
	 * 
	 */
	public LightControllerBean() {
		// TODO Auto-generated constructor stub
	}

	@PostConstruct
	private void init() throws ConfigurationError, NoConnection,
			UnknownHostException, JAXBException, FileNotFoundException {
		JAXBContext context = JAXBContext
				.newInstance(new Class[] { Settings.class });
		Unmarshaller unmarshaller = context.createUnmarshaller();
		InputStream is = this.getClass().getClassLoader()
				.getResourceAsStream("configuration/settings.xml");

		Settings settings = (Settings) unmarshaller.unmarshal(is);
		for (LightType light : settings.getLights().getLight()) {
			DAQType daq = (DAQType) (light.getDaqID());
			AddressType addressElement = daq.getSocket();
			LabJack lj;
			logger.info(
					"Found configuration for {} connected to {}, port {}",
					new Object[] { light.getName(), daq.getDAQname(),
							light.getDaqPort() });
			MString address;
			if (addressElement.getHost().getIp() == null
					|| addressElement.getHost().getIp().isEmpty()) {
				address = new MString(addressElement.getHost().getHostname());

			} else {
				address = new MString(addressElement.getHost().getIp());
			}
			switch (daq.getType()) {
			case LAB_JACK_U_3:
				lj = LabJackU3.getInstance(address, new MInteger(daq
						.getSocket().getPort()));
				break;
			case LAB_JACK_UE_9:
				lj = LabJackUE9.getInstance(address, new MInteger(daq
						.getSocket().getPort()));
				break;
			default:
				throw new ConfigurationError("Unknown DAQ type: "
						+ daq.getType());
			}
			switch (light.getType()) {
			case NAVIGATION_LIGHTS:
				navLightsController = new NavigationLightsController(lj,
						new MInteger(light.getDaqPort().intValue()));
				break;
			case UNDERWATER_LIGHTS:
				underwaterLightsController = new UnderwaterLightsController(lj,
						new MInteger(light.getDaqPort().intValue()));
				break;
			default:
				throw new ConfigurationError("Unknown Light Controller type: "
						+ light.getType());
			}
		}
	}

	@PreDestroy
	private void destroy() {
		// TODO Add unimplemented method
	}

	public UnderwaterLightsController getUnderWaterLightsController() {
		return underwaterLightsController;
	}

	public NavigationLightsController getNavigationLightsController() {
		return navLightsController;
	}
}
