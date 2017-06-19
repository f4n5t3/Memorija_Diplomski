package ss090310.etf.ac.bg.rs.memorija_diplomski.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.List;

import ss090310.etf.ac.bg.rs.memorija_diplomski.adapters.GameGridAdapter;
import ss090310.etf.ac.bg.rs.memorija_diplomski.utils.Player;
import ss090310.etf.ac.bg.rs.memorija_diplomski.R;

public class MultiPlayerActivity extends AppCompatActivity {

    private static final String TAG = "MultiPlayerActivity";
    List<Player> players;
    GameGridAdapter mAdapter;
    int seed;
    boolean gameHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_player);

        Intent intent = getIntent();

        int numCards = intent.getIntExtra("cardNum", 16);
        String difficulty = intent.getStringExtra("difficulty");
        seed = intent.getIntExtra("seed", 16);
        gameHost = intent.getBooleanExtra("host", false);

        mAdapter = new GameGridAdapter(this, numCards, difficulty, seed);

        GridView gameGrid = (GridView) findViewById(R.id.multiplayer_grid);
        gameGrid.setNumColumns(4);

        gameGrid.setAdapter(mAdapter);

        gameGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mAdapter.flip(position);
                // TODO: send a message to other devices which card has been flipped

            }

        });

    }

    // TODO: make a thread which will transfer messages between devices during the game

}
