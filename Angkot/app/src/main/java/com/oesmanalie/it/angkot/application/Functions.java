package com.oesmanalie.it.angkot.application;

import android.app.VoiceInteractor;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.oesmanalie.it.angkot.Constant;
import com.oesmanalie.it.angkot.models.Points;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

public class Functions {
    public static Points getClosestPoint(List<Points> coordList, LatLng destinationPoint ) {
        float closestDistance = 1000;
        Points closestPoint = new Points();
        double zero = 0;
        closestPoint.setLat( zero );
        closestPoint.setLon( zero );
        for (Points point : coordList) {
            //Log.d("point", point.getLat() + "-" + point.getLon());
            float distance = getTripDistance(new LatLng(point.getLat(), point.getLon()), destinationPoint );
            if (distance < closestDistance) {
                closestPoint = point;
                closestDistance = distance;
            }
        }
        return closestPoint;
    }

    public static List<Points> getListClosestPoint(List<Points> coordinateList, LatLng destinationPoint ) {
        List<Points> closestPoint = new ArrayList<>();
        List<String> lynName = new ArrayList<>();
        float limitClosestDistance = Constant.LIMIT_CLOSESTDISTANCE;
        for (Points point : coordinateList) {
            float distance = getTripDistance(new LatLng(point.getLat(), point.getLon()), destinationPoint );
            if (distance < limitClosestDistance && !(lynName.contains(point.getLyn())) ) {
                closestPoint.add(point);
                lynName.add(point.getLyn());
            }
        }
        return closestPoint;
    }

    public static List<Points> getListClosestPoint2(List<Points> coordinateList, LatLng destinationPoint ) {
        List<Points> closestPoint = new ArrayList<>();
        List<String> lynName = new ArrayList<>();
        Map<String, Points> objClosestPoint = new HashMap<>();

        float limitClosestDistance = Constant.LIMIT_CLOSESTDISTANCE;
        for (Points point : coordinateList) {

            if( objClosestPoint.containsKey(point.getLyn()) ) {
                float distance = getTripDistance(new LatLng(point.getLat(), point.getLon()), destinationPoint );
                float lastDistance = getTripDistance( objClosestPoint.get(point.getLyn()).getLatLng(), destinationPoint);
                if( distance < lastDistance ) {
                    objClosestPoint.put(point.getLyn(), point);
                }
            } else {
                float distance = getTripDistance(new LatLng(point.getLat(), point.getLon()), destinationPoint );
                if (distance < limitClosestDistance ) {
                    //closestPoint.add(point);
                    //lynName.add(point.getLyn());
                    objClosestPoint.put(point.getLyn(), point);
                }
            }

        }

        Set<String> keys = objClosestPoint.keySet();
        for (String key : keys) {
            closestPoint.add( objClosestPoint.get(key) );
        }
        return closestPoint;
    }

    public static float getTripDistance(LatLng routePoint, LatLng compared) {
        Location tLoc1 = new Location("");
        Location tLoc2 = new Location("");

        tLoc1.setLatitude(routePoint.latitude);
        tLoc1.setLongitude(routePoint.longitude);

        tLoc2.setLatitude(compared.latitude);
        tLoc2.setLongitude(compared.longitude);

        return tLoc1.distanceTo(tLoc2);
    }

    public static BitmapDescriptor getMarkerIconFromDrawable(Drawable drawable) {
        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    public static BitmapDescriptor generateBitmapDescriptorFromRes(
            Context context, int resId) {
        Drawable drawable = ContextCompat.getDrawable(context, resId);
        drawable.setBounds(
                0,
                0,
                drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(
                drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    public static double funGetDecimal( Double[] dms ){
        double decimal;
        decimal = ((dms[1] * 60)+ dms[2]) / (60*60);
        return dms[0] + decimal;
    }
}
