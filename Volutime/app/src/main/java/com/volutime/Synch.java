package com.volutime;

import java.util.concurrent.*;

public class Synch {

    public static Semaphore mutex_session_active;
    public static Semaphore mutex_seekbar_lock;
    public static Semaphore session;
    public static Semaphore time;
    public static Semaphore session_update;

    public static boolean session_active = false;
    // The lock that ties the two seek bars together
    public static boolean seekbar_lock = true; // true = locked

}
