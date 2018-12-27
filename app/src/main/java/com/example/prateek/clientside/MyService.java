package com.example.prateek.clientside;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import helper.MqttHelper;

public class MyService extends Service {
    public MyService() {
    }
    public int onStartCommand(Intent intent, int flags, int startId) {
        Bundle extras = intent.getExtras();
        if(extras == null)
            Log.d("Service","null");
        else {
            final String username = (String) extras.get("username");
            String password = (String) extras.get("password");
            //Log.d("Service username", "username :" + username);

            //public MqttAndroidClient mqttAndroidClient;
            //mqttAndroidClient = new MqttAndroidClient(context, serverUri, MqttClient.generateClientId());

            final MqttHelper mqtth = new MqttHelper(getApplicationContext(),MqttClient.generateClientId());
            mqtth.connect(username,password);

            final Globalclass globalVariable = (Globalclass) getApplicationContext();
            globalVariable.setClientId(mqtth.getClientId());

            mqtth.setCallback(new MqttCallbackExtended() {
                @Override
                public void connectComplete(boolean b, String s) {
                    Log.w("mqtt callback s", "connectedl"+s);
                    final String clientid=mqtth.getClientId();
                    mqtth.subscribeToTopic(username,clientid);
                    globalVariable.connected();
                }

                @Override
                public void connectionLost(Throwable throwable) {
                    Log.w("mqtt callback s", "connection lost"+throwable);
                    globalVariable.disConnected();
                }

                @Override
                public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
                    Log.w("Msg arrived callback s", mqttMessage.toString());
                    MainActivity activity=new MainActivity();
                    activity.showtext(mqttMessage.toString());
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
                    Log.w("mqtt callback s", "delivery complete");
                }
            });

            //final String clientid=mqtth.getClientId();
            //mqtth.subscribeToTopic(username,clientid);
            //Log.e("MyService ", "service client id "+mqtth.getClientId());

//            final Globalclass globalVariable = (Globalclass) getApplicationContext();
//            globalVariable.setClientId(mqtth.getClientId());
            Log.i("MyService ", "service client id "+globalVariable.getClientId());
        }
        return START_NOT_STICKY;

    }
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
