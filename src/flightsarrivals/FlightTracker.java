package flightsarrivals;

import java.text.DecimalFormat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.skydata.MainActivity;
import com.example.skydata.MainJSONParser;
import com.example.skydata.R;

import airportweather.Dialogs;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

public class FlightTracker extends AsyncTask<String, String, JSONObject> {
	JSONObject airplaneInfoObject;
	Context context;
	Dialogs d;
	


	public FlightTracker(Context contxt, Activity act){
		context = contxt;
		d = new Dialogs(act);
	}
	
	
	@Override
	protected void onPreExecute() {
		d.showPreloader();
	}
	

	@SuppressWarnings("static-access")
	@Override
	protected JSONObject doInBackground(String... params) {
		try {
			MainJSONParser jParser = new MainJSONParser();
			JSONObject json = jParser.queryRESTurl(params[0]);
			airplaneInfoObject = json.getJSONObject("flightTrack");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return airplaneInfoObject;
	}

	@Override
	protected void onPostExecute(JSONObject data) {
		d.hideDialog();
		System.out.println("LATLON: " + data);
		JSONArray positions = null;
		try {
			positions = data.getJSONArray("positions");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		if(positions.length() > 0){
			try {
				String fullFlightNumber = null;
				Float planeBearing = null;
				String planeLat = positions.getJSONObject(0).getString("lat");
				String planeLong = positions.getJSONObject(0).getString("lon");
				try{
					String carrier = data.getString("carrierFsCode");
					String flightNumber = data.getString("flightNumber");
					fullFlightNumber = carrier + flightNumber;
				}catch (JSONException e){
					
				}
				try{
					planeBearing = Float.parseFloat(data.getString("bearing"));
					planeBearing = Float.parseFloat(new DecimalFormat("##.#").format(planeBearing));
				} catch (JSONException e){
					planeBearing = 0.0f;
				}
				if(MainActivity.iAmOnTablet == true){
				FlightMap fmp = new FlightMap();
				fmp.showPlaneOnTheMap(planeLat, planeLong, fullFlightNumber, planeBearing);
				}else{
					d.showPlaneOnMapDialog(planeLat, planeLong, fullFlightNumber, planeBearing);
				}
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}else{
			Toast.makeText(context, R.string.no_flight_location, 1000).show();
		}
		super.onPostExecute(data);
	}
	
	
	

}
