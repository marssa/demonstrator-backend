package mise.marssa.demonstrator.control.lighting;

import java.net.UnknownHostException;

import mise.marssa.footprint.datatypes.MBoolean;
import mise.marssa.footprint.datatypes.MString;
import mise.marssa.footprint.datatypes.integer.MInteger;
import mise.marssa.footprint.exceptions.NoConnection;
import mise.marssa.footprint.interfaces.control.lighting.ILightToggle;
import mise.marssa.footprint.logger.MMarker;
import mise.marssa.services.diagnostics.daq.LabJack;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;

public class NavigationLightsController implements ILightToggle {

	static Logger logger = (Logger) LoggerFactory
			.getLogger(NavigationLightsController.class.getName());

	private boolean lightState;
	private LabJack lj;
	private String switched;
	private MInteger navLights;
	private Object[] poho = { lj.getHost().getContents(),
			lj.getPort().getValue() };

	/**
	 * 
	 * @param host
	 * @param port
	 * @param navLights
	 * @throws UnknownHostException
	 * @throws NoConnection
	 */
	public NavigationLightsController(MString host, MInteger port,
			MInteger navLights) throws UnknownHostException, NoConnection {
		// TODO There must be something wrong here. FIO4 is operating in output
		// mode, regardless of FIO4-dir
		// Set direction for FIO4 port
		// lj.write(LabJack.FIO4_DIR_ADDR, new MBoolean(true));
		this.lj = LabJack.getInstance(host, port);
		logger.info(
				"An instance of Navigation light controller was instantiated with labjack host {}, and port {}.",
				poho);
		this.lightState = false;
		this.navLights = navLights;

	}

	/**
	 * 
	 * @param host
	 * @param port
	 * @param navLights
	 * @param newState
	 * @throws NoConnection
	 * @throws UnknownHostException
	 */
	public NavigationLightsController(MString host, MInteger port,
			MInteger navLights, MBoolean newState) throws NoConnection,
			UnknownHostException {
		// TODO There must be something wrong here. FIO4 is operating in output
		// mode, regardless of FIO4-dir
		// Set direction for FIO4 port
		// lj.write(LabJack.FIO4_DIR_ADDR, new MBoolean(true));
		this.lj = LabJack.getInstance(host, port);
		logger.info(
				"An instance of Navigation light controller was instantiated with labjack host: {}, and port: {}, with state set to: {}",
				poho, newState.getValue());
		lightState = newState.getValue();
		lj.write(navLights, newState);
	}

	public void toggleLight() throws NoConnection {

		lightState = !lightState;
		if (lightState)
			switched = "ON";
		else
			switched = "OFF";

		logger.trace(
				"NavigationLights from labjack with host: {} and port: {} have been switched {} .",
				poho, switched);
		lj.write(navLights, new MBoolean(lightState));
	}

	public MBoolean getNavigationLightState() {
		logger.trace(
				MMarker.GETTER,
				"Returning LightState {}, from labjack with host: {} and port: {} .",
				new MBoolean(lightState), poho);
		return new MBoolean(lightState);
	}

	public void setNavigationLightState(MBoolean newState) throws NoConnection {
		this.lightState = newState.getValue();
		if (newState.getValue())
			switched = "ON";
		else
			switched = "OFF";
		logger.trace(
				MMarker.SETTER,
				"Switching Navigationlight : {} ,from labjack with host: {} and port: {}",
				switched, poho);
		lj.write(navLights, newState);
	}
}
