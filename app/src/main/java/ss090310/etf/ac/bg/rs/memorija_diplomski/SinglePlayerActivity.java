package ss090310.etf.ac.bg.rs.memorija_diplomski;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

public class SinglePlayerActivity extends AppCompatActivity {

    GameGridAdapter mGridAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_player);

        Intent intent = getIntent();

        int numCards = intent.getIntExtra("cardNum", 0);
        String difficulty = intent.getStringExtra("difficulty");

        mGridAdapter = new GameGridAdapter(this, numCards, difficulty.toLowerCase());

        GridView gameGrid = (GridView) findViewById(R.id.game_grid_view);

        gameGrid.setAdapter(mGridAdapter);

        gameGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mGridAdapter.flip(position);
            }
        });
    }


}
