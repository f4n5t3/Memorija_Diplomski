package ss090310.etf.ac.bg.rs.memorija_diplomski;

import android.content.Context;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;

public class MultiPlayerActivity extends AppCompatActivity implements WifiP2pManager.PeerListListener {

    private static final String TAG = "MultiPlayerActivity";

    WifiP2pManager mManager;
    WifiP2pManager.Channel mChannel;
    IntentFilter mIntentFilter;
    WifiP2PBroadcastReceiver mReceiver;

    ArrayList<WifiP2pDevice> peers = new ArrayList<>();
    WifiPeerListAdapter wifiPeerListAdapter;

    private WifiP2pManager.PeerListListener peerListListener = new WifiP2pManager.PeerListListener() {
        @Override
        public void onPeersAvailable(WifiP2pDeviceList peerList) {
            Collection<WifiP2pDevice> refreshedPeers = peerList.getDeviceList();

            if (!refreshedPeers.equals(peers)) {
                peers.clear();
                peers.addAll(refreshedPeers);

                wifiPeerListAdapter.notifyDataSetChanged();
            }

            if (peers.size() == 0) {
                Log.d(TAG, "onPeersAvailable: No devices found.");
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_player);
        
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
        Log.d(TAG, "onCreate: Intent filter created");

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

        ListView deviceListView = (ListView) findViewById(R.id.peers_list);
        wifiPeerListAdapter = new WifiPeerListAdapter(this, R.layout.device_list_item, peers);
        deviceListView.setAdapter(wifiPeerListAdapter);

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

        mManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        mChannel = mManager.initialize(this, getMainLooper(), null);

        mManager.discoverPeers(mChannel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "onSuccess: ");
            }

            @Override
            public void onFailure(int reason) {
                Log.d(TAG, "onFailure: ");
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mReceiver = new WifiP2PBroadcastReceiver(mManager, mChannel, this, peerListListener);
        registerReceiver(mReceiver, mIntentFilter);
        Log.d(TAG, "onResume: Receiver registered!");
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mReceiver);
    }

    @Override
    public void onPeersAvailable(WifiP2pDeviceList peers) {

    }
}
