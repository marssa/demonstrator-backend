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

import java.io.IOException;

import org.marssa.footprint.datatypes.composite.Coordinate;
import org.marssa.footprint.datatypes.composite.Latitude;
import org.marssa.footprint.datatypes.composite.Longitude;
import org.marssa.footprint.datatypes.decimal.DegreesDecimal;
import org.marssa.footprint.exceptions.OutOfRange;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.ext.xml.DomRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
  
/** 
 * Resource that manages a list of items. 
 *  
 */  
public class WayPointsResource extends BaseResource {  
  
    /** 
     * Handle POST requests: create a new item. 
     * @throws OutOfRange 
     */  
    @Post  
    public Representation acceptItem(Representation entity) throws OutOfRange {  
        Representation result = null;  
        // Parse the given representation and retrieve pairs of  
        // "name=value" tokens.  
        Form form = new Form(entity);  
        String waypointID 	= form.getFirstValue("waypointID");  
        String waypointName = form.getFirstValue("waypointName");  
        String waypointLat 	= form.getFirstValue("waypointLat");  
        String waypointLon 	= form.getFirstValue("waypointLon");
        
        double lat =0.0;
        double lon =0.0;
        
        if((waypointLat != "")&&(waypointLon != ""))
        {
        	 lat = Double.parseDouble(waypointLat);
             lon = Double.parseDouble(waypointLon);
        }
       
        // Register the new item if one is not already registered.  
        if (!getWaypoints().containsKey(waypointName)  
                && getWaypoints().putIfAbsent(waypointName,  
                        new Waypoint(waypointID, waypointName,new Coordinate(new Latitude(new DegreesDecimal(lat)), new Longitude(new DegreesDecimal(lon))))) == null) {  
            // Set the response's status and entity  
            setStatus(Status.SUCCESS_CREATED);  
            Representation rep = new StringRepresentation("Item created",  
                    MediaType.TEXT_PLAIN);  
            // Indicates where is located the new resource.  
            //rep.setIdentifier(getRequest().getResourceRef().getIdentifier() + "/" + waypointName);  
            result = rep;  
        } else { // Item is already registered.  
            setStatus(Status.CLIENT_ERROR_NOT_FOUND);  
            result = generateErrorRepresentation("Item " + waypointName  
                    + " already exists.", "1");  
        }  
  
        return result;  
    }  
  
    /** 
     * Generate an XML representation of an error response. 
     *  
     * @param errorMessage 
     *            the error message. 
     * @param errorCode 
     *            the error code. 
     */  
    private Representation generateErrorRepresentation(String errorMessage,  
            String errorCode) {  
        DomRepresentation result = null;  
        // This is an error  
        // Generate the output representation  
        try {  
            result = new DomRepresentation(MediaType.TEXT_XML);  
            // Generate a DOM document representing the list of  
            // items.  
            Document d = result.getDocument();  
  
            Element eltError = d.createElement("error");  
  
            Element eltCode = d.createElement("code");  
            eltCode.appendChild(d.createTextNode(errorCode));  
            eltError.appendChild(eltCode);  
  
            Element eltMessage = d.createElement("message");  
            eltMessage.appendChild(d.createTextNode(errorMessage));  
            eltError.appendChild(eltMessage);  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
  
        return result;  
    }  
  
    /** 
     * Returns a listing of all registered items. 
     */  
    @Get("xml")  
    public Representation toXml() {  
        // Generate the right representation according to its media type.  
        try {  
            DomRepresentation representation = new DomRepresentation(  
                    MediaType.TEXT_XML);  
  
            // Generate a DOM document representing the list of  
            // items.  
            Document d = representation.getDocument();  
            Element r = d.createElement("items");  
            d.appendChild(r);  
            for (Waypoint item : getWaypoints().values()) {  
                Element eltItem = d.createElement("item");  
  
                Element eltName = d.createElement("name");  
                eltName.appendChild(d.createTextNode(item.getName()));  
                eltItem.appendChild(eltName);  
  
                Element eltDescription = d.createElement("description");  
                eltDescription.appendChild(d.createTextNode(item  
                        .getName()));  
                eltItem.appendChild(eltDescription);  
  
                r.appendChild(eltItem);  
            }  
            d.normalizeDocument();  
  
            // Returns the XML representation of this document.  
            return representation;  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
  
        return null;  
    }  
  
}  