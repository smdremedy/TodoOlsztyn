package com.soldiersofmobile.todoexpert.login;

import android.content.SharedPreferences;

import javax.inject.Inject;

public class LoginManager {

    public static final String TOKEN = "token";
    public static final String USER_ID = "userId";
    private SharedPreferences sharedPreferences;

    @Inject
    public LoginManager(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }


    public void saveUserData(String userId, String token) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TOKEN, token);
        editor.putString(USER_ID, userId);
        editor.apply();

    }

    public boolean isUserLogged() {
        return !getToken().isEmpty();
    }

    public String getToken() {
        return sharedPreferences.getString(TOKEN, "");
    }

    public void logout() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(TOKEN);
        editor.remove(USER_ID);
        editor.apply();

    }

    public String getUserId() {
        return sharedPreferences.getString(USER_ID, "");
    }
}
