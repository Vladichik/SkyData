package airportsfinder;

import java.util.ArrayList;

import com.example.skydata.R;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class DatalistAdapter extends BaseAdapter {

	private Activity context;
	private ArrayList<AirportsListItemModel> list;

	public DatalistAdapter(Activity con, ArrayList<AirportsListItemModel> theList){
		context = con;
		list = theList;
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

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder vh;

		if(convertView == null){
			vh = new ViewHolder();
			LayoutInflater inflater = context.getLayoutInflater();
			convertView = inflater.inflate(R.layout.row_found_airports, parent, false);
			vh.airpName = (TextView) convertView.findViewById(R.id.airpName);
			vh.airpIcao = (TextView) convertView.findViewById(R.id.airpIcao);
			convertView.setTag(vh);
		}else{
			vh = (ViewHolder) convertView.getTag();
		}
		
		AirportsListItemModel lim = list.get(position);
		vh.airpName.setText(lim.getFoundAirportName());
		vh.airpIcao.setText(lim.getFoundAirportICAO());
		
		return convertView;
	}

	public class ViewHolder {
		public TextView airpName;
		public TextView airpIcao;
	}

}
