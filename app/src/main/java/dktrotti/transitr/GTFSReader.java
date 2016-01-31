package dktrotti.transitr;

import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.transit.realtime.GtfsRealtime;
import com.squareup.wire.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Reads GTFS data.
 *
 * Created by Dominic on 16-01-30.
 */
public class GTFSReader implements Observer {

    public void readData() {
        GtfsRealtime.FeedMessage feedMessage;
        try {
            feedMessage = GtfsRealtime.FeedMessage.parseFrom(new FileInputStream(HTTPGrabber.path));
        } catch (Exception e) {
            return;
        }
        GtfsRealtime.Position buspos;
        MapManager.getInstance().clearBusses();
        for (GtfsRealtime.FeedEntity bus: feedMessage.getEntityList()) {
            buspos = bus.getVehicle().getPosition();
            MapManager.getInstance().addBus(new LatLng(buspos.getLatitude(), buspos.getLongitude()));
        }
    }

    @Override
    public void update(Observable o, Object data) {
        readData();
    }
}
