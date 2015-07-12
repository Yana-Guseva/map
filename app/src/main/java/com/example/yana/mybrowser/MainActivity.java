package com.example.yana.mybrowser;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.yana.mybrowser.util.Util;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class MainActivity extends ActionBarActivity implements OnMapReadyCallback, LocationListener {

    GoogleMap map;
    SupportMapFragment mapFragment;
    LocationManager locationManager;
    String provider;
    Circle mCircle;
    Marker mMarker;
    private static final long MIN_TIME = 10;
    private static final float MIN_DISTANCE = 100;
    final String TAG = "myLogs";
    final int radius = 1000; //metres

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        map = mapFragment.getMap();
        if (map == null) {
            finish();
            return;
        }

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, true);

        Location lastKnownLocation = locationManager.getLastKnownLocation(provider);
        if(lastKnownLocation != null) {
            LatLng latLng = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
            //map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));
            locationManager.removeUpdates(this);
        }

//        GoogleMap.OnMyLocationChangeListener locationListener = new GoogleMap.OnMyLocationChangeListener() {
//            @Override
//            public void onMyLocationChange(Location location) {
//                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
//                map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));
//                map.addMarker(new MarkerOptions()
//                        .title("You")
//                        .snippet("You are here.")
//                        .position(latLng));
//            }
//        };

//        map.setOnMyLocationChangeListener(locationListener);

            map.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                if(mCircle == null || mMarker == null){
                    drawMarkerWithCircle(latLng);
                }else{
                    updateMarkerWithCircle(latLng);
                }
            }
        });
        mapFragment.getMapAsync(this);
    }

    private void updateMarkerWithCircle(LatLng position) {
        mCircle.setCenter(position);
        mMarker.setPosition(position);
    }

    private void drawMarkerWithCircle(LatLng position){
        double radiusInMeters = radius;
        int strokeColor = 0xffff0000; //red outline
        int shadeColor = 0x44ff0000; //opaque red fill

        CircleOptions circleOptions = new CircleOptions().center(position).radius(radiusInMeters).fillColor(shadeColor).strokeColor(strokeColor).strokeWidth(1);
        mCircle = map.addCircle(circleOptions);

        MarkerOptions markerOptions = new MarkerOptions().position(position);
        mMarker = map.addMarker(markerOptions);
    }

    @Override
    protected void onResume() {
        super.onResume();
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME, MIN_DISTANCE, this);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME, MIN_DISTANCE, this);
        //Toast.makeText(this, "Selected Provider " + provider, Toast.LENGTH_SHORT).show();
        int zoom = calculateZoomLevel(getWindow().getWindowManager().getDefaultDisplay().getWidth(), radius);
        CameraUpdate z = CameraUpdateFactory.zoomTo(zoom);
        map.moveCamera(z);
        Log.d("LOG", "" + Util.getRadius(this));
        //map.animateCamera(z);
    }

    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng sydney = new LatLng(-33.867, 151.206);
        map.setMyLocationEnabled(true);
        map.addMarker(new MarkerOptions()
                .title("Sydney")
                .snippet("The most populous city in Australia.")
                .position(sydney));
    }

    @Override
    public void onLocationChanged(Location location) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
//        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));
        map.addMarker(new MarkerOptions()
                .title("You")
                .snippet("You are here.")
                .position(latLng));


        map.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        CameraUpdate center = CameraUpdateFactory
                .newLatLng(latLng);
//        CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);
        map.moveCamera(center);
//        map.animateCamera(zoom);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d(TAG, "Status changed: " + provider);
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d(TAG, "enable " + provider);
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d(TAG, "disable " + provider);
    }

    private int calculateZoomLevel(double screenWidth, int radius) {
        double equatorLength = 6378140; // in meters
        double metersPerPixel = equatorLength / 256;
        int zoomLevel = 1;
        while ((metersPerPixel * screenWidth) > (2*radius)) {
            metersPerPixel /= 2;
            ++zoomLevel;
        }
        if((metersPerPixel * screenWidth) < (2*radius)) {
            zoomLevel--;
        }
        Log.i("ADNAN", "zoom level = "+zoomLevel);
        return zoomLevel;
    }
}
