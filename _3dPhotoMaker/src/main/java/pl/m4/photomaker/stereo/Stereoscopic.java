package pl.m4.photomaker.stereo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

public abstract class Stereoscopic {
    public final int PIXEL_MOVED = 30;

    protected Bitmap joinBitmap(Bitmap firstFrame, Bitmap secondFrame) {
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

    protected Bitmap loadBitmap(byte[] image) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        return BitmapFactory.decodeByteArray(image, 0, image.length, options);
    }

    protected int checkColorValue(int val){
        if (val > 255) return 255;
        else if (val < 0) return 0;
        else return val;
    }

    protected abstract Bitmap createStereoscopicImage(byte[] image);
    protected abstract Bitmap createStereoscopicImage(byte[] left, byte[] right);
}
