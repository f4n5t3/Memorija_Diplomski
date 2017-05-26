package ss090310.etf.ac.bg.rs.memorija_diplomski;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

public class SinglePlayerActivity extends AppCompatActivity {

    GameGridAdapter mGridAdapter;
    boolean multiplayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_player);

        Intent intent = getIntent();

        int numCards = intent.getIntExtra("cardNum", 0);
        String difficulty = intent.getStringExtra("difficulty");
        boolean isMultiplayer = intent.getBooleanExtra("multiplayer", false);

        mGridAdapter = new GameGridAdapter(this, numCards, difficulty, isMultiplayer);

        GridView gameGrid = (GridView) findViewById(R.id.game_grid_view);
        gameGrid.setNumColumns(4);

        gameGrid.setAdapter(mGridAdapter);

        gameGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            TextView scoreDisplay = (TextView) findViewById(R.id.score_display_textview);

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mGridAdapter.flip(position);
                scoreDisplay.setText("" + mGridAdapter.getAttemptsNum());
            }
        });

    }

}
