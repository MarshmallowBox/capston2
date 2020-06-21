package com.example.capstone;

import android.location.Location;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.NaverMap;

public  class Distance {
    //Location, LatLng
    public static double getDistance(Location location, LatLng latLng) {
        double theta = latLng.longitude - location.getLongitude();
        double dist = Math.sin(deg2rad(latLng.latitude)) * Math.sin(deg2rad(location.getLatitude()))
                + Math.cos(deg2rad(latLng.latitude)) * Math.cos(deg2rad(location.getLatitude())) * Math.cos(deg2rad(theta));

        return ditanceMeter(dist);
    }

    //Location, Location
    public static double getDistance(Location location1, Location location2) {
        double theta = location1.getLongitude() - location2.getLongitude();
        double dist = Math.sin(deg2rad(location1.getLatitude())) * Math.sin(deg2rad(location2.getLatitude()))
                + Math.cos(deg2rad(location1.getLatitude())) * Math.cos(deg2rad(location2.getLatitude())) * Math.cos(deg2rad(theta));

        return ditanceMeter(dist);
    }

    //LatLng, LatLng
    public static double getDistance(LatLng latLng1, LatLng latLng2) {
        double theta = latLng1.longitude - latLng2.longitude;
        double dist = Math.sin(deg2rad(latLng1.latitude)) * Math.sin(deg2rad(latLng2.latitude))
                + Math.cos(deg2rad(latLng1.latitude)) * Math.cos(deg2rad(latLng2.latitude)) * Math.cos(deg2rad(theta));

        return ditanceMeter(dist);
    }

    // This function converts decimal degrees to radians
    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    // This function converts radians to decimal degrees
    private static double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }


    private static double ditanceMeter(double dist) {
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        dist = dist * 1609.344; //미터단위로 변환

        return dist;
    }

    public static int getClusterDist(NaverMap naverMap) {
        double zoomLevel = naverMap.getCameraPosition().zoom;
        if (zoomLevel < 14) {
            return 500;
        } else if (zoomLevel < 15) {
            return 250;
        } else if (zoomLevel < 16) {
            return 150;
        } else if (zoomLevel < 17) {
            return 50;
        } else if (zoomLevel < 18) {
            return 25;
        } else
            return 1;

    }
}
