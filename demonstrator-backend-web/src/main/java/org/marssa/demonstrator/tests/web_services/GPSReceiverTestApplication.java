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
package org.marssa.demonstrator.tests.web_services;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.marssa.footprint.datatypes.composite.Coordinate;
import org.marssa.footprint.datatypes.composite.Latitude;
import org.marssa.footprint.datatypes.composite.Longitude;
import org.marssa.footprint.datatypes.decimal.DegreesDecimal;
import org.marssa.footprint.exceptions.OutOfRange;

@Path("/test/gps")
public class GPSReceiverTestApplication {

	@GET
	@Produces("application/json")
	@Path("/coordinates")
	public String getCoordinate() throws OutOfRange {
		Coordinate coordinate = new Coordinate(new Latitude(new DegreesDecimal(
				20.5)), new Longitude(new DegreesDecimal(129.8)));
		return coordinate.toJSON().getContents();
	}
}