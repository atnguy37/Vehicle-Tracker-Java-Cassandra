package com.vehicletracker.dao;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

import java.util.Iterator;
import java.util.LinkedList;

public class VehicleTrackerDao {
    private Session session = null;
    private ResultSet result = null;
    private LinkedList <GetsSets> resultList;

    public VehicleTrackerDao(String vehicle_id, String trackDate) {
        getData(vehicle_id, trackDate);

    }

    protected void getData(String vehicle_id, String trackDate) {
        session = CassandraAccess.getInstance();
        String query = "SELECT time, latitude, longtitude FROM vehicle_tracker.location WHERE vehicle_id = '"
                 + vehicle_id + "' AND date = '" + trackDate + "'";
        result = session.execute(query);
        resultList = new LinkedList<GetsSets>();

        for (Row row: result) {
            GetsSets location = new GetsSets();
            location.setTime(String.valueOf(row.getTimestamp("time")));
            location.setLatitude(row.getDouble("latitude"));
            location.setLongitude(row.getDouble("longtitude"));
            resultList.add(location);
        }
        System.out.println(query);

    }

    public Iterator<GetsSets> getResultIterator() {
        return resultList.iterator();
    }
}
