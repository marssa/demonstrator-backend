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
package org.marssa.demonstrator.control.path_planning;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import org.marssa.footprint.datatypes.MBoolean;
import org.marssa.footprint.datatypes.composite.Coordinate;
import org.marssa.footprint.datatypes.composite.Latitude;
import org.marssa.footprint.datatypes.composite.Longitude;
import org.marssa.footprint.datatypes.decimal.DegreesDecimal;
import org.marssa.footprint.datatypes.decimal.MDecimal;
import org.marssa.footprint.datatypes.integer.MInteger;
import org.marssa.footprint.exceptions.ConfigurationError;
import org.marssa.footprint.exceptions.NoConnection;
import org.marssa.footprint.exceptions.NoValue;
import org.marssa.footprint.exceptions.OutOfRange;
import org.marssa.footprint.interfaces.control.motor.IMotorController;
import org.marssa.footprint.interfaces.control.rudder.IRudderController;
import org.marssa.demonstrator.constants.Constants;
import org.marssa.demonstrator.control.electrical_motor.SternDriveMotorController;
import org.marssa.demonstrator.control.rudder.RudderController;
import org.marssa.demonstrator.web_services.path_planning.Waypoint;
import org.marssa.services.control.Ramping;
import org.marssa.services.diagnostics.daq.LabJack;
import org.marssa.services.diagnostics.daq.LabJackU3;
import org.marssa.services.navigation.GpsReceiver;
import org.marssa.services.scheduling.MTimer;
import org.marssa.services.scheduling.MTimerTask;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;

/**
 * @author Clayton Tabone
 * 
 */
public class PathPlanningController extends MTimerTask implements IMotorController,IRudderController{
	
	private static final Logger logger = (Logger) LoggerFactory
			.getLogger(PathPlanningController.class);
	
	private static final MTimerTask MTimerTask = null;
	private SternDriveMotorController motorController;
	private RudderController rudderController;
	private GpsReceiver gpsReceiver;
	private Ramping ramping;
	private final MInteger STEPPER1 = LabJack.FIO8_ADDR;
	private final MInteger STEPPER2 = LabJack.FIO9_ADDR;
	private final MInteger STEPPER3 = LabJack.FIO10_ADDR;
	private final MInteger STEPPER4 = LabJack.FIO11_ADDR;
	private final MBoolean HIGH = new MBoolean(true);
	private final MBoolean LOW = new MBoolean(false);

	private int stepRight = 0;
	private int stepLeft = 0;
	private double voltageDifference = 0;
	private double angleDifference = 0;
	private static MDecimal angle;
	
	private final MInteger MOTOR_0_DIRECTION = LabJack.FIO6_ADDR;
	private final MInteger MOTOR_1_DIRECTION = LabJack.FIO7_ADDR;
	private final MInteger STEP_DELAY = new MInteger(20);
	private final MDecimal STEP_SIZE = new MDecimal(1.0f);
	
	private Coordinate currentPositionTest;
	private double currentHeadingTest;

	
	private Coordinate nextHeading;
	private int count = 0;
	private int countPath = 1;
	private LabJack lj;
	ArrayList<Waypoint> wayPointList;
	MTimer timer;
	boolean routeReverse =false;
	/**
	 * @throws ConfigurationError
	 * @throws OutOfRange
	 * @throws NoConnection
	 * 
	 */	
	public  PathPlanningController(SternDriveMotorController motorController, 
			RudderController rudderController, GpsReceiver gpsReceiver)
	{
		this.motorController = motorController;
		this.rudderController = rudderController;
		this.gpsReceiver = gpsReceiver;
		timer = MTimer.getInstance();
		wayPointList = new ArrayList<Waypoint>();
	}
	
	public ArrayList<Waypoint> getPathList() {
		return wayPointList;
	}


	public void setPathList(ArrayList<Waypoint> wayPointList) {
		this.wayPointList = wayPointList;
	}


	public Coordinate getNextHeading() {
		return nextHeading;
	}
	
	public void setNextHeading(Coordinate nextHeading) {
		this.nextHeading = nextHeading;
	}
	
	public void setTestCurrent(Coordinate boatPosition, double boatHeading) throws OutOfRange
	{
		currentPositionTest = boatPosition;
		currentHeadingTest = boatHeading;
	}
		
