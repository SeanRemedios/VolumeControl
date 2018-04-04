package com.volutime;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class Synch {

    public static Semaphore mutex_background_counters;
    public static Semaphore mutex_session_active;
    public static Semaphore mutex_seekbar_lock;
    public static Semaphore mutex_music_state;
    public static Semaphore mutex_statistics;
    public static Semaphore session;
    public static Semaphore time;
    public static Semaphore session_update;

    // To be used in the statistics class
    public static ArrayList<Integer> times_listened = new ArrayList<>();
    public static ArrayList<Float> volumes = new ArrayList<>();
    public static ArrayList<LocalDateTime> dateTimeStarted = new ArrayList<>();

    public static boolean session_active = false;
    // The lock that ties the two seek bars together
    public static boolean seekbar_lock = true; // true = locked
    // For button use only not the session active counter
    public static boolean music_state = false; // true if music playing

    public static int timeProgress = 0;
    public static int volumeProgress = 0;

}
