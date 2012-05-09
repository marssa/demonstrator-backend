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
package mise.marssa.demonstrator;

import java.net.UnknownHostException;

import mise.marssa.demonstrator.constants.Constants;
import mise.marssa.demonstrator.control.electrical_motor.MotorController;
import mise.marssa.demonstrator.control.lighting.NavigationLightsController;
import mise.marssa.demonstrator.control.lighting.UnderwaterLightsController;
import mise.marssa.demonstrator.control.rudder.RudderController;
import mise.marssa.demonstrator.web_services.WebServices;
import mise.marssa.footprint.exceptions.ConfigurationError;
import mise.marssa.footprint.exceptions.NoConnection;
import mise.marssa.footprint.exceptions.NoValue;
import mise.marssa.footprint.exceptions.OutOfRange;
import mise.marssa.services.diagnostics.daq.LabJack;
import mise.marssa.services.diagnostics.daq.LabJackU3;
import mise.marssa.services.navigation.GpsReceiver;

import org.restlet.resource.ServerResource;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;

/**
 * @author Clayton Tabone
 * 
 */
public class Main extends ServerResource {
	private static final Logger logger = (Logger) LoggerFactory
			.getLogger(Main.class);

	/**
	 * @param args
	 *            the args
	 */
	public static void main(java.lang.String[] args) {
		LabJackU3 labJack = null;
		NavigationLightsController navLightsController;
		UnderwaterLightsController underwaterLightsController;
		MotorController motorController;
		RudderController rudderController;
		GpsReceiver gpsReceiver;
		WebServices webServices;

		// Initialise LabJack
		try {
			logger.info("Initialising LabJack ...");
			labJack = LabJackU3.getInstance(Constants.LABJACK.HOST,
					Constants.LABJACK.PORT);
			logger.info("LabJack initialized successfully on {}:{}",
					Constants.LABJACK.HOST, Constants.LABJACK.PORT);
		} catch (Exception e) {
			logger.error("Failed to initialize LabJack", e);
			System.exit(1);
		}

		// Initialise Controllers and Receivers
		try {
			logger.info("Initialising navigation lights controller ... ");
			navLightsController = new NavigationLightsController(
					Constants.LABJACK.HOST, Constants.LABJACK.PORT,
					LabJack.FIO4_DIR_ADDR);
			logger.info("Navigation lights controller initialised successfully");

			logger.info("Initialising underwater lights controller ... ");
			underwaterLightsController = new UnderwaterLightsController(
					Constants.LABJACK.HOST, Constants.LABJACK.PORT,
					LabJack.FIO13_DIR_ADDR);
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
			logger.error("ConfigurationError exception has been caught", e);
			System.exit(1);
		} catch (OutOfRange e) {
			logger.error("OutOfRange exception has been caught", e);
			System.exit(1);
		} catch (UnknownHostException e) {
			logger.error("UnknownHostException exception has been caught", e);
			System.exit(1);
		} catch (NoConnection e) {
			logger.error("NoConnection exception has been caught", e);
			System.exit(1);
		} catch (InterruptedException e) {
			logger.error("InterruptedException exception has been caught", e);
			System.exit(1);
		} catch (NoValue e) {
			logger.error("NoValue exception has been caught", e);
			System.exit(1);
		} catch (Exception e) {
			logger.error("General exception has been caught", e);
			System.exit(1);
		}
		/*
		 * // NavigationLights Tests
		 * System.out.println(navigationLights.getNavigationLightState());
		 * Percentage desiredValue = new Percentage(10f);
		 * 
		 * // MotorControl Tests try { motorController.rampTo(desiredValue); }
		 * catch (InterruptedException e) { e.printStackTrace(); }
		 * 
		 * // Rudder Tests try { rudderController.rotate(new MBoolean (true));
		 * rudderController.rotate(new MBoolean (false));
		 * rudderController.rotate(new MBoolean (true));
		 * rudderController.rotate(new MBoolean (false));
		 * rudderController.rotate(new MBoolean (false));
		 * rudderController.rotate(new MBoolean (true));
		 * rudderController.rotate(new MBoolean (true)); } catch
		 * (InterruptedException e) { e.printStackTrace(); }
		 * 
		 * // GPSReceiver Tests try {
		 * System.out.println("The GPS coordinates are " +
		 * gpsReceiver.getCoordinate()); System.out.println("Altitude is " +
		 * gpsReceiver.getElevation()); System.out.println("Course over ground "
		 * + gpsReceiver.getCOG()); System.out.println("Speed over ground " +
		 * gpsReceiver.getSOG()); //System.out.println("EPT " + gps.getEPT());
		 * ///have to find the EPT System.out.println("Time " +
		 * gpsReceiver.getDate()); } catch (NoConnection e) {
		 * e.printStackTrace(); } catch (NoValue e) { e.printStackTrace(); }
		 */
	}
}