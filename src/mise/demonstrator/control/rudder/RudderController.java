package mise.demonstrator.control.rudder;

import java.io.IOException;

import net.wimpi.modbus.ModbusException;
import net.wimpi.modbus.ModbusIOException;
import net.wimpi.modbus.ModbusSlaveException;
import mise.demonstrator.constants.Constants;
import mise.demonstrator.control.LabJack;
import mise.marssa.data_types.MBoolean;
import mise.marssa.data_types.float_datatypes.MFloat;
import mise.marssa.data_types.integer_datatypes.MInteger;
import mise.marssa.exceptions.NoConnection;
import mise.marssa.interfaces.control.rudder.IRudderController;

/**
 * @author Warren Zahra
 *
 */
public class RudderController implements IRudderController {
	
	private final MInteger STEPPER1 = LabJack.FIO8_ADDR;
	private final MInteger STEPPER2 = LabJack.FIO9_ADDR;
	private final MInteger STEPPER3 = LabJack.FIO10_ADDR;
	private final MInteger STEPPER4 = LabJack.FIO11_ADDR;
	private final MBoolean HIGH = new MBoolean(true);
	private final MBoolean LOW = new MBoolean(false);
	
	private int stepRight  = 0;
	private int stepLeft = 0;
	private float voltageDifference =0;
	private float angleDifference =0;
	private MFloat angle;
	
	private LabJack lj;
	
	public RudderController (LabJack lj) {
		this.lj = lj;
	}
	
	public void rotateMultiple(MInteger multiple,MBoolean direction) throws InterruptedException{
		for (int x = 0;x<multiple.getValue(); x++){
			rotate(direction);
		}
				
	}
	
	@Override
	public void rotate(MBoolean direction) throws InterruptedException {
		if ((stepLeft == 0 && direction.getValue()) || (stepRight==3  && direction.getValue()== false)) {
			lj.write(STEPPER1, HIGH);
			lj.write(STEPPER2, HIGH);
			lj.write(STEPPER3, LOW);
			lj.write(STEPPER4, LOW);
			stepLeft = 1;
			stepRight= 0;
			Thread.sleep(Constants.RUDDER.RUDDER_DELAY.getValue());	
			return;
		}
		if ((stepLeft == 1 && direction.getValue()) || (stepRight==2  && direction.getValue()== false)) {
			lj.write(STEPPER1, LOW);
			lj.write(STEPPER2, HIGH);
			lj.write(STEPPER3, HIGH);
			lj.write(STEPPER4, LOW);
			stepLeft= 2;
			stepRight= 3;
			Thread.sleep(Constants.RUDDER.RUDDER_DELAY.getValue());
			return;
		}
		if ((stepLeft == 2 && direction.getValue()) || (stepRight==1  && direction.getValue()== false)) {
			lj.write(STEPPER1, LOW);
			lj.write(STEPPER2, LOW);
			lj.write(STEPPER3, HIGH);
			lj.write(STEPPER4, HIGH);
			stepLeft= 3;
			stepRight= 2;
			Thread.sleep(Constants.RUDDER.RUDDER_DELAY.getValue());
			return;
		}
		if ((stepLeft == 3 && direction.getValue()) || (stepRight==0  && direction.getValue()== false)) {
			lj.write(STEPPER1, HIGH);
			lj.write(STEPPER2, LOW);
			lj.write(STEPPER3, LOW);
			lj.write(STEPPER4, HIGH);
			stepLeft= 0;
			stepRight= 1;
			Thread.sleep(Constants.RUDDER.RUDDER_DELAY.getValue());
			return;
		}
	}

	@Override
	public MFloat getAngle() throws NoConnection {
		try {
			float voltageValue = lj.read(new MInteger (0),new MInteger (8),new MInteger (1)).getValue();       //value that needs to be read from the labjack
			if (voltageValue < 2.5) {
				voltageDifference = (float) (2.5 - voltageValue);
				angleDifference = (float) (voltageDifference / 0.019);
				angle = new MFloat(90 - angleDifference);
			}
			if (voltageValue > 2.5) {
				voltageDifference = (float) (voltageValue -2.5);
				angleDifference = (float) (voltageDifference / 0.019);
				angle = new MFloat(90 + angleDifference);
			}
			if (voltageValue == 2.5){
				angle = new MFloat(0);
			}
		} catch(IOException e) {
			throw new NoConnection("Cannot read from LabJack\n" + e.getMessage(), e.getCause());
		}
		return angle;
	}
}


