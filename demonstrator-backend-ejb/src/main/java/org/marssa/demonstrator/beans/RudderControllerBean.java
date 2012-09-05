package org.marssa.demonstrator.beans;

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

import java.io.InputStream;
import java.math.BigInteger;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.marssa.demonstrator.control.rudder.RudderController;
import org.marssa.demonstrator.daq.DAQType;
import org.marssa.demonstrator.network.AddressType;
import org.marssa.demonstrator.rudder.RudderType;
import org.marssa.demonstrator.settings.Settings;
import org.marssa.footprint.datatypes.MString;
import org.marssa.footprint.datatypes.integer.MInteger;
import org.marssa.footprint.exceptions.ConfigurationError;
import org.marssa.footprint.exceptions.NoConnection;
import org.marssa.services.diagnostics.daq.LabJack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Warren Zahra
 * 
 */
@ApplicationScoped
@Singleton
public class RudderControllerBean {

	private static final Logger logger = LoggerFactory
			.getLogger(MotorControllerBean.class.getName());
	@Inject
	DAQBean daqBean;

	private RudderController rudderController;

	public RudderControllerBean() {
	}

	@PostConstruct
	private void init() throws InterruptedException, JAXBException,
			UnknownHostException, ConfigurationError, NoConnection {
		logger.info("Initializing Motor Controller Bean");
		JAXBContext context = JAXBContext
				.newInstance(new Class[] { Settings.class });
		Unmarshaller unmarshaller = context.createUnmarshaller();
		InputStream is = this.getClass().getClassLoader()
				.getResourceAsStream("configuration/settings.xml");

		Settings settings = (Settings) unmarshaller.unmarshal(is);
		RudderType rudder = settings.getRudder();
		DAQType daq = (DAQType) (rudder.getDaqID());
		AddressType addressElement = daq.getSocket();
		LabJack lj;
		logger.info("Found configuration for {} connected to {}",
				rudder.getName(), daq.getDAQname());
		if (addressElement.getHost().getIp() == null
				|| addressElement.getHost().getIp().isEmpty()) {
			String hostname = addressElement.getHost().getHostname();
			lj = daqBean.getLabJackByHostname(new MString(hostname),
					new MInteger(addressElement.getPort()));
		} else {
			String ip = addressElement.getHost().getIp();
			lj = daqBean.getLabJackByIP(new MString(ip), new MInteger(
					addressElement.getPort()));
		}
		List<MInteger> ports = new ArrayList<MInteger>();
		for (BigInteger port : rudder.getDaqPorts().getDaqPort()) {
			ports.add(new MInteger(port.intValue()));
		}
		rudderController = new RudderController(lj, ports);

		logger.info("Initialized Motor Controller Bean");
	}

	@PreDestroy
	private void destroy() {
		logger.info("Destroying Rudder Controller Bean");
	}

	public RudderController getRudderController() {
		return rudderController;
	}

}