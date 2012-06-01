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
package org.marssa.demonstrator;


import org.apache.commons.daemon.Daemon;
import org.apache.commons.daemon.DaemonContext;
import org.apache.commons.daemon.DaemonInitException;
import org.marssa.demonstrator.constants.Constants;
import org.marssa.demonstrator.control.electrical_motor.MotorController;
import org.marssa.demonstrator.control.lighting.NavigationLightsController;
import org.marssa.demonstrator.control.lighting.UnderwaterLightsController;
import org.marssa.demonstrator.control.rudder.RudderController;
import org.marssa.demonstrator.web_services.WebServices;
import org.marssa.footprint.exceptions.ConfigurationError;
import org.marssa.footprint.exceptions.OutOfRange;
import org.marssa.services.diagnostics.daq.LabJackU3;
import org.marssa.services.navigation.GpsReceiver;
import org.restlet.Component;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;

/**
 * @author Clayton Tabone
 * 
 */
public class JSVC implements Daemon {
	private static final Logger logger = (Logger) LoggerFactory
			.getLogger(JSVC.class);

	private LabJackU3 labJack;
	private NavigationLightsController navLightsController;
	private UnderwaterLightsController underwaterLightsController;
	private MotorController motorController;
	private RudderController rudderController;
	private GpsReceiver gpsReceiver;
	private WebServices webServices;

	Component component = new Component();

	/**
	 * Open configuration files, create a trace file, create ServerSockets,
	 * Threads
	 * 
	 * @param context
	 */
	public void init(DaemonContext context) throws DaemonInitException,
			Exception {
		// Initialise LabJack
		try {
			logger.info("Initialising LabJack ...");
			labJack = LabJackU3.getInstance(Constants.LABJACK.HOST,
					Constants.LABJACK.PORT);
			logger.info("LabJack initialized successfully on {}:{}",
					Constants.LABJACK.HOST, Constants.LABJACK.PORT);
		} catch (Exception e) {
			logger.error("Failed to initialize LabJack", e);
			stop();
		}

		// Initialise Controllers and Receivers
		try {
			logger.info("Initialising navigation lights controller ... ");
			navLightsController = new NavigationLightsController(
					Constants.LABJACK.HOST, Constants.LABJACK.PORT,
					LabJackU3.FIO4_DIR_ADDR);
			logger.info("Navigation lights controller initialised successfully");

			logger.info("Initialising underwater lights controller ... ");
			underwaterLightsController = new UnderwaterLightsController(
					Constants.LABJACK.HOST, Constants.LABJACK.PORT,
					LabJackU3.FIO13_DIR_ADDR);
			logger.info("Underwater lights controller initialised successfully");

			logger.info("Initialising motor controller ... ");
			motorController = new MotorController(labJack);
			logger.info("Motor controller initialised successfully");

			logger.info("Initialising rudder controller ... ");
			rudderController = new RudderController(labJack);
			logger.info("Rudder controller initialised successfully");

			logger.info("Initialising GPS receiver ... ");
			gpsReceiver = new GpsReceiver(Constants.GPS.HOST,
					Constants.GPS.PORT);
			logger.info("GPS receiver initialised successfully");

			logger.info("Initialising web services ... ");
			webServices = new WebServices(navLightsController,
					underwaterLightsController, motorController,
					rudderController, gpsReceiver);
			logger.info("Web services initialised successfully");

			logger.info("Starting restlet web servicves ... ");
			webServices.start();
			logger.info("Web servicves started. Listening on {}:{}",
					Constants.GPS.HOST, Constants.GPS.PORT);
		} catch (ConfigurationError e) {
			e.printStackTrace();
			stop();
		} catch (OutOfRange e) {
			e.printStackTrace();
			stop();
		}
	}

	/**
	 * Start the Thread, accept incoming connections
	 */
	public void start() throws Exception {
		System.out.print("Starting web services ... ");
		webServices.start();
		System.out.println("success!");
	}

	/**
	 * Inform the Thread to terminate the run(), close the ServerSockets
	 */
	public void stop() throws Exception {
		try {
			System.out.print("Stopping web services ... ");
			webServices.stop();
			System.out.println("success!");
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Destroy any object created in init()
	 */
	public void destroy() {
		labJack = null;
		navLightsController = null;
		underwaterLightsController = null;
		motorController = null;
		rudderController = null;
		gpsReceiver = null;
		webServices = null;
	}
}
