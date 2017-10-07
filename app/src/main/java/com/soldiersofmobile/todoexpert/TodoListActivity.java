package com.soldiersofmobile.todoexpert;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TodoListActivity extends AppCompatActivity {

    @BindView(R.id.todo_list)
    ListView todoList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list);
        ButterKnife.bind(this);

        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        adapter.addAll("zadanie1", "zadanie2", "zadanie3");

        todoList.setAdapter(adapter);


    }
}
