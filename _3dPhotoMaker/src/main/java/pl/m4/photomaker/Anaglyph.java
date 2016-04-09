package pl.m4.photomaker;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;

public class Anaglyph {
	private final String TAG = "m4photo:Anaglyph";
	private final int PIXEL_MOVE = 30;
	
	public Bitmap anaglyphDubois(byte[] left, byte[] right){
		if (PhotoMaker.debug)
        	Log.i(TAG, "Dubois");
    	Bitmap bmpL=BitmapFactory.decodeByteArray(left,0,left.length);
    	Bitmap bmpR=BitmapFactory.decodeByteArray(right,0,right.length);
    	
    	Bitmap mutableBitmapR = bmpR.copy(Bitmap.Config.ARGB_8888, true);
    	Bitmap mutableBitmapL = bmpL.copy(Bitmap.Config.ARGB_8888, true);
    	bmpL.recycle();
    	bmpR.recycle();
    	
    	int bWidth = mutableBitmapR.getWidth();
        int bHeight = mutableBitmapR.getHeight();
    	int[] pixelsR = new int[bWidth * bHeight], pixelsL = new int[bWidth * bHeight], pixelsRes = new int[bWidth * bHeight];
    	mutableBitmapR.getPixels(pixelsR, 0, mutableBitmapR.getWidth(), 0, 0, mutableBitmapR.getWidth(), mutableBitmapR.getHeight());
    	mutableBitmapL.getPixels(pixelsL, 0, mutableBitmapL.getWidth(), 0, 0, mutableBitmapL.getWidth(), mutableBitmapL.getHeight());
    	mutableBitmapL.recycle();
    	Bitmap mutableBitmapResult = mutableBitmapR.copy(Bitmap.Config.ARGB_8888, true);
    	mutableBitmapR.recycle();
    	
    	if (PhotoMaker.debug)
    		Log.i(TAG+" - size", pixelsR.length+" - "+pixelsL.length);
    	
//    	Ar = 0.4561 * Lr + 0.500484 * Lg + 0.176381 * Lb - 0.0434706 * Rr - 0.0879388 * Rg - 0.00155529 * Rb
//    	Ag = -0.0400822 * Lr - 0.0378246 * Lg - 0.0157589 * Lb + 0.378476 * Rr + 0.73364 * Rg - 0.0184503 * Rb
//    	Ab = -0.0152161 * Lr - 0.0205971 * Lg - 0.00546856 * Lb - 0.0721527 * Rr - 0.112961 * Rg + 1.2264 * Rb
    	long startTime = System.nanoTime(); 
    	for (int i = 0; i < pixelsR.length; i++){
			int redR = Color.red(pixelsR[i]);
			int blueR = Color.blue(pixelsR[i]);
	    	int greenR = Color.green(pixelsR[i]);
	    	int alpha = Color.alpha(pixelsR[i]);
	    	
	    	int redL = Color.red(pixelsL[i]);
	    	int blueL = Color.blue(pixelsL[i]);
	    	int greenL = Color.green(pixelsL[i]);

	    	int redRes2 = (int)(0.4561 * redL + 0.500484 * greenL + 0.176381 * blueL - 0.0434706 * redR - 0.0879388 * greenR - 0.00155529 * blueR);
	    	int greenRes2 = (int)(-0.0400822 * redL - 0.0378246 * greenL - 0.0157589 * blueL + 0.378476 * redR + 0.73364 * greenR + 0.0184503 * blueR);
	    	int blueRes2 = (int)(-0.0152161 * redL - 0.0205971 * greenL - 0.00546856 * blueL - 0.0721527 * redR - 0.112961 * greenR + 1.2264 * blueR);
	    	
	    	redRes2 = checkColorValue(redRes2);
	    	greenRes2 = checkColorValue(greenRes2);
	    	blueRes2 = checkColorValue(blueRes2);

	    	pixelsRes[i] = Color.argb(alpha, redRes2, greenRes2, blueRes2);  
    	}
    	long estimatedTime = System.nanoTime() - startTime;
    	if (PhotoMaker.debug)
    		Log.i(TAG, estimatedTime+" is a dubois time.");
    	
    	//mutableBitmap1.setPixels(pixels1, 0, mutableBitmap1.getWidth(), 0, 0, mutableBitmap1.getWidth(), mutableBitmap1.getHeight());
    	//mutableBitmap2.setPixels(pixels2, 0, mutableBitmap2.getWidth(), 0, 0, mutableBitmap2.getWidth(), mutableBitmap2.getHeight());
    	mutableBitmapResult.setPixels(pixelsRes, 0, mutableBitmapResult.getWidth(), 0, 0, mutableBitmapResult.getWidth(), mutableBitmapResult.getHeight());
    	return mutableBitmapResult;
	}
	
