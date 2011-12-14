package com.alin.gaetest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class GAETest extends Activity implements View.OnClickListener {
	
	private static final String TAG = "GAE_Test";
	
	private static final int POST_ADD = 0;
	private static final int POST_READ = 1;
	private static final int POST_TOP5 = 2;
	
	private int count = 2;
	private String First = "Fname";
	private String Last = "Lname";
	private String Major = "Major";
	private String Comment = "Comment";
	
	Button btAdd;
	Button btRead;
	//EditText etEntry;
	//ListView lvPosts;
	TextView tv;
	String text;
	
	//ArrayAdapter<InfoEntry> adapter;
    //private DefaultHttpClient httpClient = null;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        findViews();
        setListeners();

        /*adapter = new ArrayAdapter<InfoEntry>( this, R.layout.list_item );
        adapter.setNotifyOnChange( true );
        lvPosts.setAdapter(adapter);*/
    }
    
    private void findViews() {
    	btAdd = (Button) findViewById(R.id.btAdd);
    	btRead = (Button) findViewById(R.id.btRead);
    	tv = (TextView) findViewById(R.id.tvPost);
    	//etEntry = (EditText) findViewById(R.id.etEntry);
    	//lvPosts = (ListView) findViewById(R.id.lvPosts);
    }
    
    private void setListeners() {
    	btAdd.setOnClickListener(this);
    	btRead.setOnClickListener(this);
    }
    
    public void onClick(View v) {
    	switch (v.getId()) {
    	case R.id.btAdd:
    		try {
    			postData(POST_ADD);
    		} catch (JSONException e) {
    			e.printStackTrace();
    		}
    		break;
    	case R.id.btRead:
    		try {
    			postData(POST_READ);
    		} catch (JSONException e) {
    			e.printStackTrace();
    		}
    		break;
    	default:
    		break;
    	}
    }
    
    private void postData(int action) throws JSONException {
    	// Create a new HttpClient and Post Header
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost("http://ee.hac.tw:2323/insertdb.php");
		JSONObject j = new JSONObject();
		
		try {
			switch (action) {
			case POST_ADD:
				//JSON data
				j.put("Command", "New");
				j.put("Firstname", First+count);
				j.put("Lastname", Last+count);
				j.put("Major", Major+count);
				j.put("Comment", Comment+count);
				break;
			case POST_READ:
				j.put("Command", "Read");
				break;
			}
			
			JSONArray array = new JSONArray();
			array.put(j);
			
			// Post the data:
			httppost.setHeader("json", j.toString());
			httppost.getParams().setParameter("jsonpost", array);

			// Execute HTTP Post Request
			HttpResponse response = httpclient.execute(httppost);
			
			// for JSON:
			if(response != null)
			{
				InputStream is = response.getEntity().getContent();

				BufferedReader reader = new BufferedReader(new InputStreamReader(is));
				StringBuilder sb = new StringBuilder();

				String line = null;
				try {
					while ((line = reader.readLine()) != null) {
						sb.append(line + "\n");
					}
				} catch (IOException e) {
					Log.e(TAG, e.toString());
					e.printStackTrace();
				} finally {
					try {
						is.close();
					} catch (IOException e) {
						Log.e(TAG, e.toString());
						e.printStackTrace();
					}
				}
				text = sb.toString();
			}
			
			tv.setText(text);
			
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			Log.e(TAG, e.toString());
			e.printStackTrace();
		} catch (IOException e) {
    		// TODO Auto-generated catch block
			Log.e(TAG, e.toString());
			e.printStackTrace();
		}
    }
    
    /*
    private String sendToServer( String request ) throws IOException {
        String result = null;
        createHttpClient();
        HttpPost post = new HttpPost( "http://ee.hac.tw:2323/" );
        post.addHeader( "Content-Type", "text/vnd.aexp.json.req" );
        post.setEntity( new StringEntity( request ) );    
        HttpResponse resp = httpClient.execute( post );
// Execute the POST transaction and read the results
        int status = resp.getStatusLine().getStatusCode(); 
        if( status != HttpStatus.SC_OK )
                throw new IOException( "HTTP status: "+Integer.toString( status ) );
        DataInputStream is = new DataInputStream( resp.getEntity().getContent() );
        result = is.readLine();
        return result;
    }
    
    private void createHttpClient() {
    	
    }
    */
    
    /*
    private class InfoEntry {
    	
    	private final String KEY_F = "Firstname";
    	private final String KEY_L = "Lastname";
    	private final String KEY_M = "Major";
    	private final String KEY_C = "Comment";
    	
    	private String fname;
    	private String lname;
    	private String major;
    	private String comment;
    	
    	private InfoEntry(String f, String l, String m, String c) {
    		fname = f;
    		lname = l;
    		major = m;
    		comment = c;
    	}
    	
    	private JSONObject toJSON() throws JSONException {
    		JSONObject j = new JSONObject();
    		j.put(KEY_F, fname);
    		j.put(KEY_L, lname);
    		j.put(KEY_M, major);
    		j.put(KEY_C, comment);
    		return j;
    	}
    }
    */
}
