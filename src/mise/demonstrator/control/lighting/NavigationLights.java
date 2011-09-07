package mise.demonstrator.control.lighting;

import mise.demonstrator.control.LabJack;
import mise.marssa.data_types.MBoolean;
import mise.marssa.data_types.integer_datatypes.MInteger;
import mise.marssa.interfaces.control.lighting.ILightToggle;

public class NavigationLights implements ILightToggle{

private boolean lightState = false;
private LabJack lb = null;

private final MInteger NavLights = LabJack.FIO4_ADDR;
	
	public NavigationLights (LabJack lb) {
		this.lb  =lb;
	}
	
	public NavigationLights (MBoolean newState) {
		lightState = newState.getValue();
		lb.write(NavLights,new MBoolean (lightState));
	}
	
	public void toggleLight() {
		lightState = !lightState;
		lb.write(NavLights,new MBoolean (lightState));
	}
	
	public MBoolean getNavigationLightState() {
		return new MBoolean (lightState);
	}
	
	public void setNavigationLightState(MBoolean newState){
		lb.write(NavLights,new MBoolean (lightState));
	}
	

}
