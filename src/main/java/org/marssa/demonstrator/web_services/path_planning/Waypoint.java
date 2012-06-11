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
import org.json.JSONObject;
import org.marssa.footprint.datatypes.composite.Coordinate;

public class Waypoint {

private String id = null;
private String name = null;
private Coordinate coordinate = null;

public Waypoint (String _id, String _name, Coordinate _coor)
{
	this.id = _id;
	this.name = _name;
	this.coordinate =_coor;
}

public String getId() {
	return id;
}

public void setId(String id) {
	this.id = id;
}

public Coordinate getCoordinate() {
	return coordinate;
}

public void setCoordinate(Coordinate coordinate) {
	this.coordinate = coordinate;
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

/**
 * Convert this object to a JSON object for representation
 */
public JSONObject toJSON() {
try{
 JSONObject jsonobj = new JSONObject();
 jsonobj.put("id", this.id);
 jsonobj.put("coordinate", this.coordinate);
 jsonobj.put("name", this.name);
 return jsonobj;
}catch(Exception e){
 return null;
}
}

/**
 * Convert this object to a string for representation
 */
public String toString() {
 StringBuffer sb = new StringBuffer();
 sb.append("id:");
 sb.append(this.id);
 sb.append("coordinate:");
 sb.append(this.coordinate);
 sb.append(",name:");
 sb.append(this.name);
 return sb.toString();
}
}