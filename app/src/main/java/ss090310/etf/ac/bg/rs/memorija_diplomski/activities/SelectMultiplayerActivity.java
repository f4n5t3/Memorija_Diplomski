package ss090310.etf.ac.bg.rs.memorija_diplomski.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import ss090310.etf.ac.bg.rs.memorija_diplomski.R;

public class SelectMultiplayerActivity extends AppCompatActivity {

    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_multiplayer);

        preferences = getSharedPreferences(MainActivity.GAME_PREFS, Context.MODE_PRIVATE);

        Button hostGameButton = (Button) findViewById(R.id.button_host_game);
        hostGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectMultiplayerActivity.this, MultiPlayerLobbyActivity.class);
                intent.putExtra("host", true)
                        .putExtra("cardNum", preferences.getInt("card_number", 16))
                        .putExtra("difficulty", preferences.getString("difficulty", "easy"));
                startActivity(intent);
            }
        });

        Button joinGameButton = (Button) findViewById(R.id.button_join_game);
        joinGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectMultiplayerActivity.this, MultiPlayerLobbyActivity.class);
                intent.putExtra("host", false);
                startActivity(intent);
            }
        });

        Button localMultiplayerButton = (Button) findViewById(R.id.button_play_local);
        localMultiplayerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(SelectMultiplayerActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_login, null);
                final EditText username = (EditText) mView.findViewById(R.id.username_input);
                username.setHint(R.string.player2_username);
                Button loginButton = (Button) mView.findViewById(R.id.login_button);

                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();

                loginButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!username.getText().toString().isEmpty()) {
                            SharedPreferences.Editor usernameEdit = preferences.edit();
                            usernameEdit.putString("player2", username.getText().toString());
                            usernameEdit.apply();
                            dialog.hide();

                            String difficulty = preferences.getString("difficulty", "easy");
                            int cardNum = preferences.getInt("card_number", 16);
                            String player2Name = preferences.getString("player2", "");
                            Intent intent = new Intent(SelectMultiplayerActivity.this, SinglePlayerActivity.class);
                            intent.putExtra("difficulty", difficulty)
                                    .putExtra("cardNum", cardNum)
                                    .putExtra("multiplayer", true)
                                    .putExtra("player2", player2Name);
                            startActivity(intent);
                        }
                    }
                });


            }
        });
    }
}
