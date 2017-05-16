package ss090310.etf.ac.bg.rs.memorija_diplomski;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private static final int SMALL_SCREEN_CARD_NUM_INDEX = 3;
    private static final int NORMAL_SCREEN_CARD_NUM_INDEX = 6;
    private static final int LARGE_SCREEN_CARD_NUM_INDEX = 10;

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

        String[] cardNumEntries = getResources().getStringArray(R.array.number_of_cards_entries);
        List<String> cardNumsList = Arrays.asList(cardNumEntries);

        int screenSize = getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;
        final Spinner numCardsSpinner = (Spinner) findViewById(R.id.spinner_num_cards);
        ArrayAdapter<String> adapter;

        switch (screenSize) {
            case Configuration.SCREENLAYOUT_SIZE_SMALL:
                adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, cardNumsList.subList(0, SMALL_SCREEN_CARD_NUM_INDEX));
                break;
            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, cardNumsList.subList(0, NORMAL_SCREEN_CARD_NUM_INDEX));
                break;
            case Configuration.SCREENLAYOUT_SIZE_LARGE:
                adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, cardNumsList.subList(0, LARGE_SCREEN_CARD_NUM_INDEX));
                break;
            default:
                adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, cardNumsList);
                break;
        }
        numCardsSpinner.setAdapter(adapter);

        Button startSinglePlayerButton = (Button) findViewById(R.id.start_singleplayer_button);
        startSinglePlayerButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                saveSettings();
                Log.i(TAG, "onClick: Settings Saved");
                String difficulty = preferences.getString("difficulty", "easy");
                int cardNum = preferences.getInt("card_number", 16);

                Intent startGameIntent = new Intent(getApplicationContext(), SinglePlayerActivity.class);

                startGameIntent.putExtra("difficulty", difficulty).putExtra("cardNum", cardNum);
                startActivity(startGameIntent);
            }
        });

        Button startMultiPlayerButton = (Button) findViewById(R.id.start_multiplayer_button);
        startMultiPlayerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveSettings();
                Log.i(TAG, "onClick: Settings Saved");
                String difficulty = preferences.getString("difficulty", "easy");
                int cardNum = preferences.getInt("card_number", 16);
                Intent startGameIntent = new Intent(getApplicationContext(), MultiPlayerActivity.class);

                startGameIntent.putExtra("difficulty", difficulty).putExtra("cardNum", cardNum);
                startActivity(startGameIntent);
            }
        });

    }

    private void saveSettings() {
        Spinner cardNumSpinner = (Spinner) findViewById(R.id.spinner_num_cards);
        Spinner difficultySpinner = (Spinner) findViewById(R.id.spinner_difficulty);

        int cardNum = Integer.parseInt(cardNumSpinner.getSelectedItem().toString());
        String difficulty = difficultySpinner.getSelectedItem().toString();

        SharedPreferences.Editor prefsEdit = preferences.edit();
        prefsEdit.putString("difficulty", difficulty)
                .putInt("card_number", cardNum);
        prefsEdit.apply();
    }
}
