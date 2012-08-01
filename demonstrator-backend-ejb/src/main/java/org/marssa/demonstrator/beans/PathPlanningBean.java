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

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.marssa.demonstrator.control.navigation.PathPlanningController;
import org.marssa.footprint.exceptions.ConfigurationError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
@Singleton
public class PathPlanningBean {

	private static final Logger logger = LoggerFactory
			.getLogger(PathPlanningBean.class.getName());

	@Inject
	MotorControllerBean motorControllerBean;

	@Inject
	GPSReceiverBean gpsReceiverBean;

	@Inject
	RudderControllerBean rudderControllerBean;

	private PathPlanningController pathPlanningController;

	/**
	 * 
	 */
	public PathPlanningBean() {
		// TODO Auto-generated constructor stub
	}

	@PostConstruct
	private void init() throws ConfigurationError {
		logger.info("Initializing Path Planner Controller Bean");
		pathPlanningController = new PathPlanningController(
				motorControllerBean.getSternDriveMotorController(),
				rudderControllerBean.getRudderController(),
				gpsReceiverBean.getGPSReceiver());
		logger.info("Initialized Path Planner Controller Bean");

		// wps.setWaypoints(waypoints);
	}

	// public List<Waypoint> getWaypointarraylist() {
	// return waypointarraylist;
	// }

	@PreDestroy
	private void destroy() {
		logger.info("Destroying Motor Controller Bean");
		// TODO Add unimplemented method
		logger.info("Destroyed Motor Controller Bean");
	}

	public PathPlanningController getPathPlanningController() {
		return pathPlanningController;
	}

}
