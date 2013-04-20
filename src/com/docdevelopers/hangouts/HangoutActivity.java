package com.docdevelopers.hangouts;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.MediaController;
import android.widget.VideoView;

public class HangoutActivity extends Activity {
	String video = "http://www.docdevelopers.com/stevegreene.mp4";
	public VideoView stream;
	@Override
	
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hangout);
		VideoView stream = (VideoView) findViewById(R.id.stream);
		MediaController mediaController = new MediaController(this);
		Uri src = Uri.parse(video); 
		
		
		stream.setVideoURI(src);
		stream.setMediaController(mediaController);
		stream.start();
		
		
		
		
		
		
		
	}
	
	
	
	
	
	
	public void onDestroy(){
		super.onDestroy();
		VideoView stream = (VideoView) findViewById(R.id.stream);
		stream.pause();
		
	}
	
	public void onStart(){
		super.onStart();
		VideoView stream = (VideoView) findViewById(R.id.stream);
		stream.resume();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.hangout, menu);
		return true;
	}

}
