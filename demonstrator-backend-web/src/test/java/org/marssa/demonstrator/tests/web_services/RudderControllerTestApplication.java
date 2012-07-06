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

import org.marssa.footprint.datatypes.decimal.MDecimal;
import org.restlet.Application;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.data.CacheDirective;
import org.restlet.data.MediaType;
import org.restlet.routing.Router;

public class RudderControllerTestApplication extends Application {
	
	private final ArrayList<CacheDirective> cacheDirectives = new ArrayList<CacheDirective>();
	
	public RudderControllerTestApplication(ArrayList<CacheDirective> cacheDirectives) {
		this.cacheDirectives.addAll(cacheDirectives);
	}
	
	/**
     * Creates a root Restlet that will receive all incoming calls.
     */
    @Override
    public synchronized Restlet createInboundRoot() {
        Router router = new Router(getContext());
        
        // Create the rotation handler
        Restlet rotate = new Restlet() {
        	@Override
            public void handle(Request request, Response response) {
    			//TODO Handle parseException since parseBoolean doesn't check for and raise this exception
    			boolean direction = Boolean.parseBoolean(request.getAttributes().get("direction").toString());
    			if(direction)
    				WebServicesTest.setRudderAngle(new MDecimal(WebServicesTest.getRudderAngle().doubleValue() + 1.0));
				else
					WebServicesTest.setRudderAngle(new MDecimal(WebServicesTest.getRudderAngle().doubleValue() - 1.0));
				response.setCacheDirectives(cacheDirectives);
    			response.setEntity("Rotating the rudder in the direction set by direction = " + direction, MediaType.TEXT_PLAIN);
            }
        };
        
        // Create the rotation handler
        Restlet rotateMore = new Restlet() {
        	@Override
            public void handle(Request request, Response response) {
    			//TODO Handle parseException since parseBoolean doesn't check for and raise this exception
    			boolean direction = Boolean.parseBoolean(request.getAttributes().get("direction").toString());
    			if(direction)
    			WebServicesTest.setRudderAngle(new MDecimal(WebServicesTest.getRudderAngle().doubleValue() + 5.0));
				else
					WebServicesTest.setRudderAngle(new MDecimal(WebServicesTest.getRudderAngle().doubleValue() - 5.0));
    			response.setCacheDirectives(cacheDirectives);
    			response.setEntity("Rotating the rudder MORE in the direction set by direction = " + direction, MediaType.TEXT_PLAIN);
            }
        };
        
        // Create the rotation handler
        //TODO Change this to a Resource
        Restlet angle = new Restlet() {
        	@Override
            public void handle(Request request, Response response) {
        		response.setCacheDirectives(cacheDirectives);
    			response.setEntity(WebServicesTest.getRudderAngle().toString(), MediaType.TEXT_PLAIN);
            }
        };
        
        // Create the rotation handler to rotate to the extremes
        Restlet rotateFull = new Restlet() {
        	@Override
            public void handle(Request request, Response response) {
    			//TODO Handle parseException since parseBoolean doesn't check for and raise this exception
    			boolean direction = Boolean.parseBoolean(request.getAttributes().get("direction").toString());
    			if(direction)
    				WebServicesTest.setRudderAngle(new MDecimal(30.0));
				else
    				WebServicesTest.setRudderAngle(new MDecimal(-30.0));
    			response.setCacheDirectives(cacheDirectives);
    			response.setEntity("Rotating the rudder to the extreme = " + direction, MediaType.TEXT_PLAIN);
            }
        };
        
        router.attach("/angle", angle);
        router.attach("/rotate/{direction}", rotate);
        router.attach("/rotateMore/{direction}", rotateMore);
        router.attach("/rotateFull/{direction}", rotateFull);
        
        return router;
    }
}