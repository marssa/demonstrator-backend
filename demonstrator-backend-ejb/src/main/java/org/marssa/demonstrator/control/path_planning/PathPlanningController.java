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

import java.util.ArrayList;
import java.util.Collections;

import org.marssa.demonstrator.constants.Constants;
import org.marssa.footprint.datatypes.MBoolean;
import org.marssa.footprint.datatypes.composite.Coordinate;
import org.marssa.footprint.datatypes.integer.MInteger;
import org.marssa.footprint.exceptions.ConfigurationError;
import org.marssa.footprint.exceptions.NoConnection;
import org.marssa.footprint.exceptions.NoValue;
import org.marssa.footprint.exceptions.OutOfRange;
import org.marssa.footprint.interfaces.control.motor.IMotorController;
import org.marssa.footprint.interfaces.control.rudder.IRudderController;
import org.marssa.footprint.interfaces.navigation.IGpsReceiver;
import org.marssa.services.scheduling.MTimer;
import org.marssa.services.scheduling.MTimerTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Clayton Tabone
 * 
 */
public class PathPlanningController extends MTimerTask {

	private static final Logger logger = LoggerFactory
			.getLogger(PathPlanningController.class);

	private final IMotorController motorController;
	private final IRudderController rudderController;
	private final IGpsReceiver gpsReceiver;

	private Coordinate currentPositionRead;
	private double currentHeadingRead;

	private Coordinate nextHeading;
	private int count = 0;
	ArrayList<Waypoint> wayPointList;
	MTimer timer;
	boolean routeReverse = false;

