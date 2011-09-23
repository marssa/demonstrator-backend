package mise.demonstrator;

import java.io.ByteArrayInputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;

import net.wimpi.modbus.ModbusException;
import net.wimpi.modbus.ModbusIOException;
import net.wimpi.modbus.ModbusSlaveException;
import net.wimpi.modbus.io.ModbusTCPTransaction;
import net.wimpi.modbus.msg.ReadInputRegistersRequest;
import net.wimpi.modbus.msg.ReadInputRegistersResponse;
import net.wimpi.modbus.msg.ReadMultipleRegistersRequest;
import net.wimpi.modbus.msg.ReadMultipleRegistersResponse;
import net.wimpi.modbus.net.TCPMasterConnection;
import net.wimpi.modbus.procimg.Register;
import net.wimpi.modbus.procimg.SimpleRegister;

import org.restlet.resource.ServerResource;
import mise.demonstrator.constants.Constants;
import mise.demonstrator.control.LabJack;
import mise.demonstrator.control.LabJack.TimerConfigMode;
import mise.demonstrator.control.LabJack.Timers;
import mise.demonstrator.control.electrical_motor.MotorController;
import mise.demonstrator.control.rudder.RudderController;
import mise.demonstrator.control.lighting.NavigationLightsController;
import mise.demonstrator.navigation_equipment.GpsReceiver;
import mise.demonstrator.web_service.WebServices;
import mise.marssa.data_types.MString;
import mise.marssa.data_types.integer_datatypes.MInteger;
import mise.marssa.exceptions.NoConnection;
import mise.marssa.exceptions.NoValue;
import mise.marssa.exceptions.OutOfRange;

/** @author Clayton Tabone
 *
 */
public class Main extends ServerResource {
		
