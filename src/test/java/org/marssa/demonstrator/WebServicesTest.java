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
package org.marssa.demonstrator;

import java.util.ArrayList;

import org.marssa.demonstrator.constants.Constants;
import org.marssa.demonstrator.web_services.StaticFileServerApplication;
import org.marssa.footprint.datatypes.MBoolean;
import org.marssa.footprint.datatypes.composite.Coordinate;
import org.marssa.footprint.datatypes.composite.Latitude;
import org.marssa.footprint.datatypes.composite.Longitude;
import org.marssa.footprint.datatypes.decimal.DegreesDecimal;
import org.marssa.footprint.datatypes.decimal.MDecimal;
import org.marssa.footprint.datatypes.integer.MInteger;
import org.marssa.footprint.exceptions.ConfigurationError;
import org.marssa.footprint.exceptions.NoConnection;
import org.marssa.footprint.exceptions.OutOfRange;
import org.marssa.footprint.interfaces.control.IController;
import org.marssa.services.control.Ramping;
import org.marssa.services.control.Ramping.RampingType;
import org.restlet.Application;
import org.restlet.Component;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.Server;
import org.restlet.data.CacheDirective;
import org.restlet.data.MediaType;
import org.restlet.data.Protocol;
import org.restlet.data.Status;
import org.restlet.routing.Router;

public class WebServicesTest {
	
	private static final ArrayList<CacheDirective> cacheDirectives = new ArrayList<CacheDirective>();
	
	static class LightState {
		public MBoolean navLightState = new MBoolean(false);
		public MBoolean underwaterLightState = new MBoolean(false);
	}
	
	static private class TestController implements IController {
		private Ramping ramping;
		
		public TestController() {
			try {
				ramping = new Ramping(new MInteger(50), new MDecimal(1.0f), this, RampingType.ACCELERATED);
			} catch (ConfigurationError e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (OutOfRange e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoConnection e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		public void rampTo(MDecimal desiredValue) throws InterruptedException, ConfigurationError, OutOfRange {
			ramping.rampTo(desiredValue);
		}
		
		public void increase(MDecimal incrementValue) throws InterruptedException, ConfigurationError, OutOfRange {
			try {
				ramping.increase(incrementValue);
			} catch (NoConnection e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		public void decrease(MDecimal decrementValue) throws InterruptedException, ConfigurationError, OutOfRange {
			try {
				ramping.decrease(decrementValue);
			} catch (NoConnection e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		public void outputValue(MDecimal value) throws ConfigurationError,
				OutOfRange, NoConnection {
			// TODO Auto-generated method stub
			System.out.println(value);
		}

		public void setPolaritySignal(Polarity polarity)
				throws NoConnection {
			// TODO Auto-generated method stub
			
		}

		public MDecimal getValue() {
			// TODO Auto-generated method stub
			return ramping.getCurrentValue();
		}
	}
	
	private static LightState currentLightState = new LightState();
	private static MDecimal rudderAngle = new MDecimal(0.0f);
	private static TestController motorController = new TestController();
	
	private static class LightControllerTestApplication extends Application {
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
	
	private static class MotorControllerTestApplication extends Application {
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
	
	private static class RudderControllerTestApplication extends Application {
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
        				rudderAngle = new MDecimal(rudderAngle.doubleValue() + 1);
    				else
    					rudderAngle = new MDecimal(rudderAngle.doubleValue() - 1);
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
        				rudderAngle = new MDecimal(rudderAngle.doubleValue() + 5);
    				else
    					rudderAngle = new MDecimal(rudderAngle.doubleValue() - 5);
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
        			response.setEntity(rudderAngle.toString(), MediaType.TEXT_PLAIN);
	            }
	        };
	        
	        // Create the rotation handler to rotate to the extremes
	        Restlet rotateFull = new Restlet() {
	        	@Override
	            public void handle(Request request, Response response) {
        			//TODO Handle parseException since parseBoolean doesn't check for and raise this exception
        			boolean direction = Boolean.parseBoolean(request.getAttributes().get("direction").toString());
        			if(direction)
        				rudderAngle = new MDecimal(30.0f);
    				else
    					rudderAngle = new MDecimal(-30.0f);
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
	
	private static class GPSReceiverTestApplication extends Application {
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
	
	private static class MotionControlPageTestApplication extends Application {
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
					MDecimal motorSpeed = motorController.getValue();
					response.setCacheDirectives(cacheDirectives);
					response.setEntity("{\"motor\":" + motorSpeed.toJSON().getContents() + ",\"rudder\":" + rudderAngle.toJSON().getContents() + "}", MediaType.APPLICATION_JSON);
	            }
	        };
	        
	        router.attach("/rudderAndSpeed", rudderAndSpeedState);
	        
	        return router;
	    }
	}
	
	private static class LightControlPageTestApplication extends Application {
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
					response.setEntity("{\"navLights\":" + currentLightState.navLightState.toJSON() + ",\"underwaterLights\":" + currentLightState.underwaterLightState.toJSON() + "}", MediaType.APPLICATION_JSON);
	            }
	        };
	        
	        router.attach("/statusAll", lightState);
	        
	        return router;
	    }
	}
	
	/**
	 * @param args the args 
	 */
	public static void main(java.lang.String[] args) {
		// Create a new Component
		Component component = new Component();
		
		System.out.println("Starting Web Services on " + Constants.WEB_SERVICES.HOST.getContents() + ":" + Constants.WEB_SERVICES.PORT.toString() + " ...");
		
		cacheDirectives.add(CacheDirective.noCache());
		cacheDirectives.add(CacheDirective.noStore());
		
	    // Add a new HTTP server listening on the given port
	    Server server = component.getServers().add(Protocol.HTTP, Constants.WEB_SERVICES.HOST.getContents(), Constants.WEB_SERVICES.PORT.intValue());
	    server.getContext().getParameters().add("maxTotalConnections", Constants.WEB_SERVICES.MAX_TOTAL_CONNECTIONS.toString());
	    
	    // Add new client connector for the FILE protocol
	    component.getClients().add(Protocol.FILE);
	    
	    // Attach the static file server application
	    component.getDefaultHost().attach("", new StaticFileServerApplication());
	    
	    // Attach the light control application
	    component.getDefaultHost().attach("/lighting", new LightControllerTestApplication());
	    
	    // Attach the motor control application
	    component.getDefaultHost().attach("/motor", new MotorControllerTestApplication());

	    // Attach the rudder control application
	    component.getDefaultHost().attach("/rudder", new RudderControllerTestApplication());
	    
	    // Attach the GPS receiver application
	    component.getDefaultHost().attach("/gps", new GPSReceiverTestApplication());
	    
	    // Attach the motion control feedback application 
	    component.getDefaultHost().attach("/motionControlPage", new MotionControlPageTestApplication());
	    
	    // Attach the motion control feedback application
	    component.getDefaultHost().attach("/lightControlPage", new LightControlPageTestApplication());
	    
	    // Start the component
	    try {
			component.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
