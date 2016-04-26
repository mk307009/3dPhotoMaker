package pl.m4.photomaker.listener;

import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.TextView;
import pl.m4.photomaker.PhotoMaker;
import pl.m4.photomaker.R;
import pl.m4.photomaker.SettingsPreferences;

public class SettingsListener {
	private static final String TAG = "SettingsListener";
	private static final String MODE = "3dMode";
	private static final String DUBOIS = "Dubois";
	private static final String MONO = "Monochrome";
	private static final String PNS = "PNS";
	private static final String OPT_COLOR = "Optimized Color";
	private static final String FULL_COLOR = "Full Color";
	private static final String DEBUG_MODE = "DebugMode";
	private static final String ONE_FRAME = "OneFrame";
	private static final String MANUAL = "ManualMode";

	public SettingsListener(){
		Log.i(TAG,"SettingsListener created.");
	}
	
	public OnClickListener algorithmPopUp() {
		return new OnClickListener() {
			@Override
			public void onClick(View v) {
				SettingsPreferences prefers = new SettingsPreferences(v.getContext());
				PopupMenu popup = new PopupMenu(v.getContext(), v, Gravity.TOP);
				MenuInflater inflater = popup.getMenuInflater();
				inflater.inflate(R.menu.algorithms, popup.getMenu());
				Menu menu = popup.getMenu();
				for (int i = 0; i < menu.size(); i++){
					menu.getItem(i).setOnMenuItemClickListener(duboisPopup(prefers, v));
				}
				popup.show();
			}
		};
	}
	
	public OnMenuItemClickListener duboisPopup(final SettingsPreferences preferences, final View v) {
		return new OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				String name = (String) item.getTitle();
				if (PhotoMaker.debug)
					Log.i(TAG, name);
				switch (name) {
				case DUBOIS: {
					TextView text = (TextView) v.findViewById(R.id.algorithm_text_view);
					preferences.savePreferences(MODE, DUBOIS);
					text.setText(R.string.dubois);
				}break;
				case MONO: {
					TextView text = (TextView) v.findViewById(R.id.algorithm_text_view);
					preferences.savePreferences(MODE, MONO);
					text.setText(R.string.mono);
				}break;
				case PNS: {
					TextView text = (TextView) v.findViewById(R.id.algorithm_text_view);
					preferences.savePreferences(MODE, PNS);
					text.setText(R.string.pns);
				}break;
				case OPT_COLOR: {
					TextView text = (TextView) v.findViewById(R.id.algorithm_text_view);
					preferences.savePreferences(MODE, OPT_COLOR);
					text.setText(R.string.opt_color_string);
				}break;
				case FULL_COLOR: {
					TextView text = (TextView) v.findViewById(R.id.algorithm_text_view);
					preferences.savePreferences(MODE, FULL_COLOR);
					text.setText(R.string.full_color_string);
				}break;
				}
				return false;
			}

		};
	}
	
	public OnClickListener dubois3d() {
		return new OnClickListener() {
			@Override
			public void onClick(View v) {
				CheckBox cb = (CheckBox) v
						.findViewById(R.id.debug_mode_checkbox);
				SettingsPreferences prefers = new SettingsPreferences(v.getContext());
				if (cb.isChecked()){
					cb.setChecked(false);
					prefers.savePreferences(DEBUG_MODE, false);
				}
				else{
					cb.setChecked(true);
					prefers.savePreferences(DEBUG_MODE, true);
				}
			}
		};
	}

	public OnClickListener debugMode() {
		return new OnClickListener() {
			@Override
			public void onClick(View v) {
				CheckBox cb = (CheckBox) v
						.findViewById(R.id.debug_mode_checkbox);
				SettingsPreferences prefers = new SettingsPreferences(v.getContext());
				if (cb.isChecked()){
					cb.setChecked(false);
					prefers.savePreferences(DEBUG_MODE, false);
					PhotoMaker.debug = false;
				}
				else{
					cb.setChecked(true);
					prefers.savePreferences(DEBUG_MODE, true);
					PhotoMaker.debug = true;
				}
			}
		};
	}

	public OnClickListener oneFrameMode() {
		return new OnClickListener() {
			@Override
			public void onClick(View v) {
				CheckBox cb = (CheckBox) v
						.findViewById(R.id.one_frame_check);
				SettingsPreferences prefers = new SettingsPreferences(v.getContext());
				if (cb.isChecked()){
					cb.setChecked(false);
					prefers.savePreferences(ONE_FRAME, false);
					PhotoMaker.oneFrame = false;
				}
				else{
					cb.setChecked(true);
					prefers.savePreferences(ONE_FRAME, true);
					PhotoMaker.oneFrame = true;
				}
			}
		};
	}
	
	public OnClickListener manualMode() {
		return new OnClickListener() {
			@Override
			public void onClick(View v) {
				CheckBox cb = (CheckBox) v
						.findViewById(R.id.manual_mode_checkbox);
				SettingsPreferences prefers = new SettingsPreferences(v.getContext());
				if (cb.isChecked()){
					cb.setChecked(false);
					prefers.savePreferences(MANUAL, false);
					PhotoMaker.manualMode = false;
				}
				else{
					cb.setChecked(true);
					prefers.savePreferences(MANUAL, true);
					PhotoMaker.manualMode = true;
				}
			}
		};
	}

}
