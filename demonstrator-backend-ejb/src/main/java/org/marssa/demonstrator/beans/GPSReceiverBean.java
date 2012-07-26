/**
 * Copyright 2012 MARSEC-XL International Limited
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.marssa.demonstrator.beans;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.Inet4Address;
import java.net.UnknownHostException;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.enterprise.context.ApplicationScoped;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.marssa.demonstrator.gps.GPSType;
import org.marssa.demonstrator.network.AddressType;
import org.marssa.demonstrator.settings.Settings;
import org.marssa.footprint.datatypes.MString;
import org.marssa.footprint.datatypes.integer.MInteger;
import org.marssa.footprint.exceptions.ConfigurationError;
import org.marssa.footprint.exceptions.NoConnection;
import org.marssa.footprint.exceptions.NoValue;
import org.marssa.services.navigation.GpsReceiver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Clayton Tabone
 * 
 */
@ApplicationScoped
@Singleton
public class GPSReceiverBean {

	private static final Logger logger = LoggerFactory
			.getLogger(GPSReceiverBean.class.getName());

	// private final HashMap<String, GpsReceiver> gpsReceivers = new
	// HashMap<String, GpsReceiver>();
	private GpsReceiver gpsReceiver;

	/**
	 * 
	 */
	public GPSReceiverBean() {
		// TODO Auto-generated constructor stub
	}

	// init method for single GPSReceiver
	@PostConstruct
	private void init() throws ConfigurationError, NoConnection,
			UnknownHostException, JAXBException, FileNotFoundException, NoValue {
		logger.info("Initializing GPS Receiver Bean");
		JAXBContext context = JAXBContext
				.newInstance(new Class[] { Settings.class });
		Unmarshaller unmarshaller = context.createUnmarshaller();
		InputStream is = this.getClass().getClassLoader()
				.getResourceAsStream("configuration/settings.xml");

		Settings settings = (Settings) unmarshaller.unmarshal(is);
		GPSType gpsEntry = settings.getGpsReceivers().getGps();
		AddressType addressElement = gpsEntry.getSocket();
		MString ip;
		if (addressElement.getHost().getIp() == null
				|| addressElement.getHost().getIp().isEmpty()) {
			String hostname = addressElement.getHost().getHostname();
			ip = new MString(Inet4Address.getByName(hostname).getAddress()
					.toString());
		} else {
			ip = new MString(addressElement.getHost().getIp());
		}
		logger.info(
				"Found configuration for {} connected to {}, port {}",
				new Object[] { gpsEntry.getName(), ip, addressElement.getPort() });
		this.gpsReceiver = new GpsReceiver(ip, new MInteger(
				addressElement.getPort()));
		logger.info("Initialized GPS Receiver Bean");
	}

	// getter for single GPSReceiver
	public GpsReceiver getGPSReceiver() throws ConfigurationError {
		return gpsReceiver;
	}

	// getter for multiple GPSReceivers
	/*
	 * @PostConstruct private void init() throws ConfigurationError,
	 * NoConnection, UnknownHostException, JAXBException, FileNotFoundException,
	 * NoValue { logger.info("Initializing GPS Receiver Bean"); JAXBContext
	 * context = JAXBContext .newInstance(new Class[] { Settings.class });
	 * Unmarshaller unmarshaller = context.createUnmarshaller(); InputStream is
	 * = this.getClass().getClassLoader()
	 * .getResourceAsStream("configuration/settings.xml");
	 * 
	 * Settings settings = (Settings) unmarshaller.unmarshal(is); for (GPSType
	 * gpsEntry : settings.getGpsReceivers().getGps()) { AddressType
	 * addressElement = gpsEntry.getSocket();
	 * logger.info("Found configuration for {} connected to {}, port {}", new
	 * Object[] { gpsEntry.getName(), addressElement.getHost().getHostname(),
	 * }); MString ip; if (addressElement.getHost().getIp() == null ||
	 * addressElement.getHost().getIp().isEmpty()) { String hostname =
	 * addressElement.getHost().getHostname(); ip = new
	 * MString(Inet4Address.getByName(hostname).getAddress() .toString()); }
	 * else { ip = new MString(addressElement.getHost().getIp()); } logger.info(
	 * "Found configuration for {} connected to {}, port {}", new Object[] {
	 * gpsEntry.getName(), ip, addressElement.getPort() }); GpsReceiver gps =
	 * new GpsReceiver(ip, new MInteger( addressElement.getPort()));
	 * gpsReceivers.put(ip.toString() + ":" + addressElement.getPort(), gps); }
	 * logger.info("Initialized GPS Receiver Bean"); }
	 */

	@PreDestroy
	private void destroy() {
		logger.info("Destroying GPS Receiver Bean");
		// TODO Add unimplemented method
		logger.info("Destroyed GPS Receiver Bean");
	}

	// init method for GPSReceiverList
	/*
	 * public GpsReceiver getGPSByIP(MString ip, MInteger port) throws
	 * ConfigurationError { GpsReceiver gps = gpsReceivers.get(ip.toString() +
	 * ":" + port.intValue()); if (gps == null) throw new ConfigurationError(
	 * "No GPS configuration has been found for " + ip + ":" + port); return
	 * gps; }
	 * 
	 * public GpsReceiver getGPSByHostname(MString hostname, MInteger port)
	 * throws ConfigurationError, UnknownHostException { String ip =
	 * Inet4Address.getByName(hostname.toString()).getAddress() .toString();
	 * GpsReceiver gps = gpsReceivers.get(ip + ":" + port.intValue()); if (gps
	 * == null) throw new ConfigurationError(
	 * "No GPS configuration has been found for " + ip + ":" + port); return
	 * gps; }
	 */
}
