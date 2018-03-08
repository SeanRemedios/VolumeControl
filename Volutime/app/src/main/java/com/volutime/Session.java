package com.volutime;


import android.app.Notification;
import android.content.Context;

public class Session extends Thread {

    Context context;
    private NotificationUtils mNotificationUtils;

    public Session(Context context) {
        this.context = context;
        System.out.println("Session Started"); // Debug only

    }

    public void run() {
        while (true) {
            try {
                Synch.session.acquire();
                System.out.println("Acquired"); // Debug only
            } catch (Exception e) {
            }

            // We've acquired the session semaphore which means the session has started
            sendNotification();

            try {
                Synch.mutex_session_active.acquire();
                Synch.session_active = true;
                Synch.mutex_session_active.release();
            } catch (Exception e) {}


        }
    }

    private void sendNotification() {
        mNotificationUtils = new NotificationUtils(context);
        Notification.Builder nb = mNotificationUtils.
                getAndroidChannelNotification("Session Started",
                        "Pause your music to pause the current session");

        mNotificationUtils.getManager().notify(101, nb.build());
        System.out.println("Notification Sent"); // Debug only
    }

}
