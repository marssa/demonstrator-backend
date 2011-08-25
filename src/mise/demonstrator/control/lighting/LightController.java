package mise.demonstrator.control.lighting;

import mise.marssa.interfaces.control.lighting.ILightController;
import mise.marssa.data_types.MBoolean;
// @author Warren
// @version 1.0
// @created 03-Jul-2011 08:24:24
import mise.marssa.data_types.float_datatypes.MFloat;

public class LightController implements ILightController {

	private boolean lightState = false;
	
	public LightController () {
	
	}

	public LightController (MBoolean newState) {
		lightState = newState.getValue();
	}

	public void toggleLight() {
		lightState = !lightState;
	}

	public MBoolean getLightState() {
		return  new MBoolean(lightState);
	}

	@Override
	public void outputValue(MFloat value) {
		// TODO Auto-generated method stub
		
	}
}