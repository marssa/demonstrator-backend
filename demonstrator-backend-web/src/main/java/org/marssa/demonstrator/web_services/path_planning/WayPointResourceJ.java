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
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.json.JSONException;
import org.marssa.demonstrator.beans.PathPlanningBean;
import org.marssa.demonstrator.control.path_planning.Waypoints;
import org.marssa.footprint.exceptions.OutOfRange;

@Path("/test/PathPlanning")
public class WayPointResourceJ {

	@Inject
	private PathPlanningBean pathPlanningBean;

	@PUT
	@Path("GetWayPoints")
	@Consumes("application/json")
	@Produces("text/plain")
	public String acceptItem(Waypoints waypoints) throws JSONException,
			OutOfRange, NullPointerException {

		pathPlanningBean.getPathPlanningController().setPathList(
				waypoints.getWaypoints());

		return "waypoints ok";

	}
}
