package pl.m4.photomaker.stereo;

import android.content.Context;
import android.graphics.Bitmap;
import android.renderscript.Allocation;

import pl.m4.photomaker.R;
import pl.m4.photomaker.ScriptC_anaglyphFullColor;

public class AnaglyphFullColor extends Anaglyph {
    private final String TAG = "AnaglyphFullColor";
    private ScriptC_anaglyphFullColor mScript;

    public AnaglyphFullColor(Context context){
        super(context);
    }

    @Override
    public void createScript(Allocation leftImageAllocation, Allocation rightImageAllocation, Allocation outImageAllocation) {
        mScript = new ScriptC_anaglyphFullColor(getRenderScript(), context.getResources(), R.raw.anaglyphfullcolor);
        mScript.set_secondImage(rightImageAllocation);
        mScript.forEach_root(leftImageAllocation, outImageAllocation);
    }

    @Override
    public Bitmap createStereoscopicImage(byte[] image) {
        return super.createStereoscopicImage(image);
    }

    @Override
    public Bitmap createStereoscopicImage(byte[] left, byte[] right) {
        return super.createStereoscopicImage(left, right);
    }
}
