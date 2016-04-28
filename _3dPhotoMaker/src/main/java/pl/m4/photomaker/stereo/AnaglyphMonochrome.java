package pl.m4.photomaker.stereo;

import android.content.Context;
import android.graphics.Bitmap;
import android.renderscript.Allocation;

import pl.m4.photomaker.R;
import pl.m4.photomaker.ScriptC_anaglyphMonochrome;

public class AnaglyphMonochrome extends Anaglyph {
    private final String TAG = "AnaglyphMonochrome";
    private ScriptC_anaglyphMonochrome mScript;

    public AnaglyphMonochrome(Context context){
        super(context);
    }

    @Override
    public void createScript(Allocation leftImageAllocation, Allocation rightImageAllocation, Allocation outImageAllocation) {
        mScript = new ScriptC_anaglyphMonochrome(getRenderScript(), context.getResources(), R.raw.anaglyphmonochrome);
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
