package com.yosoftware.plugins.webviewcontroller;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;

@CapacitorPlugin(name = "WebViewController")
public class WebViewControllerPlugin extends Plugin {
    private static final String TAG = "WebViewController";
    private WebViewController implementation;

    @Override
    public void load() {
        try {
            // Initialize the implementation with context
            implementation = new WebViewController(getContext());
            
            ((Application) getContext().getApplicationContext())
                .registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
                    @Override
                    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

                        // Apply settings to this new activity
                        implementation.applySettingsToActivity(activity);
                    }
                    
                    @Override
                    public void onActivityStarted(Activity activity) {
                        // Not needed for debugging
                    }
                    
                    @Override
                    public void onActivityResumed(Activity activity) {
                        // Re-apply settings when activity is resumed
                        implementation.applySettingsToActivity(activity);
                    }
                    
                    @Override
                    public void onActivityPaused(Activity activity) {
                        // Not needed for debugging
                    }
                    
                    @Override
                    public void onActivityStopped(Activity activity) {
                        // Not needed for debugging
                    }
                    
                    @Override
                    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
                        // Not needed for debugging
                    }
                    
                    @Override
                    public void onActivityDestroyed(Activity activity) {
                        // Not needed for debugging
                    }
                });
            
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "Error loading plugin", e);
        }
    }
    
    
    @PluginMethod
    public void setKeepScreenAwake(PluginCall call) {
        try {
            Boolean enable = call.getBoolean("enable", false);
            implementation.setKeepScreenAwake(enable);
            
            // Try to apply to current activity
            Activity currentActivity = getBridge().getActivity();
            if (currentActivity != null) {
                implementation.applySettingsToActivity(currentActivity);
            } else {
                System.out.println("WEBVIEW_CONTROLLER: Current activity is null");
            }
            call.resolve();
        } catch (Exception e) {
            e.printStackTrace();
            call.reject("Error setting keep screen awake", e);
        }
    }
    
    @PluginMethod
    public void getSettings(PluginCall call) {
        try {
            JSObject settings = new JSObject();
            settings.put("keepScreenOn", implementation.getKeepScreenAwake());
            
            call.resolve(settings);
        } catch (Exception e) {
            call.reject("Error getting settings", e);
        }
    }
}