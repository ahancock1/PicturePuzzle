package com.puzzle;

import java.io.FileNotFoundException;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

public class RelaxImageActivity extends Activity {


	public boolean imageSelected = false;
	
	
	public static final int IMAGEREQUESTCODE = 8242008;
	public static final int DIALOG_PICASA_ERROR_ID = 0;
	public static final int DIALOG_GRID_SIZE_ID = 1;
	public static final int DIALOG_COMPLETED_ID = 2;
	
	
	public Bitmap bitmap;
	public Uri targetUri;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.relax_image);

		//make image fit image view
		ImageView imageView = (ImageView) findViewById(R.id.chosenPicture);
		imageView.setScaleType(ScaleType.FIT_XY);

		Button btnSelect = (Button) findViewById(R.id.btnSelectPic);
		Button btnTake = (Button) findViewById(R.id.btnTakePic);

		btnSelect.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				//choose picture from phone gallery
				selectImageFromGallery();
			}
		});   
		
		imageView.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				//use image selected from gallery
				if (imageSelected) {
					//load difficulty screen
					Intent intent = new Intent(RelaxImageActivity.this, RelaxDifficultyActivity.class);
					intent.putExtra("targetUri", targetUri);
	                startActivity(intent);
				}
			}
		});   
		
		
	}    

	/* (non-Javadoc)
	 * Will start an intent for external Gallery app.  
	 * Image returned via onActivityResult().
	 */
	private void selectImageFromGallery() {
		Intent galleryIntent = new Intent(Intent.ACTION_PICK, 
				MediaStore.Images.Media.INTERNAL_CONTENT_URI);
		startActivityForResult(galleryIntent, 8242008);
	}

	@Override
	protected final void onActivityResult(final int requestCode, 
			final int resultCode, 
			final Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);		

	    
		ImageView imageView = (ImageView) findViewById(R.id.chosenPicture);

	    if (resultCode == RESULT_OK){
	        targetUri = data.getData();
	        try {
	            bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUri));
	            imageView.setImageBitmap(bitmap);
	    		imageView.setScaleType(ScaleType.FIT_XY);
	    		imageSelected = true;
	    		
	        } catch (FileNotFoundException e) {
	            e.printStackTrace();
	        }
	    }
	}

}



