package ss090310.etf.ac.bg.rs.memorija_diplomski;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.List;

public class MultiPlayerActivity extends AppCompatActivity {

    List<Player> players;
    GameGridAdapter mAdapter;
    int seed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_player);

        Intent intent = getIntent();

        int numCards = intent.getIntExtra("cardNum", 16);
        String difficulty = intent.getStringExtra("difficulty");
        seed = intent.getIntExtra("seed", 16);

        mAdapter = new GameGridAdapter(this, numCards, difficulty, seed);

        GridView gameGrid = (GridView) findViewById(R.id.multiplayer_grid);
        gameGrid.setNumColumns(4);

        gameGrid.setAdapter(mAdapter);

        gameGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mAdapter.flip(position);
            }
        });
    }

}