	public Bitmap anaglyphDubois(byte[] left){
		if (PhotoMaker.debug)
        	Log.i(TAG, "Dubois");
		Bitmap bmp=BitmapFactory.decodeByteArray(left,0,left.length);
		int bWidth = bmp.getWidth();
        int bHeight = bmp.getHeight();
        int pixelSize = bWidth * bHeight;

        int[] pixelsL = new int[pixelSize],pixelsR = new int[pixelSize];
        int[] pixelsRes = new int[pixelSize];
        int nWidth = bWidth-PIXEL_MOVE;
        
        bmp.getPixels(pixelsL, 0, bWidth, 0, 0, bWidth-PIXEL_MOVE, bHeight);
        bmp.getPixels(pixelsR, 0, bWidth, PIXEL_MOVE, 0, bWidth-PIXEL_MOVE, bHeight);
        bmp.recycle();
		
    	for (int i = 0; i < pixelsL.length; i++){
    		int redR = Color.red(pixelsR[i]);
			int blueR = Color.blue(pixelsR[i]);
	    	int greenR = Color.green(pixelsR[i]);
	    	int alpha = Color.alpha(pixelsR[i]);
	    	
	    	int redL = Color.red(pixelsL[i]);
	    	int blueL = Color.blue(pixelsL[i]);
	    	int greenL = Color.green(pixelsL[i]);

	    	int redRes2 = (int)(0.4561 * redL + 0.500484 * greenL + 0.176381 * blueL - 0.0434706 * redR - 0.0879388 * greenR - 0.00155529 * blueR);
	    	int greenRes2 = (int)(-0.0400822 * redL - 0.0378246 * greenL - 0.0157589 * blueL + 0.378476 * redR + 0.73364 * greenR + 0.0184503 * blueR);
	    	int blueRes2 = (int)(-0.0152161 * redL - 0.0205971 * greenL - 0.00546856 * blueL - 0.0721527 * redR - 0.112961 * greenR + 1.2264 * blueR);
	    	
	    	redRes2 = checkColorValue(redRes2);
	    	greenRes2 = checkColorValue(greenRes2);
	    	blueRes2 = checkColorValue(blueRes2);
	    	
	    	pixelsRes[i] = Color.argb(alpha, redRes2, greenRes2, blueRes2);   	
    	}
    	Bitmap mutableBitmapResult = Bitmap.createBitmap(bWidth-PIXEL_MOVE, bHeight, Bitmap.Config.ARGB_8888);
    	mutableBitmapResult.setPixels(pixelsRes, 0, bWidth, 0, 0, nWidth, bHeight);    	
    	return mutableBitmapResult;
	}
	
	public Bitmap anaglyphMonochrome(byte[] left){
		if (PhotoMaker.debug)
        	Log.i(TAG, "Mono");
		Bitmap bmp=BitmapFactory.decodeByteArray(left,0,left.length);
		int bWidth = bmp.getWidth();
        int bHeight = bmp.getHeight();
        int pixelSize = bWidth * bHeight;
        
        Bitmap mutableBitmapL = Bitmap.createBitmap(bWidth-PIXEL_MOVE, bHeight, Bitmap.Config.ARGB_8888);
    	Bitmap mutableBitmapResult = Bitmap.createBitmap(bWidth-PIXEL_MOVE, bHeight, Bitmap.Config.ARGB_8888);
        int[] pixelsL = new int[pixelSize],pixelsR = new int[pixelSize];
        int[] pixelsRes = new int[pixelSize];
        int nWidth = mutableBitmapL.getWidth();
        
        bmp.getPixels(pixelsL, 0, bWidth, 0, 0, bWidth-PIXEL_MOVE, bHeight);
        bmp.getPixels(pixelsR, 0, bWidth, PIXEL_MOVE, 0, bWidth-PIXEL_MOVE, bHeight);
        bmp.recycle();
        mutableBitmapL.recycle();
        
        for (int i = 0; i < pixelsL.length; i++){
			int redL = Color.red(pixelsL[i]);
			int blueL = Color.blue(pixelsL[i]);
	    	int greenL = Color.green(pixelsL[i]);
	    	int alpha = Color.alpha(pixelsL[i]);
	    	redL = (int) (0.299 * redL + 0.587 * greenL + 0.114 * blueL);
	    	
	    	int redR = Color.red(pixelsR[i]);
	    	int blueR = Color.blue(pixelsR[i]);
	    	int greenR = Color.green(pixelsR[i]);
	    	greenR = (int) (0.299 * redR + 0.587 * greenR + 0.114 * blueR);
	    	blueR = (int) (0.299 * redR + 0.587 * greenR + 0.114 * blueR);
	    	pixelsRes[i] = Color.argb(alpha, redL, greenR, blueR); 	
    	}
    	mutableBitmapResult.setPixels(pixelsRes, 0, bWidth, 0, 0, nWidth, bHeight);

    	return mutableBitmapResult;
	}
	
