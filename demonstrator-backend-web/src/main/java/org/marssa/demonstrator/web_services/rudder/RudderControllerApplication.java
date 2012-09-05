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
package org.marssa.demonstrator.web_services.rudder;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.marssa.demonstrator.beans.RudderControllerBean;
import org.marssa.demonstrator.constants.Constants;
import org.marssa.footprint.datatypes.MBoolean;
import org.marssa.footprint.exceptions.NoConnection;

@Path("/rudder")
public class RudderControllerApplication {

	@Inject
	RudderControllerBean rudderControllerBean;

	@GET
	@Produces("application/json")
	public String getAngle() throws NoConnection {
		return rudderControllerBean.getRudderController().getAngle().toJSON()
				.toString();
	}

	@PUT
	@Produces("text/plain")
	@Path("/centre")
	public String centre() throws NoConnection, InterruptedException {
		rudderControllerBean.getRudderController().rotateToCentre();
		return "Rotating the rudder to the centre";
	}

	@PUT
	@Produces("text/plain")
	@Path("/rotate/{direction}")
	public String rotate(@PathParam("direction") boolean direction)
			throws NoConnection, InterruptedException {
		rudderControllerBean.getRudderController().rotate(
				new MBoolean(direction));
		return "Rotating the rudder in the direction set by direction = "
				+ direction;
	}

	@PUT
	@Produces("text/plain")
	@Path("/rotateMore/{direction}")
	public String rotateMore(@PathParam("direction") boolean direction)
			throws InterruptedException, NoConnection {
		rudderControllerBean.getRudderController().rotateMultiple(
				Constants.RUDDER.ROTATIONS, new MBoolean(direction));
		return "Rotating the rudder more in the direction set by = "
				+ direction;
	}

	// @PUT
	// @Produces("text/plain")
	// @Path("/rotateFull/{direction}")
	// public String rotateFull(@PathParam("direction") boolean direction)
	// throws InterruptedException, NoConnection {
	// rudderControllerBean.getRudderController().rotateExtreme(
	// new MBoolean(direction));
	// return "Rotating the rudder to the extreme in the direction set by = "
	// + direction;
	// }
}