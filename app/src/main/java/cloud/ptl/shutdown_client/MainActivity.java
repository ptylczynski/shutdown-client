package cloud.ptl.shutdown_client;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    Button aButton;
    Switch aSwitch;
    TextView textView;
    EditText editText;

    Boolean shouldStop = true;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        aButton = (Button) findViewById(R.id.button_send);
        aSwitch = (Switch) findViewById(R.id.switch1);
        context = getApplicationContext();
        textView = (TextView) findViewById(R.id.textView4);
        editText = (EditText) findViewById(R.id.editTextTextPersonName);
        final Handler handler = new Handler();

        aButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(aSwitch.isChecked()){
                    Toast.makeText(getApplicationContext(), "Suspending", Toast.LENGTH_SHORT).show();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                String IPPort = editText.getText().toString();
                                Socket s = new Socket(
                                        IPPort.substring(0, IPPort.indexOf(":")),
                                        Integer.valueOf(IPPort.substring(IPPort.indexOf(":") + 1)));
                                OutputStream out = s.getOutputStream();
                                PrintWriter output = new PrintWriter(out);
                                output.print("{\n" +
                                        "action:SUSPEND\n" +
                                        "secret:super_secret\n" +
                                        "}");
                                output.flush();
                                BufferedReader input = new BufferedReader(new InputStreamReader(s.getInputStream()));
                                StringBuilder sb = new StringBuilder();
                                String line = new String();
                                while((line = input.readLine()) != null){
                                    if(line.contains("message")){
                                        break;
                                    }
                                }
                                line = line.substring(
                                        line.indexOf(":") + 1,
                                        line.length()
                                );
                                final String fline = line;
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        textView.setText(fline);
                                        // Toast.makeText(context, fline, Toast.LENGTH_SHORT);
                                    }
                                });

                                output.close();
                                out.close();
                                s.close();
                            } catch (IOException e) {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        textView.setText("Wrong address");
                                        // Toast.makeText(context, fline, Toast.LENGTH_SHORT);
                                    }
                                });
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Stopping", Toast.LENGTH_SHORT).show();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                String IPPort = editText.getText().toString();
                                Socket s = new Socket(
                                        IPPort.substring(0, IPPort.indexOf(":")),
                                        Integer.valueOf(IPPort.substring(IPPort.indexOf(":") + 1)));
                                OutputStream out = s.getOutputStream();
                                PrintWriter output = new PrintWriter(out);
                                output.print("{\n" +
                                        "action:STOP\n" +
                                        "secret:super_secret\n" +
                                        "}");
                                output.flush();
                                BufferedReader input = new BufferedReader(new InputStreamReader(s.getInputStream()));
                                StringBuilder sb = new StringBuilder();
                                String line = new String();
                                while((line = input.readLine()) != null){
                                    if(line.contains("message")){
                                        break;
                                    }
                                }
                                line = line.substring(
                                        line.indexOf(":") + 1,
                                        line.length()
                                );
                                final String fline = line;
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        textView.setText(fline);
                                        // Toast.makeText(context, fline, Toast.LENGTH_SHORT);
                                    }
                                });

                                output.close();
                                out.close();
                                s.close();
                            } catch (IOException e) {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        textView.setText("Wrong address");
                                        // Toast.makeText(context, fline, Toast.LENGTH_SHORT);
                                    }
                                });
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }

            }
        });
    }
}