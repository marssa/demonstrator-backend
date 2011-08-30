package mise.demonstrator.control.lighting;

import mise.marssa.data_types.MBoolean;
import mise.marssa.interfaces.control.lighting.ILightToggle;

public class LightToggle implements ILightToggle {

	private boolean lightState = false;

	
	public LightToggle () {
	
	}
	
	public LightToggle (MBoolean newState) {
		lightState = newState.getValue();
		
	}
	
	public void toggleLight() { //to toggle from on --> off and v.v. 
		lightState = !lightState;
	}

	public MBoolean getLightState() {
		return new MBoolean (lightState);
	}
	
	public void setLightState(MBoolean lightState){
		this.lightState = lightState.getValue();
	}

}