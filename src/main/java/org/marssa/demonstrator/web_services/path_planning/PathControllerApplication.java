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

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.marssa.demonstrator.control.electrical_motor.SternDriveMotorController;
import org.marssa.demonstrator.control.path_planning.PathPlanningController;
import org.marssa.demonstrator.control.rudder.RudderController;
import org.marssa.services.navigation.GpsReceiver;
import org.restlet.Application;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.data.CacheDirective;
import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.routing.Router;

public class PathControllerApplication extends Application {

	private ArrayList<CacheDirective> cacheDirectives;
	private SternDriveMotorController motorController;
	private RudderController rudderController;
	private GpsReceiver gpsReceiver;
	private PathPlanningController pathPlanningController;
		private final ConcurrentMap<String, Waypoint> waypoints =   
            new ConcurrentHashMap<String, Waypoint>();  
	
	public PathControllerApplication(ArrayList<CacheDirective> cacheDirectives, SternDriveMotorController motorController, 
			RudderController rudderController, GpsReceiver gpsReceiver, PathPlanningController pathPlanningController) {
		this.cacheDirectives = cacheDirectives;
		this.motorController = motorController;
		this.rudderController = rudderController;
		this.gpsReceiver = gpsReceiver;
		this.pathPlanningController =pathPlanningController;
	}

	
	public ConcurrentMap<String, Waypoint> getWaypoints() {  
		ArrayList<Waypoint> waypointList = new ArrayList<Waypoint>(waypoints.values());
		pathPlanningController.setPathList(waypointList);
		return waypoints;  
    }  
	
    /**
     * Creates a root Restlet that will receive all incoming calls.
     */
    @Override
    public synchronized Restlet createInboundRoot() {
        Router router = new Router(getContext());
         // This RESTLET is used to tell the path following controller to stop the path following procedure.
        Restlet stopFollowing = new Restlet() {
        	@Override
            public void handle(Request request, Response response) {
        		response.setCacheDirectives(cacheDirectives);
        		try {
        			//We here call upon the stopfollowingpath method using the pathplanningcontroller instance.
        			pathPlanningController.stopFollowingPath();
        			response.setEntity("The system has stopped following the path ", MediaType.TEXT_PLAIN);
        		} catch (NumberFormatException e) {
        			response.setStatus(Status.CLIENT_ERROR_BAD_REQUEST, "The value of the speed resource has an incorrect format");
        		} 
            }
        };
        
        // Start the path following
     // This RESTLET is used to tell the path following controller to initiate the path following procedure.
        Restlet startFollowing = new Restlet() {
        	@Override
            public void handle(Request request, Response response) {
        		response.setCacheDirectives(cacheDirectives);
        		try {
        			//We here call upon the startfollowingpath method using the pathplanningcontroller instance.
        			pathPlanningController.startFollowingPath();
        			response.setEntity("The system has started following the path ", MediaType.TEXT_PLAIN);
        		} catch (NumberFormatException e) {
        			response.setStatus(Status.CLIENT_ERROR_BAD_REQUEST, "The value of the speed resource has an incorrect format");
        		} 
            }
        };
        
              
                
        router.attach("/enterwaypoints", WayPointResource.class);
        //the enterwaypoints method is called upon by the from end using a @post annotation. The waypointsresource class is used to receive the data.
        router.attach("/startFollowing",startFollowing);
        //The startFollowing method defined above is called upon when the front end initiates a request
        router.attach("/stopFollowing",stopFollowing);
        //The stopFollowing method defined above is called upon when the front end initiates a request
        
        return router;
    }
}