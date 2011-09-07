/**
 * 
 */
package mise.demonstrator.constants;

import mise.marssa.data_types.MString;
import mise.marssa.data_types.integer_datatypes.MInteger;

/**
 * @author Clayton Tabone
 *
 */
public final class Constants {
	public final static MInteger RETRY_AMOUNT = new MInteger(5);
	
	/* Ramping Constants */
	public final static MInteger RETRY_INTERVAL = new MInteger(5);
	
	/* Labjack Constants */
	public final static MString LABJACK_HOST = new MString("192.168.2.14");
	public final static MInteger LABJACK_PORT = new MInteger(5021);
	public final static MString GPS_HOST = new MString("192.168.1.1");
	public final static MInteger GPS_PORT = new MInteger(2947);
	public final static MInteger RUDDER_DELAY = new MInteger(2000);
	
}
