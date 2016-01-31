package dktrotti.transitr;

import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.location.Location;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements
        OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener {
    private MapFragment mMapFragment;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private LocationRequest mLocationRequest;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMapFragment = MapFragment.newInstance();
        FragmentTransaction fragmentTransaction =
                getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.mainLayout, mMapFragment);
        fragmentTransaction.commit();
        mMapFragment.getMapAsync(this);

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        mLocationRequest.setInterval(5000);

        mHandler = new Handler();
        busUpdater.run();
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

        if (id == R.id.action_about) {
            new AlertDialog.Builder(this).setTitle(getString(R.string.about_box_title))
                    .setMessage(getString(R.string.about_box_message))
                    .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    })
                    .show();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int num) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {

    }

    @Override
    public void onMapReady(GoogleMap map) {
        MapManager.getInstance().setMap(mMapFragment.getMap());
        mMapFragment.getMap().getUiSettings().setMapToolbarEnabled(false);
    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        if (mLastLocation != null) {
            MapManager.getInstance().updateLocation(mLastLocation);
        }
    }

    public void onUpdateButtonClick(View view) {
        try {
            update();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void onLocationButtonClick(View view) {
        if (mLastLocation != null) {
            MapManager.getInstance().updateLocation(mLastLocation);
            MapManager.getInstance().moveCamera(mLastLocation);
            MapManager.getInstance().setZoom(13.0f);
        }
    }

    protected void startLocationUpdates() {
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    public void update() {
        HTTPGrabber grab = new HTTPGrabber();
        GTFSReader reader = new GTFSReader();

        grab.addObserver(reader);
        try {
            grab.retrieveFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Runnable busUpdater = new Runnable() {
        @Override
        public void run() {
            try {
                update();
            } finally {
                mHandler.postDelayed(busUpdater, 30000);
            }
        }
    };
}
