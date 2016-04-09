package pl.m4.photomaker;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

public class FileManager {
	private final String TAG = "m4photo:FileManager";
	
	@SuppressLint("SimpleDateFormat")
	public void savePicturePNG(final Bitmap data,final String type) {
		new Thread(new Runnable() {
	        public void run() {
	        	FileOutputStream out = null;
	    		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
	            String currentDateandTime = sdf.format(new Date());
	            
	            File dir = new File(Environment.getExternalStorageDirectory(),Environment.DIRECTORY_DCIM+"/3d");
	            if (!dir.exists()){
	            	dir.mkdirs();
	            }
	            
	            String fileName = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) +
	                                   "/3d/3dAm_"+type+"_" + currentDateandTime + ".png";
	            Log.i(TAG, "saving file: "+fileName);
	    		try {
	    			out = new FileOutputStream(fileName);
	    			data.compress(Bitmap.CompressFormat.PNG, 100, out);
	    			// PNG is a lossless format, the compression factor (100) is ignored
	    		} catch (Exception e) {
	    			e.printStackTrace();
	    		} finally {
	    			try {
	    				if (out != null) {
	    					out.close();
	    				}
	    			} catch (IOException e) {
	    				e.printStackTrace();
	    			}
	    		}
	        }
	    }).start();

		
	}
	
	@SuppressLint("SimpleDateFormat")
	public Thread saveOnFlyPicturePNG(final Bitmap data,final String type) {
		Runnable myRunnable = new Runnable() {
			public void run() {
				FileOutputStream out = null;
	    		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
	            String currentDateandTime = sdf.format(new Date());
	            
	            File dir = new File(Environment.getExternalStorageDirectory(),Environment.DIRECTORY_DCIM+"/3d");
	            if (!dir.exists()){
	            	dir.mkdirs();
	            }
	            
	            String fileName = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) +
	                                   "/3d/3dAm_"+type+"_" + currentDateandTime + ".png";
	            
	            Log.i(TAG, "saving file: "+fileName);
	    		try {
	    			out = new FileOutputStream(fileName);
	    			data.compress(Bitmap.CompressFormat.PNG, 100, out);
	    			// PNG is a lossless format, the compression factor (100) is ignored
	    		} catch (Exception e) {
	    			e.printStackTrace();
	    		} finally {
	    			try {
	    				if (out != null) {
	    					out.close();
	    				}
	    			} catch (IOException e) {
	    				e.printStackTrace();
	    			}
	    		}
			}
		};
		Thread th = new Thread(myRunnable);
		th.start();
		return th;
	}
	
	@SuppressLint("SimpleDateFormat")
	public Thread saveOnFlyPicturePNS(final Bitmap data,final String type) {
		Runnable myRunnable = new Runnable() {
			public void run() {
				FileOutputStream out = null;
	    		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
	            String currentDateandTime = sdf.format(new Date());
	            
	            File dir = new File(Environment.getExternalStorageDirectory(),Environment.DIRECTORY_DCIM+"/3d");
	            if (!dir.exists()){
	            	dir.mkdirs();
	            }
	            
	            String fileName = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) +
	                                   "/3d/3dAm_"+type+"_" + currentDateandTime + ".pns";
	            
	            Log.i(TAG, "saving file: "+fileName);
	    		try {
	    			out = new FileOutputStream(fileName);
	    			data.compress(Bitmap.CompressFormat.PNG, 100, out);
	    			// PNG is a lossless format, the compression factor (100) is ignored
	    		} catch (Exception e) {
	    			e.printStackTrace();
	    		} finally {
	    			try {
	    				if (out != null) {
	    					out.close();
	    				}
	    			} catch (IOException e) {
	    				e.printStackTrace();
	    			}
	    		}
			}
		};
		Thread th = new Thread(myRunnable);
		th.start();
		return th;
	}
}