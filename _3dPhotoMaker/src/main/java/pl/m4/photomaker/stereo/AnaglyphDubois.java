package pl.m4.photomaker.stereo;

import android.content.Context;
import android.graphics.Bitmap;
import android.renderscript.Allocation;

import pl.m4.photomaker.R;
import pl.m4.photomaker.ScriptC_anaglyphDubois;

public class AnaglyphDubois extends Anaglyph {
    private final String TAG = "AnaglyphMonochrome";
    private ScriptC_anaglyphDubois mScript;

    public AnaglyphDubois(Context context){
        super(context);
    }

    @Override
    public void createScript(Allocation leftImageAllocation, Allocation rightImageAllocation, Allocation outImageAllocation) {
        mScript = new ScriptC_anaglyphDubois(getRenderScript(), context.getResources(), R.raw.anaglyphdubois);
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
