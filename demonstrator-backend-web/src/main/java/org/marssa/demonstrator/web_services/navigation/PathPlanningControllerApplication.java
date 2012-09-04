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
package org.marssa.demonstrator.web_services.navigation;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.json.JSONException;
import org.marssa.demonstrator.beans.PathPlanningBean;
import org.marssa.demonstrator.control.navigation.Waypoints;
import org.marssa.footprint.exceptions.ConfigurationError;
import org.marssa.footprint.exceptions.NoConnection;
import org.marssa.footprint.exceptions.OutOfRange;

import flexjson.JSONSerializer;

@Path("/path-planner")
public class PathPlanningControllerApplication {

	@Inject
	PathPlanningBean pathPlanningBean;

	@GET
	@Produces("application/json")
	@Path("/path")
	public String getPath() {
		return "{\"path\":"
				+ new JSONSerializer().deepSerialize(pathPlanningBean
						.getPathPlanningController().getPath()) + "}";
	}

	@GET
	@Produces("application/json")
	@Path("/waypoints")
	public String getWaypoints() {
		return "{\"waypoints\":"
				+ new JSONSerializer().deepSerialize(pathPlanningBean
						.getPathPlanningController().getWayPoints()) + "}";
	}

	@PUT
	@Path("/waypoints")
	@Consumes("application/json")
	@Produces("text/plain")
	public String getWaypoints(Waypoints waypoints) throws JSONException,
			OutOfRange, NullPointerException {

		pathPlanningBean.getPathPlanningController().setWayPoints(
				waypoints.getWaypoints());

		return "Received " + waypoints.getWaypoints().size() + " waypoints";
	}

	@PUT
	@Produces("text/plain")
	@Path("/start-following")
	public String startFollowing() throws NoConnection, InterruptedException,
			ConfigurationError, OutOfRange {
		pathPlanningBean.getPathPlanningController().startFollowingPath();

		return "The system has started following the path";
	}

	@PUT
	@Produces("text/plain")
	@Path("/stop-following")
	public String stopFollowing() throws NoConnection, InterruptedException,
			ConfigurationError, OutOfRange {
		pathPlanningBean.getPathPlanningController().stopFollowingPath();
		return "The system has stopped following the path";
	}

	@PUT
	@Produces("text/plain")
	@Path("/come-home")
	public String comeHome() throws NoConnection, InterruptedException,
			ConfigurationError, OutOfRange {
		pathPlanningBean.getPathPlanningController().returnHome();
		return "The system is now coming home";
	}

	@PUT
	@Produces("text/plain")
	@Path("/reverse-route")
	public String reverseRoute() throws NoConnection, InterruptedException,
			ConfigurationError, OutOfRange {
		pathPlanningBean.getPathPlanningController().reverseTheRoute();
		return "The system has reversed the route";
	}
}