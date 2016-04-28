package pl.m4.photomaker;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.Size;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.Toast;

import org.opencv.android.JavaCameraView;

import java.util.List;

@SuppressWarnings("deprecation")
public class CameraView extends JavaCameraView implements PictureCallback {

    private static final String TAG = "CameraView";
    private byte[] firstFrame = null,secondFrame = null;
    private int pictureCount = 0;

    public CameraView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public List<String> getEffectList() {
        return mCamera.getParameters().getSupportedColorEffects();
    }

    public boolean isEffectSupported() {
        return (mCamera.getParameters().getColorEffect() != null);
    }

    public String getEffect() {
        return mCamera.getParameters().getColorEffect();
    }

    public void setEffect(String effect) {
        Camera.Parameters params = mCamera.getParameters();
        params.setColorEffect(effect);
        mCamera.setParameters(params);
    }

    public List<Size> getResolutionList() {
        return mCamera.getParameters().getSupportedPreviewSizes();
    }

    public void setResolution(Size resolution) {
        disconnectCamera();
        mMaxHeight = resolution.height;
        mMaxWidth = resolution.width;
        //connectCamera(getWidth(), getHeight());
        connectCamera(mMaxWidth, mMaxHeight);
    }
    
    public void setPictureSize(int width, int height) {
    	Camera.Parameters params = mCamera.getParameters();
        params.setPictureSize(width, height);
        mCamera.setParameters(params);
    }

    public Size getResolution() {
        return mCamera.getParameters().getPreviewSize();
    }

    public void takePicture(int number) {
    	pictureCount = number;
        Log.i(TAG, "Taking picture");
        // Postview and jpeg are sent in the same buffers if the queue is not empty when performing a capture.
        // Clear up buffers to avoid mCamera.takePicture to be stuck because of a memory issue
        mCamera.setPreviewCallback(null);
        // PictureCallback is implemented by the current class
        mCamera.takePicture(null, null, this);
    }

    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
        // The camera preview was automatically stopped. Start it again.
        mCamera.startPreview();
        mCamera.setPreviewCallback(this);

        if (pictureCount == 0){
        	if (PhotoMaker.oneFrame)
        		Toast.makeText(getContext(), "Processing, please wait for preview.", Toast.LENGTH_SHORT).show();
        	else Toast.makeText(getContext(), "Move a little to the right.", Toast.LENGTH_SHORT).show();
        	firstFrame = data;
        }
        else if (pictureCount == 1){
        	Toast.makeText(getContext(), "Correctly. \nProcessing, please wait for preview.", Toast.LENGTH_SHORT).show();
        	secondFrame = data;
        }
    }
    
    public byte[] getFrameData(int number){
    	if (number == 0)
    		return firstFrame;
    	else return secondFrame;
    }
    
    public void setFrameData(byte[] src, int index){
    	if (index == 0)
    		firstFrame = src;
    	else secondFrame = src;
    }
}
