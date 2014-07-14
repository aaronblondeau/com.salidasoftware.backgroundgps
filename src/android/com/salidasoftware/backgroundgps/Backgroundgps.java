package com.salidasoftware.backgroundgps;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.greenrobot.event.EventBus;

import android.content.Intent;
import android.content.Context;
import android.location.Location;
import android.util.Log;

//https://github.com/apache/cordova-android/blob/master/framework/src/org/apache/cordova/CordovaPlugin.java
public class Backgroundgps extends CordovaPlugin{

	//An EventBus is used to notify the application of Locations received by the background service
	private static EventBus bus;

	//Keep a reference to the callback to call when new locations are received
	private static CallbackContext locationCallbackContext;

	@Override
	public void initialize(CordovaInterface cordova, CordovaWebView webView) {
	    super.initialize(cordova, webView);
	    getBus();
	}

	/**
	 * Returns the plugin's event bus
	 */
	public static EventBus getBus(){
    	if(bus == null){
        	bus = new EventBus();
        }
    	return bus;
    }

    @Override
    public void onResume(boolean multitasking) {
    	super.onResume(multitasking);
    	Log.i("Backgroundgps", "~~ onResume");

    	//This does not get called when the app starts...

        //Log.i("Backgroundgps", "~~ register");
        // Backgroundgps.getBus().register(this);
    }

    @Override
    public void onPause(boolean multitasking) {
    	super.onPause(multitasking);
    	Log.i("Backgroundgps", "~~ onPause");

    	//This does get called when the app gets paused, so unregister from the bus

    	Log.i("Backgroundgps", "~~ unregister");
    	Backgroundgps.getBus().unregister(this);

    }

    public void onEvent(final LocationEvent event){
    	Log.i("Backgroundgps", "~~ onLocationEvent");
    	cordova.getThreadPool().execute(new Runnable() {
            public void run() {

            	//Send the new gps location to the success callback

            	JSONObject result = new JSONObject();
            	Location location = event.getLocation();
            	Log.i("Backgroundgps", "~~ onLocationEvent : "+location.getLatitude()+", "+location.getLongitude());
            	try {
            		result.put("latitude", location.getLatitude());
            		result.put("longitude", location.getLongitude());
            		if(location.hasAltitude()) {
            			result.put("altitude", location.getAltitude());
            		}
            	}
            	catch(JSONException je) {}

                PluginResult pr = new PluginResult(PluginResult.Status.OK, result);
                pr.setKeepCallback(true);
                locationCallbackContext.sendPluginResult(pr);

            }
        });
    }

	@Override
	public boolean execute(String action, JSONArray args, final CallbackContext callbackContext) throws JSONException {
		Context context = this.cordova.getActivity().getApplicationContext();

	    if ("start".equals(action)) {

	    	//Start the background GPS service
	    	
	    	locationCallbackContext = callbackContext;

	    	context.startService(new Intent(context, BackgroundgpsService.class));

            Log.i("Backgroundgps", "~~ register");
            Backgroundgps.getBus().register(this);

	    	cordova.getThreadPool().execute(new Runnable() {
                public void run() {

                	JSONObject result = new JSONObject();
                	try {
                		result.put("running", true);
	                }
	            	catch(JSONException je) {}

                    PluginResult pr = new PluginResult(PluginResult.Status.OK, result);
                    pr.setKeepCallback(true);
                    callbackContext.sendPluginResult(pr);

                    //callbackContext.success(result);
                }
            });
	        
	        return true;
	    }
	    else if ("stop".equals(action)) {

	    	//Stop the background GPS service

	    	locationCallbackContext = null;

	    	context.stopService(new Intent(context, BackgroundgpsService.class));

        	Log.i("Backgroundgps", "~~ unregister");
        	Backgroundgps.getBus().unregister(this);

	    	cordova.getThreadPool().execute(new Runnable() {
                public void run() {

                    JSONObject result = new JSONObject();
                    try {
                		result.put("running", false);
	                }
	            	catch(JSONException je) {}
                    callbackContext.success(result);
                }
            });
	        
	        return true;
	    }
	    else if ("status".equals(action)) {

	    	//Callback with the background service's status as well as the last known location (if it exists)

	    	cordova.getThreadPool().execute(new Runnable() {
                public void run() {
                	JSONObject result = new JSONObject();
                	try {
                		result.put("running", BackgroundgpsService.running);
                		Location location = BackgroundgpsService.last_location;
                		if(location != null) {
                			Log.i("Backgroundgps", "~~ status location : "+location.getLatitude()+", "+location.getLongitude());
                			JSONObject last_location = new JSONObject();
                			last_location.put("latitude", location.getLatitude());
            				last_location.put("longitude", location.getLongitude());
            				result.put("last_location", last_location);
            			}
            			else{
            				Log.i("Backgroundgps", "~~ status location was null");
            			}
	                }
	            	catch(JSONException je) {}
                    callbackContext.success(result);
                }
            });
	        
	        return true;
	    }
	    return false;  // Returning false results in a "MethodNotFound" error.
	}

}