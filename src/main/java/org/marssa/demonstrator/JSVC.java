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
import org.marssa.demonstrator.control.electrical_motor.SternDriveMotorController;
import org.marssa.demonstrator.control.lighting.NavigationLightsController;
import org.marssa.demonstrator.control.lighting.UnderwaterLightsController;
import org.marssa.demonstrator.control.path_planning.PathPlanningController;
import org.marssa.demonstrator.control.rudder.RudderController;
import org.marssa.demonstrator.web_services.WebServices;
import org.marssa.footprint.exceptions.ConfigurationError;
import org.marssa.footprint.exceptions.OutOfRange;
import org.marssa.services.diagnostics.daq.LabJackU3;
import org.marssa.services.diagnostics.daq.LabJackUE9;
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

	private LabJackUE9 labJackUE9;
	private LabJackU3 labJackU3;
	private NavigationLightsController navLightsController;
	private UnderwaterLightsController underwaterLightsController;
	private SternDriveMotorController motorController;
	private RudderController rudderController;
	private GpsReceiver gpsReceiver;
	private WebServices webServices;
	private PathPlanningController pathPlanningController;
	Component component = new Component();

	/**
	 * Open configuration files, create a trace file, create ServerSockets,
	 * Threads
	 * 
	 * @param context
	 */
	@Override
	public void init(DaemonContext context) throws DaemonInitException,
			Exception {
		// Initialise LabJack
		try {
			logger.info("Initialising LabJack ...");
			labJackUE9 = LabJackUE9.getInstance(Constants.LABJACKUE9.HOST,
					Constants.LABJACKUE9.PORT);
			logger.info("LabJackUe9 initialized successfully on {}:{}",
					Constants.LABJACKUE9.HOST, Constants.LABJACKUE9.PORT);
			labJackU3 = LabJackU3.getInstance(Constants.LABJACKU3.HOST,
					Constants.LABJACKU3.PORT);
			logger.info("LabJacku3 initialized successfully on {}:{}",
					Constants.LABJACKU3.HOST, Constants.LABJACKU3.PORT);
		} catch (Exception e) {
			logger.error("Failed to initialize LabJack", e);
			stop();
		}

		// Initialise Controllers and Receivers
		try {
			logger.info("Initialising navigation lights controller ... ");
			navLightsController = new NavigationLightsController(labJackU3,
					LabJackU3.FIO4_DIR_ADDR);
			logger.info("Navigation lights controller initialised successfully");

			logger.info("Initialising underwater lights controller ... ");
			underwaterLightsController = new UnderwaterLightsController(
					labJackU3, LabJackU3.FIO13_DIR_ADDR);
			logger.info("Underwater lights controller initialised successfully");

			logger.info("Initialising motor controller ... ");
			motorController = new SternDriveMotorController(labJackUE9);
			logger.info("Motor controller initialised successfully");
			logger.info("Initialising Path Planning controller ... ");
			pathPlanningController = new PathPlanningController(
					motorController, rudderController, gpsReceiver);

			logger.info("Initialising rudder controller ... ");
			rudderController = new RudderController(labJackUE9);
			logger.info("Rudder controller initialised successfully");

			logger.info("Initialising GPS receiver ... ");
			gpsReceiver = new GpsReceiver(Constants.GPS.HOST,
					Constants.GPS.PORT);
			logger.info("GPS receiver initialised successfully");

			logger.info("Initialising web services ... ");
			webServices = new WebServices(navLightsController,
					underwaterLightsController, motorController,
					rudderController, gpsReceiver, pathPlanningController);
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
	@Override
	public void start() throws Exception {
		System.out.print("Starting web services ... ");
		webServices.start();
		System.out.println("success!");
	}

	/**
	 * Inform the Thread to terminate the run(), close the ServerSockets
	 */
	@Override
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
	@Override
	public void destroy() {
		labJackUE9 = null;
		labJackU3 = null;
		navLightsController = null;
		underwaterLightsController = null;
		motorController = null;
		rudderController = null;
		gpsReceiver = null;
		webServices = null;
	}
}
