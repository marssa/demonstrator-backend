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
package org.marssa.demonstrator.web_services.lighting;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.marssa.demonstrator.beans.LightControllerBean;
import org.marssa.footprint.datatypes.MBoolean;
import org.marssa.footprint.exceptions.NoConnection;

@Path("/lights")
@RequestScoped
public class LightControllerApplication {

	@Inject
	private LightControllerBean lightControllerBean;

	@GET
	@Produces("application/json")
	@Path("/navigation")
	public String getNavLights() {
		return lightControllerBean.getNavigationLightsController()
				.getNavigationLightState().toJSON().toString();
	}

	@POST
	@Produces("text/plain")
	@Path("/navigation/{state}")
	public String setNavLights(@PathParam("state") String state)
			throws NoConnection {
		// TODO Handle parseException
		// parseBoolean doesn't check for and raise this exception
		boolean value = Boolean.parseBoolean(state);
		lightControllerBean.getNavigationLightsController()
				.setNavigationLightState(new MBoolean(value));
		return "Set navigation lights state to " + (value ? "on" : "off");
	}

	@GET
	@Produces("application/json")
	@Path("/underwater")
	public String getUnderwaterLights() {
		return lightControllerBean.getUnderWaterLightsController()
				.getUnderwaterLightState().toJSON().toString();
	}

	@POST
	@Produces("text/plain")
	@Path("/underwater/{state}")
	public String setUnderwaterLights(@PathParam("state") String state)
			throws NoConnection {
		// TODO Handle parseException
		// parseBoolean doesn't check for and raise this exception
		boolean value = Boolean.parseBoolean(state);
		lightControllerBean.getUnderWaterLightsController()
				.setUnderwaterLightState(new MBoolean(value));
		return "Set underwater lights state to " + (value ? "on" : "off");
	}
}