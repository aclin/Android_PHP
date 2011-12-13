package com.alin.gaetest;

import java.io.DataInputStream;
import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class GAETest extends Activity implements View.OnClickListener {
	
	private static final String TAG = "GAE_Test";
	
	Button btAdd;
	Button btRead;
	//EditText etEntry;
	ListView lvPosts;
	
	ArrayAdapter<InfoEntry> adapter;
    private DefaultHttpClient httpClient = null;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        findViews();
        setListeners();

        adapter = new ArrayAdapter<InfoEntry>( this, R.layout.list_item );
        adapter.setNotifyOnChange( true );
        lvPosts.setAdapter(adapter);
    }
    
    private void findViews() {
    	btAdd = (Button) findViewById(R.id.btAdd);
    	btRead = (Button) findViewById(R.id.btRead);
    	//etEntry = (EditText) findViewById(R.id.etEntry);
    	lvPosts = (ListView) findViewById(R.id.lvPosts);
    }
    
    private void setListeners() {
    	btAdd.setOnClickListener(this);
    	btRead.setOnClickListener(this);
    }
    
    public void onClick(View v) {
    	switch (v.getId()) {
    	case R.id.btAdd:
    		JSONArray array = new JSONArray();
    		try {
    			array.put(new InfoEntry("Fname"+count, "Lname"+count, "Major"+count, "Comment"+count).toJSON());
    		} catch (JSONException e) {
    			Log.e(TAG, "JSONException", e);
    		}
    		String request = array.toString();
            Log.d(TAG, "Request: " + request );
            String response = null;
            try {
            	response = sendToServer(request);
            } catch (IOException e) {
            	Log.e(TAG, "IOException", e);
            }
    		break;
    	case R.id.btRead:
    		break;
    	default:
    		break;
    	}
    }
    
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
    
    private int count = 0;
    
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
}
