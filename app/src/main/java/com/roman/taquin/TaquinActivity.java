package com.roman.taquin;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Spinner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TaquinActivity extends AppCompatActivity {
    static final int REQUEST_IMAGE_GET = 1;
    public static int SCREEN_WIDTH;
    private static Bitmap image;
    Spinner spinner;
    List<String> spinnerList;
    ArrayAdapter<CharSequence> spinnerAdapter;
    private ImageView imageView;
    private Button play;
    private GridView gridView;
    private int number, resource;
    AdapterView.OnItemSelectedListener onItemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            number = i + 3;
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
            number = 3;
        }
    };
    private boolean type;
    private String uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taquin);
        imageView = (ImageView) findViewById(R.id.image_view);
        play = (Button) findViewById(R.id.play);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        SCREEN_WIDTH = dm.widthPixels;
        spinner = (Spinner) findViewById(R.id.spinner);
        spinnerList = new ArrayList<>();
        spinner.setOnItemSelectedListener(onItemSelectedListener);
        gridView = (GridView) findViewById(R.id.images_layout);
        spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.choices, R.layout.spinner_item);
        spinnerAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinner.setAdapter(spinnerAdapter);
        gridView.setColumnWidth(SCREEN_WIDTH / 4);
        final ArrayList<Bitmap> bitmaps = new ArrayList<>();
        bitmaps.add(BitmapFactory.decodeResource(getResources(), R.drawable.francois1));
        bitmaps.add(BitmapFactory.decodeResource(getResources(), R.drawable.francois2));
        bitmaps.add(BitmapFactory.decodeResource(getResources(), R.drawable.francois3));
        bitmaps.add(BitmapFactory.decodeResource(getResources(), R.drawable.francois4));
        gridView.setAdapter(new ImageAdapter(this, bitmaps, 4));
        final int[] resources = {
                R.drawable.francois1,
                R.drawable.francois2,
                R.drawable.francois3
        };
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bitmap imageInAdapter = bitmaps.get(position);
                resource = resources[position];
                setImage(imageInAdapter, true);
            }
        });
    }

    public void loadImage(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_IMAGE_GET);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_GET && resultCode == RESULT_OK) {
            Uri fullPhotoUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), fullPhotoUri);
                System.out.println(bitmap.getHeight());
                image = Bitmap.createScaledBitmap(bitmap, SCREEN_WIDTH, SCREEN_WIDTH, false);
                uri = fullPhotoUri.toString();
                setImage(image, false);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void setImage(Bitmap bitmap, boolean type) {
        imageView.setImageBitmap(bitmap);
        this.type = type;
        play.setEnabled(true);
    }

    public void play(View view) {
        Intent intent = new Intent(this, GameActivity.class);
        if (type)
            intent.putExtra("resource", resource);
        else
            intent.putExtra("uri", uri);
        intent.putExtra("number", number);
        startActivity(intent);
    }

    public void scores(View view) {
        Intent intent = new Intent(TaquinActivity.this, ScoresActivity.class);
        startActivity(intent);
    }
}
