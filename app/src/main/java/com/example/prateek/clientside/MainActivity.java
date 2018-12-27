package com.example.prateek.clientside;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import helper.MqttHelper;

public class MainActivity extends AppCompatActivity {
    private Button button;
    private EditText topictext;
    private EditText msgtext;
    //MqttHelper mqttHelper;

    TextView dataReceived;
    EditText data;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recieve);
        dataReceived = (TextView) findViewById(R.id.data);
        button = (Button) findViewById(R.id.button);
        data = (EditText)findViewById(R.id.recieve);
        topictext = (EditText) findViewById(R.id.topic);
        msgtext = (EditText) findViewById(R.id.message);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Globalclass globalVariable = (Globalclass) getApplicationContext();
                String clientid=globalVariable.getClientId();

                MqttHelper mqtth=new MqttHelper(getApplicationContext(),clientid);
                Log.e("main activity", "client id "+clientid);
                String topic;
                String msg;
                topic=topictext.getText().toString();
                msg=msgtext.getText().toString();
                mqtth.publishToTopic(getApplicationContext(),topic,msg,clientid);
                //startMqtt(clientid);
                //dataReceived.setText(mqttMessage.toString());
            }
        });


    }
    public void showtext(String text2){
        Log.i("recieved msg",text2);
        data.setText(text2);
    }

}