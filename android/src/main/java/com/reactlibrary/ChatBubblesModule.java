package com.reactlibrary;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Person;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Icon;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationManagerCompat;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

public class ChatBubblesModule extends ReactContextBaseJavaModule {
    private static final String CHANNEL_ID = "ChatBubbleChannel";

    private final ReactApplicationContext reactContext;
    private final NotificationManagerCompat notificationManager;

    ChatBubblesModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
        this.notificationManager = NotificationManagerCompat.from(reactContext);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Chat Bubble",
                    NotificationManager.IMPORTANCE_HIGH
            );
            this.notificationManager.createNotificationChannel(channel);
        }
    }

    @NonNull
    @Override
    public String getName() {
        return "ChatBubbles";
    }

    @ReactMethod
    public void open() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            return;
        }

        String packageName = reactContext.getPackageName();
        Resources resources = reactContext.getResources();
        int icon = resources.getIdentifier("ic_launcher", "drawable", packageName);
        int smallIcon = resources.getIdentifier("ic_notification", "drawable", packageName);

        // Create bubble intent
        Intent target = new Intent(reactContext, ReactBubbleActivity.class);
        PendingIntent bubbleIntent =
                PendingIntent.getActivity(reactContext, 0, target, 0 /* flags */);

        // Create bubble metadata
        Notification.BubbleMetadata bubbleData =
                new Notification.BubbleMetadata.Builder()
                        .setIcon(Icon.createWithResource(reactContext, smallIcon))
                        .setIntent(bubbleIntent)
                        .setDesiredHeight(600)
                        .setSuppressNotification(true)
                        .setAutoExpandBubble(true)
                        .build();

        // Create notification
        Person chatBot = new Person.Builder()
                .setBot(true)
                .setName("BubbleBot")
                .setImportant(true)
                .build();

        Notification.Builder builder =
                new Notification.Builder(reactContext, CHANNEL_ID)
                        .setContentTitle("Test Notification")
                        .setContentText("This is a test bubble")
                        .setContentIntent(bubbleIntent)
                        .setSmallIcon(smallIcon)
                        .setBubbleMetadata(bubbleData)
                        .addPerson(chatBot);

        notificationManager.notify(1, builder.build());
    }
}
