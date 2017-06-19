package ss090310.etf.ac.bg.rs.memorija_diplomski.multiplayer;

import android.util.Log;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

/**
 * Created by Stefan on 11/06/2017.
 */

public class ServerConnectionThread implements Runnable {

    private static final String TAG = "ServerConnectionThread";

    static final int SOCKET_NUM = 8888;
    public static HashMap<Socket, String> playerDevices = new HashMap<>();
    private static boolean serverStarted = false;
    private static ServerSocket serverSocket;
    private static boolean allPlayersJoined = false;

    public ServerConnectionThread() {

    }

    public boolean isServerStarted() {
        return serverStarted;
    }

    @Override
    public void run() {
        if (serverSocket == null) {
            try {
                serverSocket = new ServerSocket(SOCKET_NUM);
                serverStarted = true;
                Log.d(TAG, "Started server connection thread, port num " + SOCKET_NUM);
                while (true) {
                    Socket socket = serverSocket.accept();
                    // TODO: accept connections from other players and start game when all have joined
                    if (!allPlayersJoined) {
                        // TODO: start a listener thread for each player joined
                        ServerListenerThread serverListenerThread = new ServerListenerThread(socket);
                        (new Thread(serverListenerThread)).start();
                        // add player
                        playerDevices.put(socket, null);
                        // TODO: Check whether num of players == playerDevices num of devices and set flag to true
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
