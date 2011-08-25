/**
 * 
 */
package mise.demonstrator.navigation_equipment;

import java.io.IOException;
import de.taimos.gpsd4java.backend.GPSdEndpoint;
import de.taimos.gpsd4java.types.ParseException;
import de.taimos.gpsd4java.types.TPVObject;
import mise.demonstrator.constants.Constants;
import mise.marssa.data_types.MDate;
import mise.marssa.data_types.MString;
import mise.marssa.data_types.time.Hours;
import mise.marssa.data_types.composite_datatypes.Coordinate;
import mise.marssa.data_types.composite_datatypes.Latitude;
import mise.marssa.data_types.composite_datatypes.Longitude;
import mise.marssa.data_types.float_datatypes.DegreesFloat;
import mise.marssa.data_types.float_datatypes.speed.Knots;
import mise.marssa.data_types.integer_datatypes.DegreesInteger;
import mise.marssa.data_types.integer_datatypes.MInteger;
import mise.marssa.exceptions.NoConnection;
import mise.marssa.exceptions.NoValue;
import mise.marssa.exceptions.OutOfRange;
import mise.marssa.interfaces.navigation_equipment.IGpsReceiver;

/**
 * @author Clayton Tabone
 *
 */
public class GpsReceiver implements IGpsReceiver {

	GPSdEndpoint ep;

	public GpsReceiver(MString host, MInteger port) {
		ep = new GPSdEndpoint(host.getContents(), port.getValue());
		ep.start();
		try {
			System.out.println("gpsd4java started " + ep.version());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/* (non-Javadoc)
	 * @see mise.marssa.interfaces.navigation_equipment.IGpsReceiver#getAzimuth()
	 */
	@Override
	public DegreesInteger getAzimuth() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see mise.marssa.interfaces.navigation_equipment.IGpsReceiver#getCOG()
	 */
	@Override
	public DegreesFloat getCOG() throws NoConnection, NoValue {
		for(int i = 0; i < Constants.RETRY_AMOUNT.getValue(); i++) {
			try {
				float cog = (float) ep.poll().getFixes().get(0).getCourse();
				return new DegreesFloat(cog);
			} catch(IOException e) {
				if(i > Constants.RETRY_AMOUNT.getValue()) {
					throw new NoConnection(e.getMessage(), e.getCause());
				}
			} catch(ParseException e) {
				if(i > Constants.RETRY_AMOUNT.getValue()) {
					throw new NoValue("The COG is not available from the GPSReceiver. This is the error message from the gpsd4java library:" + e.getMessage(), e.getCause());
				}
			}
		}
		// This code is unreachable but was added here to satisfy the compiler.
		// The try/catch will loop for the RETRY_AMOUNT and if not successful will return a NoConnection Exception
		return null;
	}

	/* (non-Javadoc)
	 * @see mise.marssa.interfaces.navigation_equipment.IGpsReceiver#getCoordinate()
	 */
	@Override
	public Coordinate getCoordinate() throws NoConnection, NoValue {
		for(int i = 0; i < Constants.RETRY_AMOUNT.getValue(); i++) {
			try {
					TPVObject tpv = ep.poll().getFixes().get(0);
					Latitude latitude = new Latitude(new DegreesFloat((float) tpv.getLatitude()));
					Longitude longitude = new Longitude(new DegreesFloat((float) tpv.getLongitude()));
					return new Coordinate(latitude, longitude);
				}
			catch (OutOfRange e) {
				if(i > Constants.RETRY_AMOUNT.getValue()) {
					throw new NoConnection(e.getMessage(), e.getCause());
				}
			} catch(IOException e) {
				if(i > Constants.RETRY_AMOUNT.getValue()) {
					throw new NoConnection(e.getMessage(), e.getCause());
				}
			} catch(ParseException e) {
				if(i > Constants.RETRY_AMOUNT.getValue()) {
					throw new NoValue(e.getMessage(), e.getCause());
				}
			}
		}
		// This code is unreachable but was added here to satisfy the compiler.
		// The try/catch will loop for the RETRY_AMOUNT and if not successful will return a NoConnection Exception
		return null;
	}

	/* (non-Javadoc)
	 * @see mise.marssa.interfaces.navigation_equipment.IGpsReceiver#getDate()
	 */
	@Override
	public MDate getDate() throws NoConnection, NoValue {
		for(int i = 0; i < Constants.RETRY_AMOUNT.getValue(); i++) {
			try {
				double timestamp = ep.poll().getFixes().get(0).getTimestamp();
				System.out.println(timestamp);
				return new MDate((long) timestamp);
			} catch(IOException e) {
				if(i > Constants.RETRY_AMOUNT.getValue()) {
					throw new NoConnection(e.getMessage(), e.getCause());
				}
			} catch(ParseException e) {
				if(i > Constants.RETRY_AMOUNT.getValue()) {
					throw new NoValue("The COG is not available from the GPSReceiver. This is the error message from the gpsd4java library:" + e.getMessage(), e.getCause());
				}
			}
		}
		// This code is unreachable but was added here to satisfy the compiler.
		// The try/catch will loop for the RETRY_AMOUNT and if not successful will return a NoConnection Exception
		return null;
	}

	/* (non-Javadoc)
	 * @see mise.marssa.interfaces.navigation_equipment.IGpsReceiver#getElevation()
	 */
	@Override
	public DegreesInteger getElevation() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see mise.marssa.interfaces.navigation_equipment.IGpsReceiver#getHDOP()
	 */
	@Override
	public Float getHDOP() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see mise.marssa.interfaces.navigation_equipment.IGpsReceiver#getLocalZoneTime()
	 */
	@Override
	public Hours getLocalZoneTime() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see mise.marssa.interfaces.navigation_equipment.IGpsReceiver#getPDOP()
	 */
	@Override
	public Float getPDOP() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see mise.marssa.interfaces.navigation_equipment.IGpsReceiver#getSatelliteID()
	 */
	@Override
	public MInteger getSatelliteID() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see mise.marssa.interfaces.navigation_equipment.IGpsReceiver#getSatelliteInView()
	 */
	@Override
	public MInteger getSatelliteInView() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see mise.marssa.interfaces.navigation_equipment.IGpsReceiver#getSatellitesInUse()
	 */
	@Override
	public MInteger getSatellitesInUse() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see mise.marssa.interfaces.navigation_equipment.IGpsReceiver#getSignalSrength()
	 */
	@Override
	public Float getSignalSrength() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see mise.marssa.interfaces.navigation_equipment.IGpsReceiver#getSNR()
	 */
	@Override
	public Float getSNR() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see mise.marssa.interfaces.navigation_equipment.IGpsReceiver#getSOG()
	 */
	@Override
	public Knots getSOG() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see mise.marssa.interfaces.navigation_equipment.IGpsReceiver#getStatus()
	 */
	@Override
	public MString getStatus() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see mise.marssa.interfaces.navigation_equipment.IGpsReceiver#getTime()
	 */
	@Override
	public Hours getTime() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see mise.marssa.interfaces.navigation_equipment.IGpsReceiver#getVDOP()
	 */
	@Override
	public Float getVDOP() {
		// TODO Auto-generated method stub
		return null;
	}
}
