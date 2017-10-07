package com.soldiersofmobile.todoexpert.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;

public interface TodoApi {

    @GET("/login")
    @Headers({
            "X-Parse-Application-Id: X7HiVehVO7Zg9ufo0qCDXVPI3z0bFpUXtyq2ezYL",
            "X-Parse-REST-API-Key: LCTpX53aBmbtIXOtFmDb9dklESKUd0q58hFbnRYc",
            "X-Parse-Revocable-Session: 1"
    })
    Call<LoginResponse> getLogin(String username, String password);
}
