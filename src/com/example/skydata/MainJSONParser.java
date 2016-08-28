package com.example.skydata;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import airportweather.AirportWether;
import android.util.Log;

public class MainJSONParser{
	final String TAG = "JsonParser.java";

	static InputStream is = null;
	static JSONObject jObj = null;
	static String json = "";

	@SuppressWarnings("unused")
	public static JSONObject queryRESTurl(String url) {
		HttpClient httpclient = new DefaultHttpClient();
		HttpGet httpget = new HttpGet(url);
		HttpResponse response;

		try {
			response = httpclient.execute(httpget);
			Log.i("TAG", "Status:[" + response.getStatusLine().toString() + "]");
			HttpEntity entity = null;
			try{
				entity = response.getEntity();
			}catch(Exception e){
				Log.e("log_tag", "Error in http connection "+e.toString());
			}
			if (entity != null) {

				InputStream instream = entity.getContent();
				String result = RestClient.convertStreamToString(instream);
				//Log.i("TAG", "Result of converstion: [" + result + "]");
				instream.close();

				try {
					jObj = new JSONObject(result);

				} catch (JSONException e) {
					Log.e("JSON Parser", "Error parsing data " + e.toString());
				}
				return jObj;

			}
		} catch (ClientProtocolException e) {
			Log.e("REST", "There was a protocol based error", e);
		} catch (IOException e) {
			Log.e("REST", "There was an IO Stream related error", e);
		}

		return jObj;
	}
}
