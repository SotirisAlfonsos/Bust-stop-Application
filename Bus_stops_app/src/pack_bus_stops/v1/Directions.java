package pack_bus_stops.v1;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class Directions extends MainActivity  {
	GoogleMap map ;
    private static List<LatLng> list = new ArrayList<LatLng>();
	String lat, lon, bus_lat, bus_lon;
    List<Polyline> polylines = new ArrayList<Polyline>();
    List<Marker> tests = new ArrayList<Marker>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_the_map_act_v1);
		polylines.clear();
        tests.clear();
        list.clear();
		Intent intent = getIntent();
		lat = intent.getStringExtra(MainActivity.your_latitude);
		lon = intent.getStringExtra(MainActivity.your_longitude);
		bus_lat = intent.getStringExtra(MainActivity.bus_latitude);
		bus_lon = intent.getStringExtra(MainActivity.bus_longitude);
		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
		
		if (map!=null){
			map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
			Marker your_pos = map.addMarker(new MarkerOptions().position(new LatLng(Double.valueOf(lat),Double.valueOf(lon))).title("You are here"));
			Marker bus_stop_pos = map.addMarker(new MarkerOptions().position(new LatLng(Double.valueOf(bus_lat),Double.valueOf(bus_lon))).title("The bus stop"));
			map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.valueOf(lat),Double.valueOf(lon)), 15));
            getDirections direct = new getDirections();
            String url = direct.makeURL(Double.valueOf(lat), Double.valueOf(lon), Double.valueOf(bus_lat), Double.valueOf(bus_lon));
            (new connectAsyncTask(url)).execute();
			your_pos.showInfoWindow();
			bus_stop_pos.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
		}

		System.out.println(lat);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.the_map_act_v1, menu);
		return true;
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

    private class connectAsyncTask extends AsyncTask<Void, Void, String> {

        String url;
        connectAsyncTask(String urlPass){
            url = urlPass;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           /* progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Fetching route, Please wait...");
            progressDialog.setIndeterminate(true);
            progressDialog.show();*/
        }
        @Override
        protected String doInBackground(Void... params) {
            JSONParser jParser = new JSONParser();
            String json = jParser.getJSONFromUrl(url);
            return json;
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //progressDialog.hide();
            if(result!=null){
                drawPath(result);
            }
        }
    }

    public void drawPath(String  result) {

        try {
            //Tranform the string into a json object
            final JSONObject json = new JSONObject(result);
            JSONArray routeArray = json.getJSONArray("routes");
            JSONObject routes = routeArray.getJSONObject(0);
            JSONObject overviewPolylines = routes.getJSONObject("overview_polyline");
            String encodedString = overviewPolylines.getString("points");
            list = decodePoly(encodedString);

            for(int z = 0; z<list.size()-1;z++){
                LatLng src= list.get(z);
                LatLng dest= list.get(z+1);


                polylines.add(map.addPolyline(new PolylineOptions()
                        .add(new LatLng(src.latitude, src.longitude), new LatLng(dest.latitude,   dest.longitude))
                        .width(5)
                        .color(Color.MAGENTA).geodesic(true)));
               /* line = map.addPolyline(new PolylineOptions()
                        .add(new LatLng(src.latitude, src.longitude), new LatLng(dest.latitude,   dest.longitude))
                        .width(5)
                        .color(Color.CYAN).geodesic(true));*/
            }

        }
        catch (JSONException e) {

        }
    }

    private List<LatLng> decodePoly(String encoded) {

        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng( (((double) lat / 1E5)),
                    (((double) lng / 1E5) ));
            poly.add(p);
        }

        return poly;
    }
}
