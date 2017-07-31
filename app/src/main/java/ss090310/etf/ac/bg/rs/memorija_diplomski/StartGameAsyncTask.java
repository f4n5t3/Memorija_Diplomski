package ss090310.etf.ac.bg.rs.memorija_diplomski;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

/**
 * Created by Stefan on 18/05/2017.
 */

class StartGameAsyncTask extends AsyncTask<Void, String, Void> {

    private static final String TAG = "StartGameAsyncTask";
    private static final int SOCKET_NUM = 8888;
    private static final int TIME_OUT = 0;

    private InetAddress serverAddress;
    private boolean isServer = false;
    private MultiPlayerLobbyActivity lobbyActivity;
    private GameGridAdapter gameGridAdapter;

    public static PrintWriter writer;
    private static BufferedReader reader;

    public static HashMap<Socket, String> players;
    public static Socket clientSocket;

    StartGameAsyncTask(MultiPlayerLobbyActivity activity) {
        lobbyActivity = activity;
        isServer = true;
        Log.d(TAG, "StartGameAsyncTask: Game created on server side.");
        Toast.makeText(lobbyActivity, "Server ready", Toast.LENGTH_SHORT).show();
        players = new HashMap<>();
    }

    StartGameAsyncTask(MultiPlayerLobbyActivity activity, InetAddress serverAddress) {
        lobbyActivity = activity;
        isServer = false;
        this.serverAddress = serverAddress;
        Log.d(TAG, "StartGameAsyncTask: Game created on client side.");
        Toast.makeText(activity, "Client ready", Toast.LENGTH_SHORT).show();
        players = null;
    }

    public void setGameGridAdapter(GameGridAdapter adapter) {
        gameGridAdapter = adapter;
    }

    @Override
    protected Void doInBackground(Void... params) {
        String msg;
        if (isServer) {
            // Group owner code - game host -> initialize game
            ServerSocket serverSocket = null;
            try {
                serverSocket = new ServerSocket(SOCKET_NUM);
                clientSocket = serverSocket.accept();
                Log.d(TAG, "doInBackground: Client connected.");
                // client username
                reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                writer = new PrintWriter(clientSocket.getOutputStream(), true);
                msg = reader.readLine();
                if (msg != null) {
                    players.put(clientSocket, msg.trim());
                }
                // send host username
                String username = lobbyActivity.getSharedPreferences(MainActivity.GAME_PREFS, Context.MODE_PRIVATE).getString("username", "");
                sendMessage("username|" + username);
                while (true) {
                    // Client has connected to the socket and transferred data
                    InputStream inputStream = clientSocket.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(inputStream));
                    writer = new PrintWriter(clientSocket.getOutputStream(), true);

                    while (true) {
                        msg = reader.readLine();
                        if (msg.equals("cancel")) break;
                        publishProgress(msg);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (serverSocket != null) {
                    try {
                        serverSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            // Client code - joined game
            clientSocket = new Socket();
            try {
                clientSocket.bind(null);
                clientSocket.connect(new InetSocketAddress(serverAddress, SOCKET_NUM), TIME_OUT);
                Log.d(TAG, "Connected to " + serverAddress + " on port " + SOCKET_NUM);
                // send username
                writer = new PrintWriter((clientSocket.getOutputStream()), true);
                String username = lobbyActivity.getSharedPreferences(MainActivity.GAME_PREFS, Context.MODE_PRIVATE).getString("username", "");
                sendMessage(username);
                while(true) {
                    InputStream inputStream = clientSocket.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(inputStream));
                    writer = new PrintWriter((clientSocket.getOutputStream()), true);

                    while (true) {
                        msg = reader.readLine();
                        if (msg == null || isCancelled()) break;
                        Log.d(TAG, "Read message : " + msg);
                        publishProgress(msg);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (clientSocket.isConnected()) {
                    try {
                        clientSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        return null;
    }

    void sendMessage(String message) {
        new Thread(new MessageSender(message, writer)).start();
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        Log.d(TAG, "onProgressUpdate: sending message " + values[0] + " to MultiPlayerLobbyActivity.");
        if (values[0].startsWith("flip") && gameGridAdapter != null) {
            String[] parts = values[0].split("\\|");
            int toFlip = Integer.parseInt(parts[1]);
            gameGridAdapter.flip(toFlip);
        } else if (lobbyActivity != null)
            lobbyActivity.handleMessage(values[0]);
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

}

