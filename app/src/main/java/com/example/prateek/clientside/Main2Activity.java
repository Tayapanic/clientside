package com.example.prateek.clientside;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import helper.MqttHelper;

public class Main2Activity extends AppCompatActivity {
    private Button button;
    private EditText topictext;
    private EditText passwordtext;
    MqttHelper mqttHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        button = (Button) findViewById(R.id.signupbutton);
        topictext = (EditText) findViewById(R.id.topic);
        passwordtext=(EditText) findViewById(R.id.password);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Log.e("mainanjasaj ", "cons "+topictext.getText().toString()+passwordtext.getText().toString());
                Intent serviceIntent = new Intent(Main2Activity.this,
                        MyService.class);
                serviceIntent.putExtra("username", topictext.getText().toString());
                serviceIntent.putExtra("password", passwordtext.getText().toString());
                startService(serviceIntent);

//                final Globalclass globalVariable = (Globalclass) getApplicationContext();
//                if(globalVariable.isconnected){
//                String clientid=globalVariable.getClientId();
//                MqttHelper mqtth=new MqttHelper(getApplicationContext(),clientid);
//                mqtth.subscribeToTopic(topictext.getText().toString(),clientid);}

                //mqtth.connect(topictext.getText().toString(),passwordtext.getText().toString());   //only if login then subscribe
                //mqtth.subscribeToTopic(topictext.getText().toString());
                Intent myIntent = new Intent(Main2Activity.this,
                        MainActivity.class);
                startActivity(myIntent);
            }
        });
    }

}
