/**
 * 
 */
package mise.demonstrator.control;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;

import net.wimpi.modbus.ModbusException;
import net.wimpi.modbus.ModbusIOException;
import net.wimpi.modbus.ModbusSlaveException;
import net.wimpi.modbus.io.ModbusTCPTransaction;
import net.wimpi.modbus.msg.WriteSingleRegisterRequest;
import net.wimpi.modbus.net.TCPMasterConnection;
import net.wimpi.modbus.procimg.SimpleRegister;

import mise.marssa.data_types.MBoolean;
import mise.marssa.data_types.MString;
import mise.marssa.data_types.integer_datatypes.MInteger;
import mise.marssa.exceptions.NoConnection;

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
	
	// Direction addresses
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
	
	// TODO There must be something wrong here. FIO4 is operating in output mode, regardless of FIO4-dir
	static public final MBoolean FIO_OUT_DIRECTION = new MBoolean(true);
	static public final MBoolean FIO_IN_DIRECTION = new MBoolean(false);
	
	// Timer mode addresses
	static public final MInteger TIMER0_CONFIG_MODE_ADDR = new MInteger(7100);
	static public final MInteger TIMER1_CONFIG_MODE_ADDR = new MInteger(7102);
	
	/**
	 * The LabJack U3 has only two timers.<br />
	 * Section 2.9.1 of the LabJack documentation has a good description of these modes
	 * @see http://labjack.com/support/u3/users-guide/2.9
	 */
	public enum Timers {
		TIMER_0(0),								// Documented in section 2.9.1.1
		TIMER_1(1);								// Documented in section 2.9.1.2
		
		private int timerNumber;
		private MInteger timerConfigModeAddress;
		private TimerConfigMode timerConfigMode;
		
		
		private Timers(int timerNumber) {
			this.timerNumber = timerNumber;
			// Constuctor belongs to an enum class. Hence only 0 and 1 are permissible as the timerNumber
			if(timerNumber == 0)
				timerConfigModeAddress = LabJack.TIMER0_CONFIG_MODE_ADDR;
			else
				timerConfigModeAddress = LabJack.TIMER1_CONFIG_MODE_ADDR;
		}
		
		private void setTimerConfigMode(TimerConfigMode timerConfigMode) {
			this.timerConfigMode = timerConfigMode;
		}
		
		public TimerConfigMode getTimerConfigMode() {
			return timerConfigMode;
		}
		
		public MInteger getTimerConfigModeAddress() {
			return this.timerConfigModeAddress;
		}
	};
	
	/**
	 * Timer base clock can have a mode between 0 and 6.<br />
	 * <b>Note: Both timers use the same timer clock!</b><br />
	 * Section 2.9.1 of the LabJack documentation has a good description of these modes
	 * @see http://labjack.com/support/u3/users-guide/2.9
	 */
	public enum TimerConfigMode {
		PWM_OUTPUT_16BIT(0),					// Documented in section 2.9.1.1
		PWM_OUTPUT_8BIT(1),						// Documented in section 2.9.1.2
		PERIOD_MEASURMENT_RISING_32BIT(2),		// Documented in section 2.9.1.3
		PERIOD_MEASURMENT_FALLING_32BIT(3),		// Documented in section 2.9.1.3
		DUTY_CYCLE_MEASURMENT(4),				// Documented in section 2.9.1.4
		FIRMWARE_COUNTER_INPUT(5),				// Documented in section 2.9.1.5
		FIRMWARE_COUNTER_INPUT_DEBOUNCE(6),		// Documented in section 2.9.1.6
		FREQUENCY_OUTPUT(7),					// Documented in section 2.9.1.7
		QUADRATURE_INPUT(8),					// Documented in section 2.9.1.8
		TIME_STOP_INPUT(9),						// Documented in section 2.9.1.9
		SYTEM_TIMER_LOWER_32BITS(10),			// Documented in section 2.9.1.10
		SYTEM_TIMER_UPPER_32BITS(11),			// Documented in section 2.9.1.10
		PERIOD_MEASURMENT_RISING_16BIT(12),		// Documented in section 2.9.1.11
		PERIOD_MEASURMENT_FALLING_16BIT(13),	// Documented in section 2.9.1.11
		LINE_TO_LINE(14);						// Documented in section 2.9.1.12
		
		private int mode;
		private TimerConfigMode(int mode) {
			this.mode = mode;
		}
		
		public MInteger getValue() {
			return new MInteger(mode);
		}
	};
	
	/**
	 * Timer base clock can have a mode between 0 and 6.<br />
	 * <b>Note: Both timers use the same timer clock!</b><br />
	 * Section 2.9.1.1 of the LabJack documentation has a good description of these modes
	 * @see http://labjack.com/support/u3/users-guide/2.9
	 */
	public enum TimerBaseClock {
		CLOCK_4_MHZ(0),
		CLOCK_12_MHZ(1),
		CLOCK_48_MHZ(2),
		CLOCK_1_MHZ_DIVISOR(3),
		CLOCK_4_MHZ_DIVISOR(4),
		CLOCK_12_MHZ_DIVISOR(5),
		CLOCK_48_MHZ_DIVISOR(6);
		
		private int clock;
		private TimerBaseClock(int clock) {
			this.clock = clock;
		}
	};
	
	/**
	 * Contains a host/port connection and the instance of the LabJack class
	 * @author Clayton Tabone
	 *
	 */
	private static final class LabJackConnection {
		MString host;
		MInteger port;
		LabJack lj;
		
		public LabJackConnection(MString host, MInteger port, LabJack lj) {
			this.host = host;
			this.port = port;
			this.lj = lj;
		}
		
		public boolean inUse(MString host, MInteger port) {
			return (this.host.getContents().equals(host) && this.port.getValue() == port.getValue());
		}
	}
	
	/**
	 * Contains the unique Set of ConnectionPair objects
	 * Multiple connections can be handled by the LabJack class.
	 * However, only one LabJack connection per instance is allowed.
	 * In order to conserve resources, for every new connection request a check is made.
	 * If a connection to the given address (ConnectionPair) is already active, it is used instead of opening a new connection.
	 * @author Clayton Tabone
	 *
	 */
	private static final class LabJackConnections implements Iterator<LabJackConnection> {
		//static private Set<LabJackConnection> activeConnections;
		static private ArrayList<LabJackConnection> activeConnections = new ArrayList<LabJack.LabJackConnection>();

		@Override
		public boolean hasNext() {
			return activeConnections.iterator().hasNext();
		}

		@Override
		public LabJackConnection next() {
			return activeConnections.iterator().next();
		}

		@Override
		public void remove() {
			activeConnections.iterator().remove();
		}
		
		public LabJackConnection getConnection(MString host, MInteger port) throws UnknownHostException, NoConnection {
			if(activeConnections != null) {
				for(LabJackConnection conn : activeConnections) {
					if(conn.inUse(host, port))
						return conn;
				}
			}
			LabJack lj = new LabJack(host, port);
			LabJackConnection newConnectionPair = new LabJackConnection(host, port, lj);
			activeConnections.add(newConnectionPair);
			return newConnectionPair;
		}
	}
	
	// The static list of connection pairs, each containing an instance of the LabJack class
	static private LabJackConnections connectionPairs = new LabJackConnections();
	
	// The actual TCP connection to the LabJack used in this instance
	private TCPMasterConnection masterConnection;
	
	private LabJack(MString host, MInteger port) throws UnknownHostException, NoConnection
	{
		try {
			InetAddress address = InetAddress.getByName(host.getContents());  // the slave's address
		    masterConnection = new TCPMasterConnection(address);
		    masterConnection.setPort(port.getValue());
		    masterConnection.connect();
		} catch (UnknownHostException e) {
			throw new NoConnection("Cannot find host: " + host + "Exception details\n" + e.getMessage(), e.getCause());
		} catch (IOException e) {
			throw new NoConnection("Cannot connect to host: " + host.getContents() + " Exception details\n" + e.getMessage(), e.getCause());
		} catch (Exception e) {
			throw new NoConnection("Network failure (TCPMasterConnection): " + host + "Exception details\n" + e.getMessage(), e.getCause());
		}
	}
	
	protected void finalize() throws Throwable {
    	masterConnection.close();
    	connectionPairs.remove();
    	super.finalize(); //not necessary if extending Object.
    }
	
	/**
     * Override method to prevent Object cloning
     * 
     * @throws CloneNotSupportedException
     */
    public Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}
	
	/**
     * This method return an instance to the singleton class LabJack<br />
     * Note: This method is thread-safe
     * @param host The host IP to which LabJack is connected 
     * @param port The host port to which LabJack is connected
     * @return singleton instance to the LabJack
     * @throws UnknownHostException
     * @throws NoConnection
     */
    public static synchronized LabJack getInstance(MString host, MInteger port) throws UnknownHostException, NoConnection {
    	LabJackConnection connection = connectionPairs.getConnection(host, port);
    	return connection.lj;
    }
    
    public void setTimerMode(Timers timer, TimerConfigMode timerConfigMode) {
    	timer.setTimerConfigMode(timerConfigMode);
    	write(timer.timerConfigModeAddress, timerConfigMode.getValue());
    }
	
	public void write(MInteger registerNumber, MInteger registerValue) {
		SimpleRegister register = new SimpleRegister(registerValue.getValue());
		WriteSingleRegisterRequest writeRequest = new WriteSingleRegisterRequest(registerNumber.getValue(), register);
		
	    // Prepare the transaction
		ModbusTCPTransaction transaction = new ModbusTCPTransaction(masterConnection);
	    transaction.setRequest(writeRequest);
	    //WriteSingleRegisterResponse writeResponse = (WriteSingleRegisterResponse) transaction.getResponse();
	   
	    // Execute the transaction repeat times
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
	}
	
	public void write(MInteger registerNumber, MBoolean state) {
		   
		int highLow = (state.getValue() ? 1 : 0);
		
		write(registerNumber, new MInteger(highLow));
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

