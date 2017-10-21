package com.soldiersofmobile.todoexpert;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.facebook.stetho.timber.StethoTree;
import com.soldiersofmobile.todoexpert.api.TodoApi;
import com.soldiersofmobile.todoexpert.db.DbHelper;
import com.soldiersofmobile.todoexpert.db.TodoDao;
import com.soldiersofmobile.todoexpert.login.LoginManager;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

public class App extends Application {

    private LoginManager loginManager;
    private TodoApi todoApi;
    private TodoDao todoDao;

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
        SharedPreferences sharedPreferences
                = PreferenceManager.getDefaultSharedPreferences(this);
        loginManager = new LoginManager(sharedPreferences);


        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addNetworkInterceptor(interceptor)
                .addNetworkInterceptor(new StethoInterceptor())
                .build();


        Retrofit.Builder builder = new Retrofit.Builder();
        builder.client(client);
        builder.baseUrl("https://parseapi.back4app.com");
        builder.addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        todoApi = retrofit.create(TodoApi.class);

        todoDao = new TodoDao(new DbHelper(this));
    }

    public LoginManager getLoginManager() {
        return loginManager;
    }

    public TodoApi getTodoApi() {
        return todoApi;
    }

    public TodoDao getTodoDao() {
        return todoDao;
    }
}
