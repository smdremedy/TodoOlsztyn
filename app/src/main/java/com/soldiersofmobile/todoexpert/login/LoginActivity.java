package com.soldiersofmobile.todoexpert.login;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.soldiersofmobile.todoexpert.App;
import com.soldiersofmobile.todoexpert.BuildConfig;
import com.soldiersofmobile.todoexpert.R;
import com.soldiersofmobile.todoexpert.di.AppComponent;
import com.soldiersofmobile.todoexpert.di.AppModule;
import com.soldiersofmobile.todoexpert.di.DaggerAppComponent;
import com.soldiersofmobile.todoexpert.todolist.TodoListActivity;
import com.soldiersofmobile.todoexpert.api.LoginResponse;
import com.soldiersofmobile.todoexpert.api.TodoApi;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.username_edit_text)
    EditText usernameEditText;
    @BindView(R.id.password_edit_text)
    EditText passwordEditText;
    @BindView(R.id.login_button)
    Button loginButton;
    @BindView(R.id.register_button)
    Button registerButton;
    @BindView(R.id.progress)
    ProgressBar progress;

    @Inject
    LoginManager loginManager;
    @Inject
    TodoApi todoApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.getComponent(this).inject(this);

        LoginManager loginManager = App.getComponent(this).getLoginManager();

        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        if (BuildConfig.DEBUG) {
            usernameEditText.setText("user");
            passwordEditText.setText("pass");

        }


    }

    @OnClick(R.id.login_button)
    public void onLoginButtonClicked() {

        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        boolean hasError = false;
        if (username.isEmpty()) {
            usernameEditText.setError(getString(R.string.username_empty_error));
            hasError = true;
        }
        if (password.isEmpty()) {
            passwordEditText.setError(getString(R.string.password_empty_error));
            hasError = true;
        }

        if (!hasError) {
            //login(username, password);
            loginWithRetrofit(username, password);
        }

    }

    private void loginWithRetrofit(String username, String password) {

        Call<LoginResponse> loginResponseCall = todoApi.getLogin(username, password);

        loginResponseCall.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful()) {
                    LoginResponse body = response.body();
                    Timber.d("Body:$s", body);

                    loginManager.saveUserData(body.objectId, body.sessionToken);
                    Intent intent = new Intent(LoginActivity.this, TodoListActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    ResponseBody responseBody = response.errorBody();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {

            }
        });


    }


    private void login(final String username, final String password) {


        AsyncTask<String, Integer, String> asyncTask
                = new AsyncTask<String, Integer, String>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loginButton.setEnabled(false);
            }

            @Override
            protected String doInBackground(String... strings) {

//                for (int i = 0; i < 100; i++) {
//
//                    try {
//                        Thread.sleep(50);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    publishProgress(i);
//                }

                boolean isLogged = loginCall(strings[0], strings[1]);
                return isLogged ? null : "Login error";
            }

            @Override
            protected void onProgressUpdate(Integer... values) {
                super.onProgressUpdate(values);

                loginButton.setText(String.valueOf(values[0]));
                progress.setProgress(values[0]);
            }

            @Override
            protected void onPostExecute(String error) {
                super.onPostExecute(error);
                loginButton.setEnabled(true);
                if (error == null) {
                    finish();

                    Intent intent = new Intent(LoginActivity.this, TodoListActivity.class);
                    startActivity(intent);

                } else {
                    Toast.makeText(LoginActivity.this, error, Toast.LENGTH_SHORT).show();
                }

            }
        };

        asyncTask.execute(username, password);

    }

    private boolean loginCall(String username, String password) {
        try {
            Thread.sleep(100);
            return username.equals("user") && password.equals("pass");
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }

    @OnClick(R.id.register_button)
    public void onRegisterButtonClicked() {
    }
}
