package com.soldiersofmobile.todoexpert.di;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.soldiersofmobile.todoexpert.App;
import com.soldiersofmobile.todoexpert.api.TodoApi;
import com.soldiersofmobile.todoexpert.db.DbHelper;
import com.soldiersofmobile.todoexpert.db.TodoDao;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class AppModule {

    private App app;

    public AppModule(App app) {

        this.app = app;
    }

    @Provides
    public SharedPreferences provideSharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Provides
    public Context provideContext() {
        return app;
    }


    @Provides
    public TodoApi provideTodoApi() {
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
        return retrofit.create(TodoApi.class);
    }

    @Provides
    public TodoDao provideTodoDao(Context context) {
        return new TodoDao(new DbHelper(context));
    }
}
