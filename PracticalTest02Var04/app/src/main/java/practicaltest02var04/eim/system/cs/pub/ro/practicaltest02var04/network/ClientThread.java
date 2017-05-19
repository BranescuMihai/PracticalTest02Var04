package practicaltest02var04.eim.system.cs.pub.ro.practicaltest02var04.network;

import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import practicaltest02var04.eim.system.cs.pub.ro.practicaltest02var04.general.Constants;
import practicaltest02var04.eim.system.cs.pub.ro.practicaltest02var04.general.Utilities;

public class ClientThread extends Thread {

    private String address;
    private int port;
    private String url;
    private TextView displayResult;

    private Socket socket;

    public ClientThread(int port, String url, TextView displayResult) {
        this.address = "localhost";
        this.port = port;
        this.url = url;
        this.displayResult = displayResult;
    }

    @Override
    public void run() {
        try {
            socket = new Socket(address, port);
            if (socket == null) {
                Log.e(Constants.TAG, "[CLIENT THREAD] Could not create socket!");
                return;
            }
            BufferedReader bufferedReader = Utilities.getReader(socket);
            PrintWriter printWriter = Utilities.getWriter(socket);
            if (bufferedReader == null || printWriter == null) {
                Log.e(Constants.TAG, "[CLIENT THREAD] Buffered Reader / Print Writer are null!");
                return;
            }

            printWriter.println(url);
            printWriter.flush();

            String pageInformation;
            while ((pageInformation = bufferedReader.readLine()) != null) {
                final String finalizedPageInformation = pageInformation;
                displayResult.post(new Runnable() {
                    @Override
                    public void run() {
                        displayResult.setText(displayResult.getText().toString() + finalizedPageInformation);
                    }
                });
            }
        } catch (IOException ioException) {
            Log.e(Constants.TAG, "[CLIENT THREAD] An exception has occurred: " + ioException.getMessage());
            if (Constants.DEBUG) {
                ioException.printStackTrace();
            }
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException ioException) {
                    Log.e(Constants.TAG, "[CLIENT THREAD] An exception has occurred: " + ioException.getMessage());
                    if (Constants.DEBUG) {
                        ioException.printStackTrace();
                    }
                }
            }
        }
    }

}
