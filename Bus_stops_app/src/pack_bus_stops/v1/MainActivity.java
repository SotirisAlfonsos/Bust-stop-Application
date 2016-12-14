package pack_bus_stops.v1;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Locale;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
//import com.google.android.gms.identity.intents.Address;
import com.google.android.gms.common.api.*;
import com.google.android.gms.drive.Drive;
//import com.google.android.gms.location;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
LocationListener {
	
	LocationRequest mLocationRequest;
    LocationServices mLocationClient;

	boolean mUpdatesRequested;
	public int my_bus_timers_flag=0;
	private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
	// Milliseconds per second
	private static final int MILLISECONDS_PER_SECOND = 1000;
	// Update frequency in seconds
	public static final int UPDATE_INTERVAL_IN_SECONDS = 5;
	// Update frequency in milliseconds
	private static final long UPDATE_INTERVAL =
	    MILLISECONDS_PER_SECOND * UPDATE_INTERVAL_IN_SECONDS;
	// The fastest update frequency, in seconds
	private static final int FASTEST_INTERVAL_IN_SECONDS = 1;
	// A fast frequency ceiling in milliseconds
	private static final long FASTEST_INTERVAL =
	    MILLISECONDS_PER_SECOND * FASTEST_INTERVAL_IN_SECONDS;
	public double lat_old = 0.0;
	public double lon_old = 0.0;
	Location mLastLocation=null;
	public String msg_old="";
	public int count=0;
	public TextView walking_distance;
	public TextView bus_times;
	public TextView mAddress;
	public TextView Bus_stop_address;
	public TextView Bus_stop_name;
    public GoogleApiClient mGoogleApiClient;
	public Button directions_button;
	public final static String your_latitude = "your_latitude";
	public final static String your_longitude = "your_longitude";
	public final static String bus_latitude = "bus_latitude";
	public final static String bus_longitude = "bus_longitude";
	public double lat, lon, bus_lat, bus_lon;
	
	/**************************************************************************************************************
	What to do when the app is created.																		  
	**************************************************************************************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_main);
	    mAddress = (TextView) findViewById(R.id.address);
	    //urlText = (EditText) findViewById(R.id.edit_message);




        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            Toast.makeText(this, "GPS is Enabled in your devide", Toast.LENGTH_SHORT).show();
        }else{
            showGPSDisabledAlertToUser();
        }


        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo != null && activeNetworkInfo.isConnected()){
            Toast.makeText(this, "Network is Enabled in your devide", Toast.LENGTH_SHORT).show();
        }else{
            showNetworkDisabledAlertToUser();
        }





	    Bus_stop_name = (TextView) findViewById(R.id.Bus_stop_name);
	    Bus_stop_address = (TextView) findViewById(R.id.Bus_stop);
	    bus_times = (TextView) findViewById(R.id.Hello_world);
	    walking_distance = (TextView) findViewById(R.id.Walking_dist);
	    bus_times.setVisibility(View.GONE);
	    directions_button = (Button) findViewById(R.id.button1);
	    directions_button.setVisibility(View.GONE);
	    //mLocationClient = new LocationClient(this, this, this);
		//mLocationClient = new LocationClient(this,this,this);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

	    mLocationRequest = LocationRequest.create();
	    // Use high accuracy
	    mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
	    // Set the update interval to 5 seconds
	    mLocationRequest.setInterval(UPDATE_INTERVAL);
	    // Set the fastest update interval to 1 second
	    mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
	    //mLocationClient = new LocationClient(this, this, this);
	    mUpdatesRequested = true;
	    directions_button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, Directions.class);
				intent.putExtra(your_latitude, Double.toString(lat));
				intent.putExtra(your_longitude, Double.toString(lon));
				intent.putExtra(bus_latitude, Double.toString(bus_lat));
				intent.putExtra(bus_longitude, Double.toString(bus_lon));
				//textView.setText(Double.toString(lat));
				startActivity(intent);
			}
       });
	    
    }

    private void showGPSDisabledAlertToUser(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("GPS is disabled in your device. Would you like to enable it?").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int id){
                Intent callGPSSettingIntent = new Intent( android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(callGPSSettingIntent);
            }
        });
        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int id){
                dialog.cancel();
            }
        }); AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    private void showNetworkDisabledAlertToUser(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Network is disabled in your device. Would you like to enable it?").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int id){
                Intent callGPSSettingIntent = new Intent( Settings.ACTION_WIFI_SETTINGS);
                startActivity(callGPSSettingIntent);
            }
        });
        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int id){
                dialog.cancel();
            }
        }); AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    /**************************************************************************************************************
	Starting things about the GPS connection																	  
	**************************************************************************************************************/
    public static class ErrorDialogFragment extends DialogFragment {
		// Global field to contain the error dialog
		private Dialog mDialog;
		// Default constructor. Sets the dialog field to null
		public ErrorDialogFragment() {
		    super();
		    mDialog = null;
		}
		// Set the dialog to display
		public void setDialog(Dialog dialog) {
			System.out.println("seting dialog and shit.........");
		    mDialog = dialog;
		}
		// Return a Dialog to the DialogFragment.
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			System.out.println("Creating dialog and shit.........");
		    return mDialog;
		}
	}

    /**************************************************************************************************************
	Handle results returned to the FragmentActivity by Google Play services																	  
	**************************************************************************************************************/
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	// Decide what to do based on the original request code
		System.out.println("I am doing something.........");
		switch (requestCode) {
	    
	    case CONNECTION_FAILURE_RESOLUTION_REQUEST :
	    /*
	     * If the result code is Activity.RESULT_OK, try
	     * to connect again
	     */
	        switch (resultCode) {
	            case Activity.RESULT_OK :
	            /*
	             * Try the request again
	             */
	            	System.out.println("Should i start the request again.........");
	            break;
	        }
	    
		}
	}

	/**************************************************************************************************************
	Check that Google Play services is available																  
	**************************************************************************************************************/
	public boolean servicesConnected() {
		// Check that Google Play services is available
		int resultCode =
		        GooglePlayServicesUtil.
		                isGooglePlayServicesAvailable(this);
		// If Google Play services is available
		if (ConnectionResult.SUCCESS == resultCode) {
		    // In debug mode, log the status
		    Log.d("Location Updates",
		            "Google Play services is available.");
		    // Continue
		    return true;
		// Google Play services was not available for some reason.
		// resultCode holds the error code.
		} else {
		    // Get the error dialog from Google Play services
		    Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(
		            resultCode,
		            this,
		            CONNECTION_FAILURE_RESOLUTION_REQUEST);
	
		    // If Google Play services can provide an error dialog
		    if (errorDialog != null) {
		        // Create a new DialogFragment for the error dialog
		        ErrorDialogFragment errorFragment =
		                new ErrorDialogFragment();
		        // Set the dialog in the DialogFragment
		        errorFragment.setDialog(errorDialog);
		        // Show the error dialog in the DialogFragment
		        errorFragment.show(getSupportFragmentManager(),
		                "Location Updates");
		    }
		    return false;
		}
	}

	/**************************************************************************************************************
	The phone is connected and requests an update on his location																  
	**************************************************************************************************************/    
    @Override
    public void onConnected(Bundle dataBundle) {
        // Display the connection status
    	
    	System.out.println("I connected.........");
        Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show();
        if (mUpdatesRequested) {
        	System.out.println("i requested the update........" );
            startLocationUpdates();
            //mLocationClient.FusedLocationApi.requestLocationUpdates(mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        System.out.println("hi" );
    }

    protected void startLocationUpdates() {
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
    }
    
    /**************************************************************************************************************
	Called by Location Services if the connection to the
    location client drops because of an error.																  
	**************************************************************************************************************/
    /*@Override
    public void onDisconnected() {
    	System.out.println("I disconnected.........");
        // Display the connection status
        Toast.makeText(this, "Disconnected. Please re-connect.",
                Toast.LENGTH_SHORT).show();
    }*/
    
    /**************************************************************************************************************
	Called by Location Services if the attempt to
    Location Services fails.																  
	**************************************************************************************************************/
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        /*
         * Google Play services can resolve some errors it detects.
         * If the error has a resolution, try sending an Intent to
         * start a Google Play services activity that can resolve
         * error.
         */
    	System.out.println("connection failed.........");
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(
                        this,
                        CONNECTION_FAILURE_RESOLUTION_REQUEST);
                /*
                 * Thrown if Google Play services canceled the original
                 * PendingIntent
                 */
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
            /*
             * If no resolution is available, display a dialog to the
             * user with the error.
             */
            //showErrorDialog(connectionResult.getErrorCode());
        }
    }
	    
    /**************************************************************************************************************
   	Called when the Activity becomes visible.															  
   	**************************************************************************************************************/
    @Override
    protected void onStart() {
        super.onStart();
        System.out.println("I started.........");
        // Connect the client.
       // if (!mResolvingError) {  // more about this later
            mGoogleApiClient.connect();
        //}
    }
    
    /**************************************************************************************************************
   	Called when the Activity is no longer visible.															  
   	**************************************************************************************************************/
    @Override
    protected void onStop() {
    	System.out.println("I stoped.........");
        // Disconnecting the client invalidates it.
    	if (mGoogleApiClient.isConnected()) {
            /*
             * Remove location updates for a listener.
             * The current Activity is the listener, so
             * the argument is "this".
             */
            //mGoogleApiClient.removeLocationUpdates(this);
        }

        mGoogleApiClient.disconnect();
        super.onStop();
        
    }
	   
    /**************************************************************************************************************
   	Define the callback method that receives location updates. Call function to find where the nearest bus stop is.
   	Call function to find the times for your bus.														  
   	**************************************************************************************************************/
    // Define the callback method that receives location updates
    @Override
    public void onLocationChanged(Location location) {
    	System.out.println("location changed...........................................");
    	String[] busstoploc;
        double latitude,longitude;
    	lat = round(location.getLatitude(), 5);
    	lon = round(location.getLongitude(), 5);
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        
        // Report to the UI that the location was updated
        String msg = "Updating your location..";
        		//"Updated Location: " + Double.toString(location.getLatitude()) + "," + Double.toString(location.getLongitude());
        System.out.println(mLastLocation);
        System.out.println(msg);
        System.out.println(lat + " " + lon);
        System.out.println("Updating your location...");
        //round(mLastLocation.getLatitude(), 5) + " " + round(mLastLocation.getLongitude(), 5)
        if (mLastLocation==null) {
            latitude=0.0;
            longitude=0.0;
        }else {
            latitude=mLastLocation.getLatitude();
            longitude=mLastLocation.getLongitude();
        }
        // YOUR LOCATION HAS BEEN REVEALED................
        if (round(latitude, 5) == lat && round(longitude, 5) == lon) {
        
        	(new GetAddressTask(this)).execute(location);
        	//count++;
        	
        	busstoploc = new Nearest_Bus_Stop().Nearest_stop_calc(lat, lon);
        	bus_lat = Double.parseDouble(busstoploc[1]);
        	bus_lon = Double.parseDouble(busstoploc[2]);
        	Get_bus_stop_address(this, Double.parseDouble(busstoploc[1]), Double.parseDouble(busstoploc[2]));
        	Bus_stop_name.setText("Bus stop name: \n" + busstoploc[0]);
        	if (mGoogleApiClient.isConnected()) {
	            
	    		//mLocationClient.removeLocationUpdates(this);
        	}
        	walking_distance.setText("Walking distance: " + (Double.valueOf(busstoploc[3])).intValue() + " minutes");
            mGoogleApiClient.disconnect();
        	if(my_bus_timers_flag==0) {
        		find_time(busstoploc[0]);
        	}
        	directions_button.setVisibility(View.VISIBLE);
        	bus_times.setVisibility(View.VISIBLE);
        }
        
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
    
    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

   /* public void sendMessage(View view) {
    	Intent intent = new Intent(this, DisplayMessageActivity.class);
    	//EditText editText = (EditText) findViewById(R.id.edit_message);
    	String message = editText.getText().toString();
    	intent.putExtra(EXTRA_MESSAGE, message);
    	startActivity(intent);

    }*/
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    /**************************************************************************************************************
   	This is called when your location and the nearest bus stop have been found to calculate the time the first and 
   	second bus from the current time are arriving. it then creates an async task to read the pdf online and 
   	find the bus rout times.														  
   	**************************************************************************************************************/
    public void find_time (String bus_stop_location) {
    	// Gets the URL from the UI's text field.
        String stringUrl = "http://astikovolou.gr/documents/N--3.pdf";
        		//urlText.getText().toString();
        System.out.println(stringUrl);
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
    
        if (networkInfo != null && networkInfo.isConnected()) {
            new DownloadWebpageTask().execute(stringUrl,bus_stop_location);
        } else {
            bus_times.setText("No network connection available.");
        }
    }
    
    /*public void myClickHandler(View view) {
        // Gets the URL from the UI's text field.
        String stringUrl = "http://astikovolou.gr/documents/N--3.pdf";
        		//urlText.getText().toString();
        System.out.println(stringUrl);
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
    
        if (networkInfo != null && networkInfo.isConnected()) {
            new DownloadWebpageTask().execute(stringUrl);
        } else {
            textView.setText("No network connection available.");
        }
    }*/

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
    
    /**************************************************************************************************************
   	Download and read the pdf														  
   	**************************************************************************************************************/
    public class DownloadWebpageTask extends  AsyncTask<String, Void, String> {
    	private static final String DEBUG_TAG = "HttpExample";
        @Override
        protected String doInBackground(String... urls) {
              System.out.println("geiaaa");
            // params comes from the execute() call: params[0] is the url.
            try {
                return downloadUrl(urls[0],urls[1]);
            } catch (IOException e) {
                return "Unable to retrieve web page. URL may be invalid.";
            }
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
        	String my_bus_results[] = result.split(" ");
            bus_times.setText("Ώρες λεωφορείου:" + "\n" + "Το πρώτο που έρχεται : " + my_bus_results[0] + "\n" + "Αν το χάσεις έχει στις : " + my_bus_results[1]);
       }
        
        private String downloadUrl(String myurl, String bus_stop_name) throws IOException {
            //InputStream is = null;
            // Only display the first 500 characters of the retrieved
            // web page content.
           // int len = 500;
            try {
                URL url = new URL(myurl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                // Starts the query
                conn.connect();
                int response = conn.getResponseCode();
                Log.d(DEBUG_TAG, "The response is: " + response);
                
                
                 //no no no kai no//       
                //parser.getTextFromPage(1);
                /*PdfReader reader = new PdfReader("http://astikovolou.gr/documents/N--3.pdf");
                byte[] streamBytes = reader.getPageContent(1);
                PRTokeniser tokenizer = new PRTokeniser(new RandomAccessFileOrArray(new RandomAccessSourceFactory().createSource(streamBytes)));
                //PrintWriter out = new PrintWriter(new FileOutputStream(dest));
                while (tokenizer.nextToken()) {
                    if (tokenizer.getTokenType() == PRTokeniser.TokenType.STRING) {
                        System.out.println(tokenizer.getStringValue());
                    }
                }
                reader.close();
                
              //KALO ALLA EXEI THEMATA ME TON PINAKA 
                PdfReader reader = new PdfReader("http://astikovolou.gr/documents/N--3.pdf");
                
                PdfReaderContentParser parser = new PdfReaderContentParser(reader);
                //PrintWriter out = new PrintWriter(new FileOutputStream("c:/results.txt"));
                TextExtractionStrategy strategy;
                for (int i = 1; i <= reader.getNumberOfPages(); i++) {
                	System.out.println("printing....");
                    strategy = parser.processContent(i, new SimpleTextExtractionStrategy());
                    System.out.println(strategy.getResultantText());
                }
                reader.close();
           		
                
                //wraiooooo.........//
                try {
                    
                    PdfReader reader = new PdfReader("http://astikovolou.gr/documents/N--3.pdf");
                    System.out.println("This PDF has "+reader.getNumberOfPages()+" pages.");
                    String page = PdfTextExtractor.getTextFromPage(reader, 1);
                    System.out.println("Page Content:\n\n"+page+"\n\n");
                    System.out.println("Is this document tampered: "+reader.isTampered());
                    System.out.println("Is this document encrypted: "+reader.isEncrypted());
         
                } catch (IOException e) {
                    e.printStackTrace();
                }*/
               
                String bus_arrival_time = new Pdf_read().read_pdf(myurl,bus_stop_name);
                
       
                
                //GIA TON TEXTEDITOR
                //is = conn.getInputStream(); 
                //String contentAsString = readIt(is, len);
                return bus_arrival_time;
                
            // Makes sure that the InputStream is closed after the app is
            // finished using it.
            } finally {
                
            }
        }
        
        public String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
            Reader reader = null;
            reader = new InputStreamReader(stream, "UTF-8");        
            char[] buffer = new char[len];
            reader.read(buffer);
            return new String(buffer);
        }
    }

    /**************************************************************************************************************
   	gives your address given your location taken from the GPS and Wi-Fi													  
   	**************************************************************************************************************/
    public class GetAddressTask extends AsyncTask<Location, Void, String> {
    	Context mContext;
    	public GetAddressTask(Context context) {
    	    super();
    	    mContext = context;
    	}
    	
    	/**
    	 * Get a Geocoder instance, get the latitude and longitude
    	 * look up the address, and return it
    	 *
    	 * @params params One or more Location objects
    	 * @return A string containing the address of the current
    	 * location, or an empty string if no address can be found,
    	 * or an error message
    	 */
    	@Override
    	protected String doInBackground(Location... params) {
    	    Geocoder geocoder =
    	            new Geocoder(mContext, Locale.getDefault());
    	    // Get the current location from the input parameter list
    	    Location loc = params[0];
    	    // Create a list to contain the result address
    	    List<Address> addresses = null;
    	    //System.out.println("geocoder" + geocoder.isPresent());
    	   
	    	    try {
	    	        /*
	    	         * Return 1 address.
	    	         */
	    	    	 while (addresses==null || addresses.size() == 0) {
	    	        addresses = geocoder.getFromLocation(loc.getLatitude(),
	    	                loc.getLongitude(), 1);
	    	    	 }
	    	    } catch (IOException e1) {
                    Log.e("LocationSampleActivity",
                            "IO Exception in getFromLocation()");
                    e1.printStackTrace();
                    return ("No Internet Connection..You need Internet Connection.!");
                    //return ("IO Exception trying to get address");
	    	    } catch (IllegalArgumentException e2) {
	    	    // Error message to post in the log
	    	    String errorString = "Illegal arguments " +
	    	            Double.toString(loc.getLatitude()) +
	    	            " , " +
	    	            Double.toString(loc.getLongitude()) +
	    	            " passed to address service";
	    	    Log.e("LocationSampleActivity", errorString);
	    	    e2.printStackTrace();
	    	    return errorString;
	    	    }
    	    
    	    // If the reverse geocode returned an address
    	    if (addresses != null && addresses.size() > 0) {
    	        // Get the first address
    	        Address address = addresses.get(0);
    	        /*
    	         * Format the first line of address (if available),
    	         * city, and country name.
    	         */
    	        String addressText = String.format(
    	                "%s, %s, %s",
    	                // If there's a street address, add it
    	                address.getMaxAddressLineIndex() > 0 ?
    	                        address.getAddressLine(0) : "",
    	                // Locality is usually a city
    	                address.getLocality(),
    	                // The country of the address
    	                address.getCountryName());
    	        // Return the text
    	        return addressText;
    	    } else {
    	        return "No address found";
    	    }
    	}

    	 @Override
         protected void onPostExecute(String address) {
             // Set activity indicator visibility to "gone"
             //mActivityIndicator.setVisibility(View.GONE);
             // Display the results of the lookup.
             //mAddress.setText(address);
    		 if (Geocoder.isPresent()) {
    		 mAddress.setText("Your address is:\n" + address);
    		 }else {
    		 mAddress.setText("You Dont Have Geocoder...");
    		 }
         }
    }

    /**************************************************************************************************************
   	Get the address of the bus stop given the longitude and latitude												  
   	**************************************************************************************************************/
    public void Get_bus_stop_address(Context mContext, double Buslat, double Buslon) {
    	Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());
	    List<Address> addresses = null;
	    try {
	        addresses = geocoder.getFromLocation(Buslat,Buslon, 1);
	    }catch (IOException e1) {
    	    Log.e("LocationSampleActivity",
    	            "IO Exception in getFromLocation()");
    	    e1.printStackTrace();
    	    Bus_stop_address.setText("IO Error");
    	
	    } catch (IllegalArgumentException e2) {
		    // Error message to post in the log
		    String errorString = "Illegal arguments " +
            Double.toString(Buslat) +
            " , " +
            Double.toString(Buslon) +
            " passed to address service";
		    Log.e("LocationSampleActivity", errorString);
		    e2.printStackTrace();
		    Bus_stop_address.setText("IllegalArgument Error");
	    }
	    // If the reverse geocode returned an address
	    if (addresses != null && addresses.size() > 0) {
	        // Get the first address
	        Address address = addresses.get(0);
	        /*
	         * Format the first line of address (if available),
	         * city, and country name.
	         */
	        String addressText = String.format(
	                "%s, %s, %s",
	                // If there's a street address, add it
	                address.getMaxAddressLineIndex() > 0 ?
	                        address.getAddressLine(0) : "",
	                // Locality is usually a city
	                address.getLocality(),
	                // The country of the address
	                address.getCountryName());
	        // Return the text
	        Bus_stop_address.setText("Your bus stop address: \n" + addressText);
	    } else {
	    	Bus_stop_address.setText("No address found");
	    }
    	System.out.println(addresses);
    }
}

