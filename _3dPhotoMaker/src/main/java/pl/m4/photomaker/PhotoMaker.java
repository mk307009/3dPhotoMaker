package pl.m4.photomaker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.imgproc.Imgproc;
import org.opencv.video.Video;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera.Size;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import pl.m4.photomaker.listener.MainListener;

@SuppressWarnings("deprecation")
public class PhotoMaker extends Activity implements CvCameraViewListener2{
	private static final String TAG = "m4photo:PhotoMaker";
	private static final int RESIZE = 3;
	public static final String DEFAULT_MODE = "Dubois";
	
	public static boolean debug = false;
	public static boolean oneFrame = false;
	public static String algorithm = "Dubois";
	public static boolean manualMode = false;

    private CameraView openCvCamera;
	private List<Size> mResolutionList;
    private MenuItem[] mEffectMenuItems;
    private SubMenu mColorEffectsMenu;
    private MenuItem[] mResolutionMenuItems;
    private SubMenu mResolutionMenu;
    private ImageView imgViewResult;
    private Button settings, makePhoto, savePhoto;
    private Anaglyph anaglyph;
    private FileManager fileMan;
    private VectorDraw vDraw;
    private SettingsPreferences preferences;
    private org.opencv.core.Size size;
    private Mat secondFrame = null;
    private Mat firstFrame = null;
    private float[][] startPosTab, endPosTab;
    private MatOfPoint2f tmpCorners = null;
    private MatOfPoint2f prevPts = null;
    private float medianX = 0, medianY = 0;
    private boolean isPreview = false;
    private Bitmap resultBmp = null;
    private Thread savingThread = null;
    ProgressDialog progressBar;
    private MainListener mainViewListeners;
    private float disableButton = 0.6f;
    
