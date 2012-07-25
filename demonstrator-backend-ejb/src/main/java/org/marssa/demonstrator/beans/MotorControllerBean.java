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
import java.math.BigInteger;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.marssa.demonstrator.control.electrical_motor.AuxiliaryMotorsController;
import org.marssa.demonstrator.control.electrical_motor.SternDriveMotorController;
import org.marssa.demonstrator.daq.DAQCategory;
import org.marssa.demonstrator.daq.DAQType;
import org.marssa.demonstrator.motors.MotorType;
import org.marssa.demonstrator.network.AddressType;
import org.marssa.demonstrator.settings.Settings;
import org.marssa.footprint.datatypes.MString;
import org.marssa.footprint.datatypes.integer.MInteger;
import org.marssa.footprint.exceptions.ConfigurationError;
import org.marssa.footprint.exceptions.NoConnection;
import org.marssa.footprint.exceptions.OutOfRange;
import org.marssa.services.diagnostics.daq.LabJack;
import org.marssa.services.diagnostics.daq.LabJackU3;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Clayton Tabone
 * 
 */
@Singleton
public class MotorControllerBean {

	private static final Logger logger = LoggerFactory
			.getLogger(MotorControllerBean.class.getName());

	@Inject
	DAQBean daqBean;

	private AuxiliaryMotorsController auxMotorsController;
	private SternDriveMotorController sternDriveMotorController;

	/**
	 * 
	 */
	public MotorControllerBean() {
		// TODO Auto-generated constructor stub
	}

	@PostConstruct
	private void init() throws ConfigurationError, NoConnection,
			UnknownHostException, JAXBException, FileNotFoundException,
			OutOfRange {
		logger.info("Initializing Motor Controller Bean");
		JAXBContext context = JAXBContext
				.newInstance(new Class[] { Settings.class });
		Unmarshaller unmarshaller = context.createUnmarshaller();
		InputStream is = this.getClass().getClassLoader()
				.getResourceAsStream("configuration/settings.xml");

		Settings settings = (Settings) unmarshaller.unmarshal(is);
		for (MotorType motor : settings.getMotors().getMotor()) {
			DAQType daq = (DAQType) (motor.getConfiguration().getDaqID());
			AddressType addressElement = daq.getSocket();
			LabJack lj;
			logger.info("Found configuration for {} connected to {}",
					motor.getName(), daq.getDAQname());
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
			if (motor.getConfiguration().getAuxiliaryMotor() != null) {
				if (daq.getType() == DAQCategory.LAB_JACK_U_3)
					auxMotorsController = new AuxiliaryMotorsController(
							(LabJackU3) lj);
				else
					throw new ConfigurationError(
							"Auxiliary Motor Controller has to be connected to a LabJack U3");
			} else if (motor.getConfiguration().getSternDriveMotor() != null) {
				List<MInteger> ports = new ArrayList<MInteger>();
				for (BigInteger port : motor.getConfiguration()
						.getSternDriveMotor().getDaqPorts().getDaqPort()) {
					ports.add(new MInteger(port.intValue()));
				}
				sternDriveMotorController = new SternDriveMotorController(lj,
						ports);
			} else {
				throw new ConfigurationError("Unknown Motor Controller type");
			}
		}
		logger.info("Initialized Motor Controller Bean");
	}

	@PreDestroy
	private void destroy() {
		logger.info("Destroying Motor Controller Bean");
		// TODO Add unimplemented method
		logger.info("Destroyed Motor Controller Bean");
	}

	public AuxiliaryMotorsController getAuxMotorsController() {
		return auxMotorsController;
	}

	public SternDriveMotorController getSternDriveMotorController() {
		return sternDriveMotorController;
	}
}
