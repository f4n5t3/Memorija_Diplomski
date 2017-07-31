package ss090310.etf.ac.bg.rs.memorija_diplomski;

import android.util.Log;

import java.io.PrintWriter;

import static android.content.ContentValues.TAG;

/**
 * Created by Stefan on 01/07/2017.
 */

public class MessageSender implements Runnable {

    private String message;
    private PrintWriter writer;

    MessageSender(String message, PrintWriter writer) {
        this.message = message;
        this.writer = writer;
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