	public Bitmap anaglyphMonochrome(byte[] left, byte[] right){
		if (PhotoMaker.debug)
        	Log.i(TAG, "Mono");
		Bitmap bmpL=BitmapFactory.decodeByteArray(left,0,left.length);
    	Bitmap bmpR=BitmapFactory.decodeByteArray(right,0,right.length);
    	
    	Bitmap mutableBitmapL = bmpL.copy(Bitmap.Config.ARGB_8888, true);
    	Bitmap mutableBitmapR = bmpR.copy(Bitmap.Config.ARGB_8888, true);
    	Bitmap mutableBitmapResult = bmpL.copy(Bitmap.Config.ARGB_8888, true);
    	bmpL.recycle();
    	bmpR.recycle();

    	int bWidth = mutableBitmapL.getWidth();
        int bHeight = mutableBitmapL.getHeight();
    	int[] pixelsL = new int[bWidth * bHeight], pixelsR = new int[bWidth * bHeight];
    	int[] pixelsRes = new int[bWidth * bHeight];
    	
    	mutableBitmapL.getPixels(pixelsL, 0, mutableBitmapL.getWidth(), 0, 0, mutableBitmapL.getWidth(), mutableBitmapL.getHeight());
    	mutableBitmapR.getPixels(pixelsR, 0, mutableBitmapR.getWidth(), 0, 0, mutableBitmapR.getWidth(), mutableBitmapR.getHeight());
    	mutableBitmapL.recycle();
    	mutableBitmapR.recycle();
    	if (PhotoMaker.debug)
    		Log.i(TAG, pixelsL.length+" - "+pixelsR.length);
    	long startTime = System.nanoTime();

    	for (int i = 0; i < pixelsL.length; i++){
			int redL = Color.red(pixelsL[i]);
			int blueL = Color.blue(pixelsL[i]);
	    	int greenL = Color.green(pixelsL[i]);
	    	int alpha = Color.alpha(pixelsL[i]);
	    	redL = (int) (0.299 * redL + 0.587 * greenL + 0.114 * blueL);
	    	//pixels1[i] = Color.argb(alpha, red1, 0, 0);
	    	
	    	int redR = Color.red(pixelsR[i]);
	    	int blueR = Color.blue(pixelsR[i]);
	    	int greenR = Color.green(pixelsR[i]);
	    	greenR = (int) (0.299 * redR + 0.587 * greenR + 0.114 * blueR);
	    	blueR = (int) (0.299 * redR + 0.587 * greenR + 0.114 * blueR);
	    	//pixels2[i] = Color.argb(alpha, 0, green2, blue2);
	    	
	    	pixelsRes[i] = Color.argb(alpha, redL, greenR, blueR);    	
    	}
    	long estimatedTime = System.nanoTime() - startTime;
    	if (PhotoMaker.debug)
    		Log.i(TAG, estimatedTime+" mono times");

    	mutableBitmapResult.setPixels(pixelsRes, 0, mutableBitmapResult.getWidth(), 0, 0, mutableBitmapResult.getWidth(), mutableBitmapResult.getHeight());
    	return mutableBitmapResult;
	}
	
