package com.roman.taquin;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;

class ImageAdapter extends BaseAdapter {
    private final int NUMBER;
    private Context mContext;
    private ArrayList<Bitmap> bitmaps;

    ImageAdapter(Context c, ArrayList<Bitmap> list, int number) {
        mContext = c;
        bitmaps = list;
        NUMBER = number;

    }

    public int getCount() {
        return bitmaps.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(TaquinActivity.SCREEN_WIDTH / NUMBER, TaquinActivity.SCREEN_WIDTH / NUMBER));
//            imageView.setScaleType(ImageView.ScaleType.CENTER);
            imageView.setPadding(2, 2, 2, 2);
        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageBitmap(bitmaps.get(position));
        return imageView;
    }
}