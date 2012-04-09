/**
 * 
 */
package mise.marssa.demonstrator;

import mise.marssa.demonstrator.constants.Constants;
import mise.marssa.demonstrator.control.electrical_motor.MotorController;
import mise.marssa.demonstrator.control.lighting.NavigationLightsController;
import mise.marssa.demonstrator.control.lighting.UnderwaterLightsController;
import mise.marssa.demonstrator.control.rudder.RudderController;
import mise.marssa.demonstrator.web_services.WebServices;
import mise.marssa.footprint.datatypes.MString;
import mise.marssa.footprint.datatypes.integer.MInteger;
import mise.marssa.footprint.exceptions.ConfigurationError;
import mise.marssa.footprint.exceptions.OutOfRange;
import mise.marssa.services.diagnostics.daq.LabJack;
import mise.marssa.services.diagnostics.daq.LabJack.TimersEnabled;
import mise.marssa.services.navigation.GpsReceiver;

import org.apache.commons.daemon.Daemon;
import org.apache.commons.daemon.DaemonContext;
import org.apache.commons.daemon.DaemonInitException;
import org.restlet.Component;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;

/**
 * @author Clayton Tabone
 * 
 */
public class JSVC implements Daemon {
	private static final Logger logger = (Logger) LoggerFactory
			.getLogger(JSVC.class);

	private LabJack labJack;
	private NavigationLightsController navLightsController;
	private UnderwaterLightsController underwaterLightsController;
	private MotorController motorController;
	private RudderController rudderController;
	private GpsReceiver gpsReceiver;
	private WebServices webServices;

	Component component = new Component();

	/**
	 * Open configuration files, create a trace file, create ServerSockets,
	 * Threads
	 * 
	 * @param context
	 */
	public void init(DaemonContext context) throws DaemonInitException,
			Exception {
		// Initialise LabJack
		try {
			logger.info("Initialising LabJack ...");
			labJack = LabJack.getInstance(Constants.LABJACK.HOST,
					Constants.LABJACK.PORT);
			logger.info("LabJack initialized successfully on {}:{}",
					Constants.LABJACK.HOST, Constants.LABJACK.PORT);
		} catch (Exception e) {
			logger.error("Failed to initialize LabJack", e);
			stop();
		}

		// Initialise Controllers and Receivers
		try {
			logger.info("Initialising navigation lights controller ... ");
			navLightsController = new NavigationLightsController(
					Constants.LABJACK.HOST, Constants.LABJACK.PORT,
					LabJack.FIO4_DIR_ADDR);
			logger.info("Navigation lights controller initialised successfully");

			logger.info("Initialising underwater lights controller ... ");
			underwaterLightsController = new UnderwaterLightsController(
					Constants.LABJACK.HOST, Constants.LABJACK.PORT,
					LabJack.FIO13_DIR_ADDR);
			logger.info("Underwater lights controller initialised successfully");

			logger.info("Initialising motor controller ... ");
			motorController = new MotorController(labJack);
			logger.info("Motor controller initialised successfully");

			logger.info("Initialising rudder controller ... ");
			rudderController = new RudderController(labJack);
			logger.info("Rudder controller initialised successfully");

			logger.info("Initialising GPS receiver ... ");
			gpsReceiver = new GpsReceiver(Constants.GPS.HOST,
					Constants.GPS.PORT);
			logger.info("GPS receiver initialised successfully");

			logger.info("Initialising web services ... ");
			webServices = new WebServices(navLightsController,
					underwaterLightsController, motorController,
					rudderController, gpsReceiver);
			logger.info("Web services initialised successfully");

			logger.info("Starting restlet web servicves ... ");
			webServices.start();
			logger.info("Web servicves started. Listening on {}:{}",
					Constants.GPS.HOST, Constants.GPS.PORT);
		} catch (ConfigurationError e) {
			e.printStackTrace();
			stop();
		} catch (OutOfRange e) {
			e.printStackTrace();
			stop();
		}
	}

	/**
	 * Start the Thread, accept incoming connections
	 */
	public void start() throws Exception {
		System.out.print("Starting web services ... ");
		webServices.start();
		System.out.println("success!");
	}

	/**
	 * Inform the Thread to terminate the run(), close the ServerSockets
	 */
	public void stop() throws Exception {
		try {
			System.out.print("Stopping web services ... ");
			webServices.stop();
			System.out.println("success!");
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Destroy any object created in init()
	 */
	public void destroy() {
		labJack = null;
		navLightsController = null;
		underwaterLightsController = null;
		motorController = null;
		rudderController = null;
		gpsReceiver = null;
		webServices = null;
	}
}
