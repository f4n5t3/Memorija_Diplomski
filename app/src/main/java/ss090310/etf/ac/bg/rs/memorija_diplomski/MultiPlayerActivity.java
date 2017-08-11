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

import static ss090310.etf.ac.bg.rs.memorija_diplomski.StartGameAsyncTask.writer;

public class MultiPlayerActivity extends AppCompatActivity {

    GameGridAdapter mAdapter;
    int seed;
    boolean gameHost;
    public static Handler resultHandler;
    int turn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_player);

        final TextView player1NameLabel = (TextView) findViewById(R.id.player1_username_label);
        final TextView player1Score = (TextView) findViewById(R.id.player1_score_textview);
        final TextView player2NameLabel = (TextView) findViewById(R.id.player2_username_label);
        final TextView player2Score = (TextView) findViewById(R.id.player2_score_textview);

        Intent intent = getIntent();

        int numCards = intent.getIntExtra("cardNum", 16);
        String difficulty = intent.getStringExtra("difficulty");
        seed = intent.getIntExtra("seed", 16);
        gameHost = intent.getBooleanExtra("host", false);
        String player1Name, player2Name;
        if (gameHost) {
            player1Name = getSharedPreferences(MainActivity.GAME_PREFS, Context.MODE_PRIVATE).getString("username", "").trim();
            player2Name = StartGameAsyncTask.players.values().iterator().next();
            turn = 1;
        } else {
            player1Name = intent.getStringExtra("hostUsername");
            player2Name = getSharedPreferences(MainActivity.GAME_PREFS, Context.MODE_PRIVATE).getString("username", "").trim();
            turn = 2;
        }

        player1NameLabel.setText(player1Name + ": ");
        player2NameLabel.setText(player2Name + ": ");
        player1Score.setText("0");
        player2Score.setText("0");
        player1NameLabel.setTypeface(null, Typeface.BOLD_ITALIC);
        player2NameLabel.setTypeface(null, Typeface.NORMAL);

        mAdapter = new GameGridAdapter(this, numCards, difficulty, seed);
        mAdapter.setPlayer1(player1Name);
        mAdapter.setPlayer2(player2Name);

        resultHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                player1Score.setText(mAdapter.getP1MatchedNum() + "");
                player2Score.setText(mAdapter.getP2MatchedNum() + "");
                if (mAdapter.getTurn() == 1) {
                    player1NameLabel.setTypeface(null, Typeface.BOLD_ITALIC);
                    player2NameLabel.setTypeface(null, Typeface.NORMAL);
                }
                else {
                    player1NameLabel.setTypeface(null, Typeface.NORMAL);
                    player2NameLabel.setTypeface(null, Typeface.BOLD_ITALIC);
                }
            }
        };

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

        gameGrid.setAdapter(mAdapter);
        WifiP2PBroadcastReceiver.startGameAsyncTask.setGameGridAdapter(mAdapter);

        gameGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mAdapter.getTurn() == turn) {
                    mAdapter.flip(position);
                    sendMessage("flip|" + position);
                }
            }
        });
    }

    private void sendMessage(String message) {
        new Thread(new MessageSender(message, writer)).start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        MultiPlayerLobbyActivity.mReceiver.disconnect();
    }
}
