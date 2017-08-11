package ss090310.etf.ac.bg.rs.memorija_diplomski;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MultiplayerResultsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplayer_results);

        if (WifiP2PBroadcastReceiver.startGameAsyncTask != null)
            WifiP2PBroadcastReceiver.startGameAsyncTask.sendMessage("cancel");

        Intent intent = getIntent();
        String player1 = intent.getStringExtra("player1");
        String player2 = intent.getStringExtra("player2");
        int score1 = intent.getIntExtra("score1", 0);
        int score2 = intent.getIntExtra("score2", 0);

        TextView scoreTextView = (TextView) findViewById(R.id.multiplayer_scores_textview);
        scoreTextView.setText(player1 + "  " + score1 + " - " + score2 + "  " + player2);

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
}