    static {
        if (!OpenCVLoader.initDebug()) {
        	Log.e(TAG,"OpenCVLoader init error");
        }
    }
    
    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {
                    Log.i(TAG, "OpenCV loaded successfully");
                    openCvCamera.enableView();
                } break;
                default:
                {
                    super.onManagerConnected(status);
                } break;
            }
        }
    };
    
    public PhotoMaker() {
        Log.i(TAG, "Instantiated new " + this.getClass());
        anaglyph = new Anaglyph();
        fileMan = new FileManager();        
    }
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "called onCreate");
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_photo_maker);

        openCvCamera = (CameraView) findViewById(R.id.camview_activity_java_surface);
        imgViewResult = (ImageView) findViewById(R.id.previewPhotoResult);
        settings = (Button) findViewById(R.id.settings);
        makePhoto = (Button)findViewById(R.id.Button3dCreator);
        savePhoto = (Button)findViewById(R.id.savePhoto);
        savePhoto.setEnabled(false);
        savePhoto.setAlpha(disableButton);
        openCvCamera.setVisibility(SurfaceView.VISIBLE);
        openCvCamera.setCvCameraViewListener(this);
        
        vDraw = new VectorDraw(this, openCvCamera);
        addContentView(vDraw, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        progressBar = new ProgressDialog(this);
        mainViewListeners = new MainListener(getApplicationContext(), imgViewResult);
        startPosTab = new float[2][vDraw.getCountVec()];
        endPosTab = new float[2][vDraw.getCountVec()];
        
        preferences = new SettingsPreferences(this);
        PhotoMaker.debug = preferences.loadSavedPreferences("DebugMode");
        PhotoMaker.oneFrame = preferences.loadSavedPreferences("OneFrame");
        PhotoMaker.algorithm = preferences.loadSavedPreferences("3dMode",DEFAULT_MODE);
        PhotoMaker.manualMode = preferences.loadSavedPreferences("ManualMode");
        if (PhotoMaker.debug)
        	openCvCamera.enableFpsMeter();
	}
	
	@Override
    public void onPause()
    {
        super.onPause();
        if (openCvCamera != null)
            openCvCamera.disableView();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        //OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_3, this, mLoaderCallback);
        PhotoMaker.debug = preferences.loadSavedPreferences("DebugMode");
        PhotoMaker.oneFrame = preferences.loadSavedPreferences("OneFrame");
        PhotoMaker.algorithm = preferences.loadSavedPreferences("3dMode",DEFAULT_MODE);
        PhotoMaker.manualMode = preferences.loadSavedPreferences("ManualMode");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (openCvCamera != null)
            openCvCamera.disableView();
    }

    public void onCameraViewStarted(int width, int height) {
//    	List<Size> size = openCvCamera.getResolutionList();
//    	for (Size t: size)
//    		Log.i(TAG,t.height+" "+t.width);
    }

    public void onCameraViewStopped() {
    }

    public Mat onCameraFrame(CvCameraViewFrame inputFrame) {
        try {
            if (firstFrame == null || secondFrame == null) {
                secondFrame = inputFrame.rgba();
                prepareFirstFrame();
                return inputFrame.rgba();
            }

            if (!isPreview)
                generatePreview();
            checkSaving();

            if (PhotoMaker.oneFrame)
                return inputFrame.rgba();

            MatOfByte status = calculateOpticalFlow(inputFrame);
            setEndDrawingPosition();
            List<List<Double>> vectors = new ArrayList<List<Double>>(2);
            if (setCorrectlyDetectedVectors(status, vectors)) {
                medianX = calculateDistance(vectors.get(0), medianX);
                medianY = calculateDistance(vectors.get(1), medianY);
            }
            vDraw.moveX(medianX);
            vDraw.moveY(medianY);

            takeSecondFrame();

            if (PhotoMaker.debug) {
                Log.i(TAG, "corners = " + tmpCorners.dump());
                Log.i(TAG, "static corners = " + prevPts.dump());
                Log.i(TAG, "status pLK = " + status.dump());
            }
        } catch (NullPointerException e) {
            Log.e(TAG, "No frame captured");
        }
        return inputFrame.rgba();
    }

    private void setEndDrawingPosition() {
        for (int j = 0; j < vDraw.getCountVec(); j++) {
            endPosTab[0][j] = (float) (tmpCorners.get(j, 0)[0] * RESIZE);
            endPosTab[1][j] = (float) (tmpCorners.get(j, 0)[1] * RESIZE);
        }
        vDraw.setEndPos(endPosTab);
    }

    /**
     * @param status - status of vector from calcOpticalFlowPyrLK function.
     * @param vectors - Output list with correct vectors. vectors(0) for axis x, vectors(1) axis y.
     * @return false if not found correctly detected vectors. True any correct vectors.
     */
    private boolean setCorrectlyDetectedVectors(MatOfByte status, List<List<Double>> vectors) {
        double[][] vect = new double[2][tmpCorners.rows()];
        boolean isCorrectVector = false; //for check is any correct corner
        vectors.add(new ArrayList<Double>());
        vectors.add(new ArrayList<Double>());
        for (int i = 0 ; i < tmpCorners.rows(); i++){
        	vect[0][i] = prevPts.get(i, 0)[0] - tmpCorners.get(i, 0)[0];
        	vect[1][i] = prevPts.get(i, 0)[1] - tmpCorners.get(i, 0)[1];
        	if (status.get(i,0)[0] == 1){
                vectors.get(0).add(vect[0][i]);
                vectors.get(1).add(vect[1][i]);
        		isCorrectVector = true;
        	}
        }
        return isCorrectVector;
    }

    private void takeSecondFrame() {
        if (openCvCamera.getFrameData(1) == null && vDraw.getX() >= vDraw.getStartX() + 20) {
            try{
                openCvCamera.takePicture(1);
            }catch(RuntimeException e){
                openCvCamera.setFrameData(null, 1);
                Log.e(TAG,e.getMessage());
            }
        }
    }

    private float calculateDistance(List<Double> vectors, float median){
        Collections.sort(vectors);
        float deltaMedian = median;
        median =(float) Calc.median(vectors);
        deltaMedian -= median;
        if (PhotoMaker.debug)
            Log.i(TAG, "motion = "+Calc.isValidMotion(deltaMedian, 10));
        return median;
    }

    @NonNull
    private MatOfByte calculateOpticalFlow(CvCameraViewFrame inputFrame) {
        secondFrame = inputFrame.rgba();
        Imgproc.cvtColor(secondFrame, secondFrame, Imgproc.COLOR_BGR2GRAY);
        Imgproc.resize(secondFrame, secondFrame, size);
        //flow = new Mat(inputFrame.rgba().size(), CvType.CV_8UC1);
        //Video.calcOpticalFlowFarneback(firstFrame, secondFrame, flow,0.5,1, 1, 1, 7, 1.5,1); //-- works
        //optical flow Lucas-Kanade
        MatOfFloat err=new MatOfFloat();
        MatOfByte status = new MatOfByte();
        tmpCorners = new MatOfPoint2f();
        Video.calcOpticalFlowPyrLK(firstFrame, secondFrame, prevPts, tmpCorners, status, err);
        return status;
    }

    private void prepareFirstFrame() {
		if (openCvCamera.getFrameData(0) != null){
            byte[] photo = openCvCamera.getFrameData(0);
            Bitmap bmp = BitmapFactory.decodeByteArray(photo , 0, photo.length);
            Mat orig = new Mat(bmp.getHeight(),bmp.getWidth(), CvType.CV_8UC3);
            Bitmap myBitmap32 = bmp.copy(Bitmap.Config.ARGB_8888, true);
            Utils.bitmapToMat(myBitmap32, orig);
            Imgproc.cvtColor(orig, orig, Imgproc.COLOR_BGR2RGB,4);
            size = new org.opencv.core.Size(secondFrame.cols()/RESIZE,secondFrame.rows()/RESIZE);
            firstFrame = orig;

            Imgproc.cvtColor(firstFrame, firstFrame, Imgproc.COLOR_BGR2GRAY);
            Imgproc.resize(firstFrame, firstFrame, size);
            //detect corners on firstFrame image
            MatOfPoint prvPts =new MatOfPoint();
            prevPts =new MatOfPoint2f();
            Imgproc.goodFeaturesToTrack(firstFrame, prvPts, 30, 0.01, 10);
            prvPts.convertTo(prevPts, CvType.CV_32FC2);
            //set start position for vectors
            for (int j = 0; j < vDraw.getCountVec(); j++) {
                startPosTab[0][j] = (float) (prevPts.get(j, 0)[0] * RESIZE);
                startPosTab[1][j] = (float) (prevPts.get(j, 0)[1] * RESIZE);
            }
            vDraw.setStartPos(startPosTab);
            vDraw.setEndPos(startPosTab);
            myBitmap32.recycle();
            bmp.recycle();
        }
	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        List<String> effects = openCvCamera.getEffectList();
        if (effects == null) {
            Log.e(TAG, "Color effects are not supported by device!");
            return true;
        }

        mColorEffectsMenu = menu.addSubMenu("Color Effect");
        mEffectMenuItems = new MenuItem[effects.size()];

        int idx = 0;
        ListIterator<String> effectItr = effects.listIterator();
        while(effectItr.hasNext()) {
           String element = effectItr.next();
           mEffectMenuItems[idx] = mColorEffectsMenu.add(1, idx, Menu.NONE, element);
           idx++;
        }

        mResolutionMenu = menu.addSubMenu("Resolution");
        mResolutionList = openCvCamera.getResolutionList();
        mResolutionMenuItems = new MenuItem[mResolutionList.size()];

        ListIterator<Size> resolutionItr = mResolutionList.listIterator();
        idx = 0;
        while(resolutionItr.hasNext()) {
            Size element = resolutionItr.next();
            mResolutionMenuItems[idx] = mResolutionMenu.add(2, idx, Menu.NONE,
                    Integer.valueOf(element.width).toString() + "x" + Integer.valueOf(element.height).toString());
            idx++;
         } 

        return true;
    }
    
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.i(TAG, "called onOptionsItemSelected; selected item: " + item);
        if (item.getGroupId() == 1)
        {
            openCvCamera.setEffect((String) item.getTitle());
            Toast.makeText(this, openCvCamera.getEffect(), Toast.LENGTH_SHORT).show();
        }
        else if (item.getGroupId() == 2)
        {
            int id = item.getItemId();
            Size resolution = mResolutionList.get(id);
            openCvCamera.setResolution(resolution);
            resolution = openCvCamera.getResolution();
            String caption = Integer.valueOf(resolution.width).toString() + "x" + Integer.valueOf(resolution.height).toString();
            Toast.makeText(this, caption, Toast.LENGTH_SHORT).show();
        }
        return true;
    }
	
    
    public void photoCreator(View v){
    	savePhoto.setEnabled(false);
    	savePhoto.setAlpha(disableButton);
    	if (openCvCamera.getFrameData(0) != null){
    		openCvCamera.setFrameData(null, 0);
    		openCvCamera.setFrameData(null, 1);
    		firstFrame = null;
    		secondFrame = null;
    		isPreview = false;
    		vDraw.resetPos();
    	}
    	openCvCamera.takePicture(0);
    }
    
    public void resetPhotoSettings(){
    	openCvCamera.setFrameData(null, 0);
		openCvCamera.setFrameData(null, 1);
		firstFrame = null;
		secondFrame = null;
		isPreview = false;
		vDraw.resetPos();
		makePhoto.setEnabled(true);
		makePhoto.setAlpha(1.0f);
    }
    
    //MENU SETTINGS
	public void settings(View v){
		SettingsDialog dl = new SettingsDialog(settings, this);
		dl.show(getFragmentManager(), "Settings");
    }

	private void generatePreview(){
		if ( (PhotoMaker.oneFrame && openCvCamera.getFrameData(0) == null) || (!PhotoMaker.oneFrame && (openCvCamera.getFrameData(0) == null || openCvCamera.getFrameData(1) == null)) )
			return;
		else isPreview = true;
		
		runOnUiThread(new Runnable() {
			public void run() {
				makePhoto.setEnabled(false);
				makePhoto.setAlpha(disableButton);
			}
		});
		
		byte[] leftPic = openCvCamera.getFrameData(0);
		byte[] rightPic = null;
		if (!PhotoMaker.oneFrame)
			rightPic = openCvCamera.getFrameData(1);
		new PreviewImage().execute(leftPic, rightPic);
	}
	
	private void checkSaving(){
		if(savingThread != null && !savingThread.isAlive()){
			if (PhotoMaker.debug)
				Log.i(TAG,"picture saved...");
    		savingThread = null;
    		runOnUiThread(new Runnable() {
        	    public void run() {
        	    	progressBar.cancel();
        	    	Toast.makeText(getApplicationContext(), "Saving completed.", Toast.LENGTH_SHORT).show();
        	    	makePhoto.setEnabled(true);
        	    	makePhoto.setAlpha(1.0f);
        	    	resultBmp.recycle();
        	    }
        	});
    	}
	}
    
    public void savePhoto(View v){
        progressBar.setTitle("Saving");
        progressBar.setMessage("Please wait...");
        progressBar.setCancelable(true);
        progressBar.show();
    	String algorithm = preferences.loadSavedPreferences("3dMode",DEFAULT_MODE);
    	if (!algorithm.equals("PNS"))
    		savingThread = fileMan.saveOnFlyPicturePNG(resultBmp, algorithm);
    	else
    		savingThread = fileMan.saveOnFlyPicturePNS(resultBmp, algorithm);
    	savePhoto.setEnabled(false);
    	savePhoto.setAlpha(disableButton);
    	makePhoto.setEnabled(false);
    	makePhoto.setAlpha(disableButton);
	}
	
	private class PreviewImage extends AsyncTask<byte[], Void, Bitmap>{
	    protected Bitmap doInBackground(byte[]... frame) {	        
	    	String algorithm = preferences.loadSavedPreferences("3dMode", DEFAULT_MODE);
			Bitmap bitmapResult = null;
			switch (algorithm) {
			case "Dubois": {
				if (PhotoMaker.oneFrame)
					bitmapResult = anaglyph.anaglyphDubois(frame[0]);
				else
					bitmapResult = anaglyph.anaglyphDubois(frame[0], frame[1]);
			}break;
			case "Monochrome": {
				if (PhotoMaker.oneFrame)
					bitmapResult = anaglyph.anaglyphMonochrome(frame[0]);
				else
					bitmapResult = anaglyph.anaglyphMonochrome(frame[0], frame[1]);
			}break;
			case "PNS": {
				if (PhotoMaker.oneFrame)
					bitmapResult = anaglyph.pns3d(frame[0]);
				else
					bitmapResult = anaglyph.pns3d(frame[0], frame[1]);
			}break;
			case "OptimizedColor": {
				if (PhotoMaker.oneFrame)
					bitmapResult = anaglyph.anaglyphOptimizedColor(frame[0]);
				else
					bitmapResult = anaglyph.anaglyphOptimizedColor(frame[0], frame[1]);
			}break;
			case "FullColor": {
				if (PhotoMaker.oneFrame)
					bitmapResult = anaglyph.anaglyphFullColor(frame[0]);
				else
					bitmapResult = anaglyph.anaglyphFullColor(frame[0], frame[1]);
			}break;
			}
			return bitmapResult;
	    }

	    protected void onPostExecute(Bitmap result) {
	    	resultBmp = result;
	    	imgViewResult.setImageBitmap(Bitmap.createScaledBitmap(result, 400, 250, false));
	    	savePhoto.setEnabled(true);
	    	savePhoto.setAlpha(1.0f);
	    	makePhoto.setEnabled(true);
	    	makePhoto.setAlpha(1.0f);
	    	
	    	imgViewResult.setOnTouchListener(mainViewListeners.movePreviewListener());
	    }
	}

	public void previewOnClick(View v){
	}
}