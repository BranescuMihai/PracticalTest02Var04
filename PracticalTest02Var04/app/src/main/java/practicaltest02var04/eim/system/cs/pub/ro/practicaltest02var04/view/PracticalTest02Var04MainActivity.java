package practicaltest02var04.eim.system.cs.pub.ro.practicaltest02var04.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import practicaltest02var04.eim.system.cs.pub.ro.practicaltest02var04.R;
import practicaltest02var04.eim.system.cs.pub.ro.practicaltest02var04.general.Constants;
import practicaltest02var04.eim.system.cs.pub.ro.practicaltest02var04.network.ClientThread;
import practicaltest02var04.eim.system.cs.pub.ro.practicaltest02var04.network.ServerThread;

public class PracticalTest02Var04MainActivity extends AppCompatActivity {

    // Server widgets
    private EditText serverPortEditText = null;
    private Button connectButton = null;

    // Client widgets
    private EditText urlEditView = null;
    private Button getPageButton = null;

    private TextView displayResult;

    private ServerThread serverThread = null;
    private ClientThread clientThread = null;

    private ConnectButtonClickListener connectButtonClickListener = new ConnectButtonClickListener();
    private GetPageResultClickListener getPageResultClickListener = new GetPageResultClickListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(Constants.TAG, "[MAIN ACTIVITY] onCreate() callback method has been invoked");
        setContentView(R.layout.activity_practical_test02_var04_main);

        serverPortEditText = (EditText) findViewById(R.id.server_port_edit_text);
        connectButton = (Button) findViewById(R.id.connect_button);
        connectButton.setOnClickListener(connectButtonClickListener);

        urlEditView = (EditText) findViewById(R.id.client_url_edit_text);
        getPageButton = (Button) findViewById(R.id.get_response);
        getPageButton.setOnClickListener(getPageResultClickListener);
        displayResult = (TextView) findViewById(R.id.url_text_view);
    }

    @Override
    protected void onDestroy() {
        Log.i(Constants.TAG, "[MAIN ACTIVITY] onDestroy() callback method has been invoked");
        if (serverThread != null) {
            serverThread.stopThread();
        }
        super.onDestroy();
    }

    private class ConnectButtonClickListener implements Button.OnClickListener {

        @Override
        public void onClick(View view) {
            String serverPort = serverPortEditText.getText().toString();
            if (serverPort.isEmpty()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Server port should be filled!", Toast.LENGTH_SHORT).show();
                return;
            }
            serverThread = new ServerThread(Integer.parseInt(serverPort));
            if (serverThread.getServerSocket() == null) {
                Log.e(Constants.TAG, "[MAIN ACTIVITY] Could not create server thread!");
                return;
            }
            serverThread.start();
        }

    }

    private class GetPageResultClickListener implements Button.OnClickListener {

        @Override
        public void onClick(View view) {
            if (serverThread == null || !serverThread.isAlive()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] There is no server to connect to!", Toast.LENGTH_SHORT).show();
                return;
            }
            String url = urlEditView.getText().toString();
            if (url.isEmpty()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Parameters from client should be filled", Toast.LENGTH_SHORT).show();
                return;
            }

            displayResult.setText(Constants.EMPTY_STRING);

            clientThread = new ClientThread(
                    Integer.parseInt(serverPortEditText.getText().toString()), url, displayResult);
            clientThread.start();
        }

    }
}
