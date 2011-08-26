/**
 * 
 */
package mise.demonstrator.web_service;

import mise.marssa.data_types.float_datatypes.MFloat;
import mise.marssa.interfaces.control.electrical_motor.IMotorController;
import mise.marssa.interfaces.control.lighting.ILightToggle;
import mise.marssa.interfaces.control.rudder.IRudder;

/**
 * @author Warren Zahra
 *
 */
public abstract class WSModule implements ILightToggle, IMotorController,
		IRudder { }
