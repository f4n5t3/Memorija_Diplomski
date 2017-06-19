package ss090310.etf.ac.bg.rs.memorija_diplomski.multiplayer;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by Stefan on 17/06/2017.
 */

public class ServerListenerThread implements Runnable {

    private static final String TAG = "ServerListenerThread";

    private Socket hostSocket;

    public ServerListenerThread(Socket hostSocket) {
        this.hostSocket = hostSocket;
    }

    @Override
    public void run() {
        while (true) {
            try {
                // TODO: Process messages sent from clients to server
                InputStream inputStream = hostSocket.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                PrintWriter writer = new PrintWriter(hostSocket.getOutputStream(), true);
                String message = "";

                while ((message = reader.readLine()) != null) {
                    Log.d(TAG, "Server received message: " + message);
                    processMessage(message);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void processMessage(String message) {
        // TODO: Implement message processing
        String[] parts = message.split("\\|");
        if (parts[0].equals("startGame")) {

        } else {

        }
    }
}
