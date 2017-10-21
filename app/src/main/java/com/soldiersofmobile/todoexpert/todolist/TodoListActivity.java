package com.soldiersofmobile.todoexpert.todolist;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.soldiersofmobile.todoexpert.AddTodoActivity;
import com.soldiersofmobile.todoexpert.App;
import com.soldiersofmobile.todoexpert.R;
import com.soldiersofmobile.todoexpert.Todo;
import com.soldiersofmobile.todoexpert.api.TodoApi;
import com.soldiersofmobile.todoexpert.api.TodosResponse;
import com.soldiersofmobile.todoexpert.login.LoginActivity;
import com.soldiersofmobile.todoexpert.login.LoginManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static timber.log.Timber.*;

public class TodoListActivity extends AppCompatActivity {

    public static final int REQUEST_CODE = 123;
    @BindView(R.id.todo_list)
    ListView todoList;
    private TodoAdapter adapter;
    private LoginManager loginManager;

    private TodoApi todoApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        App application = (App) getApplication();
        loginManager = application.getLoginManager();
        todoApi = application.getTodoApi();

        if (!loginManager.isUserLogged()) {
            goToLogin();
            return;
        }


        setContentView(R.layout.activity_todo_list);
        ButterKnife.bind(this);

        adapter = new TodoAdapter();

        todoList.setAdapter(adapter);

        loadTodos();
    }

    private void loadTodos() {

        Call<TodosResponse> call = todoApi.getTodos(loginManager.getToken());
        call.enqueue(new Callback<TodosResponse>() {
            @Override
            public void onResponse(Call<TodosResponse> call, Response<TodosResponse> response) {

                if (response.isSuccessful()) {
                    TodosResponse todosResponse = response.body();

                    for (Todo result : todosResponse.results) {
                        d(result.toString());

                    }
                    adapter.addAll(todosResponse.results);


                }
            }

            @Override
            public void onFailure(Call<TodosResponse> call, Throwable t) {

            }
        });

    }

    private void goToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.todo_list, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Todo todo
                        = (Todo) data.getSerializableExtra("todo");
                adapter.add(todo);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                Intent intent = new Intent(this, AddTodoActivity.class);
                intent.putExtra("content", "zadanie" + (adapter.getCount() + 1));
                startActivityForResult(intent, REQUEST_CODE);
                return true;
            case R.id.action_refresh:
                return true;
            case R.id.action_logout:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Confirm logout");
                builder.setMessage("Are you sure?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        loginManager.logout();
                        goToLogin();
                    }
                });
                builder.setNegativeButton("No", null);
                builder.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
