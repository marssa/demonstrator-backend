package mise.marssa.demonstrator.control.lighting;

import java.net.UnknownHostException;

import mise.marssa.footprint.datatypes.MBoolean;
import mise.marssa.footprint.datatypes.MString;
import mise.marssa.footprint.datatypes.integer.MInteger;
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
	
	private boolean lightState ;
	private LabJack lj ;
	private String switched;
	private MInteger underLights;
	private Object[] poho= {lj.getHost().getContents(),lj.getPort().getValue()};
	
	
	public UnderwaterLightsController (MString host, MInteger port, MInteger underLights) throws UnknownHostException, NoConnection {
		underwaterLightLogger.info("An instance of Navigation light controller was instantiated with labjack host {}, and port {}.",host.getContents(),port.getValue());
		this.lightState = false;
		this.underLights = underLights;
		this.lj = LabJack.getInstance(host, port);
	}
	
	public UnderwaterLightsController (MString host, MInteger port, MInteger navLights, MBoolean newState) throws NoConnection, UnknownHostException {
		this.lj = LabJack.getInstance(host, port);
		underwaterLightLogger.info("An instance of UnderWater light controller was instantiated with labjack host: {}, and port: {}, with state set to: {}",poho,newState.getValue());
		lightState = newState.getValue();
		lj.write(navLights, newState);
	}
	

	public void toggleLight() throws NoConnection {
		lightState = !lightState;
		if(lightState)
			switched = "ON";
		else 
			switched = "OFF";
		underwaterLightLogger.trace("UnderwaterLights from labjack with host: {} and port: {} have been switched {} .",poho,switched);
		lj.write(underLights,new MBoolean (lightState));
	}
	
	public MBoolean getUnderwaterLightState() {
		underwaterLightLogger.trace(MMarker.GETTER,"Returning LightState {}, from labjack with host: {} and port: {} .",new MBoolean(lightState),poho);
		return new MBoolean (lightState);
	}
	
	public void setUnderwaterLightState(MBoolean newState) throws NoConnection{
		this.lightState = newState.getValue();
		if(newState.getValue())
			switched = "ON";
		else 
			switched = "OFF";
		underwaterLightLogger.trace(MMarker.SETTER,"Switching UnderWaterlight: {} ,from labjack with host: {} and port: {}",switched,poho);
		lj.write(underLights,newState);
	}
}
