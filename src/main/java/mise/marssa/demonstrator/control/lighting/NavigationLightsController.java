package mise.marssa.demonstrator.control.lighting;

import mise.marssa.footprint.data_types.MBoolean;
import mise.marssa.footprint.data_types.integer_datatypes.MInteger;
import mise.marssa.footprint.exceptions.NoConnection;
import mise.marssa.footprint.interfaces.control.lighting.ILightToggle;
import mise.marssa.footprint.logger.MMarker;
import mise.marssa.services.diagnostics.daq.LabJack;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;

public class NavigationLightsController implements ILightToggle{

	static Logger navigationLightLogger = (Logger) LoggerFactory.getLogger(NavigationLightsController.class);
	
	private boolean lightState;
	private LabJack lj;
	private String switched;
	
	static private final MInteger NAV_LIGHTS = LabJack.FIO12_ADDR;
	
	public NavigationLightsController(LabJack lj) {
		// TODO There must be something wrong here. FIO4 is operating in output mode, regardless of FIO4-dir
		// Set direction for FIO4 port
		//lj.write(LabJack.FIO4_DIR_ADDR, new MBoolean(true));
		navigationLightLogger.info("An instance of Navigation light controller was instantiated with labjack {} .",lj.getProperties);
		this.lightState = false;
		this.lj = lj;
	}
	
	public NavigationLightsController(LabJack lj, MBoolean newState) throws NoConnection {
		// TODO There must be something wrong here. FIO4 is operating in output mode, regardless of FIO4-dir
		// Set direction for FIO4 port
		//lj.write(LabJack.FIO4_DIR_ADDR, new MBoolean(true));
		
		lightState = newState.getValue();
		lj.write(NAV_LIGHTS, newState);
		Object[] navlight = {lj.getProperties,LabJack.FIO12_ADDR,newState.getValue()};
		navigationLightLogger.info("An instance of Navigation light controller was instantiated with labjack {} while pin {} was set to.",navLight);
	}
	
	public void toggleLight() throws NoConnection {
		
		lightState = !lightState;
		if(lightState)
			switched = "ON";
		else 
			switched = "OFF";
		navigationLightLogger.debug("Lights have been switched {} .",switched);
		lj.write(NAV_LIGHTS, new MBoolean(lightState));
	}
	
	public MBoolean getNavigationLightState() {
		navigationLightLogger.debug(MMarker.GETTER,"Returning LightState {} .",new MBoolean(lightState));
		return new MBoolean(lightState);
	}
	
	public void setNavigationLightState(MBoolean newState) throws NoConnection{
		this.lightState = newState.getValue();
		if(newState.getValue())
			switched = "ON";
		else 
			switched = "OFF";
		navigationLightLogger.debug(MMarker.SETTER,"Switching light",switched);
		lj.write(NAV_LIGHTS, newState);
	}
}
