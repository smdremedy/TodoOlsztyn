package com.soldiersofmobile.todoexpert.todolist;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
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
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.soldiersofmobile.todoexpert.AddTodoActivity;
import com.soldiersofmobile.todoexpert.App;
import com.soldiersofmobile.todoexpert.R;
import com.soldiersofmobile.todoexpert.RefreshIntentService;
import com.soldiersofmobile.todoexpert.Todo;
import com.soldiersofmobile.todoexpert.api.TodoApi;
import com.soldiersofmobile.todoexpert.api.TodosResponse;
import com.soldiersofmobile.todoexpert.db.TodoDao;
import com.soldiersofmobile.todoexpert.login.LoginActivity;
import com.soldiersofmobile.todoexpert.login.LoginManager;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static timber.log.Timber.*;

public class TodoListActivity extends AppCompatActivity {

    public static final int REQUEST_CODE = 123;
    private static final String[] FROM = new String[]{
            TodoDao.C_CONTENT, TodoDao.C_DONE, TodoDao.C_ID
    };
    private static final int[] TO = new int[]{
            R.id.item_check_box, R.id.item_check_box, R.id.item_delete_button
    };

    @BindView(R.id.todo_list)
    ListView todoList;
    //private TodoAdapter adapter;
    private SimpleCursorAdapter adapter;
    private LoginManager loginManager;

    private TodoApi todoApi;
    private TodoDao todoDao;


    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(TodoListActivity.this, "Refresh", Toast.LENGTH_SHORT).show();
        }
    };


    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(receiver, new IntentFilter(RefreshIntentService.ACTION));
    }


    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(receiver);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        App application = (App) getApplication();
        loginManager = application.getLoginManager();
        todoApi = application.getTodoApi();
        todoDao = application.getTodoDao();

        if (!loginManager.isUserLogged()) {
            goToLogin();
            return;
        }


        setContentView(R.layout.activity_todo_list);
        ButterKnife.bind(this);

        //adapter = new TodoAdapter();

        Cursor cursor = todoDao.getTodosCursor(loginManager.getUserId());
        adapter = new SimpleCursorAdapter(this, R.layout.item_todo,
                cursor, FROM, TO, 0);
        adapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Cursor cursor, int index) {
                if (index == cursor.getColumnIndex(TodoDao.C_DONE)) {
                    CheckBox checkBox = (CheckBox) view;
                    boolean done = cursor.getInt(index) > 0;
                    checkBox.setChecked(done);

                    return true;
                }
                return false;
            }
        });
        todoList.setAdapter(adapter);

        List<Todo> todos = todoDao.getTodos(loginManager.getUserId());
        //adapter.addAll(todos);

    }

    private void loadTodos() {

        Call<TodosResponse> call = todoApi.getTodos(loginManager.getToken());
        call.enqueue(new Callback<TodosResponse>() {
            @Override
            public void onResponse(Call<TodosResponse> call, Response<TodosResponse> response) {

                if (response.isSuccessful()) {
                    TodosResponse todosResponse = response.body();

                    for (Todo result : todosResponse.results) {
                        result.userId = loginManager.getUserId();
                        d(result.toString());
                        todoDao.create(result);

                        startService(new Intent(TodoListActivity.this, RefreshIntentService.class));

                    }
                    //adapter.addAll(todosResponse.results);


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
                //adapter.add(todo);
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
                loadTodos();
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
