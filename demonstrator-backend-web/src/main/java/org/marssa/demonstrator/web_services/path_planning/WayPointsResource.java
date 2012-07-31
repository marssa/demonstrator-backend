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


/**
 * Resource that manages a list of items.
 * 
 */
public class WayPointsResource { // extends BaseResource {

	// /**
	// * Handle POST requests: create a new item.
	// *
	// * @throws OutOfRange
	// * @throws JSONException
	// */
	// @Post("json")
	// public Representation acceptItem(JsonRepresentation entity)
	// throws JSONException {
	// Representation result = null;
	// /*
	// * Form form = new Form(entity); String waypointID =
	// * form.getFirstValue("waypointID"); String waypointName =
	// * form.getFirstValue("waypointName"); String waypointLat =
	// * form.getFirstValue("waypointLat"); String waypointLon =
	// * form.getFirstValue("waypointLon");
	// */
	//
	// JSONObject jsonInput = null;
	// jsonInput = entity.getJsonObject();
	// JSONArray the_json_array = jsonInput.getJSONArray("waypoints");
	// int size = the_json_array.length();
	// ArrayList<JSONObject> arrays = new ArrayList<JSONObject>();
	// for (int i = 0; i < size; i++) {
	// JSONObject another_json_object = the_json_array.getJSONObject(i);
	// String waypointID = another_json_object.getString("id");
	// String waypointName = another_json_object.getString("name");
	// String waypointLat = another_json_object.getString("lat");
	// String waypointLon = another_json_object.getString("lng");
	// double lat = 0.0;
	// double lon = 0.0;
	//
	// if ((waypointLat != "") && (waypointLon != "")) {
	// lat = Double.parseDouble(waypointLat);
	// lon = Double.parseDouble(waypointLon);
	// }
	//
	// // Register the new item if one is not already registered.
	// if (!getWaypoints().containsKey(waypointName)
	// && getWaypoints().putIfAbsent(waypointName,
	// new Waypoint(waypointID, waypointName, lat, lon)) == null) {
	// // Set the response's status and entity
	// setStatus(Status.SUCCESS_CREATED);
	// Representation rep = new StringRepresentation("Item created",
	// MediaType.TEXT_PLAIN);
	// // Indicates where is located the new resource.
	// // rep.setIdentifier(getRequest().getResourceRef().getIdentifier()
	// // + "/" + waypointName);
	// result = rep;
	// } else { // Item is already registered.
	// setStatus(Status.CLIENT_ERROR_NOT_FOUND);
	// result = generateErrorRepresentation("Item " + waypointName
	// + " already exists.", "1");
	// }
	//
	// }
	//
	// return result;
	// }
	//
	// /**
	// * Generate an XML representation of an error response.
	// *
	// * @param errorMessage
	// * the error message.
	// * @param errorCode
	// * the error code.
	// */
	// private Representation generateErrorRepresentation(String errorMessage,
	// String errorCode) {
	// DomRepresentation result = null;
	// // This is an error
	// // Generate the output representation
	// try {
	// result = new DomRepresentation(MediaType.TEXT_XML);
	// // Generate a DOM document representing the list of
	// // items.
	// Document d = result.getDocument();
	//
	// Element eltError = d.createElement("error");
	//
	// Element eltCode = d.createElement("code");
	// eltCode.appendChild(d.createTextNode(errorCode));
	// eltError.appendChild(eltCode);
	//
	// Element eltMessage = d.createElement("message");
	// eltMessage.appendChild(d.createTextNode(errorMessage));
	// eltError.appendChild(eltMessage);
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	//
	// return result;
	// }
	//
	// /**
	// * Returns a listing of all registered items.
	// */
	// // @Get("xml")
	// // public Representation toXml() {
	// // // Generate the right representation according to its media type.
	// // try {
	// // DomRepresentation representation = new DomRepresentation(
	// // MediaType.TEXT_XML);
	// //
	// // // Generate a DOM document representing the list of
	// // // items.
	// // Document d = representation.getDocument();
	// // Element r = d.createElement("waypoints");
	// // d.appendChild(r);
	// // for (Waypoint item : getWaypoints().values()) {
	// // Element eltItem = d.createElement("waypoint");
	// //
	// // Element eltName = d.createElement("Id");
	// // eltName.appendChild(d.createTextNode(item.getId()));
	// // eltItem.appendChild(eltName);
	// //
	// // Element eltDescription = d.createElement("Name");
	// // eltDescription.appendChild(d.createTextNode(item.getName()));
	// // eltItem.appendChild(eltDescription);
	// //
	// // Element eltLong = d.createElement("Longitude");
	// // eltDescription.appendChild(d.createTextNode(item.getLng()));
	// // eltItem.appendChild(eltLong);
	// //
	// // Element eltLat = d.createElement("Latitude");
	// // eltDescription.appendChild(d.createTextNode(item.getLat()));
	// // eltItem.appendChild(eltLat);
	// //
	// // r.appendChild(eltItem);
	// // }
	// // d.normalizeDocument();
	// //
	// // // Returns the XML representation of this document.
	// // return representation;
	// // } catch (IOException e) {
	// // e.printStackTrace();
	// // }
	// //
	// // return null;
	// // }

}