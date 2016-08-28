package airportweather;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import com.example.skydata.RestClient;


public class WeatherJSONParser {

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
				Log.i("TAG", "Result of converstion: [" + result + "]");
				instream.close();

				JSONObject metarObject;
				JSONObject taforObject;
				JSONObject appendixObject;

				try {
					jObj = new JSONObject(result);
					metarObject = jObj.getJSONObject("metar");
					taforObject = jObj.getJSONObject("taf");
					appendixObject = jObj.getJSONObject("appendix");
					try{
						AirportWether.airportReportTime = metarObject.getString("reportTime");
					}catch(JSONException e){}
					try{
						AirportWether.airportTemp = metarObject.getString("temperatureCelsius");
					}catch(JSONException e){}
					try{
						AirportWether.airportPress = metarObject.getJSONObject("conditions").getString("pressureInchesHg");
					}catch(JSONException e){}
					try{
						AirportWether.airportVisMiles = metarObject.getJSONObject("conditions").getJSONObject("visibility").getString("miles");
					}catch(JSONException e){}
					try{
						AirportWether.airportWindSpeed = metarObject.getJSONObject("conditions").getJSONObject("wind").getString("speedKnots");
					}catch(JSONException e){}
					try{
						AirportWether.airportWindGust = metarObject.getJSONObject("conditions").getJSONObject("wind").getString("gustSpeedKnots");
					}catch(JSONException e){}
					try{
						AirportWether.airportWindDir = metarObject.getJSONObject("conditions").getJSONObject("wind").getString("direction");
					}catch(JSONException e){}
					try{
						AirportWether.airportDewPoint = metarObject.getString("dewPointCelsius");
					}catch(JSONException e){}
					try{
						AirportWether.airportName = appendixObject.getJSONArray("airports").getJSONObject(0).getString("name");	
					}catch(JSONException e){}
					try{
						AirportWether.airportICAO = appendixObject.getJSONArray("airports").getJSONObject(0).getString("icao");
					}catch(JSONException e){}
					try{
						AirportWether.taf = taforObject.getString("report");
					}catch(JSONException e){}
					try{
						AirportWether.airportCloudsFirst = metarObject.getJSONObject("conditions").getJSONArray("skyConditions").getJSONObject(0).getString("coverage");
						AirportWether.airportCloudBaseFirst = metarObject.getJSONObject("conditions").getJSONArray("skyConditions").getJSONObject(0).getString("base");		
					}catch(JSONException e){}
					try{
						AirportWether.airportCloudsSecond = metarObject.getJSONObject("conditions").getJSONArray("skyConditions").getJSONObject(1).getString("coverage");
						AirportWether.airportCloudBaseSecond = metarObject.getJSONObject("conditions").getJSONArray("skyConditions").getJSONObject(1).getString("base");
					}catch(JSONException e){}
					try{
						AirportWether.airportObscuration = metarObject.getJSONArray("obscurations").getJSONObject(0).getString("phenomenon");
					}catch(JSONException e){}


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

		return null;
	}

}
