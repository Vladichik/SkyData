package flightsdepartures;


import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.skydata.MainActivity;
import com.example.skydata.MainJSONParser;
import com.example.skydata.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import airportweather.DataConvertion;
import airportweather.Dialogs;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.BatteryManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class FlightDetailsFragment extends Fragment{
	static LatLng originLatLng, destinationlatlng;
	static GoogleMap googleMap;
	static Dialogs dialogs;
	public static String airlineCODE, flightNUMBER, detDepartureTerminal, detDepartureGate, year, month, day;
	static TextView detailedAirlineCode, detailedFlightNumber, detailedAirlineName, detailedAirplaneName, detailedFlightDuration, detailedDepartureAirp, detailedDepAirportLoc, detailedDepartureTime,
	detailedDepTerminal, detailedDepGate, arrivalAirport, arrAirportLocation, detailedArrTerminal, detailedArrTime;
	private static String resHour, resMin, detAirlineCode, detFlightNum, detAirlineName, detAirplaneName, detFlightDuration, detDepartureAirp, detDepAirportLocation, detDepartureTime, 
	detArrivlAirport, detArrAirportLocation, detArrivalTerminal, detArrivalTime, departingLat, departingLong, arrivalLat, arrivalLong;

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		
		View v = inflater.inflate(R.layout.departing_flight_details, container, false);
		dialogs = new Dialogs(getActivity());
		year = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
		month = String.valueOf(Calendar.getInstance().get(Calendar.MONTH) + 1);

		detailedAirlineCode = (TextView)v.findViewById(R.id.detailedAirlineCode);
		detailedFlightNumber = (TextView)v.findViewById(R.id.detailedFlightNumber);
		detailedAirlineName = (TextView)v.findViewById(R.id.detailedAirlineName);
		detailedAirplaneName = (TextView)v.findViewById(R.id.detailedAirplaneName);
		detailedFlightDuration = (TextView)v.findViewById(R.id.detailedFlightDuration);
		detailedDepartureAirp = (TextView)v.findViewById(R.id.departureAirport);
		detailedDepAirportLoc = (TextView)v.findViewById(R.id.airportLocation);
		detailedDepTerminal = (TextView)v.findViewById(R.id.detailedDepTerminal);
		detailedDepGate = (TextView)v.findViewById(R.id.detailedDepGate);
		detailedDepartureTime = (TextView)v.findViewById(R.id.detailedDepartureTime);

		arrivalAirport = (TextView)v.findViewById(R.id.arrivalAirport);
		arrAirportLocation = (TextView)v.findViewById(R.id.arrAirportLocation);
		detailedArrTerminal = (TextView)v.findViewById(R.id.detailedArrTerminal);
		detailedArrTime = (TextView)v.findViewById(R.id.detailedArrTime);

		if(googleMap == null){
			googleMap = ((MapFragment) getActivity().getFragmentManager().findFragmentById(R.id.map2)).getMap();
			googleMap.getUiSettings().setZoomControlsEnabled(false);
			googleMap.getUiSettings().setRotateGesturesEnabled(false);
		}
		resHour = getActivity().getString(R.string.hours_short);
		resMin = getActivity().getString(R.string.departed_flight_min);
		
		return v;

	}
	

	public void executeRequest(){
		new RequestDepartingFlightDetails().execute("");
	}


	public static class RequestDepartingFlightDetails extends AsyncTask<String, String, JSONObject>{


		//String requestURL = "https://api.flightstats.com/flex/schedules/rest/v1/json/flight/"+ airlineCODE +"/"+ flightNUMBER +"/departing/"+ year +"/"+ month +"/"+ day +"?appId="+ MainActivity.appID +"&appKey="+ MainActivity.apiKey +"&extendedOptions=useInlinedReferences";
		String requestURL = "https://api.flightstats.com/flex/flightstatus/rest/v2/json/flight/status/"+ airlineCODE +"/"+ flightNUMBER +"/dep/"+ year +"/"+ month +"/"+ day +"?appId="+ MainActivity.appID +"&appKey="+ MainActivity.apiKey +"&utc=false&extendedOptions=includeDeltas%2C+useInlinedReferences%2C+useHttpErrors";
		JSONObject departingFlightDetails;


		@SuppressWarnings("static-access")
		@Override
		protected JSONObject doInBackground(String... params) {
			MainJSONParser jParser = new MainJSONParser();
			departingFlightDetails = jParser.queryRESTurl(requestURL);
			return departingFlightDetails;
		}


		@Override
		protected void onPreExecute() {
			dialogs.showPreloader();
			super.onPreExecute();
		}


		@Override
		protected void onPostExecute(JSONObject data) {
			System.out.println(data);
			String timeString, arrTimeString;

			try{
				detAirlineCode = data.getJSONObject("request").getJSONObject("airline").getJSONObject("airline").getString("iata");
				detFlightNum = data.getJSONObject("request").getJSONObject("flight").getString("interpreted");
				detAirlineName = data.getJSONObject("request").getJSONObject("airline").getJSONObject("airline").getString("name");
				detAirplaneName = data.getJSONArray("flightStatuses").getJSONObject(0).getJSONObject("flightEquipment").getJSONObject("scheduledEquipment").getString("name");
				detFlightDuration = data.getJSONArray("flightStatuses").getJSONObject(0).getJSONObject("flightDurations").getString("scheduledBlockMinutes");
				detFlightDuration = DataConvertion.formatFlightDuration(detFlightDuration, resHour, resMin);
				detDepartureAirp = data.getJSONArray("flightStatuses").getJSONObject(0).getJSONObject("departureAirport").getString("name");
				detDepAirportLocation = data.getJSONArray("flightStatuses").getJSONObject(0).getJSONObject("departureAirport").getString("city");
				detDepAirportLocation = detDepAirportLocation + ", " + data.getJSONArray("flightStatuses").getJSONObject(0).getJSONObject("departureAirport").getString("countryCode");
				detArrivlAirport = data.getJSONArray("flightStatuses").getJSONObject(0).getJSONObject("arrivalAirport").getString("name");
				detArrAirportLocation = data.getJSONArray("flightStatuses").getJSONObject(0).getJSONObject("arrivalAirport").getString("city");
				detArrAirportLocation = detArrAirportLocation + ", " + data.getJSONArray("flightStatuses").getJSONObject(0).getJSONObject("arrivalAirport").getString("countryCode");
				timeString = data.getJSONArray("flightStatuses").getJSONObject(0).getJSONObject("departureDate").getString("dateLocal");
				detDepartureTime = timeString.substring(8,10) + "/" + timeString.substring(5,7) + "/" + timeString.substring(2,4) + " " + timeString.substring(11,16);
				try{detArrivalTerminal = data.getJSONArray("flightStatuses").getJSONObject(0).getJSONObject("airportResources").getString("arrivalTerminal");}catch(JSONException e){detArrivalTerminal = "---";}
				
				
				arrTimeString = data.getJSONArray("flightStatuses").getJSONObject(0).getJSONObject("arrivalDate").getString("dateLocal");
				detArrivalTime = arrTimeString.substring(8,10) + "/" + arrTimeString.substring(5,7) + "/" + arrTimeString.substring(2,4) + " " + arrTimeString.substring(11,16);				
				departingLat = data.getJSONArray("flightStatuses").getJSONObject(0).getJSONObject("departureAirport").getString("latitude");
				departingLong = data.getJSONArray("flightStatuses").getJSONObject(0).getJSONObject("departureAirport").getString("longitude");
				arrivalLat = data.getJSONArray("flightStatuses").getJSONObject(0).getJSONObject("arrivalAirport").getString("latitude");
				arrivalLong = data.getJSONArray("flightStatuses").getJSONObject(0).getJSONObject("arrivalAirport").getString("longitude");


			}catch(JSONException e){};

			updateFlightDetails();
		}
	}

	private static void updateFlightDetails(){

		detailedAirlineCode.setText(detAirlineCode);
		detailedFlightNumber.setText(detFlightNum);
		detailedAirlineName.setText(detAirlineName);
		detailedAirplaneName.setText(detAirplaneName);
		detailedFlightDuration.setText(detFlightDuration);
		detailedDepartureAirp.setText(detDepartureAirp);
		detailedDepAirportLoc.setText(detDepAirportLocation);
		detailedDepTerminal.setText(detDepartureTerminal);
		detailedDepGate.setText(detDepartureGate);
		detailedDepartureTime.setText(detDepartureTime);
		arrivalAirport.setText(detArrivlAirport);
		arrAirportLocation.setText(detArrAirportLocation);
		detailedArrTerminal.setText(detArrivalTerminal);
		detailedArrTime.setText(detArrivalTime);
		dialogs.hideDialog();
		showAirportsOnTheMap();
	}


	@SuppressWarnings("unused")
	private static void showAirportsOnTheMap(){
		Double orginLat = Double.valueOf(departingLat);
		Double originLong = Double.valueOf(departingLong);
		Double destinationLat = Double.valueOf(arrivalLat);
		Double destinationLong = Double.valueOf(arrivalLong);
		originLatLng = new LatLng(orginLat, originLong);
		destinationlatlng = new LatLng(destinationLat, destinationLong);
		googleMap.moveCamera(CameraUpdateFactory.newLatLng(originLatLng));
		googleMap.clear();
		googleMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.airport_departure_marker)).position(new LatLng(orginLat, originLong)));
		googleMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.airport_destination_marker)).position(new LatLng(destinationLat, destinationLong)));
		PolylineOptions rectLine = new PolylineOptions().width(3).color(Color.RED);
		rectLine.add(originLatLng, destinationlatlng);
		Polyline newPolyline = googleMap.addPolyline(rectLine);

		LatLngBounds.Builder builder = new LatLngBounds.Builder();
		builder.include(originLatLng);
		builder.include(destinationlatlng);
		LatLngBounds bounds = builder.build();
		googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 105));
	}


}
