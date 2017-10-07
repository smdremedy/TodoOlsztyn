package com.soldiersofmobile.todoexpert;

import java.io.Serializable;

public class Todo
        implements Serializable {

    public Todo(String content,
                boolean isDone) {
        this.content = content;
        this.isDone = isDone;
    }

    public String content;
    public boolean isDone;
}
