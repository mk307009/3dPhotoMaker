package pl.m4.photomaker.view.listener;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.Toast;

@SuppressLint("ClickableViewAccessibility")
public class CameraViewListener {
	private float x1 = 0, x2 = 0;
	private final int MIN_DISTANCE = 100;
	private Context context;
	private ImageView preview;

	public CameraViewListener(Context context, ImageView prev) {
		this.context = context;
		preview = prev;
	}

	public OnTouchListener movePreviewListener() {
		return new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					x1 = event.getX();
					break;
				case MotionEvent.ACTION_UP:
					x2 = event.getX();
					float deltaX = x2 - x1;
					if (Math.abs(deltaX) > MIN_DISTANCE) {
						// Left to Right
						if (x2 > x1) {
							preview.setImageBitmap(null);
						}
						// Right to left
						else {
						}
					}else{
						Toast.makeText(context, "Slide right to close preview.", Toast.LENGTH_SHORT).show();
					}
					break;
				}
				return false;
			}
		};
	}
	
	public OnLongClickListener longClickToSave(){
		return new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				
				return false;
			}
		};
	}
}
