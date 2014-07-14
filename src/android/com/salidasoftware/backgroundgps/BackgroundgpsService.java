package com.salidasoftware.backgroundgps;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.os.Handler;

public class BackgroundgpsService extends Service {

    private LocationManager locationManager;
    public static Location last_location = null;
    public static Boolean running = false;
    public static Boolean save_power = true;

    @Override
    public void onCreate() {
        super.onCreate();
        running = true;
        Log.i("BackgroundgpsService", "~~ Service onCreate");
        startLocating();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("BackgroundgpsService", "~~ Service onDestroy");
        stopLocating();
        running = false;
    }

    @Override
    public IBinder onBind(Intent intent){
        return null;
    }

    private void startLocating(){
        if(locationManager == null){

            String provider = LocationManager.GPS_PROVIDER;

            // get the LocationManager
            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

            // try and improve accuracy a bit by getting new aiding data
            if(save_power){
                Bundle bundle = new Bundle();
                locationManager.sendExtraCommand("gps", "force_xtra_injection", bundle);
                locationManager.sendExtraCommand("gps", "force_time_injection", bundle);
            }

            if(save_power){
                // listen for changes at a minimum of three seconds/5 meters
                locationManager.requestLocationUpdates(provider, 3000, 5, locationListener);
            }
            else{
                locationManager.requestLocationUpdates(provider, 0, 0, locationListener);
            }
        }
    }

    public void stopLocating(){
        if(locationManager != null){
            locationManager.removeUpdates(locationListener);
            locationManager = null;
        }
    }

    private final LocationListener locationListener = new LocationListener()
    {

        public void onLocationChanged(final Location location)
        {
        	if(location != null) {
	            last_location = location;
	            Log.i("BackgroundgpsService.LocationListener", "~~ Got location : "+location.getLatitude()+", "+location.getLongitude());
	            //Deliver the location to the EventBus so that other parts of the plugin can handle it
                Backgroundgps.getBus().post(new LocationEvent(last_location));
        	}
        }

        public void onProviderDisabled(String provider) {}

        public void onProviderEnabled(String provider) {}

        public void onStatusChanged(String provider, int status, Bundle extras) {}
    };

}
