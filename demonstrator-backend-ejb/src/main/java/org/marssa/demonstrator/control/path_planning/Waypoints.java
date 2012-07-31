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
package org.marssa.demonstrator.control.path_planning;

import java.util.ArrayList;
import java.util.List;

import org.marssa.footprint.datatypes.MString;

import flexjson.JSON;
import flexjson.JSONSerializer;

public class Waypoints {

	private final List<Waypoint> waypoints = new ArrayList<Waypoint>();

	// private final List<Waypoint> waypoints = wayPointBean.getWaypointsList();

	@JSON
	public List<Waypoint> getWaypoints() {
		return new ArrayList<Waypoint>(waypoints);
	}

	public void setWaypoints(List<Waypoint> waypoints) {
		this.waypoints.clear();
		this.waypoints.addAll(waypoints);
	}

	/**
	 * Convert this object to a JSON object for representation
	 */
	public MString toJSON() {
		return new MString(new JSONSerializer().deepSerialize(this));
	}
}