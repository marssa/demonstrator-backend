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
package org.marssa.demonstrator.web_services;

import java.util.ArrayList;

import org.marssa.demonstrator.constants.Constants;
import org.marssa.demonstrator.control.electrical_motor.SternDriveMotorController;
import org.marssa.demonstrator.control.lighting.NavigationLightsController;
import org.marssa.demonstrator.control.lighting.UnderwaterLightsController;
import org.marssa.demonstrator.control.path_planning.PathPlanningController;
import org.marssa.demonstrator.control.rudder.RudderController;
import org.marssa.demonstrator.web_services.GPS_Receiver.GPSReceiverApplication;
import org.marssa.demonstrator.web_services.lightControlPage.LightControlPageApplication;
import org.marssa.demonstrator.web_services.lighting.LightControllerApplication;
import org.marssa.demonstrator.web_services.motionControlPage.MotionControlPageApplication;
import org.marssa.demonstrator.web_services.motor.SternMotorControllerApplication;
import org.marssa.demonstrator.web_services.path_planning.PathControllerApplication;
import org.marssa.demonstrator.web_services.rudder.RudderControllerApplication;
import org.marssa.services.navigation.GpsReceiver;
import org.restlet.Component;
import org.restlet.Server;
import org.restlet.data.CacheDirective;
import org.restlet.data.Protocol;
import org.restlet.resource.ServerResource;

/**
 * @author Clayton Tabone
 * 
 */
public class WebServices extends ServerResource {

	private final ArrayList<CacheDirective> cacheDirectives = new ArrayList<CacheDirective>();

	// Create a new Component
	private Component component = new Component();

	/**
	 * @throws Exception
	 * 
	 */
	public WebServices(NavigationLightsController navLightsController,
			UnderwaterLightsController underwaterLightsController,
			SternDriveMotorController sternMotorController, RudderController rudderController,
			GpsReceiver gpsReceiver, PathPlanningController pathPlanningController) {
		// Set caching directives to noCache and noStore
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
		component.getDefaultHost().attach(
				"/lighting",
				new LightControllerApplication(cacheDirectives,
						navLightsController, underwaterLightsController));

		// Attach the motor control application
		//TODO WebServices shall accept a parameter of auxiliary motor
		/* component.getDefaultHost()
				.attach("/motor",
						new AuxilirayMotorControllerApplication(cacheDirectives,
								auxiliaryMotorController));*/
		
		// Attach the motor control application
		component.getDefaultHost()
		.attach("/sternMotor",
				new SternMotorControllerApplication(cacheDirectives,
						sternMotorController));
		// Attach the rudder control application
		component.getDefaultHost().attach(
				"/rudder",
				new RudderControllerApplication(cacheDirectives,
						rudderController));

		// Attach the GPS receiver application
		component.getDefaultHost().attach("/gps",
				new GPSReceiverApplication(cacheDirectives, gpsReceiver));

		// Attach the motion control feedback application
		component.getDefaultHost().attach(
				"/motionControlPage",
				new MotionControlPageApplication(cacheDirectives,
						sternMotorController, rudderController));

		// Attach the motion control feedback application
		component.getDefaultHost().attach(
				"/lightControlPage",
				new LightControlPageApplication(cacheDirectives,
						navLightsController, underwaterLightsController));
		
		// Attach the Path Planner (Autopilot) application
				component.getDefaultHost().attach(
						"/pathPlanner",
						new PathControllerApplication(cacheDirectives,
								sternMotorController, rudderController,gpsReceiver,pathPlanningController));
	}

	public void start() throws Exception {
		// Start the component
		component.start();
	}

	public void stop() throws Throwable {
		finalize();
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		component.stop();
	}
}
