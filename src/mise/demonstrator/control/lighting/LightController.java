package mise.demonstrator.lighting;

import mise.marssa.interfaces.control.lighting.ILightController;
import mise.marssa.data_types.MBoolean;
// @author Warren
// @version 1.0
// @created 03-Jul-2011 08:24:24

public class LightController implements ILightController {

	private boolean lightState = false;
	
	public LightControllers () {
	
	}

	public LightControllers (MBoolean newState) {
		lightState = newState.getValue();
	}

	public void toggleLight() {
		lightState = !lightState;
	}

	public MBoolean getLightState() {
		return  new MBoolean(lightState);
	}
}