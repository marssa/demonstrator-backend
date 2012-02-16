/**
 * 
 */
package mise.marssa.demonstrator.control.lighting;

import mise.marssa.footprint.data_types.MBoolean;
import mise.marssa.footprint.data_types.integer_datatypes.MInteger;
import mise.marssa.footprint.exceptions.NoConnection;
import mise.marssa.footprint.interfaces.control.lighting.ILightToggle;
import mise.marssa.footprint.logger.MMarker;
import mise.marssa.services.diagnostics.daq.LabJack;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;

/**
 * @author Warren Zahra
 *
 */
public class UnderwaterLightsController implements ILightToggle {

	static Logger underwaterLightLogger = (Logger) LoggerFactory.getLogger("UnderwaterLightsController");
	
	private boolean lightState = false;
	private LabJack lj = null;
	private String switched;
	private final MInteger UnLights = LabJack.FIO13_ADDR;

	
	public UnderwaterLightsController (LabJack lb) {
		underwaterLightLogger.info("An instance of Navigation light controller was instantiated with labjack {} .",lj.getProperties);
		this.lightState = false;
		this.lj =lb;
	}
	
	public UnderwaterLightsController (MBoolean newState) throws NoConnection {
		lightState = newState.getValue();
		lj.write(UnLights,new MBoolean (lightState));
		Object[] underlight = {lj.getProperties,LabJack.FIO13_ADDR,newState.getValue()};
		underwaterLightLogger.info("An instance of Navigation light controller was instantiated with labjack {} while pin {} was set to.",underLight);
	}
	

	public void toggleLight() throws NoConnection {
		lightState = !lightState;
		if(lightState)
			switched = "ON";
		else 
			switched = "OFF";
		underwaterLightLogger.debug("Lights have been switched {} .",switched);
		lj.write(UnLights,new MBoolean (lightState));
	}
	
	public MBoolean getUnderwaterLightState() {
		underwaterLightLogger.debug(MMarker.GETTER,"Returning LightState {} .",new MBoolean(lightState));
		return new MBoolean (lightState);
	}
	
	public void setUnderwaterLightState(MBoolean newState) throws NoConnection{
		this.lightState = newState.getValue();
		if(newState.getValue())
			switched = "ON";
		else 
			switched = "OFF";
		underwaterLightLogger.debug(MMarker.SETTER,"Switching light",switched);
		lj.write(UnLights,newState);
	}
}
