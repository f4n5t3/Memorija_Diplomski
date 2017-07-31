package ss090310.etf.ac.bg.rs.memorija_diplomski;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class MultiPlayerLobbyActivity extends AppCompatActivity {

    private static final String TAG = "MultiPlayerLobby";

    WifiP2pManager mManager;
    WifiP2pManager.Channel mChannel;
    IntentFilter mIntentFilter;
    WifiP2PBroadcastReceiver mReceiver;

    ArrayList<WifiP2pDevice> peers = new ArrayList<>();
    WifiPeerListAdapter wifiPeerListAdapter;

    boolean wifiP2pEnabled = false;
    boolean gameHost = false;

    List<Player> players;
    int playerNum;
    int seed;
    String hostUsername;

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
        setContentView(R.layout.activity_multi_player_lobby);
        // Determine if the view is inflated on the host device
        gameHost = getIntent().getBooleanExtra("host", false);

        players = new ArrayList<>();
        playerNum = 0;
        
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
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(MultiPlayerLobbyActivity.this);
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

        deviceListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (gameHost) {
                    WifiP2pDevice device = peers.get(position);
                    WifiP2pConfig deviceConfig = new WifiP2pConfig();
                    deviceConfig.deviceAddress = device.deviceAddress;
                    deviceConfig.wps.setup = WpsInfo.PBC;
                    deviceConfig.groupOwnerIntent = 0;
                    players.add(new Player(device.deviceName));

                    mManager.connect(mChannel, deviceConfig, new WifiP2pManager.ActionListener() {
                        @Override
                        public void onSuccess() {
                            // Broadcast receiver will handle this
                        }

                        @Override
                        public void onFailure(int reason) {
                            Toast.makeText(MultiPlayerLobbyActivity.this, "Connect failed, please retry", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        Button startGameButton = (Button) findViewById(R.id.start_game_button);
        if (!gameHost) {
            ((ViewManager) startGameButton.getParent()).removeView(startGameButton);
        } else {
            startGameButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Random r = new Random();
                    seed = r.nextInt(1000) + 1;
                    SharedPreferences prefs = getSharedPreferences(MainActivity.GAME_PREFS, Context.MODE_PRIVATE);
                    int cardNum = prefs.getInt("card_number", 16);
                    String difficulty = prefs.getString("difficulty", "easy");
                    mReceiver.sendMessage("startGame|" + seed + "|" + cardNum + "|" + difficulty);
                    initializeMultiplayer(cardNum, difficulty);
                }
            });
        }

        mManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        mChannel = mManager.initialize(this, getMainLooper(), null);
        mReceiver = new WifiP2PBroadcastReceiver(mManager, mChannel, this, peerListListener);

        mManager.discoverPeers(mChannel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onFailure(int reason) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mReceiver, mIntentFilter);
        Log.d(TAG, "onResume: Receiver registered!");
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mReceiver);
    }

    public void setWifiP2pEnabled(boolean wifiP2Penabled) {
        this.wifiP2pEnabled = wifiP2Penabled;
    }

    public void initializeMultiplayer(int cardNum, String difficulty) {
        // TODO: initialize multiplayer game
        Intent gameIntent = new Intent(this, MultiPlayerActivity.class);
        gameIntent
                .putExtra("cardNum", cardNum)
                .putExtra("difficulty", difficulty)
                .putExtra("seed", seed)
                .putExtra("host", gameHost);

        if (!gameHost) gameIntent.putExtra("hostUsername", hostUsername);

        startActivity(gameIntent);
    }

    public void handleMessage(String msg) {
        String[] parts = msg.split("\\|");
        // start game message format -> startGame|seed|numCards|difficulty
        if (parts[0].equals("startGame")) {
            seed = Integer.parseInt(parts[1]);
            int cardNum = Integer.parseInt(parts[2]);
            String difficulty = parts[3];
            initializeMultiplayer(cardNum, difficulty);
        } else if (parts[0].equals("username")) {
            hostUsername = parts[1];
        }
    }
}
