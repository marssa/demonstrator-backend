/**
 * 
 */
package mise.demonstrator.web_service;

import mise.marssa.data_types.MBoolean;
import mise.marssa.data_types.float_datatypes.MFloat;

/**
 * @author Warren Zahra
 *
 */
public abstract class WSModuleImplementation extends AWebServices {

	/* (non-Javadoc)
	 * @see mise.marssa.interfaces.control.lighting.ILightToggle#toggleLight()
	 */
	@Override
	public void toggleLight() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see mise.marssa.interfaces.control.IController#outputValue(mise.marssa.data_types.float_datatypes.MFloat)
	 */
	@Override
	public void outputValue(MFloat value) {
		// TODO Auto-generated method stub

	}

	

	@Override
	public void rotate(MBoolean direction) throws InterruptedException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public MFloat getAngle(MFloat voltageValue) {
		return null;
		// TODO Auto-generated method stub
		
	}

}
