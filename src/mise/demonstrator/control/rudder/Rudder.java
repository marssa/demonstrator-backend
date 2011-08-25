package mise.demonstrator.control.rudder;

import mise.marssa.data_types.MBoolean;
import mise.marssa.data_types.integer_datatypes.MInteger;
import mise.marssa.interfaces.control.rudder.IRudder;

public class Rudder implements IRudder{
	
	private MInteger time;
	private MInteger stepRight  = new MInteger(0);
	private MInteger stepLeft = new MInteger(0);
	private MBoolean startStop = new MBoolean(false);
	
	
	public Rudder (MInteger time){
		this.time  = time;	
		
		}
	
	public void rotateLeft() throws InterruptedException{ //TO DO --> Change left and right to SB & PS ? Is this turning left of stepper --> which will turn the boat towards startboard?...
		
		startStop.setValue(true);
			
		if (stepLeft.getValue() == 0)
			{
			/*
			System.out.println("logic HIGH on 'CIO0'");
			System.out.println("logic HIGH on 'CIO1'");
			System.out.println("logic LOW on 'CIO2'");
			System.out.println("logic LOW on 'CIO3'");
			*/
			System.out.println("1 1 0 0");
			System.out.println("");
			stepLeft = new MInteger(1);
			stepRight= new MInteger(0);
			startStop.toggleValue();
			Thread.sleep(time.getValue());
			}
		if (startStop.getValue() && stepLeft.getValue() == 1)
			{/*
			System.out.println("logic LOW on 'CIO0'");
			System.out.println("logic HIGH on 'CIO1'");
			System.out.println("logic HIGH on 'CIO2'");
			System.out.println("logic LOW on 'CIO3'");
			*/
			System.out.println("0 1 1 0");
			System.out.println("");
			stepLeft= new MInteger(2);
			stepRight= new MInteger(3);
			startStop.toggleValue();
			Thread.sleep(time.getValue());
			}
		if (startStop.getValue() == true && stepLeft.getValue() == 2)
			{/*
			System.out.println("logic LOW on 'CIO0'");
			System.out.println("logic LOW on 'CIO1'");
			System.out.println("logic HIGH on 'CIO2'");
			System.out.println("logic HIGH on 'CIO3'");
			*/
			System.out.println("0 0 1 1");
			System.out.println("");
			stepLeft= new MInteger(3);
			stepRight= new MInteger(2);
			startStop.toggleValue();
			Thread.sleep(time.getValue());
			}
		if (startStop.getValue() == true && stepLeft.getValue() == 3)
			{/*
			System.out.println("logic HIGH on 'CIO0'");
			System.out.println("logic LOW on 'CIO1'");
			System.out.println("logic LOW on 'CIO2'");
			System.out.println("logic HIGH on 'CIO3'");
			*/
			System.out.println("1 0 0 1");
			System.out.println("");
			stepLeft= new MInteger(0);
			stepRight= new MInteger(1);
			startStop.toggleValue();
			Thread.sleep(time.getValue());
			}
		}
		
	public void rotateRight() throws InterruptedException{
	
		startStop.setValue(true);
			
		if (stepRight.getValue() == 0)
			{/*
			System.out.println("logic HIGH on 'CIO0'");
			System.out.println("logic LOW on 'CIO1'");
			System.out.println("logic LOW on 'CIO2'");
			System.out.println("logic HIGH on 'CIO3'");
			*/
			System.out.println("1 0 0 1");
			System.out.println("");
			stepRight= new MInteger(1);
			stepLeft= new MInteger(0);
			startStop.toggleValue();
			Thread.sleep(time.getValue());
			}
		if (startStop.getValue() == true && stepRight.getValue() == 1)
			{/*
			System.out.println("logic LOW on 'CIO0'");
			System.out.println("logic LOW on 'CIO1'");
			System.out.println("logic HIGH on 'CIO2'");
			System.out.println("logic HIGH on 'CIO3'");
			*/
			System.out.println("0 0 1 1");
			System.out.println("");
			stepRight= new MInteger(2);
			stepLeft= new MInteger(3);
			startStop.toggleValue();
			Thread.sleep(time.getValue());
			}
		if (startStop.getValue() == true && stepRight.getValue() == 2)
			{/*
			System.out.println("logic LOW on 'CIO0'");
			System.out.println("logic HIGH on 'CIO1'");
			System.out.println("logic HIGH on 'CIO2'");
			System.out.println("logic LOW on 'CIO3'");
			*/
			System.out.println("0 1 1 0");
			System.out.println("");
			stepRight= new MInteger(3);
			stepLeft= new MInteger(2);
			startStop.toggleValue();
			Thread.sleep(time.getValue());
			}
		if (startStop.getValue() == true && stepRight.getValue() == 3)
			{/*
			System.out.println("logic HIGH on 'CIO0'");
			System.out.println("logic HIGH on 'CIO1'");
			System.out.println("logic LOW on 'CIO2'");
			System.out.println("logic LOW on 'CIO3'");
			*/
			System.out.println("1 1 0 0");
			System.out.println("");
			stepRight= new MInteger(0);
			stepLeft= new MInteger(1);
			startStop.toggleValue();
			Thread.sleep(time.getValue());
			}
		}
	}


