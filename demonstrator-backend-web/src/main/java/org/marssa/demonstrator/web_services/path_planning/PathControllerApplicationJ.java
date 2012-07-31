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
package org.marssa.demonstrator.web_services.path_planning;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.marssa.demonstrator.beans.PathPlanningBean;
import org.marssa.footprint.exceptions.ConfigurationError;
import org.marssa.footprint.exceptions.NoConnection;
import org.marssa.footprint.exceptions.OutOfRange;

@Path("/test/PathPlanning")
public class PathControllerApplicationJ {

	@Inject
	PathPlanningBean pathPlanningBean;

	// private final PathPlanningController pathPlanningController;

	// private final ConcurrentMap<String, Waypoint> waypoints = new
	// ConcurrentHashMap<String, Waypoint>();

	@GET
	@Path("StartFollowing")
	@Consumes("application/json")
	@Produces("text/plain")
	public String StartFollowing() throws NoConnection, InterruptedException,
			ConfigurationError, OutOfRange {

		// pathPlanningBean.wps.setWaypoints();

		// pathPlanningBean.getPathPlanningController().setPathList(
		// pathPlanningBean.getWaypointList());

		pathPlanningBean.getPathPlanningController().startFollowingPath();

		return "The System has started following the path";
	}
}
