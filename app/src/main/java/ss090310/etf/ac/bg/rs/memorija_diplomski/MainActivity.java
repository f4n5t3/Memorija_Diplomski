package ss090310.etf.ac.bg.rs.memorija_diplomski;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences preferences;
    public static final String GAME_PREFS = "SettingsFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        preferences = getSharedPreferences(GAME_PREFS, Context.MODE_PRIVATE);

        String lastUserName = preferences.getString("username", "");
        TextView changeUsernameTextView = (TextView) findViewById(R.id.change_username_textview);

        if (lastUserName.isEmpty()) {
            ((TextView) findViewById(R.id.greeting_textview)).setText("");
            changeUsernameTextView.setText(R.string.log_in_label);
        } else {
            ((TextView) findViewById(R.id.greeting_textview)).setText("Hello, " + lastUserName + "!");
            changeUsernameTextView.setText(R.string.change_username_label);
        }

        changeUsernameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_login, null);
                final EditText username = (EditText) mView.findViewById(R.id.username_input);
                Button loginButton = (Button) mView.findViewById(R.id.login_button);

                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();

                loginButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!username.getText().toString().isEmpty()) {
                            SharedPreferences.Editor usernameEdit = preferences.edit();
                            usernameEdit.putString("username", username.getText().toString());
                            usernameEdit.apply();

                            String lastUserName = preferences.getString("username", "");
                            TextView changeUsernameTextView = (TextView) findViewById(R.id.change_username_textview);
                            ((TextView) findViewById(R.id.greeting_textview)).setText("Hello, " + lastUserName + "!");
                            changeUsernameTextView.setText(R.string.change_username_label);
                            dialog.hide();
                        }
                    }
                });
            }
        });

        Button settingsButton = (Button) findViewById(R.id.settings_button);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(intent);
            }
        });

        Button startGameButton = (Button) findViewById(R.id.start_game_button);

        startGameButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int playerNum = preferences.getInt("player_number", 1);
                String difficulty = preferences.getString("difficulty", "easy");
                int cardNum = preferences.getInt("card_number", 16);
                Intent startGameIntent;

                if (playerNum == 1) {
                    startGameIntent = new Intent(getApplicationContext(), SinglePlayerActivity.class);
                }
                else {
                    startGameIntent = new Intent(getApplicationContext(), MultiPlayerActivity.class);
                    startGameIntent.putExtra("playerNum", playerNum);
                }
                startGameIntent.putExtra("difficulty", difficulty).putExtra("cardNum", cardNum);
                startActivity(startGameIntent);
            }
        });

    }
}
