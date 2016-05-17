package pl.m4.photomaker.view.listener;

import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;

public class SettingsDialogGlobalLayoutListener implements OnGlobalLayoutListener {
    private View view;
    private int maxHeight;

    public SettingsDialogGlobalLayoutListener(View view, int height) {
        this.view = view;
        maxHeight = height - 100;
    }

    @Override
    public void onGlobalLayout() {
        if (view.getHeight() > maxHeight)
            view.getLayoutParams().height = maxHeight;
    }
}