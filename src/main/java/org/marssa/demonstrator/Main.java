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

import java.net.UnknownHostException;
import java.util.ArrayList;

import org.marssa.demonstrator.constants.Constants;
import org.marssa.demonstrator.control.electrical_motor.SternDriveMotorController;
import org.marssa.demonstrator.control.lighting.NavigationLightsController;
import org.marssa.demonstrator.control.lighting.UnderwaterLightsController;
import org.marssa.demonstrator.control.path_planning.PathPlanningController;
import org.marssa.demonstrator.control.rudder.RudderController;
import org.marssa.demonstrator.web_services.WebServices;
import org.marssa.demonstrator.web_services.path_planning.Waypoint;
import org.marssa.footprint.datatypes.composite.Coordinate;
import org.marssa.footprint.datatypes.composite.Latitude;
import org.marssa.footprint.datatypes.composite.Longitude;
import org.marssa.footprint.datatypes.decimal.DegreesDecimal;
import org.marssa.footprint.exceptions.ConfigurationError;
import org.marssa.footprint.exceptions.NoConnection;
import org.marssa.footprint.exceptions.NoValue;
import org.marssa.footprint.exceptions.OutOfRange;
import org.marssa.services.diagnostics.daq.LabJack;
import org.marssa.services.diagnostics.daq.LabJackU3;
import org.marssa.services.diagnostics.daq.LabJackUE9;
import org.marssa.services.navigation.GpsReceiver;
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
		LabJackU3 labJacku3 = null;
		LabJackUE9 labJackue9 = null;
		NavigationLightsController navLightsController;
		UnderwaterLightsController underwaterLightsController;
		SternDriveMotorController motorController;
		RudderController rudderController;
		GpsReceiver gpsReceiver;
		WebServices webServices;
		PathPlanningController pathPlanningController;

		
		// Initialise LabJack
		try {
			logger.info("Initialising LabJack ...");
			labJackue9 = LabJackUE9.getInstance(Constants.LABJACKUE9.HOST,
					Constants.LABJACKUE9.PORT);
			logger.info("LabJack initialized successfully on {}:{}",
					Constants.LABJACKUE9.HOST, Constants.LABJACKUE9.PORT);
			
			/*
			logger.info("Initialising navigation lights controller ... ");
			navLightsController = new NavigationLightsController(
					Constants.LABJACKUE9.HOST, Constants.LABJACKUE9.PORT,
					LabJack.FIO4_DIR_ADDR);
			logger.info("Navigation lights controller initialised successfully");

			logger.info("Initialising underwater lights controller ... ");
			underwaterLightsController = new UnderwaterLightsController(
					Constants.LABJACKUE9.HOST, Constants.LABJACKUE9.PORT,
					LabJack.FIO13_DIR_ADDR);
			logger.info("Underwater lights controller initialised successfully"); 
			*/
			
			logger.info("Initialising motor controller ... ");
			motorController = new SternDriveMotorController(labJackue9);
			logger.info("Motor controller initialised successfully");

			logger.info("Initialising rudder controller ... ");
			rudderController = new RudderController(labJacku3);
			logger.info("Rudder controller initialised successfully");

			logger.info("Initialising GPS receiver ... ");
			gpsReceiver = new GpsReceiver(Constants.GPS.HOST,
					Constants.GPS.PORT);
			logger.info("GPS receiver initialised successfully");

			logger.info("Initialising Path Planning controller ... ");
			pathPlanningController = new PathPlanningController(motorController,rudderController,gpsReceiver);
			//pathPlanningController = new PathPlanningController(null, null,null);
			logger.info("Path Planning controller initialised successfully");
			
			logger.info("Initialising web services ... ");
			webServices = new WebServices(null,
					null, motorController,
					rudderController, gpsReceiver,pathPlanningController);
			logger.info("Web services initialised successfully");

			logger.info("Starting restlet web servicves ... ");
			webServices.start();
			logger.info("Web servicves started. Listening on {}:{}",
					Constants.GPS.HOST, Constants.GPS.PORT);
		} catch (Exception e) {
			logger.error("Failed to initialize LabJack", e);
			System.exit(1);
		}

		/*
		// Initialise Controllers and Receivers
		try {
			
			logger.info("Initialising Path Planning controller ... ");
			//pathPlanningController = new PathPlanningController(motorController,rudderController,gpsReceiver);
			pathPlanningController = new PathPlanningController(null, null,null);
			logger.info("Path Planning controller initialised successfully");
			//---------------------------------CODE FOR TESTING PATH PLANNING----------------------------------
			ArrayList<Waypoint> wayPointList =  new ArrayList<Waypoint>();
			wayPointList.add(new Waypoint("","",new Coordinate(new Latitude(new DegreesDecimal(35.983267)) , new Longitude(new DegreesDecimal(14.387419)))));
			wayPointList.add(new Waypoint("","",new Coordinate(new Latitude(new DegreesDecimal(36.000184)) , new Longitude(new DegreesDecimal(14.376021)))));
			wayPointList.add(new Waypoint("","",new Coordinate(new Latitude(new DegreesDecimal(36.010598)) , new Longitude(new DegreesDecimal(14.391477)))));
			wayPointList.add(new Waypoint("","",new Coordinate(new Latitude(new DegreesDecimal(36.024835)) , new Longitude(new DegreesDecimal(14.339797)))));
			wayPointList.add(new Waypoint("","",new Coordinate(new Latitude(new DegreesDecimal(36.021192)) , new Longitude(new DegreesDecimal(14.318054)))));
			wayPointList.add(new Waypoint("","",new Coordinate(new Latitude(new DegreesDecimal(36.015387)) , new Longitude(new DegreesDecimal(14.322254)))));
			
			pathPlanningController.setPathList(wayPointList);
			pathPlanningController.setTestCurrent(new Coordinate(new Latitude(new DegreesDecimal(35.971663)) , new Longitude(new DegreesDecimal(14.358312))),270);
			pathPlanningController.startFollowingPath();
			
		}*/
			//---------------------------------CODE FOR TESTING PATH PLANNING----------------------------------
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

