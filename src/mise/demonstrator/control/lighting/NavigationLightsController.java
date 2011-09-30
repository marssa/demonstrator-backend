package mise.demonstrator.control.lighting;

import mise.demonstrator.control.LabJack;
import mise.marssa.data_types.MBoolean;
import mise.marssa.data_types.integer_datatypes.MInteger;
import mise.marssa.exceptions.NoConnection;
import mise.marssa.interfaces.control.lighting.ILightToggle;

public class NavigationLightsController implements ILightToggle{

	private boolean lightState;
	private LabJack lj;
	
	static private final MInteger NAV_LIGHTS = LabJack.FIO12_ADDR;
	
	public NavigationLightsController(LabJack lj) {
		// TODO There must be something wrong here. FIO4 is operating in output mode, regardless of FIO4-dir
		// Set direction for FIO4 port
		//lj.write(LabJack.FIO4_DIR_ADDR, new MBoolean(true));
		this.lightState = false;
		this.lj = lj;
	}
	
	public NavigationLightsController(LabJack lj, MBoolean newState) throws NoConnection {
		// TODO There must be something wrong here. FIO4 is operating in output mode, regardless of FIO4-dir
		// Set direction for FIO4 port
		//lj.write(LabJack.FIO4_DIR_ADDR, new MBoolean(true));
		lightState = newState.getValue();
		lj.write(NAV_LIGHTS, newState);
	}
	
	public void toggleLight() throws NoConnection {
		lightState = !lightState;
		lj.write(NAV_LIGHTS, new MBoolean(lightState));
	}
	
	public MBoolean getNavigationLightState() {
		return new MBoolean(lightState);
	}
	
	public void setNavigationLightState(MBoolean newState) throws NoConnection{
		this.lightState = newState.getValue();
		lj.write(NAV_LIGHTS, newState);
	}
}
