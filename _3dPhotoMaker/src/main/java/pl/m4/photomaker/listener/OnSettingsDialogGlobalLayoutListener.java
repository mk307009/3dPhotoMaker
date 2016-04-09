package pl.m4.photomaker.listener;

import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;

public class OnSettingsDialogGlobalLayoutListener implements OnGlobalLayoutListener {
    private View view;
    private int maxHeight;

    public OnSettingsDialogGlobalLayoutListener(View view, int height) {
        this.view = view;
        maxHeight = height - 100;
    }

    @Override
    public void onGlobalLayout() {
        if (view.getHeight() > maxHeight)
            view.getLayoutParams().height = maxHeight;
    }
}