package org.marssa.demonstrator.tests.control;

import org.marssa.footprint.datatypes.MDate;
import org.marssa.footprint.datatypes.MString;
import org.marssa.footprint.datatypes.composite.Coordinate;
import org.marssa.footprint.datatypes.composite.Latitude;
import org.marssa.footprint.datatypes.composite.Longitude;
import org.marssa.footprint.datatypes.decimal.DegreesDecimal;
import org.marssa.footprint.datatypes.decimal.MDecimal;
import org.marssa.footprint.datatypes.decimal.distance.Metres;
import org.marssa.footprint.datatypes.decimal.speed.Knots;
import org.marssa.footprint.datatypes.integer.DegreesInteger;
import org.marssa.footprint.datatypes.integer.MInteger;
import org.marssa.footprint.datatypes.time.ATime;
import org.marssa.footprint.exceptions.NoConnection;
import org.marssa.footprint.exceptions.NoValue;
import org.marssa.footprint.exceptions.OutOfRange;
import org.marssa.footprint.interfaces.navigation.IGpsReceiver;

public class GPSReceiverTest implements IGpsReceiver {
	
	private Coordinate currentPositionRead;
	private double currentHeadingRead;
	private RudderControllerTest rudderControllerTest;
	
	public GPSReceiverTest (RudderControllerTest _rudderControllerTest)
	{
		rudderControllerTest = _rudderControllerTest;
	}
	
	private void COGEstimate()
	{
		double rudderAngle = rudderControllerTest.getRudderAngle();
		currentHeadingRead = currentHeadingRead + rudderAngle;
		if (currentHeadingRead < 0)
		{
			currentHeadingRead += 360;
		} 
		else if (currentHeadingRead > 359)
		{
			currentHeadingRead -= 360;
		}	
	}
	
	@Override
	public Metres getAltitude() throws NoConnection, NoValue, OutOfRange {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DegreesInteger getAzimuth() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DegreesDecimal getCOG() throws NoConnection, NoValue {
		// TODO Auto-generated method stub
		return new DegreesDecimal(currentHeadingRead);
	}

	@Override
	public Coordinate getCoordinate() throws NoConnection, NoValue, OutOfRange {
		COGEstimate();
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
        return currentPositionRead;
	}

	@Override
	public MDate getDate() throws NoConnection, NoValue {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MDecimal getEPT() throws NoConnection, NoValue, OutOfRange {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MDecimal getHDOP() throws NoConnection, NoValue {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ATime getLocalZoneTime() throws NoConnection, NoValue {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MDecimal getPDOP() throws NoConnection, NoValue {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MDecimal getSNR() throws NoConnection, NoValue {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Knots getSOG() throws NoConnection, NoValue {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MInteger getSatelliteID() throws NoConnection, NoValue {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MInteger getSatelliteInView() throws NoConnection, NoValue {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MInteger getSatellitesInUse() throws NoConnection, NoValue {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MDecimal getSignalSrength() throws NoConnection, NoValue {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MString getStatus() throws NoConnection, NoValue {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ATime getTime() throws NoConnection, NoValue {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MDecimal getVDOP() throws NoConnection, NoValue {
		// TODO Auto-generated method stub
		return null;
	}

}
