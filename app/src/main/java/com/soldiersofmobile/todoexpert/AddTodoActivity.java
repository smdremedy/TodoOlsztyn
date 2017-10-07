package com.soldiersofmobile.todoexpert;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddTodoActivity extends AppCompatActivity {

    @BindView(R.id.content_edit_text)
    EditText contentEditText;
    @BindView(R.id.done_check_box)
    CheckBox doneCheckBox;
    @BindView(R.id.save_button)
    Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_todo);
        ButterKnife.bind(this);

        String content = getIntent().getStringExtra("content");
        contentEditText.setText(content);
    }

    @OnClick(R.id.save_button)
    public void onViewClicked() {

        String content = contentEditText.getText().toString();
        boolean isDone = doneCheckBox.isChecked();
        Intent data = new Intent();
        Todo todo = new Todo(content, isDone);

        data.putExtra("todo", todo);
        setResult(RESULT_OK, data);
        finish();
    }
}
