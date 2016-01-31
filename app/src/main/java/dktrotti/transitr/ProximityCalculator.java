package dktrotti.transitr;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;

/**
 * Calculates information to be sent to Pebble.
 *
 * Created by Dominic on 16-01-31.
 */
public class ProximityCalculator {
    static public double calculateDistance(LatLng latLng1, LatLng latLng2) {
        Location l1 = new Location("L1");
        l1.setLatitude(latLng1.latitude);
        l1.setLongitude(latLng1.longitude);
        Location l2 = new Location("L2");
        l2.setLatitude(latLng2.latitude);
        l2.setLongitude(latLng2.longitude);

        return l1.distanceTo(l2);
    }

    static public String getAddress(LatLng latLng, Geocoder geocoder) throws IOException {
        List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
        return addresses.get(0).getAddressLine(0);
    }
}
