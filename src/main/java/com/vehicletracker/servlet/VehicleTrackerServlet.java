package com.vehicletracker.servlet;


import com.vehicletracker.dao.GetsSets;
import com.vehicletracker.dao.VehicleTrackerDao;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;

@WebServlet("/tracker-mvc")
public class VehicleTrackerServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */

    public VehicleTrackerServlet() {
        super();
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    /**
     * @see HttpServlet@doGet(HttpServletRequest, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Double lat_for_map = 0.0;
        Double long_for_map = 0.0;

        if (request.getParameter("veh_id") != null) {
            VehicleTrackerDao vtd_formap = new VehicleTrackerDao(request.getParameter("veh_id"), request.getParameter("date_val"));
            Iterator<GetsSets> igs_formap = vtd_formap.getResultIterator();

            while (igs_formap.hasNext()) {
                GetsSets location_formap = igs_formap.next();

                lat_for_map = location_formap.getLatitude();
                long_for_map = location_formap.getLongitude();
            }
        }
        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<HTML><HEAD><TITLE>Track a Vehicle</TITLE>");
        out.println("<meta name=\"viewport\" content=\"initial-scale=1.0, user-scalable=no\"/>");
        out.println("</HEAD>");
        out.println("<BODY>");
        out.println("<h1>Vehicle Tracker on Cassandra</h1>");
        out.println("Enter a track date and id of the vehicle you want to track");
        out.println("<p>&nbsp;</p>");
        out.println("<form id = \"form1\" name = \"form1\" method = \"get\" action = \"\">");
        out.println("<table>");
        out.println("<tr><td>Date (e.g. 2014-05-19):</td>");
        out.println("<td><input type = \"text\" name = \"date_val\" id = \"date_val\" /></td></tr>");
        out.println("<tr><td>Vehicle id (e.g. FLN78197):</td>");
        out.println("<td><input type = \"text\" name = \"veh_id\" id = \"veh_id\" /></td></tr>");
        out.println("<tr><td></td><td><input type = \"submit\" name = \"submit\" id = \"submit\" value =\"Submit\"/></td></tr>");
        out.println("</table>");
        out.println("</form>");
        out.println("<p>&nbsp;</p>");

        if(request.getParameter("veh_id") != null) {
            // blank
            VehicleTrackerDao vtd = new VehicleTrackerDao(request.getParameter("veh_id"), request.getParameter("date_val"));
            Iterator<GetsSets> igs = vtd.getResultIterator();

            if (igs.hasNext()) {
                out.println("<hr/>");
                out.println("<table cellpadding = \"4\">");
                out.println("<tr><td colspan = \"3\"><h2>" + request.getParameter("veh_id") + "</h2></td></tr>");
                out.println("<tr><td><b>Date and Time</b></td><td><b>Latitude</b></td><td><b>Longitude</b></td></tr>");

                while (igs.hasNext()) {
                    GetsSets location = igs.next();

                    out.println("<tr>");
                    out.println("<td>" + location.getTime() + "</td>");
                    out.println("<td>" + location.getLatitude() + "</td>");
                    out.println("<td>" + location.getLongitude() + "</td>");
                    out.println("</tr>");
                }

                out.println("</table>");
                out.println("<div id=\"map_canvas\" style=\"width:500px; height:500px\"></div>");
                out.println("<script type=\"text/javascript\"> function initMap() { ");
                out.println("var latlng = new google.maps.LatLng(" + lat_for_map + ", " + long_for_map + "); ");
                out.println("var settings = { zoom: 10, center: latlng, mapTypeControl: true, mapTypeControlOptions: {style: google.maps.MapTypeControlStyle.DROPDOWN_MENU}, navigationControl: true, navigationControlOptions: {style: google.maps.NavigationControlStyle.SMALL}, mapTypeId: google.maps.MapTypeId.ROADMAP}; ");
                out.println("var map = new google.maps.Map(document.getElementById(\"map_canvas\"), settings); ");
                out.println("var companyPos = new google.maps.LatLng(" + lat_for_map + ", " + long_for_map + "); ");
                out.println("var companyMarker = new google.maps.Marker({ position: companyPos, map: map, title:\"Vehicle\" }); ");
                out.println("} </script>");
                out.println("<script async defer src=\"https://maps.googleapis.com/maps/api/js?key="+ AppConfigs.GOOGLE_MAP_KEY+"&callback=initMap\"> </script>");
            }
        }
        else {
            out.println("<hr/>");
            out.println("<p>&nbsp;</p>");
            out.println("Sorry, no results for vehicle id " + request.getParameter("veh_id") + " for " + request.getParameter("date_val"));
        }

        out.println("</BODY></HTML>");

    }
}
