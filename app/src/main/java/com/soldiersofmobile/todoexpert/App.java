package com.soldiersofmobile.todoexpert;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.facebook.stetho.timber.StethoTree;
import com.soldiersofmobile.todoexpert.api.TodoApi;
import com.soldiersofmobile.todoexpert.db.DbHelper;
import com.soldiersofmobile.todoexpert.db.TodoDao;
import com.soldiersofmobile.todoexpert.di.AppComponent;
import com.soldiersofmobile.todoexpert.di.AppModule;
import com.soldiersofmobile.todoexpert.di.DaggerAppComponent;
import com.soldiersofmobile.todoexpert.login.LoginManager;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

public class App extends Application {

    private AppComponent component;

    public static AppComponent getComponent(Context context) {
        return ((App) context.getApplicationContext()).component;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) {

            Stetho.initializeWithDefaults(this);
            Timber.plant(new Timber.DebugTree(), new StethoTree());

        } else {
            Timber.plant(new Timber.Tree() {
                @Override
                protected void log(int priority, String tag, String message, Throwable t) {

                    //LOG TO PROD
                }
            });
        }

        component = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();

    }

}
