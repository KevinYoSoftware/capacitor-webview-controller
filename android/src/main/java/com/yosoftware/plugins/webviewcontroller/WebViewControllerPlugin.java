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
            settings.put("isDeviceOwner", implementation.isDeviceOwner());
            settings.put("lockTaskActive", implementation.isLockTaskActive());

            call.resolve(settings);
        } catch (Exception e) {
            call.reject("Error getting settings", e);
        }
    }

    @PluginMethod
    public void isDeviceOwner(PluginCall call) {
        try {
            JSObject result = new JSObject();
            result.put("value", implementation.isDeviceOwner());
            call.resolve(result);
        } catch (Exception e) {
            e.printStackTrace();
            call.reject("Error checking device owner status", e);
        }
    }

    @PluginMethod
    public void setLockTaskPackages(PluginCall call) {
        try {
            boolean success = implementation.setLockTaskPackages();
            JSObject result = new JSObject();
            result.put("value", success);
            call.resolve(result);
        } catch (Exception e) {
            e.printStackTrace();
            call.reject("Error setting lock task packages", e);
        }
    }

    @PluginMethod
    public void startLockTask(PluginCall call) {
        try {
            Activity currentActivity = getBridge().getActivity();
            if (currentActivity != null) {
                boolean success = implementation.startLockTask(currentActivity);
                if (success) {
                    JSObject result = new JSObject();
                    result.put("value", true);
                    call.resolve(result);
                } else {
                    call.reject("Failed to start Lock Task Mode");
                }
            } else {
                call.reject("Activity not available");
            }
        } catch (Exception e) {
            e.printStackTrace();
            call.reject("Error starting Lock Task Mode", e);
        }
    }

    @PluginMethod
    public void stopLockTask(PluginCall call) {
        try {
            Activity currentActivity = getBridge().getActivity();
            if (currentActivity != null) {
                boolean success = implementation.stopLockTask(currentActivity);
                if (success) {
                    JSObject result = new JSObject();
                    result.put("value", true);
                    call.resolve(result);
                } else {
                    call.reject("Failed to stop Lock Task Mode");
                }
            } else {
                call.reject("Activity not available");
            }
        } catch (Exception e) {
            e.printStackTrace();
            call.reject("Error stopping Lock Task Mode", e);
        }
    }

    @PluginMethod
    public void isLockTaskActive(PluginCall call) {
        try {
            JSObject result = new JSObject();
            result.put("value", implementation.isLockTaskActive());
            call.resolve(result);
        } catch (Exception e) {
            e.printStackTrace();
            call.reject("Error checking Lock Task Mode status", e);
        }
    }





}