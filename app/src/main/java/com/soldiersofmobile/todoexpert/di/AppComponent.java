package com.soldiersofmobile.todoexpert.di;

import com.soldiersofmobile.todoexpert.login.LoginActivity;
import com.soldiersofmobile.todoexpert.login.LoginManager;
import com.soldiersofmobile.todoexpert.todolist.TodoListActivity;

import dagger.Component;

@Component(modules = AppModule.class)
public interface AppComponent {

    LoginManager getLoginManager();

    void inject(LoginActivity activity);

    void inject(TodoListActivity activity);
}
