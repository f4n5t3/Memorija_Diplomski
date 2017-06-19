package ss090310.etf.ac.bg.rs.memorija_diplomski.multiplayer;

import java.net.Socket;

/**
 * Created by Stefan on 14/06/2017.
 */

public class ClientConnectionThread implements Runnable {
    private static Socket socket;
    String destination;
    public static boolean serverStarted = false;
    String username;

    public ClientConnectionThread(String username) {
        this.username = username;
    }

    @Override
    public void run() {
        if (socket == null) {

        }
    }
}
