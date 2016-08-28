package flightsarrivals;


import com.example.skydata.MainActivity;
import com.example.skydata.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FlightMap extends Fragment{
	static GoogleMap googleMap;
	LatLng latlng;
	View v;
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		if(MainActivity.iAmOnTablet == true){
			v = inflater.inflate(R.layout.flight_map_layout, container, false);
			if(googleMap == null){
				googleMap = ((MapFragment) getActivity().getFragmentManager().findFragmentById(R.id.map)).getMap();
				googleMap.getUiSettings().setZoomControlsEnabled(false);
				googleMap.getUiSettings().setRotateGesturesEnabled(false);
			}
		}else{
			v = inflater.inflate(R.layout.map_dialog, container, false);
			if(googleMap == null){
				googleMap = ((MapFragment) getActivity().getFragmentManager().findFragmentById(R.id.dialog_map)).getMap();
				googleMap.getUiSettings().setZoomControlsEnabled(false);
				googleMap.getUiSettings().setRotateGesturesEnabled(false);
			}
		}
		
		return v;
	}
	
	
	public void showPlaneOnTheMap(String latitude, String longitude, String flightNumber, float planeBearing){
		Double planelatitude = Double.valueOf(latitude);
		Double planelongitude = Double.valueOf(longitude);
		latlng = new LatLng(planelatitude, planelongitude);
		googleMap.moveCamera(CameraUpdateFactory.newLatLng(latlng));
		googleMap.animateCamera(CameraUpdateFactory.zoomTo(8));
		googleMap.clear();
		googleMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.plane_marker)).rotation(planeBearing).position(new LatLng(planelatitude, planelongitude)).title(flightNumber));
	}



}
