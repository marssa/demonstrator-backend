/**
 * 
 */
package mise.demonstrator.control;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import mise.marssa.data_types.MString;
import mise.marssa.data_types.integer_datatypes.MInteger;
import mise.marssa.exceptions.NoConnection;

import net.wimpi.modbus.io.ModbusTCPTransport;
import net.wimpi.modbus.msg.ModbusMessage;
import net.wimpi.modbus.msg.ModbusMessageImpl;

/**
 * @author Clayton Tabone
 *
 */
public class LabJack {
	
	private ModbusTCPTransport modbusTransport;

	/**
	 * @throws NoConnection 
	 * 
	 */
	public LabJack(MString host, MInteger port) throws NoConnection {
		Socket socket;
		try {
			socket = new Socket(host.getContents(), port.getValue());
		} catch (UnknownHostException e) {
			throw new NoConnection("Cannot find host: " + host + "Exception details\n" + e.getMessage(), e.getCause());
		} catch (IOException e) {
			throw new NoConnection("Cannot connect to host: " + host + "Exception details\n" + e.getMessage(), e.getCause());
		}
		modbusTransport = new ModbusTCPTransport(socket);
	}

	public void getX() {
		ModbusMessageImpl msg = new ModbusMessageImpl() {
			
			@Override
			public void writeData(DataOutput dout) throws IOException {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void readData(DataInput din) throws IOException {
				// TODO Auto-generated method stub
				
			}
		};
		modbusTransport.writeMessage(msg);
	}
}
