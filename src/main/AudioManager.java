package main;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AudioManager {
    private static AudioManager instance;
    private final ExecutorService audioThreadPool;

    private AudioManager() {
        audioThreadPool = Executors.newFixedThreadPool(2);
    }

    public static AudioManager getInstance() {
        if (instance == null) {
            instance = new AudioManager();
        }
        return instance;
    }

    public ExecutorService getThreadPool() {
        return audioThreadPool;
    }

    public void shutdown() {
        audioThreadPool.shutdown();
    }
}