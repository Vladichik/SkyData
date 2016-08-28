package flightsdepartures;

import java.util.ArrayList;
import java.util.Calendar;

import org.json.JSONObject;

import com.example.skydata.AdvancedActions;
import com.example.skydata.Constants;
import com.example.skydata.MainActivity;
import com.example.skydata.MainJSONParser;
import com.example.skydata.R;

import flightsarrivals.BitmapManager;
import airportweather.Dialogs;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

@SuppressLint("DefaultLocale") public class DeparturesDataListAdapter extends BaseAdapter {

	private Activity context;
	private ArrayList<DeparturesListItemModel> list;
	private Constants remarkCodes;
	private Animation departedFieldBlinker;
	static Dialogs dlg;
	static String airlineCODE, flightNUMBER, year, month, day, DT, DG;
	FlightDetailsFragment flightDetailsFragment;



	public DeparturesDataListAdapter(Activity act, ArrayList<DeparturesListItemModel> theList){
		context = act;
		list = theList;
		BitmapManager.INSTANCE.setPlaceholder(BitmapFactory.decodeResource(
				context.getResources(), R.drawable.airline_placeholder));
		departedFieldBlinker = AnimationUtils.loadAnimation(context.getApplicationContext(),R.anim.landed_text_animation);
		dlg = new Dialogs(act);
		flightDetailsFragment = new FlightDetailsFragment();

	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressWarnings("static-access")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder vh;
		if(convertView == null){
			vh = new ViewHolder();
			LayoutInflater inflater = context.getLayoutInflater();	
			convertView = inflater.inflate(R.layout.row_departing_flight, parent, false);
			vh.departAirlineLogo = (ImageView) convertView.findViewById(R.id.departAirlineLogo);
			vh.departStatusIcon = (ImageView) convertView.findViewById(R.id.departStatusIcon);
			vh.delayedIcon = (ImageView) convertView.findViewById(R.id.delayedIcon);
			vh.departFlightNum = (TextView) convertView.findViewById(R.id.departFlightNum);
			vh.departDestinationCity = (TextView) convertView.findViewById(R.id.departDestinationCity);
			vh.destTerminal = (TextView) convertView.findViewById(R.id.destTerminal);
			vh.destGate = (TextView) convertView.findViewById(R.id.destGate);
			vh.departureTime = (TextView) convertView.findViewById(R.id.departureTime);
			vh.departRemark = (TextView) convertView.findViewById(R.id.departRemark);
			vh.departedDelayTime = (TextView) convertView.findViewById(R.id.departedDelayTime);

			vh.showFlightDetails = (ImageButton) convertView.findViewById(R.id.showFlightDetails);
			vh.shareFlight = (ImageButton) convertView.findViewById(R.id.shareFlight);
			vh.showFlightDetails.setOnClickListener(getFlightDetails);
			vh.shareFlight.setOnClickListener(shareFlightDetails);

			vh.airlineCode = (TextView) convertView.findViewById(R.id.airlineCode);
			vh.flightNumberDigits = (TextView) convertView.findViewById(R.id.flightNumberDigits);


			convertView.setTag(vh);
		}else{
			vh = (ViewHolder) convertView.getTag();
		}

		DeparturesListItemModel dlim = list.get(position);

		vh.departAirlineLogo.setTag(dlim.getAirlineLogo());
		BitmapManager.INSTANCE.loadBitmap(dlim.getAirlineLogo(), vh.departAirlineLogo, 140, 52);
		vh.departFlightNum.setText(dlim.getComposedFlightNumber());
		vh.departDestinationCity.setText(dlim.getDestinationCity());
		vh.destTerminal.setText(dlim.getTerminal());
		vh.destGate.setText(dlim.getGate());
		vh.departureTime.setText(dlim.getFlightTime());
		vh.departRemark.setText(dlim.getRemarks());
		vh.departStatusIcon.setImageResource(R.drawable.arrive_ontime_image_tablet);
		vh.delayedIcon.setVisibility(View.INVISIBLE);
		vh.departedDelayTime.setText("");
		vh.departRemark.clearAnimation();
		vh.airlineCode.setText(dlim.getAirlineCode());
		vh.flightNumberDigits.setText(dlim.getFlightNumber());


		if(dlim.getRemarkCode().indexOf(remarkCodes.DEPARTED_ON_TIME) != -1){
			vh.departRemark.setText(R.string.flight_departed);
			vh.departRemark.setTextColor(Color.parseColor("#31ae00"));
			vh.departStatusIcon.setImageResource(R.drawable.icon_departed_flight);
			vh.departRemark.startAnimation(departedFieldBlinker);
		}
		else if(dlim.getRemarkCode().indexOf(remarkCodes.DEPARTURE_DELAYED) != -1){
			String minutes = dlim.getRemarks().substring(8,11);
			vh.departRemark.setText(minutes + context.getString(R.string.minutes_late));
			vh.departRemark.setTextColor(Color.parseColor("#eea200"));
			vh.departStatusIcon.setImageResource(R.drawable.arrive_delayed_image_tablet);
		}
		else if(dlim.getRemarkCode().indexOf(remarkCodes.CANCELLED) != -1){
			vh.departRemark.setText(dlim.getRemarks());
			vh.departRemark.setTextColor(Color.parseColor("#eb1e25"));
			vh.departStatusIcon.setImageResource(R.drawable.flight_canceled);
		}
		else if(dlim.getRemarkCode().indexOf(remarkCodes.ON_TIME) != -1){
			vh.departRemark.setText(R.string.flight_on_time);
			vh.departRemark.setTextColor(Color.parseColor("#31ae00"));
		}
		else if(dlim.getRemarkCode().indexOf(remarkCodes.DEPARTED_LATE) != -1){
			String departedLateMinutes = dlim.getRemarks().substring(9, 11);
			vh.departRemark.setText(R.string.flight_departed);
			vh.departRemark.setTextColor(Color.parseColor("#31ae00"));
			vh.departStatusIcon.setImageResource(R.drawable.icon_departed_flight);
			vh.departedDelayTime.setText(departedLateMinutes + " " + context.getString(R.string.departed_flight_min));
			vh.delayedIcon.setVisibility(View.VISIBLE);
			vh.departRemark.startAnimation(departedFieldBlinker);
		}
		return convertView;
	}

