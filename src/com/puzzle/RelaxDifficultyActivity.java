package com.puzzle;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;

public class RelaxDifficultyActivity extends Activity {
		
	Level level = new Level();
	
	private int[] mImageIds = {
            R.drawable.ic_relax_3x2,
            R.drawable.ic_relax_3x3,
            R.drawable.ic_relax_4x3,
            R.drawable.ic_relax_4x4,
            R.drawable.ic_relax_5x4
    };	    
	public Uri targetUri;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.relax_difficulty);
		
		CustomGallery gallery = (CustomGallery) findViewById(R.id.gallery);
        //gallery.setPadding(50, 0, 50, 0);
        gallery.setAdapter(new ImageAdapter(this, mImageIds, false));
        targetUri = (Uri) getIntent().getParcelableExtra("targetUri");

        //back button
        Button btnBack = (Button) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	//closes current activity
            	finish();
            }
        });   
        
        gallery.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView parent, View v, int position,
					long id) {

				Intent intent = new Intent(RelaxDifficultyActivity.this,
						GameActivity.class);

				intent.putExtra("targetUri", targetUri);
				intent.putExtra("gameType", 1);
				level.setLevel(position); //minor change
				intent.putExtra("level", level); //minor change
				//CHANGED FROM
				//intent.putextra("level", position -1);
				
				startActivity(intent);
			}
        });
        
	}
}
