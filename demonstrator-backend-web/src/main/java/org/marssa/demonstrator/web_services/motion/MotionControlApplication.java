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
package org.marssa.demonstrator.web_services.motion;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.marssa.demonstrator.beans.MotorControllerBean;
import org.marssa.demonstrator.beans.RudderControllerBean;
import org.marssa.footprint.datatypes.decimal.MDecimal;
import org.marssa.footprint.exceptions.NoConnection;

@Path("/motion")
public class MotionControlApplication {

	@Inject
	MotorControllerBean motorControllerBean;

	@Inject
	RudderControllerBean rudderControllerBean;

	@GET
	@Produces("application/json")
	@Path("/all")
	public String getRudderAndSpeed() throws NoConnection {
		MDecimal motorSpeed = motorControllerBean
				.getSternDriveMotorController().getSpeed();
		MDecimal rudderAngle = rudderControllerBean.getRudderController()
				.getAngle();
		return "{\"motor\":" + motorSpeed.toJSON().toString() + ",\"rudder\":"
				+ rudderAngle.toJSON().toString() + "}";
	}
}