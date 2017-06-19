package ss090310.etf.ac.bg.rs.memorija_diplomski.utils;

/**
 * Created by Stefan on 18/05/2017.
 */

public class Player {

    private String name;
    private int score;

    public Player(String name) {
        this.name = name;
        score = 0;
    }

    public int getScore() {
        return score;
    }

    public String getName() {
        return name;
    }
}
