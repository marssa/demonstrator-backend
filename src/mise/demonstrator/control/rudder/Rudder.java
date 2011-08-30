package mise.demonstrator.control.rudder;

import mise.marssa.data_types.MBoolean;
import mise.marssa.data_types.integer_datatypes.MInteger;
import mise.marssa.interfaces.control.rudder.IRudder;

public class Rudder implements IRudder{
	
	private int time;
	private int stepRight  = 0;
	private int stepLeft = 0;
	
	public Rudder (MInteger time) {
		this.time  = time.getValue();	
	}
	
	public void rotate(MBoolean direction) throws InterruptedException{
		//TODO --> Change left and right to SB & PS ? Is this turning left of stepper --> which will turn the boat towards starboard?...
		
		if ((stepLeft == 0 && direction.getValue()) || (stepRight==3  && direction.getValue()== false)) {
			/*
			System.out.println("logic HIGH on 'CIO0'");
			System.out.println("logic HIGH on 'CIO1'");
			System.out.println("logic LOW on 'CIO2'");
			System.out.println("logic LOW on 'CIO3'");
			*/
			System.out.println("1 1 0 0");
			System.out.println("");
			stepLeft = 1;
			stepRight= 0;
			Thread.sleep(time);	
			return;
		}
		if ((stepLeft == 1 && direction.getValue()) || (stepRight==2  && direction.getValue()== false)) {
			/*
			System.out.println("logic LOW on 'CIO0'");
			System.out.println("logic HIGH on 'CIO1'");
			System.out.println("logic HIGH on 'CIO2'");
			System.out.println("logic LOW on 'CIO3'");
			*/
			System.out.println("0 1 1 0");
			System.out.println("");
			stepLeft= 2;
			stepRight= 3;
			Thread.sleep(time);
			return;
		}
		if ((stepLeft == 2 && direction.getValue()) || (stepRight==1  && direction.getValue()== false)) {
			/*
			System.out.println("logic LOW on 'CIO0'");
			System.out.println("logic LOW on 'CIO1'");
			System.out.println("logic HIGH on 'CIO2'");
			System.out.println("logic HIGH on 'CIO3'");
			*/
			System.out.println("0 0 1 1");
			System.out.println("");
			stepLeft= 3;
			stepRight= 2;
			Thread.sleep(time);
			return;
		}
		if ((stepLeft == 3 && direction.getValue()) || (stepRight==0  && direction.getValue()== false)) {
			/*
			System.out.println("logic HIGH on 'CIO0'");
			System.out.println("logic LOW on 'CIO1'");
			System.out.println("logic LOW on 'CIO2'");
			System.out.println("logic HIGH on 'CIO3'");
			*/
			System.out.println("1 0 0 1");
			System.out.println("");
			stepLeft= 0;
			stepRight= 1;
			Thread.sleep(time);
			return;
		}
	}
}


