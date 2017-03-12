package com.roman.taquin;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Chronometer;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Random;

public class GameActivity extends AppCompatActivity {
    //    private boolean landscape = false;
    private Chronometer counterView;
    private TextView movesView;
    private int number, size, empty, moves, resource;
    private ImageAdapter adapter;
    private ArrayList<Bitmap> images;
    private ArrayList<Integer> winningPositions, positions;
    private AlertDialog success, failure, solution;
    private Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        System.out.println(landscape);
//        if (!landscape)
        setContentView(R.layout.activity_game);
//        else
//            setContentView(R.layout.activity_game_landscape);

        GridView gridView = (GridView) findViewById(R.id.grid_view);
        counterView = (Chronometer) findViewById(R.id.counter_view);
        movesView = (TextView) findViewById(R.id.moves_view);
        number = getIntent().getIntExtra("number", 3);

        if (savedInstanceState != null) {
            size = savedInstanceState.getInt("size");
            empty = savedInstanceState.getInt("empty");
            moves = savedInstanceState.getInt("moves");
        } else {
            size = number * number;
            empty = size - 1;
            moves = 1;
        }
        gridView.setNumColumns(number);
        Bitmap image = null;
        resource = getIntent().getIntExtra("resource", 0);
        if (resource != 0)
            image = BitmapFactory.decodeResource(getResources(), resource);
        else
            try {
                uri = Uri.parse(getIntent().getStringExtra("uri"));
                image = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                image = Bitmap.createScaledBitmap(image, TaquinActivity.SCREEN_WIDTH, TaquinActivity.SCREEN_WIDTH, false);
            } catch (IOException e) {
                e.printStackTrace();
            }

        ArrayList<Bitmap> imagesInit = new ArrayList<>();
        int side = TaquinActivity.SCREEN_WIDTH / number;
        for (int i = 0; i < number; i++) {
            for (int j = 0; j < number; j++) {
                imagesInit.add(Bitmap.createBitmap(image, j * side, i * side, side, side));
            }
        }
        imagesInit.get(empty).eraseColor(Color.WHITE);
        winningPositions = new ArrayList<>();
        for (int i = 0; i < size; i++) winningPositions.add(i);
        positions = (ArrayList<Integer>) winningPositions.clone();

        shuffle();

        adapter = loadInGrid(number, imagesInit);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!move(position, false)) {
                    Random rand = new Random();
                    Snackbar snackbar = Snackbar.make(view, R.string.impossible, Snackbar.LENGTH_SHORT);
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.rgb(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255)));
                    snackbar.show();
                }
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(GameActivity.this);
        builder.setMessage(R.string.success)
                .setTitle(R.string.congrats);
        builder.setPositiveButton(R.string.quit, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(GameActivity.this, TaquinActivity.class);
                startActivity(intent);
            }
        });
        builder.setNegativeButton(R.string.replay, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(GameActivity.this, GameActivity.class);
                intent.putExtra("resource", resource);
                if (uri != null) {
                    intent.putExtra("uri", uri.toString()).putExtra("resource", resource);
                }
                intent.putExtra("number", number);
                startActivity(intent);
            }
        });
        success = builder.create();
        success.setCanceledOnTouchOutside(false);

        builder.setMessage(R.string.failure).setTitle(R.string.pity);
        builder.setNeutralButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        failure = builder.create();

        ImageView imageView = new ImageView(this);
        imageView.setImageBitmap(image);
        AlertDialog.Builder builder1 = new AlertDialog.Builder(GameActivity.this);
        builder1.setTitle(R.string.solution);
        builder1.setView(imageView);
        builder1.setPositiveButton(R.string.close, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        solution = builder1.create();

        counterView.start();
    }

    private ImageAdapter loadInGrid(int number, ArrayList<Bitmap> imagesInit) {
        images = new ArrayList<>();
        for (int i = 0; i < size; i++)
            images.add(imagesInit.get(positions.get(i)));
        return new ImageAdapter(this, images, number);
    }

    private boolean caseIsEmpty(int position) {
        return position == empty;
    }

    public boolean move(int position, boolean shuffle) {
        int up = position - number;
        int right = position + 1;
        int down = position + number;
        int left = position - 1;
        if (position >= number && caseIsEmpty(up)) {
            moveIndex(position, up, shuffle);
            return true;
        }
        if (position % number != number - 1 && caseIsEmpty(right)) {
            moveIndex(position, right, shuffle);
            return true;
        }
        if (position < number * (number - 1) && caseIsEmpty(down)) {
            moveIndex(position, down, shuffle);
            return true;
        }
        if (position % number != 0 && caseIsEmpty(left)) {
            moveIndex(position, left, shuffle);
            return true;
        }
        return false;
    }

    private void moveBitmap(int position, int destination) {
        Bitmap imageDep = images.get(position);
        Bitmap imageDest = images.get(destination);
        images.set(destination, imageDep);
        images.set(position, imageDest);
        movesView.setText(String.valueOf(moves++));
        adapter.notifyDataSetChanged();
    }

    private void moveIndex(int position, int destination, boolean shuffle) {
        int init = positions.get(position);
        int dest = positions.get(destination);
        positions.set(position, dest);
        positions.set(destination, init);
        empty = position;
        if (!shuffle) {
            moveBitmap(position, destination);
            if (positions.equals(winningPositions)) {
                saveScore();
                success.show();
            }
        }
    }

    private void shuffle() {
        int changes = 0;
        Random rand = new Random();
        while (changes < number * 50) {
            if (move(Math.abs(rand.nextInt(size)), true))
                changes++;
        }
    }

    private void saveScore() {
        counterView.stop();
        ScoresDAO dao = new ScoresDAO(this);
        dao.open();
        String date = new Date(System.currentTimeMillis()).toString();
        String time = counterView.getText().toString();
        Score score = new Score(date, time, moves - 1);
        dao.insertScore(score);
        dao.close();
    }

    public void stop(View view) {
        failure.show();
    }

//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
//        landscape = !landscape;
//    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putLong("chrono", counterView.getBase());
        outState.putCharSequence("moves", movesView.getText());
        outState.putIntegerArrayList("positions", positions);
        outState.putIntegerArrayList("winningPositions", positions);
        outState.putInt("number", number);
        outState.putInt("size", size);
        outState.putInt("empty", empty);

        super.onSaveInstanceState(outState);
    }

    public void show(View view) {
        solution.show();
    }
}
