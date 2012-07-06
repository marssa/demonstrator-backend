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
package org.marssa.demonstrator.web_services.lightControlPage;

import java.util.ArrayList;

import org.marssa.demonstrator.control.lighting.NavigationLightsController;
import org.marssa.demonstrator.control.lighting.UnderwaterLightsController;
import org.marssa.footprint.datatypes.MBoolean;
import org.restlet.Application;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.data.CacheDirective;
import org.restlet.data.MediaType;
import org.restlet.routing.Router;

public class LightControlPageApplication extends Application {
	
	private ArrayList<CacheDirective> cacheDirectives;
	private NavigationLightsController navLightsController;
	private UnderwaterLightsController underwaterLightsController;
	
	public LightControlPageApplication(ArrayList<CacheDirective> cacheDirectives, NavigationLightsController navLightsController, UnderwaterLightsController underwaterLightsController) {
		this.cacheDirectives = cacheDirectives;
		this.navLightsController = navLightsController;
		this.underwaterLightsController = underwaterLightsController;
	}

    /**
     * Creates a root Restlet that will receive all incoming calls.
     */
    @Override
    public synchronized Restlet createInboundRoot() {
        Router router = new Router(getContext());
        
        // Create the navigation lights state handler
        Restlet lightState = new Restlet() {
        	@Override
            public void handle(Request request, Response response) {
        		response.setCacheDirectives(cacheDirectives);
				MBoolean navLightsState = navLightsController.getNavigationLightState();
				MBoolean underWaterLightsState = underwaterLightsController.getUnderwaterLightState();
				response.setEntity("{\"navLights\":" + navLightsState.toJSON().getContents() + ",\"underwaterLights\":" + underWaterLightsState.toJSON().getContents() + "}", MediaType.APPLICATION_JSON);
            }
        };
        
        router.attach("/statusAll", lightState);
        
        return router;
    }
}