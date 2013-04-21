package com.docdevelopers.hangouts;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.MediaController;
import android.widget.VideoView;





public class HangoutActivity extends Activity {
	
	//Variables
	String video = "http://www.docdevelopers.com/stevegreene.mp4";
	public VideoView stream;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hangout);
		//Identify Devices
		VideoView stream = (VideoView) findViewById(R.id.stream);
		MediaController mediaController = new MediaController(this);
		
		//Parsing URL
		Uri src = Uri.parse(video); 
		
		//VideoView
		stream.setVideoURI(src);
		stream.setMediaController(mediaController);
		stream.start();
		
	}
	
	
	/*This are meant to save video progress
	 * Currently they pause the video but do
	 * not resume the video when I go back to
	 * the activity
	 */
	
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
	
	
	
	//Menu
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.hangout, menu);
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item){
		
		switch(item.getItemId()){
		
			case R.id.action_exit:
				finish();
				return true;
		}
		
		return false;
		
	}
}
