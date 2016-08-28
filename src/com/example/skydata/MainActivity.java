package com.example.skydata;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.skydata.R.id;


import airportsfinder.AirportsListItemModel;
import airportsfinder.DatalistAdapter;
import airportweather.AirportWether;
import airportweather.Dialogs;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import autocompleteairports.AutocompleteAirports;
import com.flurry.android.FlurryAgent;

@SuppressLint("InflateParams") public class MainActivity extends FragmentActivity implements
NavigationDrawerFragment.NavigationDrawerCallbacks{

	/**
	 * Fragment managing the behaviors, interactions and presentation of the
	 * navigation drawer.
	 */
	private NavigationDrawerFragment mNavigationDrawerFragment;
	Dialogs dialogsClass;
	Integer asyncFlag;
	ArrayList<AirportsListItemModel> foundAirportsList;
	DatalistAdapter adapter;
	ListView listview = null;
	private boolean connectionStatus = true;
	public double myLatitude, myLongitude;
	public static boolean iAmOnTablet, dialogMapFragmentLoaded;
	static View flightsArrivalsView = null;
	static View flightsDeparturesView = null;
	public static JSONArray autocompleteDatabase = null;
	public static ArrayList<String> responseList = new ArrayList<String>();
	public static String appID, apiKey;
	public static TextView batteryStatus;
	public static ImageView batIcon, connectivityIcon;
	public static Animation lowBat;
	//public ImageButton shareScreen, closeApp, moreOptions;
	
	
	/**
	 * Used to store the last screen title. For use in
	 * {@link #restoreActionBar()}.
	 */
	private CharSequence mTitle;


	@SuppressLint("InlinedApi") @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_main);
		
		this.getActionBar().setDisplayShowCustomEnabled(true);
		this.getActionBar().setCustomView(R.layout.custom_actionbar);
		this.getActionBar().setIcon(R.drawable.ic_launcher);
		
		batteryStatus = (TextView) this.getActionBar().getCustomView().findViewById(R.id.batteryStatus);
		batIcon = (ImageView) this.getActionBar().getCustomView().findViewById(id.batIcon);
		connectivityIcon = (ImageView) this.getActionBar().getCustomView().findViewById(id.connectivityIcon);
		lowBat = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.landed_text_animation);
		this.registerReceiver(DeviceData.mBatInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
		
		
		
		
		dialogsClass = new Dialogs(MainActivity.this);
		dialogsClass.showSplashScreen();
		
		appID = Constants.APP_ID;
		apiKey = Constants.API_KEY;
		boolean tabletSize = getResources().getBoolean(R.bool.isTablet);
		if (tabletSize) {
			iAmOnTablet = true;
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		} else {
			iAmOnTablet = false;
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}
		asyncFlag = 0;
		

		new checkDeviceConectivity().execute();

		mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager().findFragmentById(R.id.navigation_drawer);
		mTitle = getTitle();
		// Set up the drawer.
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer,(DrawerLayout) findViewById(R.id.drawer_layout));
		
		//FlurryAgent.setReportLocation(true);
		//FlurryAgent.init(this, Constants.FLURRY_KEY);
		
		AutocompleteAirports autoAirp = new AutocompleteAirports(MainActivity.this);
		autoAirp.loadJSONFromAsset();
	}
	

	
	@Override
	public void onNavigationDrawerItemSelected(int position) {
		// update the main content by replacing fragments
		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager
		.beginTransaction()
		.replace(R.id.container,
				PlaceholderFragment.newInstance(position + 1)).commit();
	}

	public void onSectionAttached(int number) {
		switch (number) {
		case 1:
			mTitle = getString(R.string.title_section1);
			break;
		case 2:
			mTitle = getString(R.string.title_section2);
			break;
		case 3:
			mTitle = getString(R.string.title_section3);
			break;
		}
	}

	@SuppressWarnings("deprecation")
	public void restoreActionBar() {
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(mTitle);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (!mNavigationDrawerFragment.isDrawerOpen()) {
			// Only show items in the action bar relevant to this screen
			// if the drawer is not showing. Otherwise, let the drawer
			// decide what to show in the action bar.
			getMenuInflater().inflate(R.menu.main, menu);
			restoreActionBar();
			return true;
		}
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
	    //  preparation code here
	    return super.onPrepareOptionsMenu(menu);
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		private static final String ARG_SECTION_NUMBER = "section_number";

		/**
		 * Returns a new instance of this fragment for the given section number.
		 */
		public static PlaceholderFragment newInstance(int sectionNumber) {
			PlaceholderFragment fragment = new PlaceholderFragment();
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments(args);
			return fragment;
		}

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			switch (getArguments().getInt(ARG_SECTION_NUMBER)){
			case 1:
				View v = inflater.inflate(R.layout.weather_fragment, container, false);
				AirportWether airport = new AirportWether();
				airport.getData(v, getActivity());
				return v;
			case 2:
				if(flightsArrivalsView == null){
					flightsArrivalsView = inflater.inflate(R.layout.flights_arrivals, container,false);
				}	
				return flightsArrivalsView;
				
			case 3:
				if(flightsDeparturesView == null){
					flightsDeparturesView = inflater.inflate(R.layout.flights_departures, container,false);
				}	
				return flightsDeparturesView;
				
			}
			return null;
		}

		@Override
		public void onAttach(Activity activity) {
			super.onAttach(activity);
			((MainActivity) activity).onSectionAttached(getArguments().getInt(
					ARG_SECTION_NUMBER));
		}
	}



	//*********************************** INTERNET CONNECTION CHECKER ********************************/

	public class checkDeviceConectivity extends AsyncTask<String, String, String>{

		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

		@Override
		protected String doInBackground(String... params) {		
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
					return connectionIsOk;
				} catch (IOException e) {

				}
			} else {

			}
			return connectionIsOk;
		}

		@Override
		protected void onPostExecute(String connectionIsOk) {
			if (connectionIsOk == "CONNECTED"){
				connectionStatus = true;
				checkGPSAvailability();
				System.out.println("CONNECTED TO INTERNET");
			}else{
				connectionStatus = false;
				if(connectionStatus == false){
					final Handler handler = new Handler();
				    handler.postDelayed(new Runnable() {
				      @Override
				      public void run() {
				    	  dialogsClass.showNoInternetConnectionDialog();
				      }
				    }, 5000);
					
				}
				System.out.println("NO INTERNET CONNECTION");
			}
		}
	}
	//************************************************************************************************/

	public void checkGPSAvailability(){
		GPSTracker mGPS = new GPSTracker(this);
		if(mGPS.canGetLocation ){
			mGPS.getLocation();
			System.out.println("Lat "+mGPS.getLatitude()+" Lon "+mGPS.getLongitude());
			myLatitude = mGPS.getLatitude();
			myLongitude = mGPS.getLongitude();
			new GetAirportsArroundMe().execute();
		}else{
			System.out.println("GPS RECEIVER IS INACTIVE");
		}
	}


	public class GetAirportsArroundMe extends AsyncTask<String, String, JSONArray>{

		String requestUrl = "https://api.flightstats.com/flex/airports/rest/v1/json/withinRadius/"+ myLongitude +"/"+ myLatitude +"/40?appId=" + appID + "&appKey=" + apiKey;
		JSONArray airportsJsonArr = null;

		@SuppressWarnings("static-access")
		@Override
		protected JSONArray doInBackground(String... params) {
			try {
				MainJSONParser jParser = new MainJSONParser();
				JSONObject json = jParser.queryRESTurl(requestUrl);
				airportsJsonArr = json.getJSONArray("airports");
				//airportsJsonArr = json.getJSONArray("cast");

			} catch (JSONException e) {
				e.printStackTrace();
			}

			return airportsJsonArr;
		}

		@Override
		protected void onPostExecute(JSONArray airportsJsonArr) {
			foundAirportsList = new ArrayList<AirportsListItemModel>();
			for(int i = 0; i < airportsJsonArr.length(); i++){
				try {
					String airportclass = airportsJsonArr.getJSONObject(i).getString("classification");
					int classification = Integer.parseInt(airportclass);
					if(classification < 5){
						String airportname = airportsJsonArr.getJSONObject(i).getString("name");
						String airporticao = airportsJsonArr.getJSONObject(i).getString("icao");
						foundAirportsList.add(new AirportsListItemModel(airportname, airporticao));
					}
				} catch (JSONException e) {	
					e.printStackTrace();
				}

			}
			if(airportsJsonArr.length() > 0){
				dialogsClass.showFoundAirportsDialog();
				listview = (ListView)dialogsClass.dialog.findViewById(android.R.id.list);
				adapter = new DatalistAdapter(MainActivity.this, foundAirportsList);
				listview.setAdapter(adapter);

				listview.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						TextView icaoCode = (TextView) view.findViewById(R.id.airpIcao);
						AirportWether.icaoField.setText(icaoCode.getText().toString());
						dialogsClass.hideDialog();
					}
				});
			}

			super.onPostExecute(airportsJsonArr);
		}
	}

	

	
//	@Override
//	protected void onStart()
//	{
//		super.onStart();
//		FlurryAgent.onStartSession(this, Constants.FLURRY_KEY);
//	    FlurryAgent.setLogEnabled(true);
//	    FlurryAgent.setLogEvents(true);
//	}
//	 
//	@Override
//	protected void onStop()
//	{
//		super.onStop();		
//		FlurryAgent.onEndSession(this);
//	}

}

