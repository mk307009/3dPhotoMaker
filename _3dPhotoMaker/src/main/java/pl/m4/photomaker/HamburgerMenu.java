package pl.m4.photomaker;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import pl.m4.photomaker.view.listener.SettingsDialogGlobalLayoutListener;
import pl.m4.photomaker.view.listener.SettingsListener;

public class HamburgerMenu extends DialogFragment {
    private Button button;
    private RelativeLayout[] settings;
    private List<RelativeLayout> settingsList;
    private SettingsListener settingsListener;
    private SettingsPreferences preferences;
    private PhotoMaker photoMaker;
    private Animation startMenuRotateAnimation, endMenuRotateAnimation;

    public HamburgerMenu() {
        photoMaker = null;
        settingsListener = new SettingsListener();
    }

    public HamburgerMenu(PhotoMaker photoMaker) {
        this.photoMaker = photoMaker;
        settingsListener = new SettingsListener();
        settingsList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        button = (Button) getActivity().findViewById(R.id.settings);
        endMenuRotateAnimation = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.rotate_to_zero);
        startMenuRotateAnimation = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.rotate_to_90);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.settings_layout, container, false);
        preferences = new SettingsPreferences(view.getContext());
        WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        @SuppressWarnings("deprecation")
        int height = display.getHeight();
        view.getViewTreeObserver().addOnGlobalLayoutListener(new SettingsDialogGlobalLayoutListener(view, height));
        //settings menu buttons configuration
        menuSettingsConfiguration(view);
        return view;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        Dialog dialog = new Dialog(getActivity());
        Drawable draw = new ColorDrawable(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.primary_darkness));
        //Drawable draw = new ColorDrawable(getResources().getColor(R.color.background_color_settings_menu, getActivity().getTheme()); //API >=21
        draw.setAlpha(100);
        dialog.getWindow().setBackgroundDrawable(draw);
        return dialog;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        button.startAnimation(endMenuRotateAnimation);
        photoMaker.resetPhotoSettings();
    }

    @Override
    public void onStart() {
        super.onStart();
        button.startAnimation(startMenuRotateAnimation);
    }

    private void menuSettingsConfiguration(View v) {
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

        v.findViewById(R.id.algorithm_menu).setOnClickListener(settingsListener.algorithmPopUp());
        v.findViewById(R.id.debug_mode).setOnClickListener(settingsListener.debugMode());
        v.findViewById(R.id.one_frame).setOnClickListener(settingsListener.oneFrameMode());
        v.findViewById(R.id.manual_mode).setOnClickListener(settingsListener.manualMode());
    }

    private void setAlgorithmName(String name, View v) {
        TextView algorithm3d = (TextView) v.findViewById(R.id.algorithm_text_view);
        switch (name) {
            case "Dubois": {
                algorithm3d.setText(R.string.dubois);
            }
            break;
            case "Monochrome": {
                algorithm3d.setText(R.string.mono);
            }
            break;
            case "PNS": {
                algorithm3d.setText(R.string.pns);
            }
            break;
            case "Optimized Color": {
                algorithm3d.setText(R.string.opt_color_string);
            }
            break;
            case "Full Color": {
                algorithm3d.setText(R.string.full_color_string);
            }
            break;
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
