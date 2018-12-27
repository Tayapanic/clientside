package com.example.prateek.clientside;

import android.app.Application;
import android.util.Log;

public class Globalclass extends Application {
        private String clientId;
        public boolean isconnected=false;
        public String getClientId() {
            return clientId;
        }

        public void setClientId(String name) {
            clientId = name;
        }
        public void connected() {
            Log.w("global connected", "connected");
        isconnected=true;
    }
    public void disConnected() {
        isconnected=false;
    }
        public boolean isConnected(){
            return isconnected;
        }

    }