	/**
	 * @param args the args
	 * @throws OutOfRange 
	 * @throws Exception 
	 */
	public static void main(java.lang.String[] args) throws Exception  {
		// Initialise LabJack
		LabJack labJack = null;
		try {
			//labJack = LabJack.getInstance(Constants.LABJACK.HOST, Constants.LABJACK.PORT);
			//labJack.setTimerMode(Timers.TIMER_0, TimerConfigMode.PWM_OUTPUT_16BIT);
			//labJack.setTimerMode(Timers.TIMER_1, TimerConfigMode.PWM_OUTPUT_16BIT);
		} catch (Exception e1) {
			System.err.println("Cannot connect to " + Constants.LABJACK.HOST + ":" + Constants.LABJACK.PORT);
			e1.printStackTrace();
			System.exit(1);
		}
		
		NavigationLightsController navLightsController = new NavigationLightsController(labJack);
		// TODO: MotorController needs to be modified to use an instance of LabJack
		MotorController motorController = new MotorController(labJack);
		RudderController rudderController = new RudderController(labJack);
		// TODO attach physical GPSReceiver
		GpsReceiver gpsReceiver = null;
		//GpsReceiver gpsReceiver = new GpsReceiver(Constants.GPS.HOST, Constants.GPS.PORT);
		
		try {
		//	WebServices webService = new WebServices(navLightsController, motorController, rudderController, gpsReceiver);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		TCPMasterConnection con = null; //the connection
		ModbusTCPTransaction trans = null; //the transaction
		ReadMultipleRegistersRequest req = null;
		ReadMultipleRegistersResponse res = null; //the response
		//SimpleRegister registerLSB = new SimpleRegister(12016 & 0xFFFF);
		//SimpleRegister registerMSB = new SimpleRegister((12016 & 0xFFFF0000) >> 16);
		//SimpleRegister[] registerArray = {registerLSB, registerMSB};
		
		//ReadMultipleRegistersResponse res = new ReadMultipleRegistersResponse(registerArray);
		//ReadInputRegistersRequest rirr = new ReadInputRegistersRequest(6100,3);
		// ReadInputRegistersResponse rres = null;  //the read response
		
		
		InetAddress addr = null; //the slave's address
		
		
		
		//1. Setup the parameters
		
		addr = InetAddress.getByName("192.168.2.14");  
	    con = new TCPMasterConnection(addr);
	    con.setPort(5021);
	    con.connect();
	    
  
	    req = new ReadMultipleRegistersRequest(0,10);

	  //4. Prepare the transaction
	  trans = new ModbusTCPTransaction(con);
	  trans.setRequest(req);
		
	//5. Execute the transaction repeat times
	 
	    trans.execute();
	    res = (ReadMultipleRegistersResponse) trans.getResponse();
	    //TODO verify right order for LSB/MSB
	    byte[] lsb = res.getRegister(0).toBytes();
	    byte[] msb = res.getRegister(1).toBytes();
	    
	    byte[] both = {lsb[0], lsb[1], msb[0], msb[1]};
	    
        //System.out.println("Hex Value of register " + "= "+rres.getHexMessage());
		
		ByteArrayInputStream bais = new ByteArrayInputStream(both);
	    DataInputStream din = new DataInputStream(bais);
	    System.out.println("Float representation for LSB " + din.readFloat());
	    
	    System.out.println("Float representation for LSB " + din.read());
	    System.out.println("Float representation for LSB " + din.read());
	    System.out.println("Float representation for LSB " + din.read());
	    System.out.println("Float representation for LSB " + din.read());
	    System.out.println("Float representation for LSB " + din.read());
	    
	    //res = (ReadMultipleRegistersResponse) trans.getResponse();
	    //System.out.println("Digital Inputs Status=" + res.getDiscretes().toString());
	    //System.out.println("Byte count0=" + res.readData(din));
	    System.out.println("Byte count1=" + res.getRegisterValue(1));
	    System.out.println("Register 7=" + res.getRegisterValue(8));
	    System.out.println("Register 8=" + res.getRegisterValue(9));
	   // System.out.println("Data Length=" + rres.getDataLength());
	  //  System.out.println("Function Code=" + rres.getFunctionCode());
	  //  System.out.println("Hex message=" + rres.getHexMessage());
	  //  System.out.println("output length=" + rres.getOutputLength());
	  //  System.out.println("protocolid=" + rres.getProtocolID());
	   // System.out.println("Register value 0=" + rres.getRegisterValue(15));
	  //  System.out.println("Transactionid=" + rres.getTransactionID());
	  //  System.out.println("Unit id=" + rres.getUnitID());
	 //   System.out.println("word count=" + rres.getWordCount());
	  //  System.out.println("# code=" + rres.hashCode());
	  

	   //6. Close the connection
	   con.close();

	
		/*
		// NavigationLights Tests
		System.out.println(navigationLights.getNavigationLightState());
		Percentage desiredValue = new Percentage(10f);
		
		// MotorControl Tests
		try {
			motorController.rampTo(desiredValue);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		// Rudder Tests
		try {
			rudderController.rotate(new MBoolean (true));
			rudderController.rotate(new MBoolean (false));
			rudderController.rotate(new MBoolean (true));
			rudderController.rotate(new MBoolean (false));
			rudderController.rotate(new MBoolean (false));
			rudderController.rotate(new MBoolean (true));
			rudderController.rotate(new MBoolean (true));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		// GPSReceiver Tests
		try {
			System.out.println("The GPS coordinates are " + gpsReceiver.getCoordinate());
			System.out.println("Altitude is " + gpsReceiver.getElevation());
			System.out.println("Course over ground " + gpsReceiver.getCOG());
			System.out.println("Speed over ground " + gpsReceiver.getSOG());
			//System.out.println("EPT " + gps.getEPT());      ///have to find the EPT
			System.out.println("Time " + gpsReceiver.getDate());
		} catch (NoConnection e) {
			e.printStackTrace();
		} catch (NoValue e) {
			e.printStackTrace();
		}
		
		GpsReceiver gps = new GpsReceiver(new MString (Constants.GPS.HOST.getContents()),new MInteger (Constants.GPS.PORT.getValue()));
		try {
			System.out.println("The GPS coordinates are " + gpsReceiver.getCoordinate());
			System.out.println("Altitude is " + gpsReceiver.getElevation());
			System.out.println("Course over ground " + gpsReceiver.getCOG());
			System.out.println("Speed over ground " + gpsReceiver.getSOG());
			//System.out.println("EPT " + gps.getEPT());      ///have to find the EPT
			System.out.println("Time " + gpsReceiver.getDate());
		} catch (NoConnection e) {
			e.printStackTrace();
		} catch (NoValue e) {
			e.printStackTrace();
		}
		*/
	}
}
