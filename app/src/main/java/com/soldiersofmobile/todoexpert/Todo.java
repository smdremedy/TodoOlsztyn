package com.soldiersofmobile.todoexpert;

import java.io.Serializable;

public class Todo
        implements Serializable {

    public Todo(String content,
                boolean done) {
        this.content = content;
        this.done = done;
    }

    public Todo() {

    }

    public String content;
    public boolean done;
    public String objectId;

    @Override
    public String toString() {
        return "Todo{" +
                "content='" + content + '\'' +
                ", done=" + done +
                '}';
    }
}
