package flightsdepartures;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.skydata.MainActivity;
import com.example.skydata.MainJSONParser;
import com.example.skydata.R;
import com.flurry.android.FlurryAgent;

import airportweather.Dialogs;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class FlightsDepartures extends Fragment {
	
	Button showFlightsButton;
	AutoCompleteTextView airportAutocompleteField;
	String stringFromInput;
	Dialogs dialogs;
	TextView departingAirportName;
	String departureAirportName;

	//ListView elements//
	private DeparturesDataListAdapter departuresRowAdapter;
	private ArrayList<DeparturesListItemModel> departingFlightsArray;
	private ListView listview = null;
	/////////////////////

	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View departuresView = inflater.inflate(R.layout.flights_departures_list, container, false);
		dialogs = new Dialogs(getActivity());
		airportAutocompleteField = (AutoCompleteTextView) departuresView.findViewById(R.id.departuresICAO);
		departingAirportName = (TextView) departuresView.findViewById(R.id.departingAirportName);
		showFlightsButton = (Button) departuresView.findViewById(R.id.showDeparturesData);
		showFlightsButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//dialogs.showDepartingFlightDetails();
				if(airportAutocompleteField.length() > 0){
					stringFromInput = airportAutocompleteField.getText().toString();
					InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(airportAutocompleteField.getWindowToken(), 0);
					new getDepartures().execute("");
				}
			}	
		});

		return departuresView;
	}



	public class getDepartures extends AsyncTask<String, String, JSONArray>{

		String requestURL = "https://api.flightstats.com/flex/fids/rest/v1/json/"+ stringFromInput +"/departures?appId="+ MainActivity.appID +"&appKey="+ MainActivity.apiKey +"&requestedFields=airlineCode%2CflightNumber%2CflightId%2Cflight%2CcurrentDate%2CairlineLogoUrlPng%2Cgate%2CairportName%2Ccity%2CremarksCode%2CcurrentTime%2Cgate%2Cremarks%2Cterminal&sortFields=currentDate%2CcurrentTime&timeFormat=24&timeWindowBegin=40&lateMinutes=15&useRunwayTimes=false&excludeCargoOnlyFlights=false";
		JSONArray receivedFlight;


		@Override
		protected void onPreExecute() {
			dialogs.showPreloader();
			super.onPreExecute();
		}

		@SuppressWarnings("static-access")
		@Override
		protected JSONArray doInBackground(String... params) {
			try {
				MainJSONParser jParser = new MainJSONParser();
				JSONObject json = jParser.queryRESTurl(requestURL);
				receivedFlight = json.getJSONArray("fidsData");
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return receivedFlight;
		}


		@Override
		protected void onPostExecute(JSONArray data) {
			if(data.length() > 0){
				String airlineCode = null, flightNumber = null, flightID = null, composedFlightNumber = null, destinationCity = null, destinationAirport = null, terminal = null, gate = null, flightTime = null, flightDate = null, remarks = null, remarkCode = null, airlineLogo = null;
				departingFlightsArray = new ArrayList<DeparturesListItemModel>();
				//try{
				//departingAirportName = data.getJSONObject(0).get("")
				//}
				for(int i = 0; i < data.length(); i++){
					try{
						airlineCode = data.getJSONObject(i).getString("airlineCode");
					}catch(JSONException e){
						System.out.println("ON POST EXECUTE ERROR PARSING airlineCode" + e);
					}
					try{
						flightNumber = data.getJSONObject(i).getString("flightNumber");
					}catch(JSONException e){
						System.out.println("ON POST EXECUTE ERROR PARSING flightNumber" + e);
					}
					try{
						flightID = data.getJSONObject(i).getString("flightId");
					}catch(JSONException e){
						System.out.println("ON POST EXECUTE ERROR PARSING flightId" + e);
					};
					try{
						composedFlightNumber = data.getJSONObject(i).getString("flight");
					}catch(JSONException e){
						composedFlightNumber = "---";
						System.out.println("ON POST EXECUTE ERROR PARSING composedFlightNumber" + e);
					};
					try{
						destinationCity = data.getJSONObject(i).getString("city");
					}catch(JSONException e){
						destinationCity = "---";
						System.out.println("ON POST EXECUTE ERROR PARSING destinationCity" + e);
					}
					try{
						destinationAirport = data.getJSONObject(i).getString("airportName");
					}catch(JSONException e){
						destinationAirport = "---";
						System.out.println("ON POST EXECUTE ERROR PARSING destinationAirport" + e);
					}
					try{
						terminal = data.getJSONObject(i).getString("terminal");
					}catch(JSONException e){
						terminal = "---";
						System.out.println("ON POST EXECUTE ERROR PARSING terminal" + e);
					}
					try{
						gate = data.getJSONObject(i).getString("gate");
					}catch(JSONException e){
						gate = "---";
						System.out.println("ON POST EXECUTE ERROR PARSING gate" + e);
					}
					try{
						flightTime = data.getJSONObject(i).getString("currentTime");
					}catch(JSONException e){
						flightTime = "---";
						System.out.println("ON POST EXECUTE ERROR PARSING flightTime" + e);
					}
					try{
						flightDate = data.getJSONObject(i).getString("currentDate");
						flightDate = flightDate.substring(3,5) + "/" + flightDate.substring(0,2);
					}catch(JSONException e){
						flightDate = "---";
						System.out.println("ON POST EXECUTE ERROR PARSING currentDate" + e);
					}
					try{
						remarks = data.getJSONObject(i).getString("remarks");
					}catch(JSONException e){
						remarks = "---";
						System.out.println("ON POST EXECUTE ERROR PARSING remarks" + e);
					}
					try{
						remarkCode = data.getJSONObject(i).getString("remarksCode");
						System.out.println(remarkCode);
					}catch(JSONException e){
						remarkCode = "---";
						System.out.println("ON POST EXECUTE ERROR PARSING remarkCode" + e);
					}
					try{
						airlineLogo = data.getJSONObject(i).getString("airlineLogoUrlPng");
					}catch(JSONException e){
						airlineLogo = "---";
						System.out.println("ON POST EXECUTE ERROR PARSING airlineLogo" + e);
					}
					flightTime = flightDate.substring(0,5) + " " + flightTime;

					departingFlightsArray.add(new DeparturesListItemModel(airlineCode, flightNumber, flightID, composedFlightNumber, destinationCity, destinationAirport, terminal, gate, flightTime, flightDate, remarks, remarkCode, airlineLogo));
				}
				

				listview = (ListView)getActivity().findViewById(android.R.id.list);
				departuresRowAdapter = new DeparturesDataListAdapter(getActivity(), departingFlightsArray);
				listview.setAdapter(departuresRowAdapter);

				//FlurryAgent.logEvent("Departures for " + stringFromInput + " received");
				dialogs.hideDialog();
			}else{
				dialogs.hideDialog();
				new android.os.Handler().postDelayed(
						new Runnable() {
							public void run() {
								dialogs.airportNotFoundDialog();
								//FlurryAgent.logEvent("Departures not found for " + stringFromInput);
							}
						}, 500);
			}
		}

	}

	



}
