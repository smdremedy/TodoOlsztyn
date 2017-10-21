package com.soldiersofmobile.todoexpert;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.soldiersofmobile.todoexpert.login.LoginManager;

public class App extends Application {

    private LoginManager loginManager;

    @Override
    public void onCreate() {
        super.onCreate();
        SharedPreferences sharedPreferences
                = PreferenceManager.getDefaultSharedPreferences(this);
        loginManager = new LoginManager(sharedPreferences);

    }


    public LoginManager getLoginManager() {
        return loginManager;
    }
}
