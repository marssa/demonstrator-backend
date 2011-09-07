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
public class UnderwaterLights implements ILightToggle{

private boolean lightState = false;
private LabJack lb = null;

private final MInteger UnLights = LabJack.FIO4_ADDR;

	
	public UnderwaterLights (LabJack lb) {
		this.lb =lb;
	}
	
	public UnderwaterLights (MBoolean newState) {
		lightState = newState.getValue();
		lb.write(UnLights,new MBoolean (lightState));
	}
	

	public void toggleLight() {
		lightState = !lightState;
		lb.write(UnLights,new MBoolean (lightState));
	}
	
	public MBoolean getUnderwaterLightState() {
		return new MBoolean (lightState);
	}
	
	public void setUnderwaterLightState(MBoolean newState){
		lb.write(UnLights,new MBoolean (lightState));
	}

	

}
