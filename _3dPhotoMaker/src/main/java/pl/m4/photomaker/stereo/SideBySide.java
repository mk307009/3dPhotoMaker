package pl.m4.photomaker.stereo;

import android.graphics.Bitmap;
import android.util.Log;

import pl.m4.photomaker.PhotoMaker;

public class SideBySide extends Stereoscopic{
    private final String TAG = "SideBySide";

    public SideBySide(){
    }

    /**
     * Function generating 3d photo from two frames.
     * @param left - its first/left frame from camera.
     * @param right - second/right frame from camera.
     * @return Bitmap as stereoscopic image.
     */
    @Override
    public Bitmap createStereoscopicImage(byte[] left, byte[] right) {
        long startTime = System.nanoTime();
        Bitmap leftBitmap = loadBitmap(left);
        Bitmap rightBitmap = loadBitmap(right);
        Bitmap resultBitmap = joinBitmap(leftBitmap, rightBitmap);
        leftBitmap.recycle();
        rightBitmap.recycle();
        long estimatedTime = System.nanoTime() - startTime;
        if (PhotoMaker.debug)
            Log.i(TAG,"two frames: " + estimatedTime + " is a "+getClass().getSimpleName()+" time.");
        return resultBitmap;
    }

    /**
     * Function generating 3d photo from one frame.
     * @param image - its frame from camera.
     * @return Bitmap as stereoscopic image.
     */
    @Override
    public Bitmap createStereoscopicImage(byte[] image) {
        long startTime = System.nanoTime();
        Bitmap input = loadBitmap(image);
        int bWidth = input.getWidth();
        int bHeight = input.getHeight();
        Bitmap left = Bitmap.createBitmap(input, 0, 0, bWidth-PIXEL_MOVED, bHeight);
        Bitmap right = Bitmap.createBitmap(input, PIXEL_MOVED, 0, bWidth-PIXEL_MOVED, bHeight);
        input.recycle();

        Bitmap resultBitmap = joinBitmap(left, right);
        left.recycle();
        right.recycle();

        long estimatedTime = System.nanoTime() - startTime;
        if (PhotoMaker.debug)
            Log.i(TAG,"one frame: " + estimatedTime + " is a "+getClass().getSimpleName()+" time.");
        return resultBitmap;
    }
}
