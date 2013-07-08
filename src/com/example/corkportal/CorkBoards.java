package com.example.corkportal;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class CorkBoards extends Activity {
	
	private ListView cb_list; 
	private EditText corkboard_name; 	
	private CorkboardHelper dbCorkboardHelper = null;
	private CorkboardAdapter cbAdapter = null;
	private Cursor dbCursor = null;
	private String TAG = "Corkboard_Activity";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	    try{
			setContentView(R.layout.corkboard_activity);

			
			cb_list = (ListView)findViewById(R.id.cb_list);
		
			cb_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			    public void onItemClick(AdapterView parent, View v, int position, long id){		
//			    	Cursor c = dbChannelHelper.getCorkboard(id);
//			    	String rowId = c.getString(0);
//			    	String retrieved_uri = c.getString(1);
			    	Log.d(TAG, "CLICKED");
			    	Intent intent = new Intent(v.getContext(), MainActivity.class);
					startActivityForResult(intent,0);
			    	
//			    	Intent intent = new Intent(v.getContext(), ChannelUI.class);
//			    	intent.putExtra("feedURI", retrieved_uri);
//			    	intent.putExtra("rowId", rowId);
//					startActivityForResult(intent,0);
			    }
			});
			
			corkboard_name = (EditText)findViewById(R.id.corkboard_name);
			
			
			/*Load Database of Corkboards */
			dbCorkboardHelper = new CorkboardHelper(this);
			dbCursor = dbCorkboardHelper.getAll(); 
			startManagingCursor(dbCursor); 
			cbAdapter = new CorkboardAdapter(dbCursor); 
			cb_list.setAdapter(cbAdapter);
			
		} catch (Exception e) {
			Log.e("ERROR", "ERROR AT: " + e.toString()); 
			e.printStackTrace(); 
		}
	}
	

	@Override
	public void onConfigurationChanged(Configuration newConfig)
	{
		super.onConfigurationChanged(newConfig);
	    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
		dbCorkboardHelper.close(); 
	}
	
	@SuppressWarnings("deprecation")
	public void addCorkboard (View v){
		Log.d(TAG, "Adding new CorkBoard");
		try {	
//			if(corkboard_name.getText().length() != 0){
//				Intent intent = new Intent(ACTION_CREATE_FEED);
//				startActivityForResult(intent, REQUEST_CREATE_FEED);
				dbCorkboardHelper.insert("CORKBOARD1", "data");//feedUri.toString());			
				dbCursor.requery();			
				corkboard_name.setText("");
//			}

			
		} catch (Exception e) {
			Log.e("ERROR", "ERROR AT: " + e.toString()); 
			e.printStackTrace(); 
		}	
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
//		if (requestCode == REQUEST_CREATE_FEED && resultCode == RESULT_OK) {
//			if (data == null || data.getData() == null) {
//				return;
//			}
//			
//			feedUri = data.getData();
//			Log.d(TAG, "feedUri: " + feedUri.toString());
//			
//			Musubi musubi = Musubi.getInstance(this);
//			
//			DbFeed feed = musubi.getFeed(feedUri);
//			checkForAstros(feed);
//			feedText = feed.toString();
//			
//			dbChannelHelper.insert(channel_name.getText().toString(), feedUri.toString());			
//			dbCursor.requery();
//			
//			channel_name.setText("");
//		}
	}
	
	class CorkboardAdapter extends CursorAdapter { 
		CorkboardAdapter(Cursor c){
			super(CorkBoards.this,c);
		}

		@Override
		public void bindView(View row, Context ctxt, Cursor c) {
			CorkboardHolder_C chHolder = (CorkboardHolder_C)row.getTag();
			chHolder.populateFrom(c, dbCorkboardHelper); 
		}

		@Override
		public View newView(Context ctxt, Cursor c, ViewGroup parent) {
			LayoutInflater inflater = getLayoutInflater(); 
			View row = inflater.inflate(R.layout.cb_item, parent, false);
			CorkboardHolder_C holder = new CorkboardHolder_C(row); 
			row.setTag(holder); 
			return(row);
		}		
	}
	
	class CorkboardHolder_C{
		public TextView cb_name = null, cb_info = null;
		
		CorkboardHolder_C(View row){
		 cb_name = (TextView) row.findViewById(R.id.corkboard_name);
		 cb_info = (TextView) row.findViewById(R.id.corkboard_sub_info);
		}
		
		void populateFrom(Cursor c, CorkboardHelper r){
			cb_name.setText(c.getString(c.getColumnIndexOrThrow("corkboard")));
			cb_info.setText(c.getString(c.getColumnIndexOrThrow("cb_info")));
		}
	}

}
