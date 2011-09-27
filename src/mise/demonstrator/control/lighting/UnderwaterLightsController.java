/**
 * 
 */
package mise.demonstrator.control.lighting;

import mise.demonstrator.control.LabJack;
import mise.marssa.data_types.MBoolean;
import mise.marssa.data_types.integer_datatypes.MInteger;
import mise.marssa.interfaces.control.lighting.ILightToggle;

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
	
	public UnderwaterLightsController (MBoolean newState) {
		lightState = newState.getValue();
		lj.write(UnLights,new MBoolean (lightState));
	}
	

	public void toggleLight() {
		lightState = !lightState;
		lj.write(UnLights,new MBoolean (lightState));
	}
	
	public MBoolean getUnderwaterLightState() {
		return new MBoolean (lightState);
	}
	
	public void setUnderwaterLightState(MBoolean newState){
		this.lightState = newState.getValue();
		lj.write(UnLights,newState);
	}

	

}
