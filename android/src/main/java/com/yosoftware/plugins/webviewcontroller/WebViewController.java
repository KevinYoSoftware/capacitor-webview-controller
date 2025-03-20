package com.yosoftware.plugins.webviewcontroller;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.PowerManager;
import android.util.Log;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.WindowManager;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import java.lang.reflect.Field;
import java.util.Map;

public class WebViewController {

    private DevicePolicyManager devicePolicyManager;
    private ComponentName adminComponentName;
    private boolean isDeviceOwner = false;

    private boolean isLockTaskActive = false;
    private static final String TAG = "WebViewController";
    private static final String PREFS_NAME = "WebViewControllerSettings";

    private Boolean shouldKeepScreenOn = false;
    private Context context;
    private PowerManager.WakeLock wakeLock = null;

    public WebViewController(Context context) {
        this.context = context;
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        shouldKeepScreenOn = prefs.getBoolean("keepScreenOn", false);

        devicePolicyManager = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
        adminComponentName = new ComponentName(context, WebViewDeviceAdminReceiver.class);

        if (devicePolicyManager != null) {
            isDeviceOwner = devicePolicyManager.isDeviceOwnerApp(context.getPackageName());
            Log.i(TAG, "App is device owner: " + isDeviceOwner);
        }
    }

    public boolean isDeviceOwner() {
        return isDeviceOwner;
    }

    public boolean setLockTaskPackages() {
        if (!isDeviceOwner || devicePolicyManager == null) {
            Log.w(TAG, "Cannot set lock task packages: not a device owner or DPM is null");
            return false;
        }

        try {
            // Whitelist only the current app for lock task mode
            devicePolicyManager.setLockTaskPackages(adminComponentName,
                    new String[]{context.getPackageName()});
            Log.i(TAG, "Lock task packages set successfully");
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Error setting lock task packages", e);
            return false;
        }
    }

    public boolean startLockTask(Activity activity) {
        if (activity == null) return false;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            try {
                // If we're a device owner, make sure we've set lock task packages
                if (isDeviceOwner) {
                    setLockTaskPackages();
                }

                // Start lock task mode
                activity.startLockTask();
                isLockTaskActive = true;

                // Save state to preferences
                context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                        .edit()
                        .putBoolean("lockTaskActive", true)
                        .apply();

                Log.i(TAG, "Lock Task Mode started successfully (using " +
                        (isDeviceOwner ? "enhanced" : "basic") + " mode)");
                return true;
            } catch (Exception e) {
                Log.e(TAG, "Error starting Lock Task Mode", e);
                return false;
            }
        } else {
            Log.w(TAG, "Lock Task Mode requires Android 5.0 or higher");
            return false;
        }
    }

    public boolean stopLockTask(Activity activity) {
        if (activity == null) return false;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            try {
                activity.stopLockTask();
                isLockTaskActive = false;

                // Save state to preferences
                context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                        .edit()
                        .putBoolean("lockTaskActive", false)
                        .apply();

                Log.i(TAG, "Lock Task Mode stopped successfully");
                return true;
            } catch (Exception e) {
                Log.e(TAG, "Error stopping Lock Task Mode", e);
                return false;
            }
        } else {
            Log.w(TAG, "Lock Task Mode requires Android 5.0 or higher");
            return false;
        }
    }

    public boolean isLockTaskActive() {
        return isLockTaskActive;
    }

    public void setKeepScreenAwake(boolean enable) {
        shouldKeepScreenOn = enable;

        // Save the setting to preferences
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                .edit()
                .putBoolean("keepScreenOn", shouldKeepScreenOn)
                .apply();


        if (!shouldKeepScreenOn && wakeLock != null && wakeLock.isHeld()) {
            try {
                wakeLock.release();
                System.out.println("WEBVIEW_CONTROLLER: Released wake lock");
            } catch (Exception e) {
                System.err.println("WEBVIEW_CONTROLLER: Error releasing wake lock: " + e.getMessage());
            }
        }
    }

    public boolean getKeepScreenAwake() {
        return shouldKeepScreenOn;
    }


    public void applySettingsToActivity(Activity activity) {
        if (activity == null) return;

        if (shouldKeepScreenOn) {
            keepScreenAwake(activity);
        }

    }

    private void keepScreenAwake(Activity activity) {
        activity.runOnUiThread(() -> {
            try {
                // Standard method
                activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

                // Backup method using PowerManager
                PowerManager powerManager = (PowerManager) activity.getSystemService(Context.POWER_SERVICE);
                if (wakeLock == null || !wakeLock.isHeld()) {
                    wakeLock = powerManager.newWakeLock(
                            PowerManager.SCREEN_BRIGHT_WAKE_LOCK |
                                    PowerManager.ACQUIRE_CAUSES_WAKEUP,
                            "WebViewController:WakeLock"
                    );
                    wakeLock.acquire();
                }
            } catch (Exception e) {
                System.err.println("WEBVIEW_CONTROLLER: Error keeping screen awake: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }
    public boolean clearDeviceOwner() {
        if (!isDeviceOwner || devicePolicyManager == null) {
            Log.w(TAG, "Cannot clear device owner: not a device owner or DPM is null");
            return false;
        }
    
        try {
            // This removes the app as device owner
            Log.i(TAG, "Clearing device owner status for package: " + context.getPackageName());
            devicePolicyManager.clearDeviceOwnerApp(context.getPackageName());
            
            // Update our local state
            isDeviceOwner = false;
            
            // Also ensure lock task is stopped if it was active
            if (isLockTaskActive) {
                Log.i(TAG, "Lock task was active, attempting to stop it first");
                try {
                    Activity activity = getCurrentActivity();
                    if (activity != null) {
                        activity.stopLockTask();
                        isLockTaskActive = false;
                    }
                } catch (Exception e) {
                    Log.w(TAG, "Could not stop lock task mode, but continuing with device owner removal", e);
                    // Continue with the process even if this fails
                }
            }
            
            Log.i(TAG, "Device owner status successfully cleared");
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Error clearing device owner status", e);
            return false;
        }
    }
    
    // Helper method to get current activity (if available)
    private Activity getCurrentActivity() {
        try {
            Class<?> activityThreadClass = Class.forName("android.app.ActivityThread");
            Object activityThread = activityThreadClass.getMethod("currentActivityThread").invoke(null);
            Object activityClientRecord = activityThreadClass.getMethod("getApplicationThread").invoke(activityThread);
            Field activitiesField = activityThreadClass.getDeclaredField("mActivities");
            activitiesField.setAccessible(true);
            
            Map<Object, Object> activities = (Map<Object, Object>) activitiesField.get(activityThread);
            if (activities == null) return null;
    
            for (Object activityRecord : activities.values()) {
                Class<?> activityRecordClass = activityRecord.getClass();
                Field activityField = activityRecordClass.getDeclaredField("activity");
                activityField.setAccessible(true);
                Activity activity = (Activity) activityField.get(activityRecord);
                if (activity != null) {
                    return activity;
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Error getting current activity", e);
        }
        return null;
    }
}