	private Bitmap joinBitmap(Bitmap firstFrame, Bitmap secondFrame) {
		Bitmap cs = null;
		int width, height = 0;

		if (firstFrame.getWidth() > secondFrame.getWidth()) {
			width = firstFrame.getWidth() + secondFrame.getWidth();
			height = firstFrame.getHeight();
		} else {
			width = secondFrame.getWidth() + secondFrame.getWidth();
			height = firstFrame.getHeight();
		}
		
		cs = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		Canvas comboImage = new Canvas(cs); 
	    comboImage.drawBitmap(firstFrame, 0f, 0f, null); 
	    comboImage.drawBitmap(secondFrame, firstFrame.getWidth(), 0f, null); 
		return cs;
	}
	
	
	public Bitmap pns3d(byte[] left, byte[] right){
		if (PhotoMaker.debug)
        	Log.i(TAG, "PNS");
		Bitmap bmpL=BitmapFactory.decodeByteArray(left,0,left.length);
    	Bitmap bmpR=BitmapFactory.decodeByteArray(right,0,right.length);
    	Bitmap mutableBitmapR = joinBitmap(bmpL,bmpR);
    	bmpL.recycle();
    	bmpR.recycle();
    	Bitmap mutableBitmapResult = mutableBitmapR.copy(Bitmap.Config.ARGB_8888, true);
    	mutableBitmapR.recycle();
    	
    	return mutableBitmapResult;
	}
	
	public Bitmap pns3d(byte[] left){
		if (PhotoMaker.debug)
        	Log.i(TAG, "PNS");
		Bitmap bmp=BitmapFactory.decodeByteArray(left,0,left.length);
		int bWidth = bmp.getWidth();
        int bHeight = bmp.getHeight();
        int pixelSize = (bWidth) * bHeight;
    	Bitmap mutableBitmapL = Bitmap.createBitmap(bWidth-PIXEL_MOVE, bHeight, Bitmap.Config.ARGB_8888);
    	Bitmap mutableBitmapR = Bitmap.createBitmap(bWidth-PIXEL_MOVE, bHeight, Bitmap.Config.ARGB_8888);
        int[] pixelsL = new int[pixelSize],pixelsR = new int[pixelSize];
        int nWidth = mutableBitmapL.getWidth();

        bmp.getPixels(pixelsL, 0, bWidth, 0, 0, bWidth-PIXEL_MOVE, bHeight);
        bmp.getPixels(pixelsR, 0, bWidth, PIXEL_MOVE, 0, bWidth-PIXEL_MOVE, bHeight);
        bmp.recycle();
        
    	mutableBitmapL.setPixels(pixelsL, 0, bWidth, 0, 0, nWidth, bHeight);
    	mutableBitmapR.setPixels(pixelsR, 0, bWidth, 0, 0, nWidth, bHeight);
    	
    	Bitmap bmpSum = joinBitmap(mutableBitmapL,mutableBitmapR);
    	mutableBitmapL.recycle();
    	mutableBitmapR.recycle();
    	Bitmap mutableBitmapResult = bmpSum.copy(Bitmap.Config.ARGB_8888, true);
    	
    	return mutableBitmapResult;
	}
	
	public Bitmap anaglyphOptimizedColor(byte[] left){
		Bitmap bmp=BitmapFactory.decodeByteArray(left,0,left.length);
		int bWidth = bmp.getWidth();
        int bHeight = bmp.getHeight();
        int pixelSize = bWidth * bHeight;
        
        Bitmap mutableBitmapL = Bitmap.createBitmap(bWidth-PIXEL_MOVE, bHeight, Bitmap.Config.ARGB_8888);
    	Bitmap mutableBitmapResult = Bitmap.createBitmap(bWidth-PIXEL_MOVE, bHeight, Bitmap.Config.ARGB_8888);
        int[] pixelsL = new int[pixelSize],pixelsR = new int[pixelSize];
        int[] pixelsRes = new int[pixelSize];
        int nWidth = mutableBitmapL.getWidth();
        
        bmp.getPixels(pixelsL, 0, bWidth, 0, 0, bWidth-PIXEL_MOVE, bHeight);
        bmp.getPixels(pixelsR, 0, bWidth, PIXEL_MOVE, 0, bWidth-PIXEL_MOVE, bHeight);
        bmp.recycle();
        mutableBitmapL.recycle();
        if (PhotoMaker.debug)
        	Log.i(TAG, "Optimized color");
    	for (int i = 0; i < pixelsL.length; i++){
    		//int redR = Color.red(pixelsR[i]);
			int blueR = Color.blue(pixelsR[i]);
	    	int greenR = Color.green(pixelsR[i]);
	    	int alpha = Color.alpha(pixelsR[i]);
	    	
	    	int redL = Color.red(pixelsL[i]);
	    	int blueL = Color.blue(pixelsL[i]);
	    	int greenL = Color.green(pixelsL[i]);

	    	int redRes = (int)(0 * redL + 0.7 * greenL + 0.3 * blueL);
	    	int greenRes = (int)(1 * greenR);
	    	int blueRes = (int)(1 * blueR);
	    	
	    	redRes = checkColorValue(redRes);
	    	greenRes = checkColorValue(greenRes);
	    	blueRes = checkColorValue(blueRes);
	    	
	    	pixelsRes[i] = Color.argb(alpha, redRes, greenRes, blueRes);   	
    	}
    	
    	mutableBitmapResult.setPixels(pixelsRes, 0, bWidth, 0, 0, nWidth, bHeight);    	
    	return mutableBitmapResult;
	}
	
