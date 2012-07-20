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

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.marssa.demonstrator.tests.web_services.WebServicesTest.LightState;
import org.marssa.footprint.datatypes.MBoolean;

@Path("/test/lighting")
@RequestScoped
public class LightControllerTestApplication {

	private final LightState currentLightState = new LightState();

	@GET
	@Produces("application/json")
	@Path("/navigationLights")
	public String getNavLights() {
		return currentLightState.navLightState.toJSON().toString();
	}

	@POST
	@Produces("text/plain")
	@Path("/navigationLights/{state}")
	public String setNavLights(@PathParam("state") String state) {
		boolean value = Boolean.parseBoolean(state);
		currentLightState.navLightState = new MBoolean(value);
		return "You entered the following parameter:\n" + value;
	}

	@GET
	@Produces("application/json")
	@Path("/underwaterLights")
	public String getUnderwaterLights() {
		return currentLightState.underwaterLightState.toJSON().toString();
	}

	@POST
	@Produces("text/plain")
	@Path("/underwaterLights/{state}")
	public String setUnderwaterLights(@PathParam("state") String state) {
		boolean value = Boolean.parseBoolean(state);
		currentLightState.underwaterLightState = new MBoolean(value);
		return "You entered the following parameter:\n" + value;
	}

	@GET
	@Produces("application/json")
	@Path("statusAll")
	public String getAllLights() {
		return "{\"navLights\":"
				+ WebServicesTest.getLightState().navLightState.toJSON()
				+ ",\"underwaterLights\":"
				+ WebServicesTest.getLightState().underwaterLightState.toJSON()
				+ "}";
	}
}