	public void getEstimatedPosition() throws OutOfRange
	{
		double dist = 0.003/6371.0;
		double brng = Math.toRadians(currentHeadingTest);
		double lat1 = Math.toRadians(currentPositionTest.getLatitude().getDMS().doubleValue());
		double lon1 = Math.toRadians(currentPositionTest.getLongitude().getDMS().doubleValue());

		double lat2 = Math.asin( Math.sin(lat1)*Math.cos(dist) + Math.cos(lat1)*Math.sin(dist)*Math.cos(brng) );
		double a = Math.atan2(Math.sin(brng)*Math.sin(dist)*Math.cos(lat1), Math.cos(dist)-Math.sin(lat1)*Math.sin(lat2));
		//System.out.println("a = " +  a);
		double lon2 = lon1 + a;
		lon2 = (lon2+ 3*Math.PI) % (2*Math.PI) - Math.PI;
        double lat2Degrees = Math.toDegrees(lat2);
        double lon2Degrees = Math.toDegrees(lon2);
        currentPositionTest =  new Coordinate(new Latitude(new DegreesDecimal(lat2Degrees)) , new Longitude(new DegreesDecimal(lon2Degrees)));
	   //logger.info(""+lat2Degrees+","+lon2Degrees);
        
        System.out.format("%f,%f%n",lat2Degrees,lon2Degrees);
	    
	}
	
	public void shortestAngle(double _currentHeading, double _targetHeading,int angleOut) throws NoConnection, NoValue, OutOfRange
	{
		if (_targetHeading > _currentHeading)
		{
			if((_targetHeading-_currentHeading) > 180)
			{
				//rudderController.rotateMultiple(Constants.RUDDER.ROTATIONS, new MBoolean(false));
				//logger.info("Rotate to Left");
				currentHeadingTest = currentHeadingTest -angleOut;
				if (currentHeadingTest < 0)
				{
					currentHeadingTest += 360;
				}
			}
			else
			{
				//rudderController.rotateMultiple(Constants.RUDDER.ROTATIONS, new MBoolean(true));//the rudders are brought back into the center after directing the vessel.
				//logger.info("Rotate to Right");
				currentHeadingTest = currentHeadingTest +angleOut;
				if (currentHeadingTest > 359)
				{
					currentHeadingTest -= 360;
				}
			}
			
		}
		else
		{
			if ((_currentHeading - _targetHeading) >= 180)
			{
				//rudderController.rotateMultiple(Constants.RUDDER.ROTATIONS, new MBoolean(true));//the rudders are brought back into the center after directing the vessel.
				//logger.info("Rotate to Right");
				currentHeadingTest = currentHeadingTest +angleOut;
				if (currentHeadingTest > 359)
				{
					currentHeadingTest -= 360;
				}
			}
			else
			{
				//rudderController.rotateMultiple(Constants.RUDDER.ROTATIONS, new MBoolean(false));
				//logger.info("Rotate to Left");
				currentHeadingTest = currentHeadingTest -angleOut;
				if (currentHeadingTest < 0)
				{
					currentHeadingTest += 360;
				}
			}
		}
	}
	// This method is called upon to drive the vessel in the right direction
	public void drive() throws NoConnection, NoValue, OutOfRange, InterruptedException {
		
		//double currentHeading = gpsReceiver.getCOG().doubleValue();
		double currentHeading = currentHeadingTest;
		double targetHeading = determineHeading();
		double difference =  Math.abs(currentHeading - targetHeading);
		
		//logger.info("Current Heading:"+currentHeading);
		//logger.info("Target Heading:"+targetHeading);
		
		//if the difference is minimal the system will enter this if statement and adjust the rudder slightly
		if (difference < Constants.PATH.Path_Accuracy_Lower.doubleValue())
		{
			//rotateToCentre(); 
			//logger.info("Rotate to Centre");
		}
		if ((difference >= Constants.PATH.Path_Accuracy_Lower.doubleValue()) && (difference <= Constants.PATH.Path_Accuracy_Upper.doubleValue()))
		{
			shortestAngle(currentHeading,targetHeading,5);
		}
		//if the difference is large the system will enter this if statement and adjust the rudder a lot
		else if (difference > Constants.PATH.Path_Accuracy_Upper.doubleValue())
		{
			shortestAngle(currentHeading,targetHeading,15);
		}
		//calculate bearing
	}
	
	//This method is used to determine the bearing we should be on to reach the next way point
	public double determineHeading() throws NoConnection, NoValue, OutOfRange
	{
		Coordinate currentPosition = currentPositionTest;
		
		//logger.info("Current Position:"+currentPosition);
	
		
		  double longitude1 = currentPosition.getLongitude().getDMS().doubleValue();
		  double longitude2 = nextHeading.getLongitude().getDMS().doubleValue();
		  double latitude1 = Math.toRadians(currentPosition.getLatitude().getDMS().doubleValue());
		  double latitude2 = Math.toRadians(nextHeading.getLatitude().getDMS().doubleValue());
		  double longDiff= Math.toRadians(longitude2-longitude1);
		  double y= Math.sin(longDiff)*Math.cos(latitude2);
		  double x=Math.cos(latitude1)*Math.sin(latitude2)-Math.sin(latitude1)*Math.cos(latitude2)*Math.cos(longDiff);

		  return (Math.toDegrees(Math.atan2(y, x))+360)%360;
		
	}
	
