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
package org.marssa.demonstrator.tests.web_services;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.marssa.demonstrator.tests.control.path_planning.PathPlanningControllerTest;
import org.marssa.demonstrator.tests.control.path_planning.WayPointsResourceTest;
import org.marssa.demonstrator.web_services.path_planning.WayPointsResource;
import org.marssa.demonstrator.web_services.path_planning.Waypoint;
import org.marssa.footprint.exceptions.ConfigurationError;
import org.marssa.footprint.exceptions.NoConnection;
import org.marssa.footprint.exceptions.OutOfRange;
import org.restlet.Application;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.data.CacheDirective;
import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.routing.Router;

public class PathControllerTestApplication extends Application {

	private ArrayList<CacheDirective> cacheDirectives;
	private PathPlanningControllerTest pathPlanningController;
		private final ConcurrentMap<String, Waypoint> waypoints =   
            new ConcurrentHashMap<String, Waypoint>();  
	
	public PathControllerTestApplication(ArrayList<CacheDirective> cacheDirectives, PathPlanningControllerTest pathPlanningController) {
		this.cacheDirectives = cacheDirectives;
		this.pathPlanningController = pathPlanningController;
	}

	
	public ConcurrentMap<String, Waypoint> getWaypoints() {  
		ArrayList<Waypoint> waypointList = new ArrayList<Waypoint>(waypoints.values());
		//pathPlanningController.setPathList(waypointList);
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
        			ArrayList<Waypoint> waypointList = new ArrayList<Waypoint>(waypoints.values());
        			pathPlanningController.setPathList(waypointList);
        			pathPlanningController.startFollowingPath();
        			response.setEntity("The system has started following the path ", MediaType.TEXT_PLAIN);
        		} catch (NumberFormatException e) {
        			response.setStatus(Status.CLIENT_ERROR_BAD_REQUEST, "The value of the speed resource has an incorrect format");
        		} catch (NoConnection e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ConfigurationError e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (OutOfRange e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
            }
        };
        
        Restlet comeHome = new Restlet() {
        	@Override
            public void handle(Request request, Response response) {
        		response.setCacheDirectives(cacheDirectives);
        		try {
        			//We here call upon the startfollowingpath method using the pathplanningcontroller instance.
        			pathPlanningController.returnHome();
        			response.setEntity("The system is now coming home ", MediaType.TEXT_PLAIN);
        		} catch (NumberFormatException e) {
        			response.setStatus(Status.CLIENT_ERROR_BAD_REQUEST, "The value of the speed resource has an incorrect format");
        		} catch (NoConnection e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ConfigurationError e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (OutOfRange e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
            }
        };
        
        Restlet reverseRoute = new Restlet() {
        	@Override
            public void handle(Request request, Response response) {
        		response.setCacheDirectives(cacheDirectives);
        		try {
        			//We here call upon the startfollowingpath method using the pathplanningcontroller instance.
        			pathPlanningController.reverseTheRoute();
        			response.setEntity("The system has now reveresed the route ", MediaType.TEXT_PLAIN);
        		} catch (NumberFormatException e) {
        			response.setStatus(Status.CLIENT_ERROR_BAD_REQUEST, "The value of the speed resource has an incorrect format");
        		} catch (NoConnection e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ConfigurationError e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (OutOfRange e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
            }
        };
                
        router.attach("/waypoints", WayPointsResourceTest.class);
        //the enterwaypoints method is called upon by the from end using a @post annotation. The waypointsresource class is used to receive the data.
        router.attach("/startFollowing",startFollowing);
        //The startFollowing method defined above is called upon when the front end initiates a request
        router.attach("/stopFollowing",stopFollowing);
        //The stopFollowing method defined above is called upon when the front end initiates a request
        router.attach("/comeHome",comeHome);
        router.attach("/reverseRoute",reverseRoute);
        return router;
    }
}