/**
 * eAdventure (formerly <e-Adventure> and <e-Game>) is a research project of the
 *    <e-UCM> research group.
 *
 *    Copyright 2005-2010 <e-UCM> research group.
 *
 *    You can access a list of all the contributors to eAdventure at:
 *          http://e-adventure.e-ucm.es/contributors
 *
 *    <e-UCM> is a research group of the Department of Software Engineering
 *          and Artificial Intelligence at the Complutense University of Madrid
 *          (School of Computer Science).
 *
 *          C Profesor Jose Garcia Santesmases sn,
 *          28040 Madrid (Madrid), Spain.
 *
 *          For more info please visit:  <http://e-adventure.e-ucm.es> or
 *          <http://www.e-ucm.es>
 *
 * ****************************************************************************
 *
 *  This file is part of eAdventure, version 2.0
 *
 *      eAdventure is free software: you can redistribute it and/or modify
 *      it under the terms of the GNU Lesser General Public License as published by
 *      the Free Software Foundation, either version 3 of the License, or
 *      (at your option) any later version.
 *
 *      eAdventure is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU Lesser General Public License for more details.
 *
 *      You should have received a copy of the GNU Lesser General Public License
 *      along with eAdventure.  If not, see <http://www.gnu.org/licenses/>.
 */

package ead.engine.home.preferences;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import ead.engine.home.HomeActivity;
import es.eucm.eadventure.engine.R;
import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.IntentAction;

/**
 * Preference screen for EAdventure Mobile, built from the file preferences.xml.  
 * 
 * @author Roberto Tornero
 */
public class PreferencesActivity extends PreferenceActivity {

	/**
	 * Tag to find the audio preferences
	 */
	public static final String AUDIO_PREF = "AudioPref";
	/**
	 * Tag to find the debug preferences
	 */
	public static final String DEBUG_PREF = "DebugPref";
	/**
	 * Tag to find the vibrating preferences
	 */
	public static final String VIBRATE_PREF = "VibratePref";

	/**
	 * Create the preferences screen from the xml 
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.preferences_activity);

		final ActionBar actionBar = (ActionBar) findViewById(R.id.actionbar);
		actionBar.setHomeAction(new IntentAction(this, createIntent(this, HomeActivity.class), R.drawable.launcher_icon3));
		actionBar.setTitle("Preferences");

		addPreferencesFromResource(R.xml.preferences);

		PreferenceScreen intentPref = (PreferenceScreen) this.findPreference("InstallPref");
		Intent intent = new Intent(this, LaunchAndExplorerActivity.class);
		intentPref.setIntent(intent);

		PreferenceScreen websitePref = (PreferenceScreen) this.findPreference("WebPref");
		websitePref.setIntent(new Intent().setAction(Intent.ACTION_VIEW).setData(Uri.parse("http://e-adventure.e-ucm.es/")));

		overridePendingTransition(R.anim.fade, R.anim.hold);

	}

	/*
	 * (non-Javadoc)
	 * @see android.app.Activity#onStart()
	 */
	@Override
	protected void onStart() {

		super.onStart();
		overridePendingTransition(R.anim.fade, R.anim.hold);
	} 

	/**
	 * Create a new intent to change between activities
	 */
	public static Intent createIntent(Context context, Class<?> c) {
		Intent i = new Intent(context, c);
		i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		return i;
	}

}
