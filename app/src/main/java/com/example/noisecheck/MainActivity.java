package com.example.noisecheck;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import static java.lang.Thread.sleep;

public class MainActivity extends AppCompatActivity {

    Button start, stop;
    TextView textView;
    Thread thread;
    Handler handler;
    boolean threadFlag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        start = (Button)findViewById(R.id.button1);
        stop = (Button)findViewById(R.id.button2);
        textView = (TextView)findViewById(R.id.textView);

        final SoundMeter sm = new SoundMeter();
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sm.start();
                textView.setText("");
                threadFlag = true;
                thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while(threadFlag) {
                            try {
                                handler.sendEmptyMessage(1);
                                sleep(500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                });
                thread.start();
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sm.stop();
                threadFlag = false;

            }
        });

        handler = new Handler() {

            public void handleMessage(Message msg) {
                if (msg.what == 1)
                    textView.append(sm.getAmplitude()+"\n");
            }

        };

    }

}
