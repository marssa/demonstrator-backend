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

import org.marssa.demonstrator.constants.Constants;
import org.marssa.demonstrator.tests.control.TestController;
import org.marssa.footprint.datatypes.decimal.MDecimal;
import org.marssa.footprint.exceptions.ConfigurationError;
import org.marssa.footprint.exceptions.OutOfRange;
import org.restlet.Application;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.data.CacheDirective;
import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.routing.Router;

public class MotorControllerTestApplication extends Application {
	
	private final ArrayList<CacheDirective> cacheDirectives = new ArrayList<CacheDirective>();
	private TestController motorController;
	
	public MotorControllerTestApplication(ArrayList<CacheDirective> cacheDirectives, TestController motorController) {
		this.cacheDirectives.addAll(cacheDirectives);
		this.motorController = motorController;
	}
	
	/**
     * Creates a root Restlet that will receive all incoming calls.
     */
    @Override
    public synchronized Restlet createInboundRoot() {
        Router router = new Router(getContext());
        
        // Create the motor speed control handler
        Restlet speedControl = new Restlet() {
        	@Override
            public void handle(Request request, Response response) {
        		try {
        			double value = Double.parseDouble(request.getAttributes().get("speed").toString());
        			try {
						motorController.rampTo(new MDecimal(value));
					} catch (ConfigurationError e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (OutOfRange e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
        			response.setCacheDirectives(cacheDirectives);
        			response.setEntity("Ramping motor speed to " + value + "%", MediaType.TEXT_PLAIN);
        		} catch (NumberFormatException e) {
        			response.setCacheDirectives(cacheDirectives);
        			response.setStatus(Status.CLIENT_ERROR_BAD_REQUEST, "The value of the speed resource has an incorrect format");
        		} catch (InterruptedException e) {
        			response.setCacheDirectives(cacheDirectives);
        			response.setStatus(Status.INFO_PROCESSING, "The ramping algorithm has been interrupted");
        			e.printStackTrace();
        		}
            }
        };
        
        // Create the increase motor speed control handler
        Restlet increaseSpeed = new Restlet() {
        	@Override
            public void handle(Request request, Response response) {
        		try {
        			motorController.increase(Constants.MOTOR.STEP_SIZE);
        			response.setCacheDirectives(cacheDirectives);
    				response.setEntity("Increasing motor speed by " + Constants.MOTOR.STEP_SIZE + "%", MediaType.TEXT_PLAIN);
        		} catch (NumberFormatException e) {
        			response.setCacheDirectives(cacheDirectives);
        			response.setStatus(Status.CLIENT_ERROR_BAD_REQUEST, "The value of the speed resource has an incorrect format");
        		} catch (InterruptedException e) {
        			response.setCacheDirectives(cacheDirectives);
        			response.setStatus(Status.INFO_PROCESSING, "The ramping algorithm has been interrupted");
        			e.printStackTrace();
				} catch (ConfigurationError e) {
					response.setCacheDirectives(cacheDirectives);
					response.setStatus(Status.SERVER_ERROR_INTERNAL, "The request has returned a ConfigurationError");
					e.printStackTrace();
				} catch (OutOfRange e) {
					response.setCacheDirectives(cacheDirectives);
					response.setStatus(Status.SERVER_ERROR_INTERNAL, "The specified value is out of range");
					e.printStackTrace();
				}
            }
        };
        
        // Create the decrease motor speed control handler
        Restlet decreaseSpeed = new Restlet() {
        	@Override
            public void handle(Request request, Response response) {
        		try {
					motorController.decrease(Constants.MOTOR.STEP_SIZE);
					response.setCacheDirectives(cacheDirectives);
    				response.setEntity("Decreasing motor speed by " + Constants.MOTOR.STEP_SIZE + "%", MediaType.TEXT_PLAIN);
        		} catch (NumberFormatException e) {
        			response.setCacheDirectives(cacheDirectives);
        			response.setStatus(Status.CLIENT_ERROR_BAD_REQUEST, "The value of the speed resource has an incorrect format");
        		} catch (InterruptedException e) {
        			response.setCacheDirectives(cacheDirectives);
        			response.setStatus(Status.INFO_PROCESSING, "The ramping routinee has been interrupted");
        			e.printStackTrace();
				} catch (ConfigurationError e) {
					response.setCacheDirectives(cacheDirectives);
					response.setStatus(Status.SERVER_ERROR_INTERNAL, "The request has returned a ConfigurationError");
					e.printStackTrace();
				} catch (OutOfRange e) {
					response.setCacheDirectives(cacheDirectives);
					response.setStatus(Status.SERVER_ERROR_INTERNAL, "The specified value is out of range");
					e.printStackTrace();
				}
            }
        };
        
        // Create the motor speed monitoring handler
        Restlet speedMonitor = new Restlet() {
        	@Override
            public void handle(Request request, Response response) {
        		response.setCacheDirectives(cacheDirectives);
    			response.setEntity(motorController.getValue().toString(), MediaType.TEXT_PLAIN);
            }
        };
        
        router.attach("/speed", speedMonitor);
        router.attach("/speed/{speed}", speedControl);
        router.attach("/increaseSpeed", increaseSpeed);
        router.attach("/decreaseSpeed", decreaseSpeed);
        
        return router;
    }
}
