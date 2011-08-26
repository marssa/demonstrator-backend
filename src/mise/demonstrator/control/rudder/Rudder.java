package mise.demonstrator.control.rudder;

import mise.marssa.data_types.MBoolean;
import mise.marssa.data_types.integer_datatypes.MInteger;
import mise.marssa.interfaces.control.rudder.IRudder;

public class Rudder implements IRudder{
	
	private int time;
	private int stepRight  = 0;
	private int stepLeft = 0;
	private boolean startStop = false;
	
	public Rudder (MInteger time) {
		this.time  = time.getValue();	
	}
	
	public void rotate(MBoolean direction) throws InterruptedException{
		//TODO --> Change left and right to SB & PS ? Is this turning left of stepper --> which will turn the boat towards starboard?...
		
		startStop = true;
			
		if ((startStop && stepLeft == 0 && direction.getValue()) || (startStop && stepRight==3  && direction.getValue()== false)) {
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
			startStop = !startStop;
			Thread.sleep(time);
		}
		if ((startStop && stepLeft == 1 && direction.getValue()) || (startStop && stepRight==2  && direction.getValue()== false)) {
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
			startStop = !startStop;
			Thread.sleep(time);
		}
		if ((startStop == true && stepLeft == 2 && direction.getValue()) || (startStop && stepRight==1  && direction.getValue()== false)) {
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
			startStop = !startStop;
			Thread.sleep(time);
		}
		if ((startStop == true && stepLeft == 3 && direction.getValue()) || (startStop && stepRight==0  && direction.getValue()== false)) {
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
			startStop =! startStop;
			Thread.sleep(time);
		}
	}
}


