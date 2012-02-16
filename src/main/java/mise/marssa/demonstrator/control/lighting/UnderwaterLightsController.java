/**
 * 
 */
package mise.marssa.demonstrator.control.lighting;

import mise.marssa.services.diagnostics.daq.LabJack;
import mise.marssa.footprint.datatypes.MBoolean;
import mise.marssa.footprint.datatypes.integer.MInteger;
import mise.marssa.footprint.exceptions.NoConnection;
import mise.marssa.footprint.interfaces.control.lighting.ILightToggle;

/**
 * @author Warren Zahra
 *
 */
public class UnderwaterLightsController implements ILightToggle {

	private boolean lightState = false;
	private LabJack lj = null;

	private final MInteger UnLights = LabJack.FIO13_ADDR;

	
	public UnderwaterLightsController (LabJack lb) {
		this.lj =lb;
	}
	
	public UnderwaterLightsController (MBoolean newState) throws NoConnection {
		lightState = newState.getValue();
		lj.write(UnLights,new MBoolean (lightState));
	}
	

	public void toggleLight() throws NoConnection {
		lightState = !lightState;
		lj.write(UnLights,new MBoolean (lightState));
	}
	
	public MBoolean getUnderwaterLightState() {
		return new MBoolean (lightState);
	}
	
	public void setUnderwaterLightState(MBoolean newState) throws NoConnection{
		this.lightState = newState.getValue();
		lj.write(UnLights,newState);
	}

	

}
