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

import org.marssa.footprint.datatypes.composite.Coordinate;
import org.marssa.footprint.datatypes.composite.Latitude;
import org.marssa.footprint.datatypes.composite.Longitude;
import org.marssa.footprint.datatypes.decimal.DegreesDecimal;
import org.marssa.footprint.exceptions.OutOfRange;
import org.restlet.Application;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.data.CacheDirective;
import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.routing.Router;

public class GPSReceiverTestApplication extends Application {
	
	private final ArrayList<CacheDirective> cacheDirectives = new ArrayList<CacheDirective>();
	
	public GPSReceiverTestApplication(ArrayList<CacheDirective> cacheDirectives) {
		this.cacheDirectives.addAll(cacheDirectives);
	}
	
	/**
     * Creates a root Restlet that will receive all incoming calls.
     */
    @Override
    public synchronized Restlet createInboundRoot() {
        Router router = new Router(getContext());
        
        // Create the navigation lights state handler
        Restlet coordinates = new Restlet() {
        	@Override
            public void handle(Request request, Response response) {
        		//TODO Handle parseException since parseBoolean doesn't check for and raise this exception
        		try {
        			Coordinate coordinate = new Coordinate(new Latitude(new DegreesDecimal(20.5)), new Longitude(new DegreesDecimal(129.8)));
        			response.setCacheDirectives(cacheDirectives);
					response.setEntity( coordinate.toJSON().getContents() , MediaType.APPLICATION_JSON);
				} catch (OutOfRange e) {
					response.setCacheDirectives(cacheDirectives);
					response.setStatus(Status.CLIENT_ERROR_REQUESTED_RANGE_NOT_SATISFIABLE, "No connection error has been returned");
					e.printStackTrace();
				}
            }
        };
        
        router.attach("/coordinates",coordinates);
        
        return router;
    }
}