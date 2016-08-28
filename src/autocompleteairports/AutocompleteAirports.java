package autocompleteairports;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.skydata.MainActivity;


import android.app.Activity;
import android.content.Context;

public class AutocompleteAirports {
	
	Context cont;
	public ArrayList<AutocompleteAirportsListModel> autoAirportModel;
	public AutocompleteAirports(Activity act){
		cont = act;
	}
	
	public JSONArray loadJSONFromAsset() {
        String json = null;
        JSONArray jsa = null;
        autoAirportModel = new ArrayList<AutocompleteAirportsListModel>();
        //JSONObject tempObject = new JSONObject();
        MainActivity.autocompleteDatabase = new JSONArray();
      
        try {

            InputStream is = cont.getAssets().open("airports.json");

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");
            
            try {
				jsa = new JSONArray(json);
				for(int i = 0; i < jsa.length(); i++){
					String name = jsa.getJSONObject(i).getString("name");
					String iata = jsa.getJSONObject(i).getString("iata");
					autoAirportModel.add(new AutocompleteAirportsListModel(name, iata));
					//if(jsa.getJSONObject(i).getString("type").indexOf("airport") > 0){
					//}
					
					
					
					//tempObject.putOpt("name", name).putOpt("iata", iata);	
					//autocompleteDatabase.put(i, tempObject);
					
					//MainActivity.responseList.add(name);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
            System.out.println(autoAirportModel);
            


        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return jsa;

    }

}
