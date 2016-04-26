package pl.m4.photomaker;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;

public class AnaglyphFullColor extends Stereoscopic {
    private final String TAG = "AnaglyphFullColor";

    @Override
    protected Bitmap createStereoscopicImage(byte[] image) {
        Bitmap bmp = BitmapFactory.decodeByteArray(image, 0, image.length);
        int bWidth = bmp.getWidth();
        int bHeight = bmp.getHeight();
        int pixelSize = bWidth * bHeight;

        Bitmap mutableBitmapL = Bitmap.createBitmap(bWidth - PIXEL_MOVE, bHeight, Bitmap.Config.ARGB_8888);
        Bitmap mutableBitmapResult = Bitmap.createBitmap(bWidth - PIXEL_MOVE, bHeight, Bitmap.Config.ARGB_8888);
        int[] pixelsL = new int[pixelSize], pixelsR = new int[pixelSize];
        int[] pixelsRes = new int[pixelSize];
        int nWidth = mutableBitmapL.getWidth();

        bmp.getPixels(pixelsL, 0, bWidth, 0, 0, bWidth - PIXEL_MOVE, bHeight);
        bmp.getPixels(pixelsR, 0, bWidth, PIXEL_MOVE, 0, bWidth - PIXEL_MOVE, bHeight);
        bmp.recycle();
        mutableBitmapL.recycle();
        if (PhotoMaker.debug)
            Log.i(TAG, "full color");
        for (int i = 0; i < pixelsL.length; i++) {
            int blueR = Color.blue(pixelsR[i]);
            int greenR = Color.green(pixelsR[i]);
            int alpha = Color.alpha(pixelsR[i]);

            int redL = Color.red(pixelsL[i]);

            int redRes = (int) redL;
            int greenRes = (int) greenR;
            int blueRes = (int) blueR;

            redRes = checkColorValue(redRes);
            greenRes = checkColorValue(greenRes);
            blueRes = checkColorValue(blueRes);

            pixelsRes[i] = Color.argb(alpha, redRes, greenRes, blueRes);
        }
        mutableBitmapResult.setPixels(pixelsRes, 0, bWidth, 0, 0, nWidth, bHeight);
        return mutableBitmapResult;
    }

    @Override
    protected Bitmap createStereoscopicImage(byte[] left, byte[] right) {
        Bitmap bmpL = BitmapFactory.decodeByteArray(left, 0, left.length);
        Bitmap bmpR = BitmapFactory.decodeByteArray(right, 0, right.length);

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
            Log.i(TAG, pixelsR.length + " - " + pixelsL.length);

        long startTime = System.nanoTime();
        for (int i = 0; i < pixelsR.length; i++) {
            int blueR = Color.blue(pixelsR[i]);
            int greenR = Color.green(pixelsR[i]);
            int alpha = Color.alpha(pixelsR[i]);

            int redL = Color.red(pixelsL[i]);

            int redRes = (int) (redL);
            int greenRes = (int) (greenR);
            int blueRes = (int) (blueR);

            redRes = checkColorValue(redRes);
            greenRes = checkColorValue(greenRes);
            blueRes = checkColorValue(blueRes);

            pixelsRes[i] = Color.argb(alpha, redRes, greenRes, blueRes);
        }
        long estimatedTime = System.nanoTime() - startTime;
        if (PhotoMaker.debug)
            Log.i(TAG, estimatedTime + " is a full algorithm time.");
        mutableBitmapResult.setPixels(pixelsRes, 0, mutableBitmapResult.getWidth(), 0, 0, mutableBitmapResult.getWidth(), mutableBitmapResult.getHeight());
        return mutableBitmapResult;
    }
}
