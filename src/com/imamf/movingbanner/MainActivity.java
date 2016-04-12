package com.imamf.movingbanner;

/**
 * create by imam ferianto
 * <iferianto@yahoo.com>
 */

import static com.imamf.movingbanner.Konfigurasi.REGISTRATION_URL;

import java.util.ArrayList;
import java.util.HashMap;
import org.json.JSONException;
import org.json.JSONObject;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private SharedPreferences sharedPreferences;
	private static Boolean WAS_REGISTER=false;

    public static final String KEY_DEVICE = "id";
    public static final String KEY_ADDRESS = "address";
    public static final String KEY_PORT = "port";
    public static final String KEY_INTERVAL = "interval";
    public static final String KEY_PROVIDER = "provider";
    public static final String KEY_STATUS = "status";
    
    private static String uniqueId="";
	private static String inputName="";
	private static String inputEmail="";
	private static String inputPhone="";
	private static String inputVHN="";
	private static String inputAddress="";

	EditText ed_inputName;
	EditText ed_inputEmail;
	EditText ed_inputPhone;
	EditText ed_inputVHN;
	EditText ed_inputAddress;
	TextView tvName;
	TextView tvID;
	Button buttonSubmit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
				
		getPreferences();
		if(!WAS_REGISTER){
			setContentView(R.layout.activity_main);
			getInputObject();
			setInputValueFromPreference();
			registrationHandler();
		}else{
			setContentView(R.layout.activity_welcome);
			getViewWelcome();
		}
		
		
	}
	
	void registrationHandler(){
		//add onclick
		buttonSubmit=(Button)findViewById(R.id.buttonSubmit);
		buttonSubmit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub		        
				if(ValidInput()){ 
					savePreverences();
					postAndWait();
				}
			}
			
		});
		
		
	}
	
	//post to server
	void postAndWait(){
		HashMap<String, String> postparam = new HashMap<String, String>();
		postparam.put("inputName",inputName);
		postparam.put("inputEmail", inputEmail);
		postparam.put("inputPhone", inputPhone);
		postparam.put("inputVHN", inputVHN);
		postparam.put("inputAddress", inputAddress);
		PostVars task = new PostVars(this, postparam,  new PostComplete(this) );
		task.titletext = "Posting Data";
		task.execute(REGISTRATION_URL);
	}
	
	//on post complete
	public class PostComplete implements AsyncTaskCompleteListener {
		private Context context;

		public PostComplete(Context c) {
			this.context = c;
		}

		@Override
		public void onTaskCompleted(String result, Boolean error,
			HashMap<String, String> postparam) {
			String status="";
			String message="";			
			Boolean selferror=false;			
			JSONObject jObj = null;
			
			try {
				jObj = new JSONObject(result);
			}catch (JSONException e) {
				selferror = true;
			}

			if(!selferror){
				try {
					status = jObj.getString("status");
				} catch (JSONException e) {
				}				
				
				try {
					message = jObj.getString("message");
				} catch (JSONException e) {
				}
				
				try {
					uniqueId = jObj.getString("uniqueId");
				} catch (JSONException e) {
				}				
			}

			if(status.length()>0) {
				if(status.equals("OK") && uniqueId.length()>0){
					
					//save new generated device from server
					WAS_REGISTER=true;
					savePreverences();
					
					//show alert
					showAlert("Registration Success!",true);
				}
				else  showAlert("Registration Failed, Please Try again\n"+message,false);
			}else if(selferror){
			    showAlert("There was error on posting method or error parsing json",false);				
			}else{
				showAlert("Posting Failed with no message",false);				
			}
		}

	}

	
	
	
	//get input object
	void getInputObject(){
		ed_inputName=(EditText)findViewById(R.id.inputName);
		ed_inputEmail=(EditText)findViewById(R.id.inputEmail);
		ed_inputPhone=(EditText)findViewById(R.id.inputPhone);
		ed_inputVHN=(EditText)findViewById(R.id.inputVHN);
		ed_inputAddress=(EditText)findViewById(R.id.inputAddress);
	}

	//set inputval
	void setInputValueFromPreference(){
		ed_inputName.setText(inputName);
		ed_inputEmail.setText(inputEmail);
		ed_inputPhone.setText(inputPhone);
		ed_inputVHN.setText(inputVHN);
		ed_inputAddress.setText(inputAddress);		
	}

	void getViewWelcome(){
		tvName=(TextView)findViewById(R.id.tvName);
		tvID=(TextView)findViewById(R.id.tvID);
		tvName.setText("Welcome : "+inputName);
		tvID.setText("YourID : "+uniqueId);
	}
	
	//check and validate input
	private boolean ValidInput(){
		boolean isvalid=false;
		
		//GET INPUT
		inputName=ed_inputName.getText().toString().trim();
		if(inputName.length()>0) isvalid=true;
		else isvalid=false;
						
		inputEmail=ed_inputEmail.getText().toString().trim();
		if(inputEmail.length()>0) isvalid=isvalid&true;
		else isvalid=false;

		inputPhone=ed_inputPhone.getText().toString().trim();
		if(inputPhone.length()>0) isvalid=isvalid&true;
		else isvalid=false;
		
		inputVHN=ed_inputVHN.getText().toString().trim();
		if(inputVHN.length()>0) isvalid=isvalid&true;
		else isvalid=false;
		
		inputAddress=ed_inputAddress.getText().toString().trim();
		if(inputAddress.length()>0) isvalid=isvalid&true;
		else isvalid=false;
		
		if(!isvalid){
			showAlert("There was an empty input\nPlease check your type",false);
		}
		return isvalid;
	}
	
	//show alert message
	void showAlert(String msg,final boolean setExit){
	new AlertDialog.Builder(this)
	.setMessage(msg)
	.setPositiveButton("OK",
			new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,	int id) {
					// Do something here
					if(setExit) finish();
				}
			}).create().show();
	}

	
	//get user preverences
	private void getPreferences(){
		sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);		
		WAS_REGISTER=sharedPreferences.getBoolean("WAS_REGISTER",false);
		uniqueId=sharedPreferences.getString(KEY_DEVICE,"");
		inputName=sharedPreferences.getString("inputName","").trim();
		inputEmail=sharedPreferences.getString("inputEmail","").trim();
		inputPhone=sharedPreferences.getString("inputPhone","").trim();
		inputVHN=sharedPreferences.getString("inputVHN","").trim();
		inputAddress=sharedPreferences.getString("inputAddress","").trim();
	}
		
	//save registration state
	private void savePreverences(){
		Editor ed = sharedPreferences.edit();
		ed.putBoolean("WAS_REGISTER",WAS_REGISTER);
		ed.putString(KEY_DEVICE,uniqueId);
		ed.putString("inputName",inputName);
		ed.putString("inputEmail",inputEmail);
		ed.putString("inputPhone",inputPhone);
		ed.putString("inputVHN",inputVHN);
		ed.putString("inputAddress",inputAddress);
		ed.commit();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
		//return true;
	}
	
    private void addShortcuts(boolean start, int name) {
        Intent shortcutIntent = new Intent(Intent.ACTION_MAIN);
        shortcutIntent.setComponent(new ComponentName(getPackageName(), ".ShortcutActivity"));
        shortcutIntent.putExtra(ShortcutActivity.EXTRA_ACTION, start);

        Intent installShortCutIntent = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
        installShortCutIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
        installShortCutIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, getString(name));
        installShortCutIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
                Intent.ShortcutIconResource.fromContext(this, R.drawable.ic_launcher));

        sendBroadcast(installShortCutIntent);
    }	

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		/*
		int id = item.getItemId();		
		if (id == R.id.status) {
			return true;
		}
		return super.onOptionsItemSelected(item);
		*/
		
		   if (item.getItemId() == R.id.status) {
	            startActivity(new Intent(this, StatusActivity.class));
	            return true;
	        } else if (item.getItemId() == R.id.shortcuts) {
	            addShortcuts(true, R.string.shortcut_start);
	            addShortcuts(false, R.string.shortcut_stop);
	            return true;
	        } else if (item.getItemId() == R.id.about) {
	            startActivity(new Intent(this, AboutActivity.class));
	            return true;
	        }
	        return super.onOptionsItemSelected(item);		
	}
}
