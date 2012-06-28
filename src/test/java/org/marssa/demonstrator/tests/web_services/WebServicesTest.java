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
package org.marssa.demonstrator.tests.web_services;

import java.util.ArrayList;

import org.marssa.demonstrator.Main;
import org.marssa.demonstrator.constants.Constants;
import org.marssa.demonstrator.control.electrical_motor.SternDriveMotorController;
import org.marssa.demonstrator.control.lighting.NavigationLightsController;
import org.marssa.demonstrator.control.lighting.UnderwaterLightsController;
import org.marssa.demonstrator.control.path_planning.PathPlanningController;
import org.marssa.demonstrator.control.rudder.RudderController;
import org.marssa.demonstrator.tests.control.RudderControllerTest;
import org.marssa.demonstrator.tests.control.SternDriveMotorControllerTest;
import org.marssa.demonstrator.tests.control.TestController;
import org.marssa.demonstrator.tests.control.path_planning.PathPlanningControllerTest;
import org.marssa.demonstrator.web_services.StaticFileServerApplication;
import org.marssa.demonstrator.web_services.WebServices;
import org.marssa.footprint.datatypes.MBoolean;
import org.marssa.footprint.datatypes.decimal.MDecimal;
import org.marssa.services.diagnostics.daq.LabJackUE9;
import org.marssa.services.navigation.GpsReceiver;
import org.restlet.Component;
import org.restlet.Server;
import org.restlet.data.CacheDirective;
import org.restlet.data.Protocol;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;

public class WebServicesTest {
	private static final Logger logger = (Logger) LoggerFactory
			.getLogger(WebServicesTest.class);
	private static final ArrayList<CacheDirective> cacheDirectives = new ArrayList<CacheDirective>();
	private static TestController motorController = new TestController();
	private static SternDriveMotorControllerTest sternMotorController = new SternDriveMotorControllerTest(null);
	private static PathPlanningControllerTest pathPlanningController = new PathPlanningControllerTest();
	static LabJackUE9 labJackue9 ;
	NavigationLightsController navLightsController;
	UnderwaterLightsController underwaterLightsController;
	static RudderControllerTest rudderController;
	GpsReceiver gpsReceiver;
	WebServices webServices;

	static class LightState {
		public MBoolean navLightState = new MBoolean(false);
		public MBoolean underwaterLightState = new MBoolean(false);
	}

	private static LightState lightState = new LightState();
	private static MDecimal rudderAngle = new MDecimal(0.0f);

	public static MDecimal getRudderAngle() {
		return rudderAngle;
	}

	public static void setRudderAngle(MDecimal newRudderAngle) {
		rudderAngle = newRudderAngle;
	}

	public static LightState getLightState() {
		return lightState;
	}

	public static void setLightState(LightState newLightState) {
		lightState = newLightState;
	}

	/**
	 * @param args
	 *            the args
	 */
	public static void main(java.lang.String[] args) {
		/*
		logger.info("Initialising LabJack ...");
		labJackue9 = LabJackUE9.getInstance(Constants.LABJACKUE9.HOST,
				Constants.LABJACKUE9.PORT);
		logger.info("LabJack initialized successfully on {}:{}",
				Constants.LABJACKUE9.HOST, Constants.LABJACKUE9.PORT);

		logger.info("Initialising motor controller ... ");
		motorController = new SternDriveMotorControllerTest(labJackue9);
		logger.info("Motor controller initialised successfully");

		logger.info("Initialising rudder controller ... ");
		rudderController = new RudderControllerTest(labJackue9);
		logger.info("Rudder controller initialised successfully");
        */
		logger.info("Initialising Path Planning controller ... ");
		pathPlanningController = new PathPlanningControllerTest();
		//pathPlanningController = new PathPlanningController(null, null,null);
		logger.info("Path Planning controller initialised successfully");
		
		// Create a new Component
		Component component = new Component();

		System.out.println("Starting Web Services on "
				+ Constants.WEB_SERVICES.HOST.getContents() + ":"
				+ Constants.WEB_SERVICES.PORT.toString() + " ...");

		cacheDirectives.add(CacheDirective.noCache());
		cacheDirectives.add(CacheDirective.noStore());

		// Add a new HTTP server listening on the given port
		Server server = component.getServers().add(Protocol.HTTP,
				Constants.WEB_SERVICES.HOST.getContents(),
				Constants.WEB_SERVICES.PORT.intValue());
		server.getContext()
				.getParameters()
				.add("maxTotalConnections",
						Constants.WEB_SERVICES.MAX_TOTAL_CONNECTIONS.toString());

		// Add new client connector for the FILE protocol
		component.getClients().add(Protocol.FILE);

		// Attach the static file server application
		component.getDefaultHost()
				.attach("", new StaticFileServerApplication());

		// Attach the light control application
		component.getDefaultHost().attach("/lighting",
				new LightControllerTestApplication(cacheDirectives));

		// Attach the Stern control application
		component.getDefaultHost().attach("/sternMotor",
				new SternMotorControllerTestApplication(cacheDirectives));

		// Attach the rudder control application
		component.getDefaultHost().attach("/rudder",
				new RudderControllerTestApplication(cacheDirectives));

		// Attach the GPS receiver application
		component.getDefaultHost().attach("/gps",
				new GPSReceiverTestApplication(cacheDirectives));

		// Attach the motion control feedback application
		component.getDefaultHost().attach("/lightControlPage",
				new LightControlPageTestApplication(cacheDirectives));

		// Attach the path planner application
		component.getDefaultHost().attach(
				"/pathPlanner",
				new PathControllerTestApplication(cacheDirectives,
						pathPlanningController));

		// Start the component
		try {
			component.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
