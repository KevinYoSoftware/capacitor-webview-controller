package com.yosoftware.plugins.webviewcontroller;

import android.util.Log;

public class WebViewController {

    public String echo(String value) {
        Log.i("Echo", value);
        return value;
    }
}
