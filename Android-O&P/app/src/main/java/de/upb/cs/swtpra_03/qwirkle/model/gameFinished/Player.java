package de.upb.cs.swtpra_03.qwirkle.model.gameFinished;

public class Player {

    private String name;
    private int score;
    private int min;
    private int sec;

    public Player(String name, int score, int min, int sec) {
        this.name = name;
        this.score = score;
        this.min = min;
        this.sec = sec;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public int getMin() {
        return min;
    }

    public int getSec() {
        return sec;
    }
}
