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
package org.marssa.demonstrator.tests.control.path_planning;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import org.marssa.demonstrator.constants.Constants;
import org.marssa.demonstrator.web_services.path_planning.Waypoint;
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
import org.marssa.services.diagnostics.daq.LabJack;
import org.marssa.services.scheduling.MTimer;
import org.marssa.services.scheduling.MTimerTask;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;

/**
 * @author Clayton Tabone
 * 
 */
public class PathPlanningControllerTest extends MTimerTask implements IMotorController,IRudderController{
	
	private static final Logger logger = (Logger) LoggerFactory
			.getLogger(PathPlanningControllerTest.class);
	
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
	
	private Coordinate currentPositionRead;
	private double currentHeadingRead;

	
	private Coordinate nextHeading;
	private int count = 0;
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
	public  PathPlanningControllerTest()
	{
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
		currentPositionRead = boatPosition;
		currentHeadingRead = boatHeading;
	}
		
	public void getEstimatedPosition() throws OutOfRange
	{
		double dist = 0.003/6371.0;
		double brng = Math.toRadians(currentHeadingRead);
		double lat1 = Math.toRadians(currentPositionRead.getLatitude().getDMS().doubleValue());
		double lon1 = Math.toRadians(currentPositionRead.getLongitude().getDMS().doubleValue());

		double lat2 = Math.asin( Math.sin(lat1)*Math.cos(dist) + Math.cos(lat1)*Math.sin(dist)*Math.cos(brng) );
		double a = Math.atan2(Math.sin(brng)*Math.sin(dist)*Math.cos(lat1), Math.cos(dist)-Math.sin(lat1)*Math.sin(lat2));
		//System.out.println("a = " +  a);
		double lon2 = lon1 + a;
		lon2 = (lon2+ 3*Math.PI) % (2*Math.PI) - Math.PI;
        double lat2Degrees = Math.toDegrees(lat2);
        double lon2Degrees = Math.toDegrees(lon2);
        currentPositionRead =  new Coordinate(new Latitude(new DegreesDecimal(lat2Degrees)) , new Longitude(new DegreesDecimal(lon2Degrees)));
	   //logger.info(""+lat2Degrees+","+lon2Degrees);
        
        System.out.format("%f,%f%n",lat2Degrees,lon2Degrees);
	 }
	
    public void readPosition() throws NoConnection, NoValue, OutOfRange
    {
    	// currentPositionRead = gpsReceiver.getCoordinate();
    }
	public void shortestAngle(double _currentHeading, double _targetHeading,double angleOut) throws NoConnection, NoValue, OutOfRange, InterruptedException
	{
		if (_targetHeading > _currentHeading)
		{
			if((_targetHeading-_currentHeading) > 180)
			{
				logger.info("Rotate to Left");
				currentHeadingRead = currentHeadingRead -angleOut;
				if (currentHeadingRead < 0)
				{
					currentHeadingRead += 360;
				}
			}
			else
			{
				logger.info("Rotate to Right");
				currentHeadingRead = currentHeadingRead +angleOut;
				if (currentHeadingRead > 359)
				{
					currentHeadingRead -= 360;
				}				
			}
			
		}
		else
		{
			if ((_currentHeading - _targetHeading) >= 180)
			{
				logger.info("Rotate to Right");
				currentHeadingRead = currentHeadingRead +angleOut;
				if (currentHeadingRead > 359)
				{
					currentHeadingRead -= 360;
				}
			}
			else
			{
				logger.info("Rotate to Left");
				currentHeadingRead = currentHeadingRead -angleOut;
				if (currentHeadingRead < 0)
				{
					currentHeadingRead += 360;
				}
			}
		}
	}
	// This method is called upon to drive the vessel in the right direction
	public void drive() throws NoConnection, NoValue, OutOfRange, InterruptedException {
		
		double currentHeading = currentHeadingRead;
		double targetHeading = determineHeading();
		double difference =  Math.abs(currentHeading - targetHeading);
		
		logger.info("Current Heading:"+currentHeading);
		logger.info("Target Heading:"+targetHeading);
		
		//if the difference is minimal the system will enter this if statement and adjust the rudder slightly
		if (difference < Constants.PATH.Path_Accuracy_Lower.doubleValue())
		{
			logger.info("Rotate to Centre");
		}
		if ((difference >= Constants.PATH.Path_Accuracy_Lower.doubleValue()) && (difference <= Constants.PATH.Path_Accuracy_Upper.doubleValue()))
		{
			shortestAngle(currentHeading,targetHeading,5.0);
		}
		//if the difference is large the system will enter this if statement and adjust the rudder a lot
		else if (difference > Constants.PATH.Path_Accuracy_Upper.doubleValue())
		{
			shortestAngle(currentHeading,targetHeading,15.0);
		}
		//calculate bearing
	}
	
