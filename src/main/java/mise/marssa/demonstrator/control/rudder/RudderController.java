package mise.marssa.demonstrator.control.rudder;

import java.io.IOException;

import mise.marssa.services.diagnostics.daq.LabJack;
import mise.marssa.demonstrator.constants.Constants;
import mise.marssa.footprint.datatypes.MBoolean;
import mise.marssa.footprint.datatypes.decimal.MFloat;
import mise.marssa.footprint.datatypes.integer.MInteger;
import mise.marssa.footprint.exceptions.NoConnection;
import mise.marssa.footprint.interfaces.control.rudder.IRudderController;

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
	private static MFloat angle;
	
	private LabJack lj;
	
	public RudderController (LabJack lj) throws NoConnection, InterruptedException {
		this.lj = lj;
		rotate(new MBoolean (false));
		rotate(new MBoolean (true));
	}
	/**
	 * The rotateMultiple is used to use the rotate method multiple times
	 */
	public synchronized void rotateMultiple(MInteger multiple,MBoolean direction) throws InterruptedException, NoConnection{
		for (int x = 0;x<multiple.getValue(); x++){
			
			if(angle.getValue()>30 && direction.getValue()== true){
				break;
			}
			else if(angle.getValue()<-30 && direction.getValue()== false){
				break;
			}
			rotate(direction);
		}
				
	}
	/**
	 * The rotateExtreme is used to rotate to the extreme possible angles 
	 */
	public synchronized void rotateExtreme(MBoolean direction) throws InterruptedException, NoConnection{
		
		while(angle.getValue()<32 && direction.getValue()== true){
		rotate(direction);
		}
		
		while(angle.getValue()>-32 && direction.getValue()== false){
			rotate(direction);
		}
						
	}
	/**
	 * The rotateToCentre is used to rotate the rudder to approximate its centre position 
	 */
	public void rotateToCentre() throws NoConnection, InterruptedException{
		while (angle.getValue()>5){
		rotate(new MBoolean (false));	
		}
		while (angle.getValue()<-5){
		rotate(new MBoolean (true));	
		}
	}
	
	/**
	 * The rotate is used to rotate the stepper motor by one step in either left or right direction
	 * The MBoolean direction false means that the rudder has negative angle (turns the boat to the left direction)
	 * The MBoolean direction true means that the rudder has positive angle (turns the boat to the right direction)
	 */
	public void rotate(MBoolean direction) throws NoConnection, InterruptedException  {
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
	/**
	 * The getAngle returns the actual angle of the rudder 
	 */
	public MFloat getAngle() throws NoConnection {
		try {
			float voltageValue = lj.read(new MInteger (0),new MInteger (8),new MInteger (1)).getValue();       //value that needs to be read from the labjack
			if (voltageValue < 2.45) {
				voltageDifference = (float) (2.45 - voltageValue);
				angleDifference = (float) (voltageDifference * 57.14);
				angle = new MFloat(angleDifference);
			}
			if (voltageValue > 2.45) {
				voltageDifference = (float) (voltageValue -2.45);
				angleDifference = (float) (voltageDifference * 57.14);
				angle = new MFloat(-angleDifference);
			}
			if (voltageValue == 2.45){
				angle = new MFloat(0);
			}
		} catch(IOException e) {
			throw new NoConnection("Cannot read from LabJack\n" + e.getMessage(), e.getCause());
		}
		return angle;
	}
}


