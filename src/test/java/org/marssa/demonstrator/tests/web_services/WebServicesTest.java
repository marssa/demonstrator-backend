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

import org.marssa.demonstrator.constants.Constants;
import org.marssa.demonstrator.tests.control.SternDriveMotorControllerTest;
import org.marssa.demonstrator.tests.control.TestController;
import org.marssa.demonstrator.tests.control.path_planning.PathPlanningControllerTest;
import org.marssa.demonstrator.web_services.StaticFileServerApplication;
import org.marssa.footprint.datatypes.MBoolean;
import org.marssa.footprint.datatypes.decimal.MDecimal;
import org.restlet.Component;
import org.restlet.Server;
import org.restlet.data.CacheDirective;
import org.restlet.data.Protocol;

public class WebServicesTest {

	private static final ArrayList<CacheDirective> cacheDirectives = new ArrayList<CacheDirective>();
	private static TestController motorController = new TestController();
	private static SternDriveMotorControllerTest sternMotorController = new SternDriveMotorControllerTest();
	private static PathPlanningControllerTest pathPlanningController = new PathPlanningControllerTest();

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

		// Attach the motor control application
		component.getDefaultHost().attach(
				"/motor",
				new MotorControllerTestApplication(cacheDirectives,
						motorController));
		
		// Attach the Stern control application
				component.getDefaultHost().attach(
						"/sternMotor",
						new SternMotorControllerTestApplication(cacheDirectives));

		// Attach the rudder control application
		component.getDefaultHost().attach("/rudder",
				new RudderControllerTestApplication(cacheDirectives));

		// Attach the GPS receiver application
		component.getDefaultHost().attach("/gps",
				new GPSReceiverTestApplication(cacheDirectives));

		// Attach the motion control feedback application
		component.getDefaultHost().attach(
				"/motionControlPage",
				new MotionControlPageTestApplication(cacheDirectives,
						motorController));

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
