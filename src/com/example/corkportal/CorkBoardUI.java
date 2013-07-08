package com.example.corkportal;

import java.io.ByteArrayOutputStream;
import java.io.File;

import org.json.JSONObject;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;


public class CorkBoardUI extends Activity {

	private static final String TAG = "CorkBoardUI"; 
	
	
	private static final int REQUEST_CAPTURE_MEDIA = 1;
	private static final int REQUEST_RETRIEVE_MEDIA = 2;



	static final String PICSAY_PACKAGE_PREFIX = "com.shinycore.picsay";
	static final String ACTION_MEDIA_CAPTURE = "mobisocial.intent.action.MEDIA_CAPTURE";

	public static final String PICTURE_SUBFOLDER = "Pictures/Musubi";
	public static final String HTML_SUBFOLDER = "Musubi/HTML";
	public static final String FILES_SUBFOLDER = "Musubi/Files";
	public static final String APPS_SUBFOLDER = "Musubi/Apps";

	private String mRowId;
	private static Uri newImageUri;
	
	private Bitmap mImageBitMap; 

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.corkboard_ui);	
		Bundle extras = getIntent().getExtras();
		mRowId = extras.getString("cb_id");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
			return super.onOptionsItemSelected(item);
	}

	@Override
	public void onResume() {
		super.onResume();
	}
	
	private File createFile() throws Exception
	{
		Log.d(TAG, "Entered CreateFile");
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			File path = new File(Environment.getExternalStorageDirectory().getPath() + "/CorkBoard/");
			path.mkdirs();

			File file = new File(path, "CorkBoard" + "_" + mRowId + ".jpg");
			Log.d("TAG", path + " AND " + file);
			Log.d(TAG, "Returned File");
			return file; 
		}
		Log.d(TAG, "Returned NULL");
		return null; 
	}

	@SuppressWarnings("deprecation")
	public void AddPhotos(View v){
		AlertDialog alert = new AlertDialog.Builder(CorkBoardUI.this).create();
		alert.setTitle("Photo Options"); 
		alert.setButton("From Gallery", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent gallery = new Intent(Intent.ACTION_GET_CONTENT);
				gallery.setType("image/*");	
			    startActivityForResult(Intent.createChooser(gallery, "Select Picture"), REQUEST_RETRIEVE_MEDIA);

			}
		});

		alert.setButton2("Take Photo", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
												
				Intent intent = new Intent(ACTION_MEDIA_CAPTURE);
				    File photoFile;
				    try
				    {
				        // place where to store camera taken picture
				    	Log.d(TAG,"Creating File");
				        photoFile = createFile();
				        if(photoFile == null){
				        	Log.d(TAG, "Null photoFile");
				        	return;
				        }
				        
				        photoFile.delete();
				    }
				    catch(Exception e)
				    {
				        Log.v(TAG, "Can't create file to take picture!");
				        return;
				    }

				    newImageUri = Uri.fromFile(photoFile);
				    intent.putExtra(MediaStore.EXTRA_OUTPUT, newImageUri);
			    	Log.v(TAG, "SUCCESS CREATING FILE");

			    	try {
						startActivityForResult(intent, REQUEST_CAPTURE_MEDIA);
					} catch (ActivityNotFoundException e) {
						intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
						startActivityForResult(intent, REQUEST_CAPTURE_MEDIA);
					}			    	
			}
		});
		alert.show(); 
	}
	
	public void scaleDownBitmap(Bitmap photo, int newHeight, Context context) {

		 final float densityMultiplier = context.getResources().getDisplayMetrics().density;        

		 int h= (int) (newHeight*densityMultiplier);
		 int w= (int) (h * photo.getWidth()/((double) photo.getHeight()));

		 photo=Bitmap.createScaledBitmap(photo, w, h, true);
		 
		 mImageBitMap = photo; 
	}
	
	
	public void grabImage()
	{
		Bitmap newBitMap = null; 
	    this.getContentResolver().notifyChange(newImageUri, null);
	    ContentResolver cr = this.getContentResolver();
	    try
	    {
	    	newBitMap = android.provider.MediaStore.Images.Media.getBitmap(cr, newImageUri);
	    	scaleDownBitmap(newBitMap, 100, CorkBoardUI.this); 
	    }
	    catch (Exception e)
	    {
	        Toast.makeText(this, "Failed to load", Toast.LENGTH_SHORT).show();
	        Log.d(TAG, "Failed to load", e);
	    }
	}

	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_CAPTURE_MEDIA && resultCode == RESULT_OK) {
			grabImage();
			if(mImageBitMap == null)
			    Log.d(TAG, "Bitmap is null");

	    	new JSONObject();
		    getBytesFromBitmap(mImageBitMap); 
		    
//		    MemObj pictureObj = new MemObj("image", base, byte_arr);		    
//		    
//		    activeFeed.postObj(pictureObj);
		    	
            
            return; 	
		}
	}

	public byte[] getBytesFromBitmap(Bitmap bitmap) {
	    ByteArrayOutputStream stream = new ByteArrayOutputStream();
	    bitmap.compress(CompressFormat.JPEG, 100, stream);
	    return stream.toByteArray();
	}
	
}
