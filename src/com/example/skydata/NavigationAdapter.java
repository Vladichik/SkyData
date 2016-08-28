package com.example.skydata;

import android.app.ActionBar.Tab;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class NavigationAdapter extends BaseAdapter {
	
	Context context;
    String[] data;
	private Integer[] imags;
    private static LayoutInflater inflater = null;

    public NavigationAdapter(Context context, String[] data, Integer[] imags) {
        // TODO Auto-generated constructor stub
        this.context = context;
        this.data = data;
        this.imags = imags;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return data[position];
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View vi = convertView;
        if (vi == null)
            vi = inflater.inflate(R.layout.row_navigation_drawer, null);
        ImageView tabIcon = (ImageView)vi.findViewById(R.id.tabImage);
        TextView text = (TextView) vi.findViewById(R.id.tabName);
        tabIcon.setImageResource(imags[position]);
        text.setText(data[position]);
        return vi;
	}

}
