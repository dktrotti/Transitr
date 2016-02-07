package dktrotti.transitr;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.transit.realtime.GtfsRealtime;

/**
 * Represents a single bus
 *
 * Created by Dominic on 16-02-07.
 */
public class Bus {

    public String getRoute() {
        return "";
    }

    public GtfsRealtime.FeedEntity getTrip() {

    }

    public String getTripId() {
        return getTrip().getId();
    }

    public LatLng getLatLng() {
        return new LatLng(0, 0);
    }

    public Marker getMarker() {

    }

    public void setMarker(Marker marker) {

    }
}
