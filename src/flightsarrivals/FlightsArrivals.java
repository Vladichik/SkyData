package flightsarrivals;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.skydata.MainActivity;
import com.example.skydata.MainJSONParser;
import com.example.skydata.R;
import com.flurry.android.FlurryAgent;

import airportweather.Dialogs;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import autocompleteairports.AutocompleteAdapter;
import autocompleteairports.AutocompleteAirports;
import autocompleteairports.AutocompleteAirportsListModel;

public class FlightsArrivals extends Fragment {
	AutoCompleteTextView icaoInputField;
	Button showFlightsButton;
	String stringFromInput;
	ArrayList<FlightsListItemModel> arrivingFlightsArray;
	FlightDatalistAdapter flightsListAdapter;

	AutocompleteAirports aca;
	AutocompleteAdapter autocompAdapter;

	ListView listview = null;
	Dialogs dialogs;
	String airportName, reportTime;
	TextView airportNameview;
	
	private boolean menutriger = false;
	private ObjectAnimator retract, extend;



	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.flights_arrivals_list, container, false);
		icaoInputField = (AutoCompleteTextView)v.findViewById(R.id.arrivalsICAO);
		showFlightsButton = (Button)v.findViewById(R.id.showArrivalsData);
		airportNameview = (TextView)v.findViewById(R.id.issueAirportName);
		showFlightsButton.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View v) {
				if(icaoInputField.length() > 0){
					stringFromInput = icaoInputField.getText().toString();
					InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(icaoInputField.getWindowToken(), 0);
					new checkInternetConnection().execute();
				}
			}
		});

		//ArrayAdapter<String> adp=new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, MainActivity.responseList);
		//icaoInputField.setAdapter(adp);

		//aca = new AutocompleteAirports(getActivity());
		//autocompAdapter = new AutocompleteAdapter(getActivity(), aca.autoAirportModel);
		//icaoInputField.setAdapter(autocompAdapter);
		icaoInputField.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Toast.makeText(getActivity(), "Position clicked: " + position, Toast.LENGTH_SHORT).show();
			}
		});

		dialogs = new Dialogs(getActivity());

		return v;
	}



	public class checkInternetConnection extends AsyncTask<String, String, Boolean>{

		ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		boolean isConnected;

		@Override
		protected void onPreExecute() {
			dialogs.showPreloader();
			super.onPreExecute();
		}


		@Override
		protected void onPostExecute(Boolean result) {
			if(result == true){
				new getFlights().execute();
			}else{
				dialogs.showNoInternetConnectionDialog();
			}
			super.onPostExecute(result);
		}


		@Override
		protected Boolean doInBackground(String... params) {
			if (activeNetworkInfo != null) {
				try {
					HttpURLConnection urlc = (HttpURLConnection) 
							(new URL("http://clients3.google.com/generate_204")
							.openConnection());
					urlc.setRequestProperty("User-Agent", "Android");
					urlc.setRequestProperty("Connection", "close");
					urlc.setConnectTimeout(1500); 
					urlc.connect();
					isConnected = true;
					return isConnected;

				} catch (IOException e) {}
			} else {}
			return isConnected;
		};

	}


	public class getFlights extends AsyncTask<String, String, JSONArray>{

		String requestURL = "https://api.flightstats.com/flex/fids/rest/v1/json/"+ stringFromInput +"/arrivals?appId="+ MainActivity.appID +"&appKey="+ MainActivity.apiKey +"&requestedFields=airlineCode%2Cflight%2CdestinationAirportName%2Ccity%2CscheduledTime%2CscheduledDate%2CactualTime%2CcurrentTime%2Cgate%2Cremarks%2CairlineLogoUrlPng%2ClastUpdatedDate%2ClastUpdatedTime%2Cterminal%2Cgate%2CflightId&sortFields=scheduledDate+currentTime&timeFormat=24&timeWindowBegin=40&lateMinutes=15&useRunwayTimes=false&excludeCargoOnlyFlights=false";
		JSONArray receivedFlight;

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
			//dialogs.hideDialog();
			if(data.length() > 0){
				//System.out.println("RECEIVED DATA FROM FLIGHTS ASYNCTASK" + data);
				String flightID = null, airline = null, flight = null, remark = null, city = null, scheduledDate = null, scheduledTime = null, actualTime = null, terminal = null, logo = null, constructedSchedDate = null;
				arrivingFlightsArray = new ArrayList<FlightsListItemModel>();
				try {
					airportName = data.getJSONObject(0).getString("destinationAirportName");
					airportNameview.setText(airportName);
				} catch (JSONException e1) {}
				for(int i = 0; i < data.length(); i++){
					try{
						flightID = data.getJSONObject(i).getString("flightId");
					}catch(JSONException e){
						System.out.println("ON POST EXECUTE ERROR PARSING flightId" + e);
					};
					try{
						//TODO CHECK THIS DATA = MAYBE ITS USELESS
						airline = data.getJSONObject(i).getString("airlineCode"); 
					}catch(JSONException e){
						System.out.println("ON POST EXECUTE ERROR PARSING airlineCode" + e);
					};
					try{
						flight = data.getJSONObject(i).getString("flight");
					}catch(JSONException e){
						System.out.println("ON POST EXECUTE ERROR PARSING airlineCode" + e);
					};
					try{
						remark = data.getJSONObject(i).getString("remarks");
					}catch(JSONException e){
						System.out.println("ON POST EXECUTE ERROR PARSING remark" + e);
					}
					try{
						city = data.getJSONObject(i).getString("city");
					}catch(JSONException e){
						System.out.println("ON POST EXECUTE ERROR PARSING city" + e);
					}
					try{
						scheduledDate = data.getJSONObject(i).getString("scheduledDate");
						scheduledDate = scheduledDate.substring(3,5) + "/" + scheduledDate.substring(0,2);
					}catch(JSONException e){
						System.out.println("ON POST EXECUTE ERROR PARSING scheduledDate" + e);
					}
					try{
						scheduledTime = data.getJSONObject(i).getString("scheduledTime");
					}catch(JSONException e){
						System.out.println("ON POST EXECUTE ERROR PARSING scheduledTime" + e);
					}
					try{
						actualTime = data.getJSONObject(i).getString("currentTime");
					}catch(JSONException e){
						System.out.println("ON POST EXECUTE ERROR PARSING actualTime" + e);
					}
					try{
						terminal = data.getJSONObject(i).getString("terminal");
					}catch(JSONException e){
						System.out.println("ON POST EXECUTE ERROR PARSING terminal" + e);
					}
					try{
						logo = data.getJSONObject(i).getString("airlineLogoUrlPng");
					}catch(JSONException e){
						System.out.println("ON POST EXECUTE ERROR PARSING logo" + e);
					}
					constructedSchedDate = scheduledDate.substring(0,5) + " " + scheduledTime;

					arrivingFlightsArray.add(new FlightsListItemModel(flightID, airline, flight, remark, city, constructedSchedDate, actualTime, terminal, logo));

				}

				listview = (ListView)getActivity().findViewById(android.R.id.list);
				flightsListAdapter = new FlightDatalistAdapter(getActivity(), arrivingFlightsArray);
				listview.setAdapter(flightsListAdapter);
				if(MainActivity.iAmOnTablet != false){

				}

				//FlurryAgent.logEvent("Arrivals for " + airportName + " received");
				dialogs.hideDialog();	
			}else{
				dialogs.hideDialog();
				new android.os.Handler().postDelayed(
						new Runnable() {
							public void run() {
								dialogs.airportNotFoundDialog();
								//FlurryAgent.logEvent("Arrivals not found for " + stringFromInput);
							}
						}, 500);
			}
		}
	}
	



}
