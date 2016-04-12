package com.imamf.movingbanner;
/**
 * create by imam ferianto
 * <iferianto@yahoo.com>
 */

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import static com.imamf.movingbanner.Konfigurasi.MYTAG;
import static com.imamf.movingbanner.Konfigurasi.ENABLE_DEBUG;
import static com.imamf.movingbanner.Konfigurasi.REGISTRATION_TIMEOUT;
import static com.imamf.movingbanner.Konfigurasi.WAIT_TIMEOUT;

public class PostVars extends AsyncTask<String, Void, String> {

	private final HttpClient httpclient = new DefaultHttpClient();
	final HttpParams params = httpclient.getParams();
	HttpResponse response;
	private String content = null;
	private boolean error = false;
	private ProgressDialog pd=null;
	private Context context=null;
	private JSONObject jObj=null;
    private AsyncTaskCompleteListener listener;
    private HashMap<String, String> postparam=new  HashMap<String, String>();
	public String titletext="Processing";

	public PostVars(Context c,HashMap<String, String> m1,AsyncTaskCompleteListener onTaskCompleted) {
		this.context = c;
		this.listener=onTaskCompleted;
		this.postparam=m1;
	}

	@Override
	protected void onPostExecute(String result) {
		 //called on ui thread
        if (this.pd != null) {
            this.pd.dismiss();
        }
        
        if(listener!=null){
        	listener.onTaskCompleted(result,this.error,this.postparam);
        }
	}

	private String typeof(AsyncTaskCompleteListener listener2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void onPreExecute() {
		   this.pd = new ProgressDialog(this.context);
		   this.pd.setTitle(titletext);
           this.pd.setMessage("Please wait...");
           this.pd.setCancelable(true);
           this.pd.setOnCancelListener(new DialogInterface.OnCancelListener()
           {
               @Override
               public void onCancel(DialogInterface dialog)
               {
                   // cancel AsyncTask
                   cancel(false);
               }
           });

           this.pd.show();		
	}

	
	
	@Override
	protected String doInBackground(String... urls) {
		String URL = null;
		
		try {

			String srcurl= urls[0];
			int posx=srcurl.length();
			
			
			if(ENABLE_DEBUG) Log.d(MYTAG, "scrurl="+ srcurl);
			

			// add name value pair for the country code
			List<NameValuePair> httpparams = new ArrayList<NameValuePair>();
			
			//ambil variabel post pada url query string
			if(srcurl.indexOf("?")!=-1){
				posx=srcurl.indexOf("?");
				String sparam=srcurl.substring(posx+1,srcurl.length());
				if(ENABLE_DEBUG) Log.d(MYTAG, "param = " + sparam);				
				String[] pairs = sparam.split("&");
				 for (String pair : pairs) {
				    int idx =pair.indexOf("=");
				    String varname=pair.substring(0, idx);
				    String varval=pair.substring(idx + 1);
				    if(ENABLE_DEBUG) Log.d(MYTAG,varname+"=" +varval);
					httpparams.add(new BasicNameValuePair(URLDecoder.decode(varname, "UTF-8"),URLDecoder.decode(varval, "UTF-8")));
				 }
			}
			//ambil variabel post yang diset pada m
			for(Map.Entry<String,String> entry:postparam.entrySet()){
				if(ENABLE_DEBUG) Log.d(MYTAG, entry.getKey()+" = " + entry.getValue().trim());
				httpparams.add(new BasicNameValuePair(entry.getKey(),entry.getValue().trim()));
			}
			
			
			URL = srcurl.substring(0,posx);
			if(ENABLE_DEBUG) Log.d(MYTAG, "url = " + URL);
			
			int start = 0;
			int limit = 9999;

			HttpConnectionParams.setConnectionTimeout(params,REGISTRATION_TIMEOUT);
			HttpConnectionParams.setSoTimeout(params, WAIT_TIMEOUT);
			ConnManagerParams.setTimeout(params, WAIT_TIMEOUT);

			HttpPost httpPost = new HttpPost(URL);
			
			if(ENABLE_DEBUG) Log.d(MYTAG, "after...");
	
			
			//tambahkan variabel post lokasi
			httpPost.setEntity(new UrlEncodedFormEntity(httpparams));
			response = httpclient.execute(httpPost);

			StatusLine statusLine = response.getStatusLine();
			if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				response.getEntity().writeTo(out);
				out.close();
				content = out.toString();
			} else {
				// Closes the connection.
				Log.w("HTTP1:", statusLine.getReasonPhrase());
				response.getEntity().getContent().close();
				throw new IOException(statusLine.getReasonPhrase());
			}
		} catch (ClientProtocolException e) {
			Log.w("HTTP2:", e);
			content = e.getMessage();
			error = true;
			//cancel(true);
		} catch (IOException e) {
			Log.w("HTTP3:", e);
			content = e.getMessage();
			error = true;
			//cancel(true);
		} catch (Exception e) {
			Log.w("HTTP4:", e);
			content = e.getMessage();
			error = true;
			//cancel(true);
		}
		
		if(ENABLE_DEBUG) Log.d(MYTAG, "content = " + content);		
		return content;

	}
}