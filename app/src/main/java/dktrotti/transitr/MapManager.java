package dktrotti.transitr;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

/**
 * Singleton for providing a single access point to the map
 *
 * Created by Dominic on 16-01-30.
 */
public class MapManager {
    private static MapManager ourInstance = new MapManager();

    private GoogleMap map;
    private BitmapDescriptor busIcon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED);
    private ArrayList<Marker> busMarkers;
    private Marker userMarker;

    public static MapManager getInstance() {
        return ourInstance;
    }

    private MapManager() {
        busMarkers = new ArrayList<>();
    }

    public void setMap(GoogleMap map) {
        this.map = map;
    }

    public void addBus(LatLng latLng) {
        if (map == null) {
            return;
        }
        MarkerOptions options = new MarkerOptions()
                .position(latLng)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.bus))
                .anchor(0.5f, 0.5f);
        busMarkers.add(map.addMarker(options));
    }

    public void addBus(LatLng latLng, String route) {
        if (map == null) {
            return;
        }
        MarkerOptions options = new MarkerOptions()
                .position(latLng)
                .title(route)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.bus))
                .anchor(0.5f, 0.5f);
        busMarkers.add(map.addMarker(options));
    }

    public void addBus(LatLng latLng, String route, int delay) {
        if (map == null) {
            return;
        }
        MarkerOptions options = new MarkerOptions()
                .position(latLng)
                .title(route)
                .snippet("Delay: " + ((Integer) (delay / 60)).toString() + " mins")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.bus))
                .anchor(0.5f, 0.5f);
        busMarkers.add(map.addMarker(options));
    }

    public void clearBusses() {
        for (Marker bus: busMarkers) {
            bus.remove();
        }
    }

    public void updateLocation(Location location) {
        if (map == null) {
            return;
        }
        if (userMarker != null) {
            userMarker.remove();
        }
        MarkerOptions options = new MarkerOptions()
                .position(new LatLng(location.getLatitude(), location.getLongitude()))
                .title("You")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
        userMarker = map.addMarker(options);
    }

    public void moveCamera(Location location) {
        if (map == null) {
            return;
        }
        map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude())));
    }

    public void setZoom(float zoom) {
        if (map == null) {
            return;
        }
        map.moveCamera(CameraUpdateFactory.zoomTo(zoom));
    }
}
