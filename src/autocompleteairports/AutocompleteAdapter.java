package autocompleteairports;

import java.util.ArrayList;

import com.example.skydata.R;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

public class AutocompleteAdapter extends ArrayAdapter<AutocompleteAirportsListModel>{
	
	private Activity context;
	private ArrayList<AutocompleteAirportsListModel> list;

	
	public AutocompleteAdapter(Activity con, ArrayList<AutocompleteAirportsListModel> theList){
		super(con, 0, theList);
		context = con;
		list = theList;
	}

//	@Override
//	public int getCount() {
//		return list.size();
//	}
//
//	@Override
//	public Object getItem(int position) {
//		return list.get(position);
//	}
//
//	@Override
//	public long getItemId(int position) {
//		return position;
//	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder vh;
		if(convertView == null){
			vh = new ViewHolder();
			LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.row_autocomplete_airports, null);
			//LayoutInflater inflater = context.getLayoutInflater();
			//convertView = inflater.inflate(R.layout.row_autocomplete_airports, parent, false);
			vh.autoName = (TextView) convertView.findViewById(R.id.autoName);
			vh.autoIata = (TextView) convertView.findViewById(R.id.autoIata);
			convertView.setTag(vh);
		}else{
			vh = (ViewHolder) convertView.getTag();
		}
		
		AutocompleteAirportsListModel aalm = list.get(position);
		vh.autoName.setText(aalm.getName());
		vh.autoIata.setText(aalm.getIcao());
		return convertView;
	}
	
	public class ViewHolder {
		public TextView autoName;
		public TextView autoIata;
	}

//	@Override
//	public Filter getFilter() {
//		// TODO Auto-generated method stub
//		return nameFilter;
//	}
//	
//	
//	
//	
//	 Filter nameFilter = new Filter() {
//	        @Override
//	        public String convertResultToString(Object resultValue) {
//	            String str = ((AutocompleteAirportsListModel)(resultValue)).getName(); 
//	            return str;
//	        }
//	        @Override
//	        protected FilterResults performFiltering(CharSequence constraint) {
//	            if(constraint != null) {
//	            	sugestion.clear();
//	                for (AutocompleteAirportsListModel customer : listAll) {
//	                    if(customer.getName().toLowerCase().startsWith(constraint.toString().toLowerCase())){
//	                    	sugestion.add(customer);
//	                    }
//	                }
//	                FilterResults filterResults = new FilterResults();
//	                filterResults.values = sugestion;
//	                filterResults.count = sugestion.size();
//	                return filterResults;
//	            } else {
//	                return new FilterResults();
//	            }
//	        }
//	        @Override
//	        protected void publishResults(CharSequence constraint, FilterResults results) {
//	            ArrayList<AutocompleteAirportsListModel> filteredList = (ArrayList<AutocompleteAirportsListModel>) results.values;
//	            if(results != null && results.count > 0) {
//	                clear();
//	                for (AutocompleteAirportsListModel c : filteredList) {
//	                    add(c);
//	                }
//	                notifyDataSetChanged();
//	            }
//	        }
//	    };
}