	//This method is used to determine the bearing we should be on to reach the next way point
	public double determineHeading() throws NoConnection, NoValue, OutOfRange
	{
		Coordinate currentPosition = currentPositionRead;
		
		 logger.info("Current Position:"+currentPosition);
	
		
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
		Coordinate currentPosition = currentPositionRead;
        double earthRadius = 3958.75;
	    double dLat = Math.toRadians(nextHeading.getLatitude().getDMS().doubleValue() - currentPosition.getLatitude().getDMS().doubleValue());
	    double dLng = Math.toRadians(nextHeading.getLongitude().getDMS().doubleValue() - currentPosition.getLongitude().getDMS().doubleValue());
	    double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
	               Math.cos(Math.toRadians(currentPosition.getLatitude().getDMS().doubleValue())) * Math.cos(Math.toRadians(nextHeading.getLatitude().getDMS().doubleValue())) *
	               Math.sin(dLng/2) * Math.sin(dLng/2);
	    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
	    double distance = (earthRadius * c);
		
		logger.info("Distance to Next Waypoint:"+distance);
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
				logger.info("Kill Engines");
				wayPointList = new ArrayList<Waypoint>();
				timer.cancel();
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
				logger.info("Driving to next waypoint");
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
		logger.info("Cruising speed set");
	}
	//this method is called upon by the RESTlet web services.
	public void startFollowingPath() throws NoConnection, InterruptedException, ConfigurationError, OutOfRange
	{
		//POP OUT ITEM SOMEHOW
		count =0;
		logger.info("Cruising Thrust 60%");
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
		logger.info("Stop Following the Path");
		wayPointList = new ArrayList<Waypoint>();
	}
	//Path Planning Controller
	//Motor Controller
    
	public void returnHome() throws NoConnection, InterruptedException, ConfigurationError, OutOfRange
	{

	}
	
	public void reverseTheRoute() throws NoConnection, InterruptedException, ConfigurationError, OutOfRange
	{
		logger.info("Cruising Thrust 60%");
		logger.info("Reversing the Route");
		timer.cancel();
		routeReverse = true; 
	}

	/**
	 * The rotateMultiple is used to use the rotate method multiple times
	 */
	public synchronized void rotateMultiple(MInteger multiple,
			MBoolean direction) throws InterruptedException, NoConnection {
		for (int x = 0; x < multiple.intValue(); x++) {
			if (angle.doubleValue() > 30 && direction.getValue() == true) {
				break;
			} else if (angle.doubleValue() < -30
					&& direction.getValue() == false) {
				break;
			}
			rotate(direction);
		}

	}

	/**
	 * The rotateExtreme is used to rotate to the extreme possible angles
	 */
	public synchronized void rotateExtreme(MBoolean direction)
			throws InterruptedException, NoConnection {

		while (angle.doubleValue() < 32 && direction.getValue() == true) {
			rotate(direction);
		}

		while (angle.doubleValue() > -32 && direction.getValue() == false) {
			rotate(direction);
		}

	}

	/**
	 * The rotateToCentre is used to rotate the rudder to approximate its centre
	 * position
	 */
	public void rotateToCentre() throws NoConnection, InterruptedException {
		while (angle.doubleValue() > 5) {
			rotate(new MBoolean(false));
		}
		while (angle.doubleValue() < -5) {
			rotate(new MBoolean(true));
		}
	}

	/**
	 * The rotate is used to rotate the stepper motor by one step in either left
	 * or right direction The MBoolean direction false means that the rudder has
	 * negative angle (turns the boat to the left direction) The MBoolean
	 * direction true means that the rudder has positive angle (turns the boat
	 * to the right direction)
	 */
	public void rotate(MBoolean direction) throws NoConnection,
			InterruptedException {
		if ((stepLeft == 0 && direction.getValue())
				|| (stepRight == 3 && direction.getValue() == false)) {
			lj.write(STEPPER1, HIGH);
			lj.write(STEPPER2, HIGH);
			lj.write(STEPPER3, LOW);
			lj.write(STEPPER4, LOW);
			stepLeft = 1;
			stepRight = 0;
			Thread.sleep(Constants.RUDDER.RUDDER_DELAY.intValue());
			return;
		}
		if ((stepLeft == 1 && direction.getValue())
				|| (stepRight == 2 && direction.getValue() == false)) {
			lj.write(STEPPER1, LOW);
			lj.write(STEPPER2, HIGH);
			lj.write(STEPPER3, HIGH);
			lj.write(STEPPER4, LOW);
			stepLeft = 2;
			stepRight = 3;
			Thread.sleep(Constants.RUDDER.RUDDER_DELAY.intValue());
			return;
		}
		if ((stepLeft == 2 && direction.getValue())
				|| (stepRight == 1 && direction.getValue() == false)) {
			lj.write(STEPPER1, LOW);
			lj.write(STEPPER2, LOW);
			lj.write(STEPPER3, HIGH);
			lj.write(STEPPER4, HIGH);
			stepLeft = 3;
			stepRight = 2;
			Thread.sleep(Constants.RUDDER.RUDDER_DELAY.intValue());
			return;
		}
		if ((stepLeft == 3 && direction.getValue())
				|| (stepRight == 0 && direction.getValue() == false)) {
			lj.write(STEPPER1, HIGH);
			lj.write(STEPPER2, LOW);
			lj.write(STEPPER3, LOW);
			lj.write(STEPPER4, HIGH);
			stepLeft = 0;
			stepRight = 1;
			Thread.sleep(Constants.RUDDER.RUDDER_DELAY.intValue());
			return;
		}
	}

	/**
	 * The getAngle returns the actual angle of the rudder
	 */
	public MDecimal getAngle() throws NoConnection {
		try {
			double voltageValue = lj.read(new MInteger(0), new MInteger(8),
					new MInteger(1)).doubleValue(); // value that needs to be
													// read from the labjack
			if (voltageValue < 2.45) {
				voltageDifference = 2.45 - voltageValue;
				angleDifference = voltageDifference * 57.14;
				angle = new MDecimal(angleDifference);
			}
			if (voltageValue > 2.45) {
				voltageDifference = voltageValue - 2.45;
				angleDifference = voltageDifference * 57.14;
				angle = new MDecimal(-angleDifference);
			}
			if (voltageValue == 2.45) {
				angle = new MDecimal(0);
			}
		} catch (IOException e) {
			throw new NoConnection("Cannot read from LabJack\n"
					+ e.getMessage(), e.getCause());
		}
		return angle;
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
}	

