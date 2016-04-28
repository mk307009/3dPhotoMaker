package pl.m4.photomaker.stereo;

import android.content.Context;
import android.graphics.Bitmap;
import android.renderscript.Allocation;
import android.renderscript.RenderScript;
import android.util.Log;

import pl.m4.photomaker.PhotoMaker;

public abstract class Anaglyph extends Stereoscopic{
    private final String TAG = "Anaglyph";
    private RenderScript renderScript;
    protected Context context;

    public Anaglyph(Context context){
        this.context = context;
    }

    private void runScript(Bitmap leftBitmap, Bitmap rightBitmap, Bitmap bitmapOut){
        renderScript = RenderScript.create(context);
        Allocation leftFrameAllocation = Allocation.createFromBitmap(renderScript, leftBitmap,
                Allocation.MipmapControl.MIPMAP_NONE,
                Allocation.USAGE_SCRIPT);
        Allocation rightFrameAllocation = Allocation.createFromBitmap(renderScript, rightBitmap,
                Allocation.MipmapControl.MIPMAP_NONE,
                Allocation.USAGE_SCRIPT);
        Allocation outAllocation = Allocation.createFromBitmap(renderScript, bitmapOut,
                Allocation.MipmapControl.MIPMAP_NONE,
                Allocation.USAGE_SCRIPT);
        createScript(leftFrameAllocation, rightFrameAllocation, outAllocation);
        outAllocation.copyTo(bitmapOut);
    }

    protected RenderScript getRenderScript(){
        return renderScript;
    }

    /**
     * create new specialized RenderScript object and setup parameters and run forEach_root method.
     */
    public abstract void createScript(Allocation leftImageAllocation, Allocation rightImageAllocation, Allocation outImageAllocation);

    /**
     * Function generating 3d photo from two frames.
     * @param left - its first/left frame from camera.
     * @param right - second/right frame from camera.
     * @return Bitmap as stereoscopic image.
     */
    @Override
    protected Bitmap createStereoscopicImage(byte[] left, byte[] right) {
        long startTime = System.nanoTime();
        Bitmap leftBitmap = loadBitmap(left);
        Bitmap rightBitmap = loadBitmap(right);
        int bWidth = leftBitmap.getWidth();
        int bHeight = leftBitmap.getHeight();

        Bitmap resultBitmap = Bitmap.createBitmap(bWidth, bHeight, leftBitmap.getConfig());
        runScript(leftBitmap, rightBitmap, resultBitmap);
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
    protected Bitmap createStereoscopicImage(byte[] image) {
        long startTime = System.nanoTime();
        Bitmap input = loadBitmap(image);
        int bWidth = input.getWidth();
        int bHeight = input.getHeight();
        Bitmap left = Bitmap.createBitmap(input, 0, 0, bWidth-PIXEL_MOVED, bHeight);
        Bitmap right = Bitmap.createBitmap(input, PIXEL_MOVED, 0, bWidth-PIXEL_MOVED, bHeight);
        input.recycle();

        Bitmap resultBitmap = Bitmap.createBitmap(bWidth-PIXEL_MOVED, bHeight, input.getConfig());
        runScript(left, right, resultBitmap);
        left.recycle();
        right.recycle();

        long estimatedTime = System.nanoTime() - startTime;
        if (PhotoMaker.debug)
            Log.i(TAG,"one frame: " + estimatedTime + " is a "+getClass().getSimpleName()+" time.");
        return resultBitmap;
    }
}
