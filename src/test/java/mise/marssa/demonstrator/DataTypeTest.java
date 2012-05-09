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
package mise.marssa.demonstrator;

import mise.marssa.footprint.datatypes.MBoolean;
import mise.marssa.footprint.datatypes.MDate;
import mise.marssa.footprint.datatypes.MString;
import mise.marssa.footprint.datatypes.decimal.distance.ADistance;
import mise.marssa.footprint.datatypes.decimal.distance.Metres;
import mise.marssa.footprint.datatypes.decimal.electrical.charge.ACharge;
import mise.marssa.footprint.datatypes.decimal.electrical.charge.Coulombs;
import mise.marssa.footprint.datatypes.decimal.electrical.current.ACurrent;
import mise.marssa.footprint.datatypes.decimal.electrical.current.Amps;
import mise.marssa.footprint.datatypes.decimal.electrical.impedance.AImpedance;
import mise.marssa.footprint.datatypes.decimal.electrical.impedance.KOhms;
import mise.marssa.footprint.datatypes.decimal.electrical.power.APower;
import mise.marssa.footprint.datatypes.decimal.electrical.power.Watts;
import mise.marssa.footprint.datatypes.decimal.electrical.voltage.AVoltage;
import mise.marssa.footprint.datatypes.decimal.electrical.voltage.Volts;
import mise.marssa.footprint.datatypes.time.ATime;
import mise.marssa.footprint.datatypes.time.Milliseconds;
import mise.marssa.footprint.exceptions.OutOfRange;

public class DataTypeTest {
	public static void main(java.lang.String[] args) {
		try {
			System.out.println((ADistance) new Metres(0.1f));
		} catch (OutOfRange e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println((ACharge) new Coulombs(0.02f));
		System.out.println((ACurrent) new Amps(0.4f));
		System.out.println((AImpedance) new KOhms(2.7f));
		System.out.println((APower) new Watts(125.0f));
		System.out.println((AVoltage) new Volts(5.0f));
		System.out.println((ATime) new Milliseconds(3700000));
		System.out.println(new MDate(System.currentTimeMillis()).toJSON());
		System.out.println(new MBoolean(true).toJSON());
		System.out.println(new MString("Hello World").toJSON());
	}
}
