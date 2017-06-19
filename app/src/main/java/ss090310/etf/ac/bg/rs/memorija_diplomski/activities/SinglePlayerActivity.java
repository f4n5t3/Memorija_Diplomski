package ss090310.etf.ac.bg.rs.memorija_diplomski.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import ss090310.etf.ac.bg.rs.memorija_diplomski.adapters.GameGridAdapter;
import ss090310.etf.ac.bg.rs.memorija_diplomski.utils.Player;
import ss090310.etf.ac.bg.rs.memorija_diplomski.R;

public class SinglePlayerActivity extends AppCompatActivity {

    private GameGridAdapter mGridAdapter;
    private int turn; // 1 - player1, 2 - player2
    private int scores[] = new int[2];
    private Player player1, player2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_player);

        final TextView player1NameLabel = (TextView) findViewById(R.id.player1_username_label);
        final TextView player1Score = (TextView) findViewById(R.id.player1_score_textview);
        final TextView player2NameLabel = (TextView) findViewById(R.id.player2_username_label);
        final TextView player2Score = (TextView) findViewById(R.id.player2_score_textview);

        Intent intent = getIntent();

        int numCards = intent.getIntExtra("cardNum", 0);
        String difficulty = intent.getStringExtra("difficulty");
        final boolean isMultiplayer = intent.getBooleanExtra("multiplayer", false);
        String player1Name = getSharedPreferences(MainActivity.GAME_PREFS, Context.MODE_PRIVATE).getString("username", "").trim();
        String player2Name;

        player1NameLabel.setText(player1Name + ": ");
        player1Score.setText("0");
        player1 = new Player(player1Name);

        mGridAdapter = new GameGridAdapter(this, numCards, difficulty, isMultiplayer);
        mGridAdapter.setPlayer1(player1Name);

        if (isMultiplayer) {
            player2Name = intent.getStringExtra("player2").trim();
            player2NameLabel.setText(player2Name + ": ");
            player2Score.setText("0");
            player2 = new Player(player2Name);
            player1NameLabel.setTypeface(null, Typeface.BOLD_ITALIC);
            player2NameLabel.setTypeface(null, Typeface.NORMAL);
            mGridAdapter.setPlayer2(player2Name);
        } else {
            player2NameLabel.setText("");
            player2Score.setText("");
        }

        GridView gameGrid = (GridView) findViewById(R.id.game_grid_view);
        gameGrid.setNumColumns(4);

        gameGrid.setAdapter(mGridAdapter);

        gameGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mGridAdapter.flip(position);
                // TODO: change so result is updated real-time (when there is a match), not when user flips the next card
                player1Score.setText(mGridAdapter.getP1MatchedNum() + "");
                if (isMultiplayer) {
                    player2Score.setText(mGridAdapter.getP2MatchedNum() + "");
                    if (mGridAdapter.getTurn() == 1) {
                        player1NameLabel.setTypeface(null, Typeface.BOLD_ITALIC);
                        player2NameLabel.setTypeface(null, Typeface.NORMAL);
                    }
                    else {
                        player1NameLabel.setTypeface(null, Typeface.NORMAL);
                        player2NameLabel.setTypeface(null, Typeface.BOLD_ITALIC);
                    }
                }
            }
        });

    }

}
