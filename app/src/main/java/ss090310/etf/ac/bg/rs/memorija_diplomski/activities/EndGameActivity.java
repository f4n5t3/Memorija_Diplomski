package ss090310.etf.ac.bg.rs.memorija_diplomski.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ss090310.etf.ac.bg.rs.memorija_diplomski.R;
import ss090310.etf.ac.bg.rs.memorija_diplomski.utils.ScoreEntry;

public class EndGameActivity extends AppCompatActivity {

    private SharedPreferences prefs;
    public static final String GAME_PREFS = MainActivity.GAME_PREFS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_game);

        prefs = getSharedPreferences(GAME_PREFS, Context.MODE_PRIVATE);

        Intent intent = getIntent();
        int score = intent.getIntExtra("score", 0);

        updateHighScores(score);

        TextView gameScore = (TextView) findViewById(R.id.end_game_score_val);
        gameScore.setText(String.valueOf(score));

        displayHighScores();

        Button backToMainButton = (Button) findViewById(R.id.button_back_to_main);
        backToMainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backToMainIntent = new Intent(getApplicationContext(), MainActivity.class);
                backToMainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(backToMainIntent);
            }
        });
    }

    private void updateHighScores(int score) {
        if (score > 0) {
            SharedPreferences.Editor scoresEdit = prefs.edit();
            String scores = prefs.getString("highScores", "");
            String username = prefs.getString("username", "");
            ScoreEntry newScore = new ScoreEntry(username, score);
            if (scores.length() > 0) {
                List<ScoreEntry> scoreList = new ArrayList<>();
                String[] oldScores = scores.split("\\|");
                for (String s : oldScores) {
                    String[] parts = s.split(" - ");
                    scoreList.add(new ScoreEntry(parts[0], Integer.parseInt(parts[1])));
                }
                scoreList.add(newScore);

                Collections.sort(scoreList);

                StringBuilder builder = new StringBuilder("");
                for (int i = 0; i < scoreList.size(); i++) {
                    if (i >= 10) break;
                    if (i > 0) builder.append("|");
                    builder.append(scoreList.get(i).toString());
                }

                scoresEdit.putString("highScores", builder.toString());
            } else {
                scoresEdit.putString("highScores", newScore.toString());
            }
            scoresEdit.apply();
        }
    }

    private void displayHighScores() {
        TextView topTenScores = (TextView) findViewById(R.id.game_scores_top10);
        prefs = getSharedPreferences(GAME_PREFS, Context.MODE_PRIVATE);
        String [] savedScores = prefs.getString("highScores", "").split("\\|");

        if (savedScores.length > 0) {
            int cnt = 1;
            StringBuilder builder = new StringBuilder("");
            for (String s : savedScores) {
                builder.append(cnt++).append(". ").append(s).append("\n");
            }

            topTenScores.setText(builder.toString());
        }
    }
}
