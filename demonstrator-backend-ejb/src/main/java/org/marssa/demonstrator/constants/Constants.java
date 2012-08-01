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
package org.marssa.demonstrator.constants;

import org.marssa.footprint.datatypes.decimal.MDecimal;
import org.marssa.footprint.datatypes.integer.MInteger;

/**
 * @author Clayton Tabone
 * 
 */
public class Constants {

	/**
	 * Motor Constants
	 * 
	 * @author Clayton Tabone
	 */
	public final static class MOTOR {
		public final static MDecimal STEP_SIZE = new MDecimal(10);
		public final static MDecimal MAX_VALUE = new MDecimal(100);
		public final static MDecimal MIN_VALUE = new MDecimal(-100);
	}

	/**
	 * Rudder Constants
	 * 
	 * @author Clayton Tabone
	 */
	public final static class RUDDER {
		// TODO What exactly should RUDDER_DELAY represent?
		public final static MInteger RUDDER_DELAY = new MInteger(50);
		public final static MInteger ROTATIONS = new MInteger(5);
		public final static MInteger BIG_ROTATIONS = new MInteger(20);
	}

	/**
	 * Path Planning Constants
	 * 
	 * @author Zak Borg
	 */
	public final static class PATH {
		public final static MDecimal Path_Accuracy_Lower = new MDecimal(10);
		public final static MDecimal Path_Accuracy_Upper = new MDecimal(20);
	}
}
