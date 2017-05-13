package ss090310.etf.ac.bg.rs.memorija_diplomski;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.Arrays;
import java.util.List;

public class SettingsActivity extends AppCompatActivity {

    private static final int SMALL_SCREEN_CARD_NUM_INDEX = 3;
    private static final int NORMAL_SCREEN_CARD_NUM_INDEX = 6;
    private static final int LARGE_SCREEN_CARD_NUM_INDEX = 10;

    SharedPreferences preferences;
    private static final String GAME_PREFS = MainActivity.GAME_PREFS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        preferences = getSharedPreferences(GAME_PREFS, Context.MODE_PRIVATE);

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

        Button confirmButton = (Button) findViewById(R.id.confirm_settings_button);
        confirmButton.setOnClickListener(new View.OnClickListener() {

            Spinner numPlayersSpinner = (Spinner) findViewById(R.id.spinner_num_players);
            Spinner cardNumSpinner = (Spinner) findViewById(R.id.spinner_num_cards);
            Spinner difficultySpinner = (Spinner) findViewById(R.id.spinner_difficulty);

            @Override
            public void onClick(View v) {
                int playerNum = Integer.parseInt(numPlayersSpinner.getSelectedItem().toString());
                int cardNum = Integer.parseInt(cardNumSpinner.getSelectedItem().toString());
                String difficulty = difficultySpinner.getSelectedItem().toString();

                SharedPreferences.Editor prefsEdit = preferences.edit();
                prefsEdit.putString("difficulty", difficulty)
                        .putInt("player_number", playerNum)
                        .putInt("card_number", cardNum);
                prefsEdit.apply();

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }
}
