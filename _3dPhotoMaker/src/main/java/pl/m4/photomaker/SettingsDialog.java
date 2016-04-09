package pl.m4.photomaker;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.opencv.photo.Photo;

import pl.m4.photomaker.listener.OnSettingsDialogGlobalLayoutListener;
import pl.m4.photomaker.listener.SettingsListener;

public class SettingsDialog extends DialogFragment {
	private Button button;
	private RelativeLayout[] settings;
	private SettingsListener settingsListener;
	private SettingsPreferences preferences;
	private PhotoMaker photoMaker;

	public SettingsDialog(Button button, PhotoMaker photoMaker) {
		super();
		this.button = button;
		this.photoMaker = photoMaker;
		settingsListener = new SettingsListener();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		button.setRotation(90);
		getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		View v = inflater.inflate(R.layout.settings_layout, container, false);
		
		preferences = new SettingsPreferences(v.getContext());
		WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		@SuppressWarnings("deprecation")
		int height = display.getHeight();
		v.getViewTreeObserver().addOnGlobalLayoutListener(new OnSettingsDialogGlobalLayoutListener(v, height));
		//menu buttons
		setSettingsListener(preferences, v);
		return v;
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		super.onCreateDialog(savedInstanceState);
		Dialog dialog = new Dialog(getActivity());
		int color = Color.parseColor("#4279BD");
		Drawable d = new ColorDrawable(color);
		d.setAlpha(100);
		dialog.getWindow().setBackgroundDrawable(d);		
		return dialog;
	}

	@Override
	public void onStart() {
		super.onStart();
		//to remove background
		//Window window = getDialog().getWindow();
		//window.setDimAmount(0);// remove background dim
	}
	
	@Override
	public void onDismiss(DialogInterface dialog) {
		super.onDismiss(dialog);
		if (button.getRotation() > 0)
			button.setRotation(0);
		photoMaker.resetPhotoSettings();
	}
	
	private void setSettingsListener(SettingsPreferences preferences, View v){
		boolean isDebugMode = preferences.loadSavedPreferences("DebugMode");
		boolean isOneFrame = preferences.loadSavedPreferences("OneFrame");
		boolean isManualMode = preferences.loadSavedPreferences("ManualMode");
		String alg3d = preferences.loadSavedPreferences("3dMode", PhotoMaker.DEFAULT_MODE);
		
		CheckBox debugMode = (CheckBox) v.findViewById(R.id.debug_mode_checkbox);
		debugMode.setChecked(isDebugMode);
		CheckBox oneFrame = (CheckBox) v.findViewById(R.id.one_frame_check);
		oneFrame.setChecked(isOneFrame);
		CheckBox manualMode = (CheckBox) v.findViewById(R.id.manual_mode_checkbox);
		manualMode.setChecked(isManualMode);
		setAlgorithmName(alg3d, v);
		
		settings = new RelativeLayout[4];
		settings[0] = (RelativeLayout) v.findViewById(R.id.algorithm_menu);
		settings[0].setOnClickListener(settingsListener.algorithmPopUp());
		settings[1] = (RelativeLayout) v.findViewById(R.id.debug_mode);
		settings[1].setOnClickListener(settingsListener.debugMode());
		settings[2] = (RelativeLayout) v.findViewById(R.id.one_frame);
		settings[2].setOnClickListener(settingsListener.oneFrameMode());
		settings[3] = (RelativeLayout) v.findViewById(R.id.manual_mode);
		settings[3].setOnClickListener(settingsListener.manualMode());
	}
	
	private void setAlgorithmName(String name, View v){
		TextView algorithm3d = (TextView) v.findViewById(R.id.algorithm_text_view);
		switch(name){
			case "Dubois": {
				algorithm3d.setText(R.string.dubois);
			}break;
			case "Monochrome": {
				algorithm3d.setText(R.string.mono);
			}break;
			case "PNS": {
				algorithm3d.setText(R.string.pns);
			}break;
			case "OptimizedColor": {
				algorithm3d.setText(R.string.opt_color_string);
			}break;
			case "FullColor": {
				algorithm3d.setText(R.string.full_color_string);
			}break;
		}
	}

	@Override
	public void show(FragmentManager manager, String tag) {
		super.show(manager, tag);
	}

	@Override
	public int show(FragmentTransaction transaction, String tag) {
		return super.show(transaction, tag);
	}	

}
