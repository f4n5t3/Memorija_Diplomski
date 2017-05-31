package ss090310.etf.ac.bg.rs.memorija_diplomski;

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

import static android.content.ContentValues.TAG;

/**
 * Created by Stefan on 18/05/2017.
 */

class GameAsyncTask extends AsyncTask<Void, String, Void> {

    private static final int SOCKET_NUM = 8888;
    private static final int TIME_OUT = 500;

    private InetAddress serverAddress;
    private boolean isServer = false;
    private MultiPlayerLobbyActivity mActivity;

    private PrintWriter writer;
    private BufferedReader reader;


    GameAsyncTask(MultiPlayerLobbyActivity activity) {
        mActivity = activity;
        isServer = true;
        Log.d(TAG, "GameAsyncTask: Game created on server side.");
        Toast.makeText(mActivity, "Game created on server side", Toast.LENGTH_SHORT).show();
    }

    GameAsyncTask(MultiPlayerLobbyActivity activity, InetAddress serverAddress) {
        mActivity = activity;
        isServer = false;
        this.serverAddress = serverAddress;
        Log.d(TAG, "GameAsyncTask: Game created on client side.");
        Toast.makeText(activity, "Game created on client side", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected Void doInBackground(Void... params) {
        String msg;
        if (isServer) {
            // Group owner code - game host -> initialize game
            ServerSocket serverSocket = null;
            try {
                serverSocket = new ServerSocket(SOCKET_NUM);
                while (true) {
                    Socket clientSocket = serverSocket.accept();
                    Log.d(TAG, "doInBackground: Client connected.");
                    // Client has connected to the socket and transferred data
                    InputStream inputStream = clientSocket.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(inputStream));
                    writer = new PrintWriter(clientSocket.getOutputStream(), true);
                    Log.d(TAG, "Server created on: " + SOCKET_NUM);

                    while ((msg = reader.readLine()) != null) {
                        Log.d(TAG, "Received message: " + msg);
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
            // Clients code - joined game
            Socket clientSocket = new Socket();
            try {
                clientSocket.bind(null);
                clientSocket.connect(new InetSocketAddress(serverAddress, SOCKET_NUM), TIME_OUT);
                Log.d(TAG, "Connected to " + serverAddress + " on port " + SOCKET_NUM);
                while(true) {
                    InputStream inputStream = clientSocket.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(inputStream));
                    writer = new PrintWriter(clientSocket.getOutputStream(), true);

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
        new Thread(new MessageSender(message)).start();
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        Log.d(TAG, "onProgressUpdate: sending message " + values[0] + " to MultiPlayerLobbyActivity.");
        mActivity.handleMessage(values[0]);
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    private class MessageSender implements Runnable {

        String message;

        MessageSender(String message) {
            this.message = message;
        }

        @Override
        public void run() {
            if (writer != null) {
                writer.println(message);
            } else {
                Log.d(TAG, "run: Error - writer null!");
            }
        }
    }
}

