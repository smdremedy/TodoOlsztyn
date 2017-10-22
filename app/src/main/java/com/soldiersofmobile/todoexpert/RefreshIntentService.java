package com.soldiersofmobile.todoexpert;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.soldiersofmobile.todoexpert.todolist.TodoListActivity;

public class RefreshIntentService extends IntentService {


    public static final int NOTIFICATION_ID = 1;
    public static final String ACTION = "com.soldiersofmobile.todoexpert.REFRESH";

    public RefreshIntentService() {
        super(RefreshIntentService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        //TODO synchro

        Notification.Builder builder = new Notification.Builder(this);
        builder.setContentTitle("New Todos!");
        builder.setContentText("You have 10 new todos.");
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setAutoCancel(true);


        Intent startIntent = new Intent(this, TodoListActivity.class);
        startIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, startIntent, 0);


        builder.setContentIntent(pendingIntent);

        Notification notification = builder.build();

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.notify(NOTIFICATION_ID, notification);

        sendBroadcast(new Intent(ACTION));


    }
}
