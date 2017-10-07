package com.soldiersofmobile.todoexpert;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
            login(username, password);
        }

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
