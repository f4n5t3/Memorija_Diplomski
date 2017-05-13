package ss090310.etf.ac.bg.rs.memorija_diplomski;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MultiPlayerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_player);

        final TextView displayUsername = (TextView) findViewById(R.id.multiplayer_username_display_textview);
        final Button changeUsernameBtn = (Button) findViewById(R.id.change_username_button);
        String username = getSharedPreferences(MainActivity.GAME_PREFS, Context.MODE_PRIVATE).getString("username", "");
        if (username.isEmpty()) {
            displayUsername.setText("");
            changeUsernameBtn.setText(R.string.log_in_label);
        } else {
            displayUsername.setText("Hello, " + username);
            changeUsernameBtn.setText(R.string.change_username_label);
        }

        changeUsernameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(MultiPlayerActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_login, null);
                final EditText username = (EditText) mView.findViewById(R.id.username_input);
                Button loginButton = (Button) mView.findViewById(R.id.login_button);

                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();

                loginButton.setOnClickListener(new View.OnClickListener() {
                    SharedPreferences preferences = getSharedPreferences(MainActivity.GAME_PREFS, Context.MODE_PRIVATE);
                    @Override
                    public void onClick(View v) {
                        if (!username.getText().toString().isEmpty()) {
                            SharedPreferences.Editor usernameEdit = preferences.edit();
                            usernameEdit.putString("username", username.getText().toString());
                            usernameEdit.apply();

                            displayUsername.setText("Hello, " + username.getText().toString());
                            changeUsernameBtn.setText(R.string.change_username_label);
                            dialog.hide();
                        }
                    }
                });
            }
        });

        Button hostGameButton = (Button) findViewById(R.id.host_game_button);
        hostGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Implement hosting game
            }
        });

        Button joinGameButton = (Button) findViewById(R.id.join_game_button);
        joinGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Implement joining game
            }
        });
    }
}
