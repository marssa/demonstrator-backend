package mise.demonstrator.light_controller;

import mise.marssa.data_types.MBoolean;
import mise.marssa.interfaces.control.lighting.ILightToggle;

public class LightToggle implements ILightToggle {

	private boolean lightState = false;

	
	public LightToggle () {
	
	}
	
	public LightToggle (MBoolean newState) {
		lightState = newState.getValue();
		
	}
	
	public void toggleLight() {
		lightState = !lightState;
	}

	public MBoolean getLightState() {
		return new MBoolean (lightState);
	}

}