package pl.m4.photomaker;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

public class VectorDraw extends View {
	private static final String TAG = "VectorDraw";
	private Paint paint, recPaint;
	private float x, y;
	private int width;
	private int height;
	private int left, right, bot, top;
	private float[][] endPos;
	private float[][] startPos;
	private int countVec = 0;
	private float origX, origY;
	private CameraView camera;

	public VectorDraw(Context activity, CameraView openCvCamera) {
		super(activity);
		Log.i(TAG, "constructor VectorDraw");
		paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		recPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		camera = openCvCamera;
		paint.setColor(Color.RED);
		//paint.setShadowLayer(8, 0, 0, Color.WHITE);
		setLayerType(LAYER_TYPE_SOFTWARE, paint);
		
		recPaint.setColor(Color.BLUE);
		recPaint.setStyle(Style.STROKE);
		
		WindowManager wm = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		width = size.x;
		height = size.y;
		
		left = width /2 - 50; top = height /2 - 20; right = width / 2 + 10; bot = height /2 + 20;
		x = left + 20;
		y = top + 20;
		origX = x;
		origY = y;
		
		countVec = 15;
		endPos = new float[2][countVec];
		startPos = new float[2][countVec];
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (camera.getFrameData(0) != null && camera.getFrameData(1) == null && !PhotoMaker.oneFrame) {
			canvas.drawRect(left, top, right, bot, recPaint);
			canvas.drawCircle(x, y, 15f, paint);
			if (PhotoMaker.debug)
				for (int i = 0; i < countVec; i++) {
					canvas.drawLine(startPos[0][i], startPos[1][i],
							endPos[0][i], endPos[1][i], paint);
				}
		}
		this.invalidate();
	}
	
	public void changeColorByValue(Paint paint, int value){
		int red = 255 - value;
		int green = 60 + value;
		if (this.y < top+15 || this.y > bot-15 || this.x < left + 15 || this.x > right -15){
			red = 255;
			green = 0;
		}
		green = Calc.correctColorValue(green);
		red = Calc.correctColorValue(red);
		int color = Color.rgb(red, green, 0);
		paint.setColor(color);
	}
	
	public void moveX(float dx) {
		this.x = origX + (dx*3);
		changeColorByValue(paint, (int)dx*20);
	}
	
	public void moveY(float dy) {
		this.y = origY + dy;
	}
	
	public float getStartX(){
		return this.origX;
	}
	
	public float getStartY(){
		return this.origY;
	}

	public void setStartPos(float[][] tab){
		startPos = tab;
	}
	
	public void setEndPos(float[][] tab){
		endPos = tab;
	}

	public int getCountVec() {
		return countVec;
	}

	public void setCountVec(int count) {
		this.countVec = count;
	}
	
	@Override
	public float getX() {
		return this.x;
	}
	
	@Override
	public float getY() {
		return this.y;
	}
	
	public void resetPos(){
		x = left + 20;
		y = top + 20;
	}
}