	//this method is used to determine if we have arrived at the next destination. This is calculated if the distance between our current position and
	//the target waypoint is less than 10 meters
	public boolean arrived() throws NoConnection, NoValue, OutOfRange
	{
		Coordinate currentPosition = currentPositionTest;
        double earthRadius = 3958.75;
	    double dLat = Math.toRadians(nextHeading.getLatitude().getDMS().doubleValue() - currentPosition.getLatitude().getDMS().doubleValue());
	    double dLng = Math.toRadians(nextHeading.getLongitude().getDMS().doubleValue() - currentPosition.getLongitude().getDMS().doubleValue());
	    double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
	               Math.cos(Math.toRadians(currentPosition.getLatitude().getDMS().doubleValue())) * Math.cos(Math.toRadians(nextHeading.getLatitude().getDMS().doubleValue())) *
	               Math.sin(dLng/2) * Math.sin(dLng/2);
	    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
	    double distance = (earthRadius * c);
		
		//logger.info("Distance to Next Waypoint:"+distance);
		if (distance < 0.0621371192) //10 meters in miles
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	//This method is used in order to check if the end of the trip has been reached, i.e there are no more way points in the list.
	public boolean endOfTrip()
	{
		if (count == wayPointList.size()-1)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	//This method is called upon every 1 second by the timer event.
	public void run()
	{
		try {
			boolean arrive = arrived();
			if (arrive && endOfTrip()) //If we have arrived and its the end of the trip (no more way points)
			{
				motorController.stop();
				logger.info("Kill Engines");
			}
			else if (arrive && ! endOfTrip()) //if we have arrived at our next way point but its not the end of the trip
			{
				logger.info("Arrived....next waypoint");
				count++;
				setNextHeading(wayPointList.get(count).getCoordinate()); //we get the next way points from the list and drive.
				getEstimatedPosition();
				drive();
			}
			else
			{
				//logger.info("Driving to next waypoint");
				getEstimatedPosition();
				drive();//else if we are on our way to the next way point we continue driving the vessel
			}
		} catch (NoConnection e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoValue e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OutOfRange e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		/*catch (ConfigurationError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/ 
	}
	
	public void setCruisingThrust() throws NoConnection, InterruptedException, ConfigurationError, OutOfRange
	{
		motorController.stop();
		motorController.increase();//20% forward
		motorController.increase();//20% forward
		motorController.increase();//20% forward
	}
	//this method is called upon by the RESTlet web services.
	public void startFollowingPath() throws NoConnection, InterruptedException, ConfigurationError, OutOfRange
	{
		//POP OUT ITEM SOMEHOW
		count =0;
		setCruisingThrust();
		if (routeReverse == true)
		{
			Collections.reverse(wayPointList);
			routeReverse = false;
		}
		setNextHeading(wayPointList.get(count).getCoordinate()); //we set the next way point to the first in the list
		timer.addSchedule(this,0,10); //we create the timer schedule for every 1 sec.
	}
	
	//This method is called upon by the RESTlet web services.
	public void stopFollowingPath()
	{
		timer.cancel(); //this cancels the timer.
	}
	//Path Planning Controller
	//Motor Controller
    
	public void returnHome() throws NoConnection, InterruptedException, ConfigurationError, OutOfRange
	{
		setCruisingThrust();
		timer.cancel();
		setNextHeading(wayPointList.get(0).getCoordinate()); //we set the next way point to the first in the list
		timer.addSchedule(this,0,10);
	}
	
	public void reverseTheRoute() throws NoConnection, InterruptedException, ConfigurationError, OutOfRange
	{
		setCruisingThrust();
		timer.cancel();
		count =0;
		routeReverse = true;
		setNextHeading(wayPointList.get(count).getCoordinate()); //we set the next way point to the first in the list
		timer.addSchedule(this,0,10); 
	}

	@Override
	public MDecimal getValue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void outputValue(MDecimal arg0) throws ConfigurationError,
			OutOfRange, NoConnection {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPolaritySignal(Polarity arg0) throws NoConnection {
		// TODO Auto-generated method stub
		
	}

	@Override
	public MDecimal getAngle() throws NoConnection {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void rotate(MBoolean arg0) throws InterruptedException, NoConnection {
		// TODO Auto-generated method stub
		
	}	
}
