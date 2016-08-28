package com.example.skydata;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;


public class RestClient {
	
	static final String TAG = "[RestClient]";

	/***
	 * For use in converting the result of query upon a REST service
	 * @param InputStream is
	 * @return String conversionOfStream
	 */
	public static String convertStreamToString(InputStream is) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		String line = null;

		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			Log.e(TAG, "Could not read from input", e);
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				Log.e(TAG, "Failed to close inputStream", e);
			}
		}
		return sb.toString();
	}

	/***
	 * Query a generic RESTUrl and receive the anticipated JSON response as a string 
	 * @param url
	 * @return
	 */
	public static String queryRESTurl(String url) {
		String result = null;

		try {
			HttpResponse response = new DefaultHttpClient().execute(new HttpGet(url));
			Log.v(TAG, "Status:[" + response.getStatusLine().toString() + "]");
			HttpEntity entity = response.getEntity();

			if (entity != null) {
				InputStream instream = entity.getContent();
				result = RestClient.convertStreamToString(instream);
				instream.close();
			}
		} catch (ClientProtocolException e) {
			Log.e("REST", "There was a protocol based error", e);
		} catch (IOException e) {
			Log.e("REST", "There was an IO Stream related error", e);
		}

		Log.i(TAG, "Result of query: [" + result + "]");
		return result;
	} 

}
