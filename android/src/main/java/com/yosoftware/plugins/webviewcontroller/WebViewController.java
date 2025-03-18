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

public class WebViewController {
    private static final String TAG = "WebViewController";
    private static final String PREFS_NAME = "WebViewControllerSettings";
    
    private Boolean shouldKeepScreenOn = false;
    private Context context;
    private PowerManager.WakeLock wakeLock = null;
    
    public WebViewController(Context context) {
        this.context = context;
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        shouldKeepScreenOn = prefs.getBoolean("keepScreenOn", false);
        
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
}