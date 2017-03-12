package com.roman.taquin;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetImage extends AsyncTask<String, Void, Bitmap> {
    @Override
    protected Bitmap doInBackground(String... uri) {
        String responseString = "";
        URL url = null;
        Bitmap bmImg = null;
        try {
            url = new URL(uri[0]);
            HttpURLConnection httpconn = (HttpURLConnection) url.openConnection();
            httpconn.setDoInput(true);
            httpconn.connect();
            InputStream is = httpconn.getInputStream();
            bmImg = BitmapFactory.decodeStream(is);
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
        return bmImg;
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        super.onPostExecute(result);
        //Do anything with response..//
    }
}