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


public class WayPointResource {// extends BaseResource {
//
// /** The underlying Item object. */
// Waypoint waypoint;
//
// /** The sequence of characters that identifies the resource. */
// String waypointName;
//
// @Override
// protected void doInit() throws ResourceException {
// // Get the "itemName" attribute value taken from the URI template
// // /items/{itemName}.
// this.waypointName = (String) getRequest().getAttributes().get(
// "waypointName");
//
// // Get the item directly from the "persistence layer".
// this.waypoint = getWaypoints().get(waypointName);
//
// setExisting(this.waypoint != null);
// }
//
// /**
// * Handle DELETE requests.
// */
// @Delete
// public void removeItem() {
// if (waypoint != null) {
// // Remove the item from the list.
// getWaypoints().remove(waypoint.getName());
// }
//
// // Tells the client that the request has been successfully fulfilled.
// setStatus(Status.SUCCESS_NO_CONTENT);
// }
//
// /**
// * Handle PUT requests.
// *
// * @throws IOException
// * @throws OutOfRange
// */
// @Put
// public void storeItem(Representation entity) throws IOException {
// // The PUT request updates or creates the resource.
// if (waypoint == null) {
// waypoint = new Waypoint("1", waypointName, 12.35, 12.35);
// }
//
// // Update the description.
// Form form = new Form(entity);
// waypoint.setName(form.getFirstValue("description"));
//
// if (getWaypoints().putIfAbsent(waypoint.getName(), waypoint) == null) {
// setStatus(Status.SUCCESS_CREATED);
// } else {
// setStatus(Status.SUCCESS_OK);
// }
// }
//
// @Get("xml")
// public Representation toXml() {
// try {
// DomRepresentation representation = new DomRepresentation(
// MediaType.TEXT_XML);
// // Generate a DOM document representing the item.
// Document d = representation.getDocument();
//
// Element eltItem = d.createElement("item");
// d.appendChild(eltItem);
// Element eltName = d.createElement("name");
// eltName.appendChild(d.createTextNode(waypoint.getName()));
// eltItem.appendChild(eltName);
//
// Element eltDescription = d.createElement("description");
// eltDescription.appendChild(d.createTextNode(waypoint.getName()));
// eltItem.appendChild(eltDescription);
//
// d.normalizeDocument();
//
// // Returns the XML representation of this document.
// return representation;
// } catch (IOException e) {
// e.printStackTrace();
// }
//
// return null;
// }
}