	public Bitmap anaglyphOptimizedColor(byte[] left, byte[] right){
    	Bitmap bmpL=BitmapFactory.decodeByteArray(left,0,left.length);
    	Bitmap bmpR=BitmapFactory.decodeByteArray(right,0,right.length);
    	
    	Bitmap mutableBitmapR = bmpR.copy(Bitmap.Config.ARGB_8888, true);
    	Bitmap mutableBitmapL = bmpL.copy(Bitmap.Config.ARGB_8888, true);
    	Bitmap mutableBitmapResult = bmpR.copy(Bitmap.Config.ARGB_8888, true);
    	bmpL.recycle();
    	bmpR.recycle();
    	
    	int bWidth = mutableBitmapR.getWidth();
        int bHeight = mutableBitmapR.getHeight();
    	int[] pixelsR = new int[bWidth * bHeight], pixelsL = new int[bWidth * bHeight], pixelsRes = new int[bWidth * bHeight];
    	mutableBitmapR.getPixels(pixelsR, 0, mutableBitmapR.getWidth(), 0, 0, mutableBitmapR.getWidth(), mutableBitmapR.getHeight());
    	mutableBitmapL.getPixels(pixelsL, 0, mutableBitmapL.getWidth(), 0, 0, mutableBitmapL.getWidth(), mutableBitmapL.getHeight());
    	mutableBitmapL.recycle();
    	mutableBitmapR.recycle();
    	
        	
    	if (PhotoMaker.debug){
    		Log.i(TAG+" - size", pixelsR.length+" - "+pixelsL.length);
    		Log.i(TAG, "optimized color");
    	}
    	long startTime = System.nanoTime(); 
    	for (int i = 0; i < pixelsR.length; i++){
    		//int redR = Color.red(pixelsR[i]);
			int blueR = Color.blue(pixelsR[i]);
	    	int greenR = Color.green(pixelsR[i]);
	    	int alpha = Color.alpha(pixelsR[i]);
	    	
	    	int redL = Color.red(pixelsL[i]);
	    	int blueL = Color.blue(pixelsL[i]);
	    	int greenL = Color.green(pixelsL[i]);

	    	int redRes = (int)(0 * redL + 0.7 * greenL + 0.3 * blueL);
	    	int greenRes = (int)(1 * greenR);
	    	int blueRes = (int)(1 * blueR);
	    	
	    	redRes = checkColorValue(redRes);
	    	greenRes = checkColorValue(greenRes);
	    	blueRes = checkColorValue(blueRes);
	    	
	    	pixelsRes[i] = Color.argb(alpha, redRes, greenRes, blueRes);
    	}
    	long estimatedTime = System.nanoTime() - startTime;
    	if (PhotoMaker.debug)
    		Log.i(TAG, estimatedTime+" is a optimized algorithm time.");
    	mutableBitmapResult.setPixels(pixelsRes, 0, mutableBitmapResult.getWidth(), 0, 0, mutableBitmapResult.getWidth(), mutableBitmapResult.getHeight());
    	return mutableBitmapResult;
	}
	
