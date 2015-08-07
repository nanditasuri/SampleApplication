package com.example.nandita.practiceapp1;

import android.app.Activity;
import android.graphics.Color;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.w3c.dom.Document;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


public class ShowMapActivity extends Activity implements OnMapReadyCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_map);

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_show_map, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {


        boolean dir = getIntent().getBooleanExtra("DIRECTIONS", false);
        googleMap.setMyLocationEnabled(true);
        googleMap.setMapType(googleMap.MAP_TYPE_NORMAL);
        googleMap.getUiSettings().setZoomControlsEnabled(true);

        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria c = new Criteria();
        String provider = lm.getBestProvider(c, true);
        Log.i("The provider is ", provider);

        if (dir == false) {


            Location l = lm.getLastKnownLocation(provider);
            if (l != null) {
                Log.i("The location is not  null", " location is found " + l.toString());
                LatLng currentPosition = new LatLng(l.getLatitude(), l.getLongitude());

               /* googleMap.addMarker(new MarkerOptions()
                                .position(currentPosition)
                                .title("Current Location"));*/

                googleMap.addMarker(new MarkerOptions()
                        .position(currentPosition)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                        .title("ME"));
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(currentPosition));
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                        currentPosition, 15));
            } else
                Log.i("The location is null", " could not find location");
               Toast.makeText(this,"Could not find your last location",Toast.LENGTH_LONG).show();
        }
        else{

            String  toLoc = getIntent().getStringExtra("TO_LOCATION");
            String  frmLoc = getIntent().getStringExtra("FROM_LOCATION");

            Toast.makeText(this,"Directions for the location from "+frmLoc,Toast.LENGTH_LONG).show();
            Geocoder gc = new Geocoder(this);
            List<Address> toLocs,frmLocs;

           try {

               toLocs = gc.getFromLocationName(toLoc, 1);
               frmLocs = gc.getFromLocationName(frmLoc, 1);
               Log.i("ShowMap","Calling findDirections");

               findDirections(frmLocs.get(0).getLatitude(),frmLocs.get(0).getLongitude(),toLocs.get(0).getLatitude(),
                       toLocs.get(0).getLongitude(),"driving");
               LatLng src = new LatLng(frmLocs.get(0).getLatitude(),frmLocs.get(0).getLongitude());
               LatLng dest = new LatLng(toLocs.get(0).getLatitude(),toLocs.get(0).getLongitude());

               googleMap.addMarker(new MarkerOptions()
                       .position(src)
                       .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                       .title("Source"));
               googleMap.addMarker(new MarkerOptions()
                       .position(dest)
                       .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                       .title("Destination"));

               LatLngBounds.Builder builder = new LatLngBounds.Builder();
               builder.include(src);
               builder.include(dest);

               googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(),10));
               googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(),10));
           }catch(Exception e ){
                e.printStackTrace();
           }

        }
    }
    public void findDirections(double fromPositionDoubleLat, double fromPositionDoubleLong, double toPositionDoubleLat, double toPositionDoubleLong, String mode)
    {
        Log.i("ShowMap","in finding directions");

        Map<String, String> map = new HashMap<String, String>();
        map.put("user_current_lat", String.valueOf(fromPositionDoubleLat));
        map.put("user_current_long", String.valueOf(fromPositionDoubleLong));
        map.put("destination_lat", String.valueOf(toPositionDoubleLat));
        map.put("destination_long", String.valueOf(toPositionDoubleLong));
        map.put("driving", mode);
        Log.i("ShowMap","Calling asynctask");

        GetDirectionsAsyncTask asyncTask = new GetDirectionsAsyncTask(this);
        asyncTask.execute(map);
    }
    public void handleGetDirectionsResult(ArrayList directionPoints)
    {
        Log.i("ShowMap", "in handlegetdirections");

        Polyline newPolyline;
        GoogleMap mMap = ((MapFragment)getFragmentManager().findFragmentById(R.id.map)).getMap();
        PolylineOptions rectLine = new PolylineOptions().width(8).color(Color.BLUE);
        for(int i = 0 ; i < directionPoints.size() ; i++)
        {
            rectLine.add((LatLng) directionPoints.get(i));
        }
        newPolyline = mMap.addPolyline(rectLine);
    }
}












/*try {
                List<Address> la;
                la = gc.getFromLocationName(toLoc, 1);

                LatLng destPosition = new LatLng(la.get(0).getLatitude(), la.get(0).getLongitude());

                Location l = lm.getLastKnownLocation(provider);
                if (l != null) {
                    Log.i("The location is not  null", " location is found " + l.toString());
                    LatLng currentPosition = new LatLng(l.getLatitude(), l.getLongitude());
                    googleMap.addMarker(new MarkerOptions()
                            .position(currentPosition)
                            .title("Current Location"));

                }
                googleMap.addMarker(new MarkerOptions()
                        .position(destPosition)
                        .title("Destination Location"));

                 googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                        destPosition, 10));


            }catch(Exception e){
                Log.i("Map","In exception");

                e.printStackTrace();
            }*/

/*            try {

                List<Address> la;
                la = gc.getFromLocationName(toLoc, 1);
                Location l = lm.getLastKnownLocation(provider);
                String url = "http://maps.googleapis.com/maps/api/directions/xml?"
                        + "origin=" + la.get(0).getLatitude() + "," + la.get(0).getLongitude()
                        + "&destination=" + l.getLatitude() + "," + l.getLongitude()
                        + "&sensor=false&units=metric&mode="+"driving";

                HttpClient httpClient = new DefaultHttpClient();
                HttpContext localContext = new BasicHttpContext();
                HttpPost httpPost = new HttpPost(url);
                HttpResponse response = httpClient.execute(httpPost, localContext);
                InputStream in = response.getEntity().getContent();
                DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                Document doc = builder.parse(in);
                ArrayList<LatLng> directionPoint = md.getDirection(doc);
                PolylineOptions rectLine = new PolylineOptions().width(3).color(Color.RED);

                for(int i = 0 ; i < directionPoint.size() ; i++) {
                    rectLine.add(directionPoint.get(i));
                }

                googleMap.addPolyline(rectLine);






            } catch (Exception e) {
                e.printStackTrace();
            }*/
