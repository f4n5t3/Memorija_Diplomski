package ss090310.etf.ac.bg.rs.memorija_diplomski;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pManager;
import android.util.Log;

/**
 * Created by Stefan on 16/05/2017.
 */

public class WifiP2PBroadcastReceiver extends BroadcastReceiver {
    
    private static final String TAG = "WifiP2PBroadcastReceive";

    private WifiP2pManager mManager;
    private WifiP2pManager.Channel mChannel;
    private Activity mActivity;
    private WifiP2pManager.PeerListListener mListener;

    public WifiP2PBroadcastReceiver(WifiP2pManager manager, WifiP2pManager.Channel channel, Activity activity, WifiP2pManager.PeerListListener listener) {
        super();
        mManager = manager;
        mChannel = channel;
        mActivity = activity;
        mListener = listener;
        Log.d(TAG, "WifiP2PBroadcastReceiver: Constructor");
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive: Enter");
        String action = intent.getAction();
        switch (action) {
            case WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION:
                // check if wifi p2p mode is on or off and alert the activity
                int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
                if (state == WifiP2pManager.WIFI_P2P_STATE_DISABLED) {
                    // set flag in activity to false
                } else {
                    // set flag in activity to true
                }
                break;
            case WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION:
                // peer list changed
                if (mManager != null) {
                    mManager.requestPeers(mChannel, mListener);
                }
                break;
            case WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION:
                // connection state changed
                break;
            case WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION:
                intent.getParcelableExtra(WifiP2pManager.EXTRA_WIFI_P2P_DEVICE);
                break;
        }
        Log.d(TAG, "onReceive: Exit");
    }
}
