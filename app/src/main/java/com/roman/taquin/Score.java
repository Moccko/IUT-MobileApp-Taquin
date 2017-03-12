package com.roman.taquin;

/**
 * Created by roman on 11/03/17.
 */

class Score {
    private String date;
    private String time;
    private int moves;

    Score(String date, String time, int moves) {
        this.date = date;
        this.time = time;
        this.moves = moves;
    }

    String getDate() {
        return date;
    }

    String getTime() {
        return time;
    }

    int getMoves() {
        return moves;
    }
}
