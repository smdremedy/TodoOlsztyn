package com.soldiersofmobile.todoexpert.todolist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;

import com.soldiersofmobile.todoexpert.R;
import com.soldiersofmobile.todoexpert.Todo;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class TodoAdapter extends BaseAdapter {

    private List<Todo> todos = new ArrayList<>();

    @Override
    public int getCount() {
        return todos.size();
    }

    @Override
    public Todo getItem(int position) {
        return todos.get(position);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        Timber.d("Pos: %d view:%s", position, convertView);
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.item_todo, viewGroup, false);
            view.setTag(new ViewHolder(view));
        }

        ViewHolder viewHolder = (ViewHolder) view.getTag();

        Todo todo = getItem(position);
        viewHolder.itemCheckBox.setChecked(todo.done);
        viewHolder.itemCheckBox.setText(todo.content);
        viewHolder.itemDeleteButton.setText(todo.objectId);

        return view;
    }

    public void addAll(List<Todo> results) {
        todos.addAll(results);
        notifyDataSetChanged();
    }

    public void add(Todo todo) {
        todos.add(todo);
        notifyDataSetChanged();
    }

    static class ViewHolder {
        @BindView(R.id.item_check_box)
        CheckBox itemCheckBox;
        @BindView(R.id.item_delete_button)
        Button itemDeleteButton;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