	/**
	 * @throws ConfigurationError
	 * @throws OutOfRange
	 * @throws NoConnection
	 * 
	 */
	public PathPlanningController(IMotorController motorController,
			IRudderController rudderController, IGpsReceiver gpsReceiver) {
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

	public void readPosition() throws NoConnection, NoValue, OutOfRange {
		currentPositionRead = gpsReceiver.getCoordinate();
	}

	public void shortestAngle(double _currentHeading, double _targetHeading,
			MInteger angleOut) throws NoConnection, NoValue, OutOfRange,
			InterruptedException {
		if (_targetHeading > _currentHeading) {
			if ((_targetHeading - _currentHeading) > 180) {
				rudderController.rotateMultiple(angleOut, new MBoolean(true));
			} else {
				rudderController.rotateMultiple(angleOut, new MBoolean(false));
			}

		} else {
			if ((_currentHeading - _targetHeading) >= 180) {
				rudderController.rotateMultiple(angleOut, new MBoolean(false));
			} else {
				rudderController.rotateMultiple(angleOut, new MBoolean(true));
			}
		}
	}

	// This method is called upon to drive the vessel in the right direction
	public void drive() throws NoConnection, NoValue, OutOfRange,
			InterruptedException {

		double currentHeading = gpsReceiver.getCOG().doubleValue();

		double targetHeading = determineHeading();
		double difference = Math.abs(currentHeading - targetHeading);

		if (difference < Constants.PATH.Path_Accuracy_Lower.doubleValue()) {

			rudderController.rotateToCentre();
		}
		if ((difference >= Constants.PATH.Path_Accuracy_Lower.doubleValue())
				&& (difference <= Constants.PATH.Path_Accuracy_Upper
						.doubleValue())) {
			shortestAngle(currentHeading, targetHeading, new MInteger(5));
		}
		// if the difference is large the system will enter this if statement
		// and adjust the rudder a lot
		else if (difference > Constants.PATH.Path_Accuracy_Upper.doubleValue()) {
			shortestAngle(currentHeading, targetHeading, new MInteger(15));
		}
		// calculate bearing
	}

	// This method is used to determine the bearing we should be on to reach the
	// next way point
	public double determineHeading() throws NoConnection, NoValue, OutOfRange {
		double longitude1 = currentPositionRead.getLongitude().getDMS()
				.doubleValue();
		double longitude2 = nextHeading.getLongitude().getDMS().doubleValue();
		double latitude1 = Math.toRadians(currentPositionRead.getLatitude()
				.getDMS().doubleValue());
		double latitude2 = Math.toRadians(nextHeading.getLatitude().getDMS()
				.doubleValue());
		double longDiff = Math.toRadians(longitude2 - longitude1);
		double y = Math.sin(longDiff) * Math.cos(latitude2);
		double x = Math.cos(latitude1) * Math.sin(latitude2)
				- Math.sin(latitude1) * Math.cos(latitude2)
				* Math.cos(longDiff);

		return (Math.toDegrees(Math.atan2(y, x)) + 360) % 360;

	}

	// this method is used to determine if we have arrived at the next
	// destination. This is calculated if the distance between our current
	// position and
	// the target waypoint is less than 10 meters
	public boolean arrived() throws NoConnection, NoValue, OutOfRange {
		double earthRadius = 3958.75;
		double dLat = Math.toRadians(nextHeading.getLatitude().getDMS()
				.doubleValue()
				- currentPositionRead.getLatitude().getDMS().doubleValue());
		double dLng = Math.toRadians(nextHeading.getLongitude().getDMS()
				.doubleValue()
				- currentPositionRead.getLongitude().getDMS().doubleValue());
		double a = Math.sin(dLat / 2)
				* Math.sin(dLat / 2)
				+ Math.cos(Math.toRadians(currentPositionRead.getLatitude()
						.getDMS().doubleValue()))
				* Math.cos(Math.toRadians(nextHeading.getLatitude().getDMS()
						.doubleValue())) * Math.sin(dLng / 2)
				* Math.sin(dLng / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		double distance = (earthRadius * c);

		// logger.info("Distance to Next Waypoint:"+distance);
		if (distance < 0.0621371192) // 10 meters in miles
		{
			return true;
		} else {
			return false;
		}
	}

	// This method is used in order to check if the end of the trip has been
	// reached, i.e there are no more way points in the list.
	public boolean endOfTrip() {
		if (count == wayPointList.size() - 1) {
			return true;
		} else {
			return false;
		}
	}

	// This method is called upon every 1 second by the timer event.
	@Override
	public void run() {
		try {
			boolean arrive = arrived();
			if (arrive && endOfTrip()) {
				// If we have arrived and its the end of the trip (no more way
				// points)
				motorController.stop();
				logger.info("Kill Engines");
				timer.cancel();
				wayPointList = new ArrayList<Waypoint>();
			} else if (arrive && !endOfTrip()) {
				// if we have arrived at our next way point but its not the end
				// of the trip
				logger.info("Arrived....next waypoint");
				count++;
				// we get the next way points from the list and drive.
				setNextHeading(wayPointList.get(count).getCoordinate());
				readPosition();
				drive();
			} else {
				// else if we are on our way to the next way point we continue
				// driving the vessel
				readPosition();
				drive();
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
		/*
		 * catch (ConfigurationError e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); }
		 */
	}

	public void setCruisingThrust() throws NoConnection, InterruptedException,
			ConfigurationError, OutOfRange {
		motorController.stop();
		motorController.increase();// 20% forward
		motorController.increase();// 20% forward
		motorController.increase();// 20% forward
	}

	// this method is called upon by the RESTlet web services.
	public void startFollowingPath() throws NoConnection, InterruptedException,
			ConfigurationError, OutOfRange {
		// POP OUT ITEM SOMEHOW
		count = 0;
		setCruisingThrust();
		if (routeReverse == true) {
			Collections.reverse(wayPointList);
			routeReverse = false;
		}
		// we set the next way point to the first in the list
		setNextHeading(wayPointList.get(count).getCoordinate());
		// we create the timer schedule for every 1 sec.
		timer.addSchedule(this, 0, 10);
	}

	// This method is called upon by the RESTlet web services.
	public void stopFollowingPath() {
		timer.cancel(); // this cancels the timer.
		wayPointList = new ArrayList<Waypoint>();
	}

	// Path Planning Controller
	// Motor Controller

	public void returnHome() throws NoConnection, InterruptedException,
			ConfigurationError, OutOfRange {
		// setCruisingThrust();
		// timer.cancel();
		// setNextHeading(wayPointList.get(0).getCoordinate()); //we set the
		// next way point to the first in the list
		// timer.addSchedule(this,0,10);
	}

	public void reverseTheRoute() throws NoConnection, InterruptedException,
			ConfigurationError, OutOfRange {
		timer.cancel();
		routeReverse = true;
	}

}
