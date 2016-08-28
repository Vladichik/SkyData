package airportweather;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.skydata.MainActivity;
import com.example.skydata.OnSwipeTouchListener;
import com.example.skydata.R;
import com.flurry.android.FlurryAgent;


import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;



@SuppressLint("SetJavaScriptEnabled") public class AirportWether{
	TextView airportNameFiled, airportIcaoName, reportTime, weatherTemp, weatherHumid, weatherClouds, weatherPress, weatherVis, weatherWind, weatherDew, weatherObscuration, tafReport;
	WebView weatherMap;
	public static EditText icaoField;
	Button showWeather;
	MainActivity mainact;
	public static String airportTypedIcao, airportName, airportICAO, airportReportTime, airportTemp, airportTempF, airportHumidity, airportCloudsFirst, airportCloudsSecond, 
	airportCloudBaseFirst, airportCloudBaseSecond, airportPress, airportMilibar, airportVisMiles, airportVisKm, airportWindSpeed, airportWindDir, airportWindGust, airportDewPoint,
	airportObscuration, taf;
	boolean menutriger = false;
	ObjectAnimator retract, extend;


	@SuppressWarnings("static-access")
	public <a> void getData(View v, final Activity a) {
		mainact = (MainActivity) a;
		airportNameFiled = (TextView) v.findViewById(R.id.airportName);
		airportIcaoName = (TextView) v.findViewById(R.id.airportICAO);
		reportTime = (TextView) v.findViewById(R.id.reportTime);
		weatherTemp = (TextView) v.findViewById(R.id.weatherTemp);
		weatherHumid = (TextView) v.findViewById(R.id.weatherHumid);
		weatherClouds = (TextView) v.findViewById(R.id.weatherClouds);
		weatherPress = (TextView) v.findViewById(R.id.weatherPress);
		weatherVis = (TextView) v.findViewById(R.id.weatherVis);
		weatherWind = (TextView) v.findViewById(R.id.weatherWind);
		weatherDew = (TextView) v.findViewById(R.id.weatherDew);
		tafReport = (TextView) v.findViewById(R.id.tafReport);
		weatherObscuration = (TextView) v.findViewById(R.id.weatherObscuration);
		icaoField = (EditText) v.findViewById(R.id.weatherICAOInput);
		showWeather = (Button) v.findViewById(R.id.showWeatherData);

		showWeather.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(icaoField.length() > 0){
					airportTypedIcao = icaoField.getText().toString();

					//Remove Keyboard on button click
					InputMethodManager imm = (InputMethodManager) a.getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(icaoField.getWindowToken(), 0);
					//Toast.makeText(v.getContext(), airportICAO, 500).show();

					resetStrings(); 
					new AsyncTaskParseJson().execute();	
				}
			}
		});

		if(mainact.iAmOnTablet == true){
			weatherMap = (WebView)v.findViewById(R.id.weatherwebview);
			weatherMap.getSettings().setJavaScriptEnabled(true);
			weatherMap.loadUrl("http://openweathermap.org/help/tiles.html?opacity=0.7&l=clouds");

			Button loadCloudsMap = (Button)v.findViewById(R.id.mapTypeClouds);
			Button loadPrecipitationsMap = (Button)v.findViewById(R.id.mapTypePrecipitation);
			Button loadPressuureMap = (Button)v.findViewById(R.id.mapTypePressure);
			Button loadWindMap = (Button)v.findViewById(R.id.mapTypeWind);
			Button loadSnowMap = (Button)v.findViewById(R.id.mapTypeSnow);

			Display display = mainact.getWindowManager().getDefaultDisplay();
			Point size = new Point();
			display.getSize(size);
			float displayHeight = size.y;
			final LinearLayout containerLayout = (LinearLayout)v.findViewById(R.id.map_menu);

			retract = ObjectAnimator.ofFloat(containerLayout, "translationY", 0, displayHeight - (displayHeight - 150));
			extend = ObjectAnimator.ofFloat(containerLayout, "translationY", displayHeight - (displayHeight - 150), 0);
			retract.setDuration(400);
			extend.setDuration(400);


			containerLayout.setOnTouchListener(new OnSwipeTouchListener(this){
				@Override
				public void onSwipeUp() {
					if(menutriger){
						extend.start();
						menutriger = false;
					}
				}
				@Override
				public void onSwipeDown() {
					if(!menutriger){
						retract.start();
						menutriger = true;
					}
				}

			});

			loadCloudsMap.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					String url = "http://openweathermap.org/help/tiles.html?opacity=0.7&l=clouds";
					changeMapTypeAndHideMenu(url);
				}
			});
			loadPrecipitationsMap.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					String url = "http://openweathermap.org/help/tiles.html?opacity=0.8&l=precipitation";
					changeMapTypeAndHideMenu(url);
				}
			});
			loadPressuureMap.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					String url = "http://openweathermap.org/help/tiles.html?opacity=0.6&l=pressure";
					changeMapTypeAndHideMenu(url);
				}
			});
			loadWindMap.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					String url = "http://openweathermap.org/help/tiles.html?opacity=0.6&l=wind";
					changeMapTypeAndHideMenu(url);
				}
			});
			loadSnowMap.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					String url = "http://openweathermap.org/help/tiles.html?opacity=0.7&l=snow";
					changeMapTypeAndHideMenu(url);
				}
			});

		}
	}

	private void changeMapTypeAndHideMenu(String mapURL){
		retract.start();
		menutriger = true;
		weatherMap.loadUrl(mapURL);
	}



	public void resetStrings(){
		airportName = null;
		airportWindGust = null;
		airportCloudsFirst = null;
		airportCloudBaseFirst = null;
		airportCloudsSecond = null;
		airportCloudBaseSecond = null;
		airportObscuration = null;
	}


	public class AsyncTaskParseJson extends AsyncTask<String, String, String> {
		Dialogs dialogs = new Dialogs(mainact);
		String yourJsonStringUrl = "https://api.flightstats.com/flex/weather/rest/v1/json/all/"+ airportTypedIcao +"?appId="+ MainActivity.appID +"&appKey=" + MainActivity.apiKey;
		//"http://api.geonames.org/weatherIcaoJSON?ICAO="+ airportICAO +"&username=demo";

		ConnectivityManager connectivityManager = (ConnectivityManager) mainact.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		JSONArray dataJsonArr = null;
		@Override
		protected void onPreExecute() {	
			dialogs.showPreloader();
		}

		@SuppressWarnings("static-access")
		@Override
		protected String doInBackground(String... arg0) {

			String connectionIsOk = null;
			if (activeNetworkInfo != null) {
				try {
					HttpURLConnection urlc = (HttpURLConnection) 
							(new URL("http://clients3.google.com/generate_204")
							.openConnection());
					urlc.setRequestProperty("User-Agent", "Android");
					urlc.setRequestProperty("Connection", "close");
					urlc.setConnectTimeout(1500); 
					urlc.connect();
					connectionIsOk = "CONNECTED";

					try {
						WeatherJSONParser jParser = new WeatherJSONParser();
						JSONObject json = jParser.queryRESTurl(yourJsonStringUrl);
						dataJsonArr = json.getJSONArray("request");

					} catch (JSONException e) {
						e.printStackTrace();
					}

					return connectionIsOk;
				} catch (IOException e) {

				}
			} 
			return connectionIsOk;
		}

		@Override
		protected void onPostExecute(String strFromDoInBg) {

			if(strFromDoInBg == "CONNECTED"){
				if(airportName != null){
					//FlurryAgent.logEvent("Weather for " + airportName + " received");
					DataConvertion.ConvertMilesToKilometers(airportVisMiles);
					DataConvertion.CalculateRelativeAirHumidity(airportTemp, airportDewPoint);
					DataConvertion.ConvertInHgtoMilibar(airportPress);
					DataConvertion.ConverCentigradeToFarenheit(airportTemp);
					DataConvertion.FormatReportTime(airportReportTime);


					airportNameFiled.setText(airportName);
					airportIcaoName.setText(airportICAO);
					reportTime.setText(airportReportTime + "Z");
					weatherTemp.setText(String.format("%.0f", Double.valueOf(airportTemp)) + "\u2103" + " / " + airportTempF + "\u2109");
					weatherHumid.setText(airportHumidity + "%");
					weatherPress.setText(airportPress + "inHg " + "(" + airportMilibar + "hPa)");
					weatherVis.setText(airportVisKm + " km " + "(" + String.format("%.0f", Double.valueOf(airportVisMiles)) + " ml)");
					weatherWind.setText(airportWindSpeed != null ? String.format("%.0f", Double.valueOf(airportWindSpeed)) + " kts / " + (airportWindDir == null ? "VRB" : airportWindDir + "\u00B0") + 
							(airportWindGust != null ? "   gust: " + String.format("%.0f", Double.valueOf(airportWindGust)) + "kts" : "") : mainact.getString(R.string.not_available));
					weatherDew.setText(String.format("%.0f", Double.valueOf(airportDewPoint)) + "\u00B0");
					tafReport.setText(taf);
					weatherObscuration.setText(airportObscuration != null ? airportObscuration : mainact.getString(R.string.obscurations_level));
					try{
						if(airportCloudsFirst == null){
							weatherClouds.setText(R.string.clear_sky);
						}else{
							airportCloudsFirst = DataConvertion.CloudCoverageConverter(airportCloudsFirst);
							try{airportCloudsSecond = DataConvertion.CloudCoverageConverter(airportCloudsSecond);}catch(Exception e){}
							weatherClouds.setText(airportCloudsFirst + " " + (airportCloudBaseFirst != null ? airportCloudBaseFirst + "ft." : "") + (airportCloudsSecond != null ? ", " + airportCloudsSecond + " " + airportCloudBaseSecond + "ft." : ""));
						}
					}catch(Exception e){}

					dialogs.hideDialog();
					System.out.println("CONNECTED");
				}else{		
					dialogs.airportNotFoundDialog();
				}
			}else{		
				dialogs.showNoInternetConnectionDialog();
			}
		}	
	}




}
