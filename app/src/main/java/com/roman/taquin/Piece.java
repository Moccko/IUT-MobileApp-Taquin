package com.roman.taquin;

import android.graphics.Bitmap;

/**
 * Created by roman on 05/03/17.
 */

public class Piece {
    private Bitmap image;
    private int position;

    public Piece(Bitmap bitmap, int position) {
        image = bitmap;
        this.position = position;
    }

}
