package org.marssa.demonstrator.tests.control.path_planning;


import java.io.IOException;  
import java.util.ArrayList;

import org.marssa.demonstrator.web_services.path_planning.Waypoint;
import org.marssa.footprint.datatypes.composite.Coordinate;
import org.marssa.footprint.datatypes.composite.Latitude;
import org.marssa.footprint.datatypes.composite.Longitude;
import org.marssa.footprint.datatypes.decimal.DegreesDecimal;
import org.marssa.footprint.exceptions.OutOfRange;
import org.restlet.data.Form;  
import org.restlet.representation.Representation;  
import org.restlet.resource.ClientResource;  
import org.restlet.resource.ResourceException;  
  
public class PathPlannerWebservicesClient {  
  
    public static void main(String[] args) throws IOException,  
            ResourceException, OutOfRange {  
        // Define our Restlet client resources.  
        ClientResource itemsResource = new ClientResource(  
                "http://localhost:8182/pathPlanner/waypoints");  
        ClientResource itemResource = null;  
  
        // Create a new item  
        ArrayList<Waypoint> wayPointList =  new ArrayList<Waypoint>();
        Waypoint item1 = new Waypoint("01","Start Point",new Coordinate(new Latitude(new DegreesDecimal(35.983267)) , new Longitude(new DegreesDecimal(14.387419))));  
        Waypoint item2 = new Waypoint("02","End Point",new Coordinate(new Latitude(new DegreesDecimal(36.000184)) , new Longitude(new DegreesDecimal(14.376021))));  
        
        
        Representation r = itemsResource.post(getRepresentation(item1));  
        if (itemsResource.getStatus().isSuccess()) 
        {  
        	System.out.println("Success......");
        	ClientResource startFollowing = new ClientResource(  
                    "http://localhost:8182/pathPlanner/startFollowing"); 
        	startFollowing.get();
        }  
  
       
    }  
  
    /** 
     * Prints the resource's representation. 
     *  
     * @param clientResource 
     *            The Restlet client resource. 
     * @throws IOException 
     * @throws ResourceException 
     */  
    public static void get(ClientResource clientResource) throws IOException,  
            ResourceException {  
        clientResource.get();  
        if (clientResource.getStatus().isSuccess()  
                && clientResource.getResponseEntity().isAvailable()) {  
            clientResource.getResponseEntity().write(System.out);  
        }  
    }  
  
    /** 
     * Returns the Representation of an item. 
     *  
     * @param item 
     *            the item. 
     *  
     * @return The Representation of the item. 
     */  
    public static Representation getRepresentation(Waypoint item) {  
        // Gathering informations into a Web form.  
        Form form = new Form();  
        form.add("waypointID", item.getId());  
        form.add("waypointName", item.getName());  
        form.add("waypointLat", item.getLat()); 
        form.add("waypointLon", item.getLon()); 
        return form.getWebRepresentation();  
    }  
  
}  