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
package org.marssa.demonstrator.web_services.motionControlPage;

import java.util.ArrayList;


import org.marssa.demonstrator.control.electrical_motor.MotorController;
import org.marssa.demonstrator.control.rudder.RudderController;
import org.marssa.footprint.datatypes.decimal.MDecimal;
import org.marssa.footprint.exceptions.NoConnection;
import org.restlet.Application;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.data.CacheDirective;
import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.routing.Router;

public class MotionControlPageApplication extends Application {
	
	private ArrayList<CacheDirective> cacheDirectives;
	private MotorController motorController = null;
	private RudderController rudderController = null;
	
	public MotionControlPageApplication(ArrayList<CacheDirective> cacheDirectives, MotorController motorController, RudderController rudderController) {
		this.cacheDirectives = cacheDirectives;
		this.motorController = motorController;
		this.rudderController = rudderController;
	}

    /**
     * Creates a root Restlet that will receive all incoming calls.
     */
    @Override
    public synchronized Restlet createInboundRoot() {
        Router router = new Router(getContext());
        
        // Create the navigation lights state handler
        Restlet rudderAndSpeedState = new Restlet() {
        	@Override
            public void handle(Request request, Response response) {
        		response.setCacheDirectives(cacheDirectives);
				try {
					MDecimal motorSpeed = motorController.getValue();
					MDecimal rudderAngle = rudderController.getAngle();
					response.setEntity("{\"motor\":" + motorSpeed.toJSON().getContents() + ",\"rudder\":" + rudderAngle.toJSON().getContents() + "}", MediaType.APPLICATION_JSON);
				} catch (NoConnection e) {
					response.setStatus(Status.SERVER_ERROR_INTERNAL, "No connection error has been returned");
					e.printStackTrace();
				}
    			
            }
        };
        
        router.attach("/rudderAndSpeed", rudderAndSpeedState);
        
        return router;
    }
}