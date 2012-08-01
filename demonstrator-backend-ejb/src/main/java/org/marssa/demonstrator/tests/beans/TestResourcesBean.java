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
package org.marssa.demonstrator.tests.beans;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.marssa.demonstrator.beans.RudderControllerBean;
import org.marssa.demonstrator.control.navigation.PathPlanningController;
import org.marssa.demonstrator.tests.control.GPSReceiverTest;
import org.marssa.demonstrator.tests.control.RudderControllerTest;
import org.marssa.demonstrator.tests.control.SternDriveMotorControllerTest;
import org.marssa.footprint.datatypes.MBoolean;
import org.marssa.footprint.datatypes.decimal.MDecimal;
import org.marssa.footprint.exceptions.ConfigurationError;
import org.marssa.footprint.exceptions.NoConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
@Singleton
public class TestResourcesBean {

	private static final Logger logger = LoggerFactory
			.getLogger(TestResourcesBean.class.getName());

	@Inject
	RudderControllerBean rudderControllerBean;

	private SternDriveMotorControllerTest motorController;

	private PathPlanningController pathPlanningController;

	private MBoolean navLightState = new MBoolean(false);
	private MBoolean underwaterLightState = new MBoolean(false);
	private MDecimal rudderAngle = new MDecimal(0.0f);

	/**
	 * 
	 */
	public TestResourcesBean() {
		// TODO Auto-generated constructor stub
	}

	@PostConstruct
	private void init() throws ConfigurationError, NoConnection,
			InterruptedException {
		logger.info("Initializing Test Resources Bean");

		logger.info("Initialising Path Planning controller ... ");
		RudderControllerTest rudderController = new RudderControllerTest(
				rudderControllerBean.getRudderController());

		GPSReceiverTest gpsReceiver = new GPSReceiverTest(rudderController);

		motorController = new SternDriveMotorControllerTest();

		pathPlanningController = new PathPlanningController(motorController,
				rudderController, gpsReceiver);

		logger.info("Path Planning controller initialised successfully");

		logger.info("Test Resources Bean initialized successfully");
	}

	@PreDestroy
	private void destroy() {
		logger.info("Destroying Motor Controller Bean");
		// TODO Add unimplemented method
		logger.info("Destroyed Motor Controller Bean");
	}

	public SternDriveMotorControllerTest getMotorController() {
		return motorController;
	}

	public PathPlanningController getPathPlanningController() {
		return pathPlanningController;
	}

	public MDecimal getRudderAngle() {
		return rudderAngle;
	}

	public void setRudderAngle(MDecimal newRudderAngle) {
		rudderAngle = newRudderAngle;
	}

	public MBoolean getNavigationLightState() {
		return navLightState;
	}

	public void setNavigationLightState(MBoolean newLightState) {
		navLightState = newLightState;
	}

	public MBoolean getUnderwaterLightState() {
		return underwaterLightState;
	}

	public void setUnderwaterLightState(MBoolean newLightState) {
		underwaterLightState = newLightState;
	}
}
