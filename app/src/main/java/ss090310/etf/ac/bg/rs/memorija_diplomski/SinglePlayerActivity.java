package ss090310.etf.ac.bg.rs.memorija_diplomski;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

public class SinglePlayerActivity extends AppCompatActivity {

    GameGridAdapter mGridAdapter;
    //Player player1, player2;
    public static Handler resultHandler;

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
        //player1 = new Player(player1Name);

        mGridAdapter = new GameGridAdapter(this, numCards, difficulty, isMultiplayer);
        mGridAdapter.setPlayer1(player1Name);

        if (isMultiplayer) {
            player2Name = intent.getStringExtra("player2").trim();
            player2NameLabel.setText(player2Name + ": ");
            player2Score.setText("0");
            //player2 = new Player(player2Name);
            player1NameLabel.setTypeface(null, Typeface.BOLD_ITALIC);
            player2NameLabel.setTypeface(null, Typeface.NORMAL);
            mGridAdapter.setPlayer2(player2Name);
        } else {
            player2NameLabel.setText("");
            player2Score.setText("");
        }

        GridView gameGrid = (GridView) findViewById(R.id.game_grid_view);
        if (numCards <= 32)
            gameGrid.setNumColumns(4);
        else {
            if (numCards%5 == 0)
                gameGrid.setNumColumns(5);
            else if (numCards%6 == 0)
                gameGrid.setNumColumns(6);
            else if (numCards%8 == 0)
                gameGrid.setNumColumns(8);
            else
                gameGrid.setNumColumns(4);
        }

        resultHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
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
        };

        gameGrid.setAdapter(mGridAdapter);

        gameGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mGridAdapter.flip(position);
            }
        });
    }

//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
//        GridView gameGrid = (GridView) findViewById(R.id.game_grid_view);
//        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
//            int colNum = gameGrid.getNumColumns();
//            gameGrid.setNumColumns(mGridAdapter.getCount() / colNum);
//            mGridAdapter.notifyDataSetChanged();
//        }
//    }
}
