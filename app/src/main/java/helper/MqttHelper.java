package helper;

import android.content.Context;
import android.util.Log;

import com.example.prateek.clientside.MainActivity;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.DisconnectedBufferOptions;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

/**
 * Created by wildan on 3/19/2017.
 */

public class MqttHelper {
    public MqttAndroidClient mqttAndroidClient;

    //final String serverUri = "tcp://192.168.0.128:1883";
    final String serverUri = "tcp://3.16.176.162:1883";  //public ipv4 address of the machine
//    final String serverUri = "tcp://ec2-3-16-176-162.us-east-2.compute.amazonaws.com:1883";
    Context context;

   // final String clientId = "ExampleAndroidClient";   //need to resolve this !!!!
   // final String username = "user";
   // final String password = "pwd";

    public MqttHelper(Context context,String clientId){
        //MqttClient.generateClientId()
        Log.w("mqtt helper", clientId);
        mqttAndroidClient = new MqttAndroidClient(context, serverUri, clientId);
        this.context = context;
//        mqttAndroidClient.setCallback(new MqttCallbackExtended() {
//            @Override
//            public void connectComplete(boolean b, String s) {
//                Log.w("mqtt callback", "connectedl"+s);
//            }
//            @Override
//            public void connectionLost(Throwable throwable) {
//                Log.w("mqtt callback", "connection lost"+throwable);
//            }
//            @Override
//            public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
//                Log.w("Msg arrived callback", mqttMessage.toString());
//                MainActivity activity=new MainActivity();
//                activity.showtext(mqttMessage.toString());
//            }
//            @Override
//            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
//                Log.w("mqtt callback", "delivery complete");
//            }
//        });
    }
    public String getClientId(){
        return mqttAndroidClient.getClientId();
    }

    public void setCallback(MqttCallbackExtended callback) {
        mqttAndroidClient.setCallback(callback);
    }

    public void connect(final String username, String password){   //connect and subscribe to topic-username
        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setAutomaticReconnect(true);
        mqttConnectOptions.setCleanSession(true);
        mqttConnectOptions.setUserName(username);
        mqttConnectOptions.setPassword(password.toCharArray());
        Log.w("Connect:user+password", username+password);

        try {

            mqttAndroidClient.connect(mqttConnectOptions, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {

                    DisconnectedBufferOptions disconnectedBufferOptions = new DisconnectedBufferOptions();
                    disconnectedBufferOptions.setBufferEnabled(true);
                    disconnectedBufferOptions.setBufferSize(100);
                    disconnectedBufferOptions.setPersistBuffer(true);
                    disconnectedBufferOptions.setDeleteOldestMessages(true);
                    mqttAndroidClient.setBufferOpts(disconnectedBufferOptions);
                       /* try {
                            mqttAndroidClient.subscribe(username, 2, context, new IMqttActionListener() {
                                @Override
                                public void onSuccess(IMqttToken iMqttToken) {
                                    Log.w("Mqtt","Subscribed! ");
                                }

                                @Override
                                public void onFailure(IMqttToken iMqttToken, Throwable throwable) {
                                    Log.w("Mqtt", "Subscribed fail!");
                                }
                            });
                        } catch (MqttException e) {
                            e.printStackTrace();
                        }   */


                    Log.w("Mqtt", "succesfully connected to: " + serverUri );
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.w("Mqtt", "Failed to connect to: " + serverUri + exception.toString());
                }
            });


        } catch (MqttException ex){
            ex.printStackTrace();
        }
    }
    public void subscribeToTopic(final String topic,final String clientid) {

        MemoryPersistence memPer = new MemoryPersistence();
        final MqttAndroidClient client = new MqttAndroidClient(
                context, serverUri,clientid , memPer);
        try {
            client.connect(context, new IMqttActionListener() {

                @Override
                public void onSuccess(IMqttToken mqttToken) {
                    Log.i("mqtt", "Client connected sub"+client.getClientId());

                    try {
                        client.subscribe(topic, 1, context, new IMqttActionListener() {
                            @Override
                            public void onSuccess(IMqttToken iMqttToken) {
                                Log.w("Mqtt","Subscribed! on subscribe function");
                            }

                            @Override
                            public void onFailure(IMqttToken iMqttToken, Throwable throwable) {
                                Log.w("Mqtt", "Subscribed fail!");
                            }
                        });

                    } catch (MqttPersistenceException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();

                    } catch (MqttException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(IMqttToken arg0, Throwable arg1) {
                    // TODO Auto-generated method stub
                    Log.w("mqtt ", "lient connection failed: while subscribing"+arg1.getMessage());

                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void publishToTopic(Context context,final String topic, final String msg,final String clientid) {

        MemoryPersistence memPer = new MemoryPersistence();
        //Log.i("main ", "cons "+topic+msg);
        final MqttAndroidClient client = new MqttAndroidClient(
                context, serverUri, clientid, memPer);
        Log.i("mqtt","is client connected when we are trying to publish"+client.isConnected());
        if(client.isConnected()){
            Log.i("mqtt", "While publishing, client is already connected");  //we want this
            MqttMessage message = new MqttMessage(msg.getBytes());
            message.setQos(1);
            message.setRetained(false);

            try {
                client.publish(topic, message);
                Log.i("mqtt", "Message published" + message + topic);

            } catch (MqttPersistenceException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();

            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
        else {
            try {
                client.connect(context, new IMqttActionListener() {
                    @Override
                    public void onSuccess(IMqttToken mqttToken) {
                        // Log.i("mqtt", "Client connected "+client.getClientId());

                        MqttMessage message = new MqttMessage(msg.getBytes());
                        message.setQos(1);
                        message.setRetained(true);

                        try {
                            client.publish(topic, message);
                            Log.i("mqtt", "Message published" + message + topic);

                        } catch (MqttPersistenceException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();

                        } catch (MqttException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(IMqttToken arg0, Throwable arg1) {
                        // TODO Auto-generated method stub
                        Log.w("mqtt ", "client connection failed: while publishing " + client.getClientId()+arg1.getMessage());

                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

}