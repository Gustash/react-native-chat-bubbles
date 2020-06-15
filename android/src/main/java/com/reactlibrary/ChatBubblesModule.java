package com.reactlibrary;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Person;
import android.content.ComponentName;
import android.content.Intent;
import android.content.LocusId;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.content.res.Resources;
import android.graphics.drawable.Icon;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationManagerCompat;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
        Icon bubbleIcon = Icon.createWithResource(reactContext, smallIcon);

        // Create bubble metadata
        Notification.BubbleMetadata bubbleData =
                new Notification.BubbleMetadata.Builder()
                        .setIcon(bubbleIcon)
                        .setIntent(bubbleIntent)
                        .setDesiredHeight(600)
                        .setSuppressNotification(true)
                        .setAutoExpandBubble(true)
                        .build();

        Person user = new Person.Builder().setName("Me").build();

        // Create notification
        Person chatBot = new Person.Builder()
                .setBot(true)
                .setName("BubbleBot")
                .setImportant(true)
                .build();

        Activity currentActivity = reactContext.getCurrentActivity();
        ShortcutManager shortcutManager = reactContext.getSystemService(ShortcutManager.class);

        if (currentActivity != null && shortcutManager != null) {
            Set<String> categories = new HashSet<>();
            List<ShortcutInfo> shortcuts = new ArrayList<>();

            categories.add("com.example.android.bubbles.category.TEXT_SHARE_TARGET");

            ShortcutInfo shortcutInfo = new ShortcutInfo.Builder(reactContext, "1")
                    .setLocusId(new LocusId("1"))
                    .setActivity(new ComponentName(reactContext, currentActivity.getClass()))
                    .setShortLabel("user name")
                    .setIcon(bubbleIcon)
                    .setLongLived(true)
                    .setCategories(categories)
                    .setIntent(new Intent(reactContext, currentActivity.getClass()).setAction(Intent.ACTION_VIEW))
                    .build();

            shortcuts.add(shortcutInfo);

            shortcutManager.addDynamicShortcuts(shortcuts);
        }

        Notification.Builder builder =
            new Notification.Builder(reactContext, CHANNEL_ID)
                .setContentTitle("Test Notification")
                .setContentText("This is a test bubble")
                .setContentIntent(bubbleIntent)
                .setSmallIcon(smallIcon)
                .setBubbleMetadata(bubbleData)
                .addPerson(chatBot)
                .setCategory(Notification.CATEGORY_MESSAGE)
                .setShortcutId("1")
                .setLocusId(new LocusId("1"))
                .setShowWhen(true)
                .setStyle(
                    new Notification.MessagingStyle(user)
                        .addMessage(
                                new Notification.MessagingStyle.Message(
                                    "some text",
                                    new Date().getTime(),
                                    chatBot
                            )
                        )
                    .setGroupConversation(false)
                );

        notificationManager.notify(1, builder.build());
    }
}
