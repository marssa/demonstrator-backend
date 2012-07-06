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

import org.marssa.demonstrator.tests.web_services.WebServicesTest.LightState;
import org.marssa.footprint.datatypes.MBoolean;
import org.restlet.Application;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.data.CacheDirective;
import org.restlet.data.MediaType;
import org.restlet.routing.Router;

public class LightControllerTestApplication extends Application {
	
	private final ArrayList<CacheDirective> cacheDirectives = new ArrayList<CacheDirective>();
	private LightState currentLightState = new LightState();
	
	public LightControllerTestApplication(ArrayList<CacheDirective> cacheDirectives) {
		this.cacheDirectives.addAll(cacheDirectives);
	}
	
    /**
     * Creates a root Restlet that will receive all incoming calls.
     */
    @Override
    public synchronized Restlet createInboundRoot() {
        Router router = new Router(getContext());
        
        // Create the navigation lights state handler
        Restlet setNavLights = new Restlet() {
        	@Override
            public void handle(Request request, Response response) {
        		boolean value = Boolean.parseBoolean(request.getAttributes().get("state").toString());
        		currentLightState.navLightState = new MBoolean(value);
        		response.setCacheDirectives(cacheDirectives);
    			response.setEntity("You entered the following parameter:\n" + value, MediaType.TEXT_PLAIN);
            }
        };
        
        // Create the navigation lights state handler
        Restlet getNavLights = new Restlet() {
        	@Override
            public void handle(Request request, Response response) {
        		response.setCacheDirectives(cacheDirectives);
    			response.setEntity(currentLightState.navLightState.toString(), MediaType.TEXT_PLAIN);
            }
        };
        
        // Create the underwater lights state handler
        Restlet setUnderwaterLights = new Restlet() {
        	@Override
            public void handle(Request request, Response response) {
        		boolean value = Boolean.parseBoolean(request.getAttributes().get("state").toString());
        		currentLightState.underwaterLightState = new MBoolean(value);
        		response.setCacheDirectives(cacheDirectives);
    			response.setEntity("You entered the following parameter:\n" + value, MediaType.TEXT_PLAIN);
            }
        };
        
        // Create the navigation lights state handler
        Restlet getUnderwaterLights = new Restlet() {
        	@Override
            public void handle(Request request, Response response) {
        		response.setCacheDirectives(cacheDirectives);
    			response.setEntity(currentLightState.underwaterLightState.toString(), MediaType.TEXT_PLAIN);
            }
        };
        
        router.attach("/navigationLights", getNavLights);
        router.attach("/underwaterLights", getUnderwaterLights);
        router.attach("/navigationLights/{state}", setNavLights);
        router.attach("/underwaterLights/{state}", setUnderwaterLights);
        
        return router;
    }
}
