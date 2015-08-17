package com.materialdesign.mobibittech.geofencingexampleone;

import android.app.Application;

import com.parse.Parse;

/**
 * Created by rajendra on 8/17/2015.
 */
public class Root extends Application{
    private String app_id = "n7DE3dDBb5eZtDvm4vyRrMNLjyVPZJbVrupufskt";
    private String client_id="DtLd6gRcYV8fEza3sZJW4ssk1XGhhqDjGh6hjvK2";

    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(Root.this,app_id,client_id);
    }
}
