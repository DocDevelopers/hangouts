package com.docdevelopers.hangouts;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;






import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;



import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.VideoView;
import android.widget.AdapterView.OnItemClickListener;





public class HangoutActivity extends Activity implements OnItemClickListener {
	
	//Variables
	String video = "http://www.docdevelopers.com/tutorial.mp4";
	
	public VideoView stream;
	
	protected static final int DIALOG_KEY = 0;
    ListView mListView;
    Button mClear;
    Button mRefresh1;
    Button mRefresh2;
    ProgressDialog mProgressDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hangout);
		//Identify Devices
		//VideoView stream = (VideoView) findViewById(R.id.stream);
		//MediaController mediaController = new MediaController(this);
		
		
		WebView w = (WebView)findViewById(R.id.webView1);
	 	w.setWebViewClient(new WebViewClient());
	 	
	 	w.getSettings().setJavaScriptEnabled(true);
	 	w.setVerticalScrollBarEnabled(false);
	 	w.setHorizontalScrollBarEnabled(false);

	String customHtml = "<html><body leftmargin=\"0\" topmargin=\"0\" rightmargin=\"0\" bottommargin=\"0\"><iframe width=\"100%\" height=\"100%\" src=\"http://www.youtube.com/embed/mOnrkfA6Vw0\" frameborder=\"0\" allowfullscreen></iframe></body></html>";
	w.setWebChromeClient(new WebChromeClient() {
	});
	w.loadData(customHtml, "text/html", "UTF-8");
		//Parsing URL
		//Uri src = Uri.parse(video); 
		 mListView = (ListView) findViewById(R.id.listView1);
		 mListView.setTextFilterEnabled(true);
		  LoadRecipesTask1 mLoadRecipesTask = new LoadRecipesTask1();
          mLoadRecipesTask.execute("http://gdata.youtube.com/feeds/api/videos/mOnrkfA6Vw0/comments");
		
		//VideoView
		//stream.setVideoURI(src);
		//stream.setMediaController(mediaController);
		//stream.start();
		
	}
	
	
	/*This are meant to save video progress
	 * Currently they pause the video but do
	 * not resume the video when I go back to
	 * the activity
	 */
	

	
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
	
	
	
	//Start comments
	
	protected Dialog onCreateDialog(int id) {
        switch (id) {
        case DIALOG_KEY:                                                               // 1
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);         // 2
            mProgressDialog.setMessage("Retrieving recipes...");                       // 3
            mProgressDialog.setCancelable(false);                                      // 4
            return mProgressDialog;
        }
        return null;
    }

    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Datum datum = (Datum) mListView.getItemAtPosition(position);
        Uri uri = Uri.parse("http://androidcookbook.com/Recipe.seam?recipeId=" + datum.getAuthor());
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        this.startActivity(intent);
    }

    public static ArrayList<Datum> parse(String url) throws IOException, XmlPullParserException {
        final ArrayList<Datum> results = new ArrayList<Datum>();

        URL input = new URL(url);

        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(true);
        XmlPullParser xpp = factory.newPullParser();

        xpp.setInput(input.openStream(), null);
        int eventType = xpp.getEventType();
        String currentTag = null;
        String author = null;
        String title = null;
        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_TAG) {
                currentTag = xpp.getName();
            } else if (eventType == XmlPullParser.TEXT) {
                if ("author".equals(currentTag)) {
                    author = xpp.getText();
                }
                if ("content".equals(currentTag)) {
                    title = xpp.getText();
                }
            } else if (eventType == XmlPullParser.END_TAG) {
                if ("entry".equals(xpp.getName())) {
                    results.add(new Datum(author, title));
                }
            }
            eventType = xpp.next();
        }
        return results;
    }

    public static ArrayList<Datum> parse2(String url) throws IOException, XmlPullParserException {
        final ArrayList<Datum> results = new ArrayList<Datum>();

        URL input = new URL(url);

        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(true);
        XmlPullParser xpp = factory.newPullParser();

        xpp.setInput(input.openStream(), null);
        xpp.nextTag();
        xpp.require(XmlPullParser.START_TAG, null, "feed");
        while (xpp.nextTag() == XmlPullParser.START_TAG) {
            xpp.require(XmlPullParser.START_TAG, null, "entry");

            xpp.nextTag();
            xpp.require(XmlPullParser.START_TAG, null, "author");
            String author = xpp.nextText();
            xpp.require(XmlPullParser.END_TAG, null, "author");

            xpp.nextTag();
            xpp.require(XmlPullParser.START_TAG, null, "content");
            String title = xpp.nextText();
            xpp.require(XmlPullParser.END_TAG, null, "content");

            xpp.nextTag();
            xpp.require(XmlPullParser.END_TAG, null, "entry");

            results.add(new Datum( title, author));
        }
        xpp.require(XmlPullParser.END_TAG, null, "feed");

        return results;
    }

    protected class LoadRecipesTask1 extends AsyncTask<String, Void, ArrayList<Datum>> {

        @Override
        protected void onPreExecute() {
            HangoutActivity.this.setProgressBarIndeterminateVisibility(true);
        }

        @Override
        protected ArrayList<Datum> doInBackground(String... urls) {
            ArrayList<Datum> datumList = new ArrayList<Datum>();
            try {
                datumList = parse(urls[0]);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }
            return datumList;
        }

        @Override
        protected void onPostExecute(ArrayList<Datum> result) {
            mListView.setAdapter(new ArrayAdapter<Datum>(HangoutActivity.this, R.layout.list_item, result));
            HangoutActivity.this.setProgressBarIndeterminateVisibility(false);
        }
    }

    protected class LoadRecipesTask2 extends AsyncTask<String, Integer, ArrayList<Datum>> {
        
        @Override
        protected void onPreExecute() {
            mProgressDialog.show();                                                          // 1
        }
        
        @Override
        protected ArrayList<Datum> doInBackground(String... urls) {
            ArrayList<Datum> datumList = new ArrayList<Datum>();
            for (int i = 0; i < urls.length; i++) {                                          // 2
                try {
                    datumList = parse(urls[i]);
                    publishProgress((int) (((i+1) / (float) urls.length) * 100));            // 3
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                }
            }
            return datumList;
        }
        
        @Override
        protected void onProgressUpdate(Integer... values) {                                 // 4
            mProgressDialog.setProgress(values[0]);                                          // 5
        }
        
        @Override
        protected void onPostExecute(ArrayList<Datum> result) {
            mListView.setAdapter(new ArrayAdapter<Datum>(HangoutActivity.this, R.layout.list_item, result));
            mProgressDialog.dismiss();                                                       // 6
        }
	
	
	
	
    }
}
