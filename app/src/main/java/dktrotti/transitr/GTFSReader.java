package dktrotti.transitr;

import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.transit.realtime.GtfsRealtime;
import com.squareup.wire.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;

/**
 * Reads GTFS data.
 *
 * Created by Dominic on 16-01-30.
 */
public class GTFSReader implements Observer {

    public void readData() {
        GtfsRealtime.FeedMessage vehicleFeedMessage;
        GtfsRealtime.FeedMessage tripFeedMessage;

        try {
            vehicleFeedMessage = GtfsRealtime.FeedMessage.parseFrom(new FileInputStream(HTTPGrabber.vehiclePath));
            tripFeedMessage = GtfsRealtime.FeedMessage.parseFrom(new FileInputStream(HTTPGrabber.tripPath));
        } catch (Exception e) {
            return;
        }

        HashMap<String, String> trips = new HashMap<>();
        HashMap<String, Integer> delays = new HashMap<>();
        for (GtfsRealtime.FeedEntity trip: tripFeedMessage.getEntityList()) {
            trips.put(trip.getId(), trip.getTripUpdate().getTrip().getRouteId());
            if (trip.getTripUpdate().getDelay() > 0) {
                delays.put(trip.getId(), trip.getTripUpdate().getDelay());
            }
        }

        GtfsRealtime.Position buspos;
        MapManager.getInstance().clearBusses();
        for (GtfsRealtime.FeedEntity bus: vehicleFeedMessage.getEntityList()) {
            buspos = bus.getVehicle().getPosition();
            if (delays.containsKey(bus.getVehicle().getTrip().getTripId())) {
                MapManager.getInstance().addBus(new LatLng(buspos.getLatitude(), buspos.getLongitude()),
                        trips.get(bus.getVehicle().getTrip().getTripId()),
                        delays.get(bus.getVehicle().getTrip().getTripId()));
            } else {
                MapManager.getInstance().addBus(new LatLng(buspos.getLatitude(), buspos.getLongitude()),
                        trips.get(bus.getVehicle().getTrip().getTripId()));
            }
        }
    }

    @Override
    public void update(Observable o, Object data) {
        readData();
    }
}
