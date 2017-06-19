package ss090310.etf.ac.bg.rs.memorija_diplomski.utils;

import android.support.annotation.NonNull;

/**
 * Created by Stefan on 10/05/2017.
 */

public class ScoreEntry implements Comparable<ScoreEntry> {
    private int score;
    private String username;

    public ScoreEntry(String username, int score) {
        this.username = username;
        this.score = score;
    }

    @Override
    public int compareTo(@NonNull ScoreEntry o) {
        if (o.score > score) return 1;
        else if (o.score < score) return -1;
        else return 0;
    }

    @Override
    public String toString() {
        return username + " - " + score;
    }
}
