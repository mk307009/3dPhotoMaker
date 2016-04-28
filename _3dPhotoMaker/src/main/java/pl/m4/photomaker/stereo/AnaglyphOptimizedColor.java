package pl.m4.photomaker.stereo;

import android.content.Context;
import android.graphics.Bitmap;
import android.renderscript.Allocation;

import pl.m4.photomaker.R;
import pl.m4.photomaker.ScriptC_anaglyphOptimizedColor;

public class AnaglyphOptimizedColor extends Anaglyph {
    private final String TAG = "AnaglyphOptimizedColor";
    private ScriptC_anaglyphOptimizedColor mScript;

    public AnaglyphOptimizedColor(Context context){
        super(context);
    }

    @Override
    public void createScript(Allocation leftImageAllocation, Allocation rightImageAllocation, Allocation outImageAllocation) {
        mScript = new ScriptC_anaglyphOptimizedColor(getRenderScript(), context.getResources(), R.raw.anaglyphoptimizedcolor);
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
