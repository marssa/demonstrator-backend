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
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.marssa.demonstrator.daq.DAQType;
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
public class DAQBean {

	private static final Logger logger = LoggerFactory.getLogger(DAQBean.class
			.getName());

	private final HashMap<String, LabJack> daqs = new HashMap<String, LabJack>();

	/**
	 * 
	 */
	public DAQBean() {
		// TODO Auto-generated constructor stub
	}

	@PostConstruct
	private void init() throws ConfigurationError, NoConnection,
			UnknownHostException, JAXBException, FileNotFoundException {
		logger.info("Initializing DAQ Bean");
		JAXBContext context = JAXBContext
				.newInstance(new Class[] { Settings.class });
		Unmarshaller unmarshaller = context.createUnmarshaller();
		InputStream is = this.getClass().getClassLoader()
				.getResourceAsStream("configuration/settings.xml");

		Settings settings = (Settings) unmarshaller.unmarshal(is);
		for (DAQType daq : settings.getDaqs().getDaq()) {
			AddressType addressElement = daq.getSocket();
			MString address;
			if (addressElement.getHost().getIp() == null
					|| addressElement.getHost().getIp().isEmpty()) {
				String hostname = addressElement.getHost().getHostname();
				address = new MString(Inet4Address.getByName(hostname)
						.getAddress().toString());
			} else {
				address = new MString(addressElement.getHost().getIp());
			}
			logger.info(
					"Found configuration for {} connected to {}, port {}",
					new Object[] { daq.getDAQname(), address,
							addressElement.getPort() });
			LabJack lj;
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
			daqs.put(address.toString() + ":" + addressElement.getPort(), lj);
		}
		logger.info("Initialized DAQ Bean");
	}

	@PreDestroy
	private void destroy() {
		logger.info("Destroying DAQ Bean");
		for (Map.Entry<String, LabJack> daq : daqs.entrySet()) {
			// TODO disconnect all LabJack instances
			// daq.getValue().disconnect();
		}
		daqs.clear();
		logger.info("Destroyed DAQ Bean");
	}

	public LabJack getLabJackByIP(MString ip, MInteger port)
			throws ConfigurationError {
		LabJack lj = daqs.get(ip.toString() + ":" + port.intValue());
		if (lj == null)
			throw new ConfigurationError(
					"No LabJack configuration has been found for " + ip + ":"
							+ port);
		return lj;
	}

	public LabJack getLabJackByHostname(MString hostname, MInteger port)
			throws ConfigurationError, UnknownHostException {
		String ip = Inet4Address.getByName(hostname.toString()).getAddress()
				.toString();
		LabJack lj = daqs.get(ip + ":" + port.intValue());
		if (lj == null)
			throw new ConfigurationError(
					"No LabJack configuration has been found for " + ip + ":"
							+ port);
		return lj;
	}
}
