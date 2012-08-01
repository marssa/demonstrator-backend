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
package org.marssa.demonstrator.control.navigation;

import org.json.JSONObject;

public class Waypoint {

	private String id;
	private String name;
	private double lat;

	public Waypoint() {
	}

	public Waypoint(String _id, String _name, double lat, double lng) {
		this.id = _id;
		this.name = _name;
		this.lat = lat;
		this.lng = lng;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLng() {
		return lng;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}

	private double lng;

	/**
	 * Convert this object to a JSON object for representation
	 */
	public JSONObject toJSON() {
		try {
			JSONObject jsonobj = new JSONObject();
			jsonobj.put("id", this.id);
			jsonobj.put("lat", this.lat);
			jsonobj.put("lng", this.lng);
			jsonobj.put("name", this.name);
			return jsonobj;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Convert this object to a string for representation
	 */
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("id:");
		sb.append(this.id);
		sb.append(",lat:");
		sb.append(this.lat);
		sb.append(",lng:");
		sb.append(this.lng);
		sb.append(",name:");
		sb.append(this.name);
		return sb.toString();
	}

}