	public class ViewHolder {
		ImageView departAirlineLogo, departStatusIcon, delayedIcon;
		ImageButton showFlightDetails, shareFlight;
		TextView airlineCode, flightNumberDigits, departFlightNum, departDestinationCity, destTerminal, destGate, departureTime, departRemark, departedDelayTime;
	}

	private OnClickListener shareFlightDetails = new OnClickListener() {
		@Override
		public void onClick(View v) {
			View element = (View) v.getParent().getParent().getParent();
			String filename = "departing_flight";
			AdvancedActions.shareElement(element, context, filename);
		}

	};


	private OnClickListener getFlightDetails = new OnClickListener() {
		@SuppressWarnings("static-access")
		@Override
		public void onClick(View v) {
			View objectView = (View) v.getParent().getParent().getParent();
			TextView ALC = (TextView) objectView.findViewById(R.id.airlineCode);
			TextView FND = (TextView) objectView.findViewById(R.id.flightNumberDigits);
			TextView TRM = (TextView) objectView.findViewById(R.id.destTerminal);
			TextView DSG = (TextView) objectView.findViewById(R.id.destGate);
			TextView DPT = (TextView) objectView.findViewById(R.id.departureTime);

			if(MainActivity.iAmOnTablet == true){
				flightDetailsFragment.airlineCODE = ALC.getText().toString();
				flightDetailsFragment.flightNUMBER = FND.getText().toString();
				flightDetailsFragment.day = DPT.getText().toString().substring(0,2);
				flightDetailsFragment.detDepartureTerminal = TRM.getText().toString();
				flightDetailsFragment.detDepartureGate = DSG.getText().toString();
				flightDetailsFragment.executeRequest();

			}else{
				airlineCODE = ALC.getText().toString();
				flightNUMBER = FND.getText().toString();
				DT = TRM.getText().toString();
				DG = DSG.getText().toString();
				year = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
				month = String.valueOf(Calendar.getInstance().get(Calendar.MONTH) + 1);
				day = DPT.getText().toString().substring(0,2);
				new RequestFlightDetailsPhone().execute("");
			}

		}
	};


	private static class RequestFlightDetailsPhone extends AsyncTask<String, String, JSONObject>{

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
			dlg.showPreloader();
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(final JSONObject object) {
			dlg.hideDialog();
			final Handler handler = new Handler();
			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					dlg.showDepartingFlightDetails(object, DT, DG);
				}
			}, 200);
			super.onPostExecute(object);
		}

	}

}
