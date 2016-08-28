package airportweather;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.example.skydata.MainActivity;
import com.example.skydata.R;
import com.flurry.android.FlurryAgent;
import flightsarrivals.FlightMap;

public class Dialogs {

	Context cont;
	public Dialog dialog;
	Button hideDialogButton;
	boolean flg = false;

	public Dialogs(Activity act){
		cont = act;
		dialog = new Dialog(cont);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
	}

	public void hideDialog(){
		dialog.hide();
	}

	public void destroyDialog(){
		dialog.dismiss();
	}

	public void showSplashScreen(){
		dialog.setContentView(R.layout.splash);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		dialog.setCancelable(false);
		dialog.show();

		Window window = dialog.getWindow();
		window.setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		final Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				hideDialog();
			}
		}, 5000);
	}

	public void showPreloader(){
		dialog.setContentView(R.layout.progress_bar_spinner);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		dialog.setCancelable(true);
		dialog.show();
	}


	public void showNoInternetConnectionDialog(){
		dialog.setContentView(R.layout.no_internet_connection);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		dialog.setCancelable(false);
		hideDialogButton = (Button) dialog.findViewById(R.id.dialogCose);
		hideDialogButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				hideDialog();
			}
		});
		dialog.show();
		Window window = dialog.getWindow();
		window.setLayout(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	}

	public void showFoundAirportsDialog(){
		dialog.setContentView(R.layout.found_airports_list);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		dialog.setCancelable(false);
		hideDialogButton = (Button) dialog.findViewById(R.id.closeAirportsDialog);
		hideDialogButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				hideDialog();
			}
		});
		dialog.show();
		//FlurryAgent.logEvent("Nearby airports dialog poped");
	}

	public void airportNotFoundDialog(){
		dialog.setContentView(R.layout.no_airport_found_dialog);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		dialog.setCancelable(false);
		hideDialogButton = (Button) dialog.findViewById(R.id.dialogClose);
		hideDialogButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				hideDialog();
			}
		});
		dialog.show();	
	}
	
	
	public void aboutSkyDataDialog(){
		dialog.setContentView(R.layout.about_skydata_dialog);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		dialog.setCancelable(false);
//		hideDialogButton = (Button) dialog.findViewById(R.id.dialogClose);
//		hideDialogButton.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				hideDialog();
//			}
//		});
		dialog.show();	
	}
	

	public void showPlaneOnMapDialog(String lat, String lon, String flightNumber, float bearing){
		if(MainActivity.dialogMapFragmentLoaded == false){
			MainActivity.dialogMapFragmentLoaded = true;
			dialog.setContentView(R.layout.map_dialog_fragment);

			dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
			dialog.setCancelable(false);
			hideDialogButton = (Button) dialog.findViewById(R.id.closeMapDialog);
			hideDialogButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					destroyDialog();
				}
			});
		}
		dialog.show();	
		FlightMap flm = new FlightMap();
		flm.showPlaneOnTheMap(lat, lon, flightNumber, bearing);
		//FlurryAgent.logEvent("Show plane on phone map " + flightNumber);
	}

	public void showDepartingFlightDetails(JSONObject data, String dTerminal, String dGate){
		dialog.setContentView(R.layout.dep_flight_details_phone);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		dialog.setCancelable(false);
		String detAirlineCode = null, detFlightNum = null, detAirlineName = null, detAirplaneName = null, detFlightDuration = null, depAirport = null, 
				airpLocation = null, detDepTerminal = dTerminal, detDepGate = dGate, detDepartureTime = null, arrAirport = null, arrAirportLocation = null, detArrTerminal = null,
				detArrTime = null, resHour, resMin, timeString, arrTimeString;
		TextView detailedAirlineCode, detailedFlightNumber, detailedAirlineName, detailedAirplaneName, detailedFlightDuration, departureAirport, airportLocation, detailedDepTerminal, detailedDepGate,
		detailedDepartureTime, arrivalAirport, arrivalAirportLocation, detailedArrTerminal, detailedArrTime;
		resHour = cont.getString(R.string.hours_short);
		resMin = cont.getString(R.string.departed_flight_min);

		try {
			detAirlineCode = data.getJSONObject("request").getJSONObject("airline").getJSONObject("airline").getString("iata");
			detFlightNum = data.getJSONObject("request").getJSONObject("flight").getString("interpreted");
			detAirlineName = data.getJSONObject("request").getJSONObject("airline").getJSONObject("airline").getString("name");
			detAirplaneName = data.getJSONArray("flightStatuses").getJSONObject(0).getJSONObject("flightEquipment").getJSONObject("scheduledEquipment").getString("name");
			detFlightDuration = data.getJSONArray("flightStatuses").getJSONObject(0).getJSONObject("flightDurations").getString("scheduledBlockMinutes");
			detFlightDuration = DataConvertion.formatFlightDuration(detFlightDuration, resHour, resMin);
			depAirport = data.getJSONArray("flightStatuses").getJSONObject(0).getJSONObject("departureAirport").getString("name");
			airpLocation = data.getJSONArray("flightStatuses").getJSONObject(0).getJSONObject("departureAirport").getString("city");
			airpLocation = airpLocation + ", " + data.getJSONArray("flightStatuses").getJSONObject(0).getJSONObject("departureAirport").getString("countryCode");
			timeString = data.getJSONArray("flightStatuses").getJSONObject(0).getJSONObject("departureDate").getString("dateLocal");
			detDepartureTime = timeString.substring(8,10) + "/" + timeString.substring(5,7) + "/" + timeString.substring(2,4) + " " + timeString.substring(11,16);
			arrAirport = data.getJSONArray("flightStatuses").getJSONObject(0).getJSONObject("arrivalAirport").getString("name");
			arrAirportLocation = data.getJSONArray("flightStatuses").getJSONObject(0).getJSONObject("arrivalAirport").getString("city");
			arrAirportLocation = arrAirportLocation + ", " + data.getJSONArray("flightStatuses").getJSONObject(0).getJSONObject("arrivalAirport").getString("countryCode");
			try{detArrTerminal = data.getJSONArray("flightStatuses").getJSONObject(0).getJSONObject("airportResources").getString("arrivalTerminal");}catch(JSONException e){detArrTerminal = "---";}
			arrTimeString = data.getJSONArray("flightStatuses").getJSONObject(0).getJSONObject("arrivalDate").getString("dateLocal");
			detArrTime = arrTimeString.substring(8,10) + "/" + arrTimeString.substring(5,7) + "/" + arrTimeString.substring(2,4) + " " + arrTimeString.substring(11,16);
			
			
		} catch (JSONException e) {
			e.printStackTrace();
		}


		detailedAirlineCode = (TextView) dialog.findViewById(R.id.detailedAirlineCode);
		detailedFlightNumber = (TextView) dialog.findViewById(R.id.detailedFlightNumber);
		detailedAirlineName = (TextView) dialog.findViewById(R.id.detailedAirlineName);
		detailedAirplaneName = (TextView) dialog.findViewById(R.id.detailedAirplaneName);
		detailedFlightDuration = (TextView) dialog.findViewById(R.id.detailedFlightDuration);
		departureAirport = (TextView) dialog.findViewById(R.id.departureAirport);
		airportLocation = (TextView) dialog.findViewById(R.id.airportLocation);
		detailedDepTerminal = (TextView) dialog.findViewById(R.id.detailedDepTerminal);
		detailedDepGate = (TextView) dialog.findViewById(R.id.detailedDepGate);
		detailedDepartureTime = (TextView) dialog.findViewById(R.id.detailedDepartureTime);
		arrivalAirport = (TextView) dialog.findViewById(R.id.arrivalAirport);
		arrivalAirportLocation = (TextView) dialog.findViewById(R.id.arrivalAirportLocation);
		detailedArrTerminal = (TextView) dialog.findViewById(R.id.detailedArrTerminal);
		detailedArrTime = (TextView) dialog.findViewById(R.id.detailedArrTime);

		detailedAirlineCode.setText(detAirlineCode);
		detailedFlightNumber.setText(detFlightNum);
		detailedAirlineName.setText(detAirlineName);
		detailedAirplaneName.setText(detAirplaneName);
		detailedFlightDuration.setText(detFlightDuration);
		departureAirport.setText(depAirport);
		airportLocation.setText(airpLocation);
		airportLocation.setText(airpLocation);
		detailedDepTerminal.setText(detDepTerminal);
		detailedDepGate.setText(detDepGate);
		detailedDepartureTime.setText(detDepartureTime);
		arrivalAirport.setText(arrAirport);
		arrivalAirportLocation.setText(arrAirportLocation);
		detailedArrTerminal.setText(detArrTerminal);
		detailedArrTime.setText(detArrTime);

		hideDialogButton = (Button) dialog.findViewById(R.id.close_details_dialog);
		hideDialogButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				hideDialog();
			}
		});
		dialog.show();
		//FlurryAgent.logEvent("Show departure flight on phone " + detAirlineCode + detFlightNum + " " + depAirport + " " + arrAirport);
	}

}
