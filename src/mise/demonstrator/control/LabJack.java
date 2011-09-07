/**
 * 
 */
package mise.demonstrator.control;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import mise.marssa.data_types.MBoolean;
import mise.marssa.data_types.MString;
import mise.marssa.data_types.integer_datatypes.MInteger;
import mise.marssa.exceptions.NoConnection;
import net.wimpi.modbus.ModbusException;
import net.wimpi.modbus.ModbusIOException;
import net.wimpi.modbus.ModbusSlaveException;
import net.wimpi.modbus.io.ModbusTCPTransaction;
import net.wimpi.modbus.msg.WriteSingleRegisterRequest;
import net.wimpi.modbus.msg.WriteSingleRegisterResponse;
import net.wimpi.modbus.net.TCPMasterConnection;
import net.wimpi.modbus.procimg.SimpleRegister;



/**
 * @author Warren Zahra
 *
 */
public class LabJack {
	
	// Register addresses
	static public final MInteger FIO4_ADDR = new MInteger(6004);
	static public final MInteger FIO5_ADDR = new MInteger(6005);
	static public final MInteger FIO6_ADDR = new MInteger(6006);
	static public final MInteger FIO7_ADDR = new MInteger(6007);
	static public final MInteger FIO8_ADDR = new MInteger(6008);
	static public final MInteger FIO9_ADDR = new MInteger(6009);
	static public final MInteger FIO10_ADDR = new MInteger(6010);
	static public final MInteger FIO11_ADDR = new MInteger(6011);
	static public final MInteger FIO12_ADDR = new MInteger(6012);
	static public final MInteger FIO13_ADDR = new MInteger(6013);
	static public final MInteger FIO14_ADDR = new MInteger(6014);
	static public final MInteger FIO15_ADDR = new MInteger(6015);
	static public final MInteger FIO16_ADDR = new MInteger(6016);
	static public final MInteger FIO17_ADDR = new MInteger(6017);
	static public final MInteger FIO18_ADDR = new MInteger(6018);
	static public final MInteger FIO19_ADDR = new MInteger(6019);
	
	//Direction ADDRESS
	static public final MInteger FIO4_DIR_ADDR = new MInteger(6104);
	static public final MInteger FIO5_DIR_ADDR = new MInteger(6105);
	static public final MInteger FIO6_DIR_ADDR = new MInteger(6106);
	static public final MInteger FIO7_DIR_ADDR = new MInteger(6107);
	static public final MInteger FIO8_DIR_ADDR = new MInteger(6108);
	static public final MInteger FIO9_DIR_ADDR = new MInteger(6109);
	static public final MInteger FIO10_DIR_ADDR = new MInteger(6110);
	static public final MInteger FIO11_DIR_ADDR = new MInteger(6111);
	static public final MInteger FIO12_DIR_ADDR = new MInteger(6112);
	static public final MInteger FIO13_DIR_ADDR = new MInteger(6113);
	static public final MInteger FIO14_DIR_ADDR = new MInteger(6114);
	static public final MInteger FIO15_DIR_ADDR = new MInteger(6115);
	static public final MInteger FIO16_DIR_ADDR = new MInteger(6116);
	static public final MInteger FIO17_DIR_ADDR = new MInteger(6117);
	static public final MInteger FIO18_DIR_ADDR = new MInteger(6118);
	static public final MInteger FIO19_DIR_ADDR = new MInteger(6119);
	
	static public final MInteger TIMER0_MODE_ADDR = new MInteger(5006);
	static public final MInteger TIMER1_MODE_ADDR = new MInteger(5006);
	
	// Timer modes
	static public final MInteger TIMER_PWM_MODE = new MInteger(2);
	public static final MInteger FIO4 = null;
	
	

	TCPMasterConnection connection = null;  //the connection
    ModbusTCPTransaction transaction = null;  //the transaction
    //ReadInputRegistersRequest readRegisterRequest = null;  //the read request
    //ReadInputDiscretesRequest readDiscreteRequest = null;  //the read request
    //ReadInputRegistersResponse readResponse = null;  //the read response
    InetAddress address = null;  // the slave's address
    SimpleRegister register = null;
    WriteSingleRegisterRequest writeRequest = null;
    WriteSingleRegisterResponse Wres = null;
       
	public LabJack(MString host, MInteger port) throws Exception
	{
		try {
				address = InetAddress.getByName(host.getContents());  
			    connection = new TCPMasterConnection(address);
			    connection.setPort(port.getValue());
			    connection.connect();
				} catch (UnknownHostException e) {
					throw new NoConnection("Cannot find host: " + host + "Exception details\n" + e.getMessage(), e.getCause());
				} catch (IOException e) {
					throw new NoConnection("Cannot connect to host: " + host.getContents() + " Exception details\n" + e.getMessage(), e.getCause());
				} catch (Exception e) {
		             System.out.println("Error");
		             e.printStackTrace();
		        } 
		
			}
	
	public void write(MInteger registerNumber, MBoolean state) {
		   
		int highLow = (state.getValue() ? 1 : 0);
				
		register = new SimpleRegister(highLow);
		WriteSingleRegisterRequest writeRequest = new WriteSingleRegisterRequest(registerNumber.getValue(),register);
		//WriteSingleRegisterResponse Wres = new WriteSingleRegisterResponse();
		
		    //4w. Prepare the transaction
		    transaction = new ModbusTCPTransaction(connection);
		    transaction.setRequest(writeRequest);
		   // Wres = (WriteSingleRegisterResponse) transaction.getResponse();
		   
		    //5w. Execute the transaction repeat times
		    try {
				transaction.execute();
			} catch (ModbusIOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ModbusSlaveException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ModbusException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}        
		    
		    //6. Close the connection
		    connection.close();
		
	}
	/*
	public void read(MInteger register, MInteger count) throws ModbusIOException, ModbusSlaveException, ModbusException{
		
			//3r. Prepare the request
			readRegisterRequest = new ReadInputRegistersRequest(register.getValue(), count.getValue());
			//readDiscreteRequest = new ReadInputDiscretesRequest(register.getValue(), count.getValue());
	
			//4r. Prepare the transaction
			transaction = new ModbusTCPTransaction(connection);
			transaction.setRequest(readRegisterRequest);
			
			//5r. Execute the READ transaction
            transaction.execute();
            readResponse = (ReadInputRegistersResponse) transaction.getResponse();
            System.out.println("Hex Value of register " + "= "+readResponse.getHexMessage());
		
	}*/
}

