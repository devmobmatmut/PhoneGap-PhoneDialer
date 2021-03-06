package com.devmobmatmut.phonedialer;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.Manifest;
import android.content.pm.PackageManager;

public class PhoneDialer extends CordovaPlugin {
	public static final int CALL_REQ_CODE = 0;
	public static final int PERMISSION_DENIED_ERROR = 20;
  	public static final String CALL_PHONE = Manifest.permission.CALL_PHONE;
  	
  	private CallbackContext callbackContext;
  	private JSONArray executeArgs;
  	
	@Override
	public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
		this.callbackContext = callbackContext;
    		this.executeArgs = args;
		if (cordova.hasPermission(CALL_PHONE)) {
			callPhone(args);		
		} else {
			cordova.requestPermission(this, CALL_REQ_CODE, CALL_PHONE);	
		}
		return true;
	}
	
	public void onRequestPermissionResult(int requestCode, String[] permissions, int[] grantResults) throws JSONException {
	    for (int r : grantResults) {
	      if (r == PackageManager.PERMISSION_DENIED) {
	        this.callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.ERROR, PERMISSION_DENIED_ERROR));
	        return;
	      }
	    }
	    switch (requestCode) {
	      case CALL_REQ_CODE:
	        callPhone(executeArgs);
	        break;
	    }
	}
	
	private void callPhone(JSONArray args) throws JSONException {
		try {
		    	String phoneNumber = args.getString(0);
		    	Uri uri = Uri.parse("tel:"+phoneNumber);
	                Intent callIntent = new Intent(Intent.ACTION_CALL);
	                callIntent.setData(uri);
	                this.cordova.getActivity().startActivity(callIntent);
	                callbackContext.success();
	            } catch (Exception e) {
	        	String msg = "Exception Dialing Phone Number: " + e.getMessage();
	        	System.err.println(msg);
		        callbackContext.error(msg);
	            }
	}
}