	public Bitmap anaglyphFullColor(byte[] left, byte[] right){
    	Bitmap bmpL=BitmapFactory.decodeByteArray(left,0,left.length);
    	Bitmap bmpR=BitmapFactory.decodeByteArray(right,0,right.length);
    	
    	Bitmap mutableBitmapR = bmpR.copy(Bitmap.Config.ARGB_8888, true);
    	Bitmap mutableBitmapL = bmpL.copy(Bitmap.Config.ARGB_8888, true);
    	Bitmap mutableBitmapResult = bmpR.copy(Bitmap.Config.ARGB_8888, true);
    	bmpL.recycle();
    	bmpR.recycle();
    	
    	int bWidth = mutableBitmapR.getWidth();
        int bHeight = mutableBitmapR.getHeight();
    	int[] pixelsR = new int[bWidth * bHeight], pixelsL = new int[bWidth * bHeight], pixelsRes = new int[bWidth * bHeight];
    	mutableBitmapR.getPixels(pixelsR, 0, mutableBitmapR.getWidth(), 0, 0, mutableBitmapR.getWidth(), mutableBitmapR.getHeight());
    	mutableBitmapL.getPixels(pixelsL, 0, mutableBitmapL.getWidth(), 0, 0, mutableBitmapL.getWidth(), mutableBitmapL.getHeight());
    	mutableBitmapL.recycle();
    	mutableBitmapR.recycle();
    	
    	if (PhotoMaker.debug)
    		Log.i(TAG,"fullColor - "+pixelsR.length+" - "+pixelsL.length);
    	
    	long startTime = System.nanoTime(); 
    	for (int i = 0; i < pixelsR.length; i++){
    		//int redR = Color.red(pixelsR[i]);
			int blueR = Color.blue(pixelsR[i]);
	    	int greenR = Color.green(pixelsR[i]);
	    	int alpha = Color.alpha(pixelsR[i]);
	    	
	    	int redL = Color.red(pixelsL[i]);
	    	//int blueL = Color.blue(pixelsL[i]);
	    	//int greenL = Color.green(pixelsL[i]);

	    	int redRes = (int)(redL);
	    	int greenRes = (int)(greenR);
	    	int blueRes = (int)(blueR);
	    	
	    	redRes = checkColorValue(redRes);
	    	greenRes = checkColorValue(greenRes);
	    	blueRes = checkColorValue(blueRes);
	    	
	    	pixelsRes[i] = Color.argb(alpha, redRes, greenRes, blueRes);
    	}
    	long estimatedTime = System.nanoTime() - startTime;
    	if (PhotoMaker.debug)
    		Log.i(TAG, estimatedTime+" is a full algorithm time.");
    	mutableBitmapResult.setPixels(pixelsRes, 0, mutableBitmapResult.getWidth(), 0, 0, mutableBitmapResult.getWidth(), mutableBitmapResult.getHeight());
    	return mutableBitmapResult;
	}
	
	public Bitmap anaglyphFullColor(byte[] left){
		Bitmap bmp=BitmapFactory.decodeByteArray(left,0,left.length);
		int bWidth = bmp.getWidth();
        int bHeight = bmp.getHeight();
        int pixelSize = bWidth * bHeight;
        
        Bitmap mutableBitmapL = Bitmap.createBitmap(bWidth-PIXEL_MOVE, bHeight, Bitmap.Config.ARGB_8888);
    	Bitmap mutableBitmapResult = Bitmap.createBitmap(bWidth-PIXEL_MOVE, bHeight, Bitmap.Config.ARGB_8888);
        int[] pixelsL = new int[pixelSize],pixelsR = new int[pixelSize];
        int[] pixelsRes = new int[pixelSize];
        int nWidth = mutableBitmapL.getWidth();
        
        bmp.getPixels(pixelsL, 0, bWidth, 0, 0, bWidth-PIXEL_MOVE, bHeight);
        bmp.getPixels(pixelsR, 0, bWidth, PIXEL_MOVE, 0, bWidth-PIXEL_MOVE, bHeight);
        bmp.recycle();
        mutableBitmapL.recycle();
        if (PhotoMaker.debug)
        	Log.i(TAG, "full color");
    	for (int i = 0; i < pixelsL.length; i++){
    		//int redR = Color.red(pixelsR[i]);
			int blueR = Color.blue(pixelsR[i]);
	    	int greenR = Color.green(pixelsR[i]);
	    	int alpha = Color.alpha(pixelsR[i]);
	    	
	    	int redL = Color.red(pixelsL[i]);
	    	//int blueL = Color.blue(pixelsL[i]);
	    	//int greenL = Color.green(pixelsL[i]);

	    	int redRes = (int)(redL);
	    	int greenRes = (int)(greenR);
	    	int blueRes = (int)(blueR);
	    	
	    	redRes = checkColorValue(redRes);
	    	greenRes = checkColorValue(greenRes);
	    	blueRes = checkColorValue(blueRes);
	    	
	    	pixelsRes[i] = Color.argb(alpha, redRes, greenRes, blueRes);   	
    	}
    	
    	mutableBitmapResult.setPixels(pixelsRes, 0, bWidth, 0, 0, nWidth, bHeight);    	
    	return mutableBitmapResult;
	}
	
	private int checkColorValue(int val){
		if (val > 255) return 255;
		else if (val < 0) return 0;
		else return val;
	}

}
