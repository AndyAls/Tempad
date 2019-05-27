
package padd.qlckh.cn.tempad.serial;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;

import android_serialport_api.SerialPortFinder;
import padd.qlckh.cn.tempad.R;

public class SerialPortPreferences extends PreferenceActivity {

	private App mApplication;
	private SerialPortFinder mSerialPortFinder;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mApplication = (App) getApplication();
		mSerialPortFinder = mApplication.mSerialPortFinder;

		addPreferencesFromResource(R.xml.serial_port_preferences);

		// Devices
		final ListPreference devices = (ListPreference)findPreference("DEVICE");
//        String[] entries = mSerialPortFinder.getAllDevices();
//        String[] entryValues = mSerialPortFinder.getAllDevicesPath();
		String[] entries=new String[]{"a","b","c","d","e"};
		String[] entryValues=new String[]{"1","2","3","4","5"};
		devices.setEntries(entries);
		devices.setEntryValues(entryValues);
		devices.setSummary(devices.getValue());
		devices.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				preference.setSummary((String)newValue);
				return true;
			}
		});

		// Baud rates
		final ListPreference baudrates = (ListPreference)findPreference("BAUDRATE");
		baudrates.setSummary(baudrates.getValue());
		baudrates.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				preference.setSummary((String)newValue);
				return true;
			}
		});
	}
}
