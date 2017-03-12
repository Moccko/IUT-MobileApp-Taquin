package com.roman.taquin;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class ScoresActivity extends AppCompatActivity {
    ListView scores;
    SimpleAdapter adapter;
    ScoresDAO dao;
    Button erase;
    ArrayList<HashMap<String, String>> listItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scores);
        scores = (ListView) findViewById(R.id.list_view);
        erase = (Button) findViewById(R.id.erase);
        dao = new ScoresDAO(this);
        loadScores();
        scores.setAdapter(adapter);
    }

    private void loadScores() {
        listItems = new ArrayList<>();
        dao.open();
        ArrayList<Score> scoresList = dao.findAll();
        dao.close();
        int ranking = 1;
        if (scoresList.isEmpty())
            erase.setEnabled(false);
        else {
            for (Score score : scoresList) {
                HashMap<String, String> item = new HashMap<>();
                item.put("ranking", "#" + ranking++);
                item.put("date", score.getDate());
                item.put("time", score.getTime());
                item.put("moves", String.valueOf(score.getMoves()));
                listItems.add(item);
            }
            adapter = new SimpleAdapter(this.getBaseContext(), listItems, R.layout.list_view_item,
                    new String[]{"ranking", "date", "time", "moves"},
                    new int[]{R.id.ranking, R.id.date, R.id.time, R.id.moves}
            );
        }
    }

    public void eraseScores(View view) {
        dao.open();
        erase.setEnabled(dao.eraseAll());
        dao.close();
        listItems.clear();
        adapter.notifyDataSetChanged();
    }
}
