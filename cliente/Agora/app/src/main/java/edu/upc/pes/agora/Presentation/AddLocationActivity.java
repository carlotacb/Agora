package edu.upc.pes.agora.Presentation;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import edu.upc.pes.agora.R;

public class AddLocationActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {

    private GoogleMap mMap;
    private double lat;
    private double lng;
    private Button savePos;
    private Button cancelPos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_location);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        savePos = (Button) findViewById(R.id.btnSavePos);
        cancelPos = (Button) findViewById(R.id.btnCancelPos);

        savePos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), ProposalsActivity.class);
                if (getIntent().hasExtra("Title")){
                    i.putExtra("Title", getIntent().getStringExtra("Title"));
                }
                if (getIntent().hasExtra("Description")){
                    i.putExtra("Description", getIntent().getStringExtra("Description"));
                }
                i.putExtra("lat", lat);
                i.putExtra("lng", lng);
                startActivity(i);
            }
        });

        cancelPos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), ProposalsActivity.class);
                if (getIntent().hasExtra("Title")){
                    i.putExtra("Title", getIntent().getStringExtra("Title"));
                }
                if (getIntent().hasExtra("Description")){
                    i.putExtra("Description", getIntent().getStringExtra("Description"));
                }
                startActivity(i);
            }
        });
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if(gpsAvailable()){
            //If current location is available show in map
            LocationManager locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 0, this);
            LatLng currentLoc = new LatLng(lat, lng);
            if (lat != 0 && lng != 0) {
                mMap.addMarker(new MarkerOptions().position(currentLoc).title("Your location"));
                moveCamera(currentLoc);
            }
        }else {
            //Else show Barcelona -> Barrio?
            lat = 41;
            lng = 2;
            LatLng bcn = new LatLng(lat, lng);
            mMap.addMarker(new MarkerOptions().position(bcn).title("Marker in Barcelona"));
            moveCamera(bcn);
        }


        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                lat = latLng.latitude;
                lng = latLng.longitude;
                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(latLng));
                moveCamera(latLng);
            }
        });

    }

    private void moveCamera (LatLng pos){
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pos,13));
        mMap.animateCamera(CameraUpdateFactory.zoomIn());
        mMap.animateCamera(CameraUpdateFactory.zoomTo(13), 2000, null);
    }

    private Boolean gpsAvailable() {
        ContentResolver contentResolver = getBaseContext().getContentResolver();
        boolean gpsStatus = Settings.Secure.isLocationProviderEnabled(contentResolver, LocationManager.GPS_PROVIDER);
        return gpsStatus;
    }

    @Override
    public void onLocationChanged(Location location) {
        this.setLat(location.getLatitude());
        this.setLng(location.getLongitude());
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
}
