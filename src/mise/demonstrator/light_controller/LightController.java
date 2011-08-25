package mise.demonstrator.light_controller;

import mise.marssa.interfaces.control.light.ILightController;
import mise.marssa.data_types.MBoolean;
// @author Warren
// @version 1.0
// @created 03-Jul-2011 08:24:24

public class LightController implements ILightController {

	private MBoolean lightState = new MBoolean(false);

	
	public LightController () {
	
	}
	
	public LightController (MBoolean newState) {
		lightState = newState;
		
	}
	
	public void toggleLight() {
		lightState.toggleValue();
	}

	@Override
	public MBoolean getLightState() {
		// TODO Auto-generated method stub
		return  (MBoolean) lightState;
	}

	
		
}

/*
public class LightControllers implements ILightController {

	private boolean lightState = false;

	
	public LightControllers () {
	
	}
	
	public LightControllers (MBoolean newState) {
		lightState = newState.getValue();
	}
	
	public void toggleLight() {
		lightState = !lightState;
	}

	@Override
	public MBoolean getLightState() {
		// TODO Auto-generated method stub
		return  new MBoolean(lightState);
	}

	
		
}
*/