package com.volutime;


public class TimeKeeper extends Thread{

    //private int time = 432000; // 7.2 minutes = 1% of 12 hours
    public static final int TIMEMAXWAIT = 432000; // Time to wait until we check again
    private int time = 3000; // Change to TIMEMAXWAIT

    public void run() {
        while (true) {
            try {
                sleep(time);

                if (Synch.session_active) {
                    // Change to TIMEMAXWAIT when done testing
                    time = 3000; // Session is active, check every 10 seconds
                    Synch.time.release();
                } else {
                    time = 1000; // Check every 1 second if there's a new session
                }

            } catch (Exception e) {}
        }
    }
}
