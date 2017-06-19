package ss090310.etf.ac.bg.rs.memorija_diplomski.adapters;

import android.content.Context;
import android.net.wifi.p2p.WifiP2pDevice;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import ss090310.etf.ac.bg.rs.memorija_diplomski.R;

/**
 * Created by Stefan on 15/05/2017.
 */

public class WifiPeerListAdapter extends ArrayAdapter<WifiP2pDevice> {

    private List<WifiP2pDevice> devices;
    private Context mContext;

    public WifiPeerListAdapter(Context context, int resourceId, List<WifiP2pDevice> devices) {
        super(context, resourceId, devices);
        this.devices = devices;
        mContext = context;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.device_list_item, null);
        }
        WifiP2pDevice device = devices.get(position);
        if (device != null) {
            TextView deviceName = (TextView) v.findViewById(R.id.device_name);
            TextView deviceStatus = (TextView) v.findViewById(R.id.device_status);
            if (deviceName != null) {
                deviceName.setText(device.deviceName);
            }
            if (deviceStatus != null) {
                deviceStatus.setText(getDeviceStatus(device.status));
            }
        }

        return v;
    }

    private String getDeviceStatus(int status) {
        switch (status) {
            case WifiP2pDevice.AVAILABLE:
                return "Available";
            case WifiP2pDevice.INVITED:
                return "Invited";
            case WifiP2pDevice.CONNECTED:
                return "Connected";
            case WifiP2pDevice.FAILED:
                return "Failed";
            case WifiP2pDevice.UNAVAILABLE:
                return "Unavailable";
            default:
                return "Unknown";
        }
    }
}
