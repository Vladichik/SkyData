package flightsarrivals;


import java.util.ArrayList;

import com.example.skydata.AdvancedActions;
import com.example.skydata.MainActivity;
import com.example.skydata.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.BitmapFactory;
import android.graphics.Color;
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

@SuppressLint({ "ResourceAsColor", "DefaultLocale" }) public class FlightDatalistAdapter extends BaseAdapter {

	private Activity context;
	private ArrayList<FlightsListItemModel> list;
	Animation landedFieldBlinker;
	FlightsArrivals flar;
	MainActivity mac;

	public FlightDatalistAdapter(Activity con, ArrayList<FlightsListItemModel> theList){
		context = con;
		list = theList;
		BitmapManager.INSTANCE.setPlaceholder(BitmapFactory.decodeResource(
				context.getResources(), R.drawable.airline_placeholder));
		landedFieldBlinker = AnimationUtils.loadAnimation(context.getApplicationContext(),R.anim.landed_text_animation);
		flar = new FlightsArrivals();
		mac = new MainActivity();
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

	@SuppressLint("DefaultLocale") @Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder vh;
		if(convertView == null){
			vh = new ViewHolder();
			LayoutInflater inflater = context.getLayoutInflater();	
			convertView = inflater.inflate(R.layout.row_arriving_flight, parent, false);
			vh.airlneLogo = (ImageView) convertView.findViewById(R.id.airlineLogo);
			vh.statusIcon = (ImageView) convertView.findViewById(R.id.statusIcon);

			vh.showPlanePosition = (ImageButton) convertView.findViewById(R.id.showPlanePosition);
			vh.showPlanePosition.setOnClickListener(showPlaneListener);

			vh.shareFlight = (ImageButton) convertView.findViewById(R.id.shareFlight);
			vh.shareFlight.setOnClickListener(shareFlightView);

			vh.flightNumber = (TextView) convertView.findViewById(R.id.flightNum);
			vh.city = (TextView) convertView.findViewById(R.id.cityOrigin);
			vh.scheduledTime = (TextView)convertView.findViewById(R.id.scheduledTime);
			vh.ETA = (TextView) convertView.findViewById(R.id.currentTime);
			vh.remark = (TextView) convertView.findViewById(R.id.flightRemark);

			if(MainActivity.iAmOnTablet == true){
				vh.terminalNumber = (TextView) convertView.findViewById(R.id.terminalNumber);
			}
			convertView.setTag(vh);
		}else{
			vh = (ViewHolder) convertView.getTag();
		}

		FlightsListItemModel flim = list.get(position);

		vh.airlneLogo.setTag(flim.getAirlineImage());
		vh.showPlanePosition.setTag(flim.getFlightID());
		BitmapManager.INSTANCE.loadBitmap(flim.getAirlineImage(), vh.airlneLogo, 130, 50);
		vh.flightNumber.setText(flim.getFlightNumber());
		vh.city.setText(flim.getCity());
		vh.scheduledTime.setText(flim.getScheduledTime());
		vh.ETA.setText(flim.getCurrentTime());
		vh.remark.setText(flim.getRemarks());
		vh.remark.setTextColor(Color.parseColor("#31ae00"));
		vh.statusIcon.setImageResource(R.drawable.arrive_ontime_image_tablet);
		vh.remark.clearAnimation();
		if(MainActivity.iAmOnTablet == true){
			vh.terminalNumber.setText(flim.getTerminal());
		}
		if(flim.getRemarks().toLowerCase().indexOf("cancelled") != -1){
			vh.remark.setText(flim.getRemarks());
			vh.remark.setTextColor(Color.parseColor("#eb1e25"));
			vh.statusIcon.setImageResource(R.drawable.flight_canceled);
		}
		else if(flim.getRemarks().toLowerCase().indexOf("arrived") != -1){
			vh.remark.setText(context.getString(R.string.landed));
			vh.remark.setTextColor(Color.parseColor("#31ae00"));
			vh.statusIcon.setImageResource(R.drawable.landed_tablet);
			vh.remark.startAnimation(landedFieldBlinker);

		}else if (flim.getRemarks().toLowerCase().indexOf("late") != -1){
			vh.remark.setText(flim.getRemarks().substring(0,3) + context.getString(R.string.minutes_late));
			vh.remark.setTextColor(Color.parseColor("#eea200"));
			vh.statusIcon.setImageResource(R.drawable.arrive_delayed_image_tablet);
		}


		return convertView;
	}

	public class ViewHolder {
		public ImageButton showPlanePosition, shareFlight;
		public ImageView airlneLogo;
		public ImageView statusIcon;
		public TextView flightNumber;
		public TextView city;
		public TextView scheduledTime;
		public TextView ETA;
		public TextView terminalNumber;
		public TextView remark;
		public TextView flightID;
	}

	private OnClickListener showPlaneListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
				final String flightId = v.getTag().toString();
				String trackURL = "https://api.flightstats.com/flex/flightstatus/rest/v2/json/flight/track/"+ flightId +"?appId="+ MainActivity.appID +"&appKey="+ MainActivity.apiKey +"&includeFlightPlan=false&maxPositions=1";
				new FlightTracker(context, context).execute(trackURL);
		}
	};

	private OnClickListener shareFlightView = new OnClickListener() {
		@Override
		public void onClick(View vi) {
			View element = (View) vi.getParent().getParent().getParent();
			String filename = "arriving_flight";
			AdvancedActions.shareElement(element, context, filename);
		}
	};


}
