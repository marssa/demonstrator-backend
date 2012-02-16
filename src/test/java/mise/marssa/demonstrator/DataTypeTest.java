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
