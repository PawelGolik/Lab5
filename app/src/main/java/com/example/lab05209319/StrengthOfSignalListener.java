package com.example.lab05209319;

import android.content.Context;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.util.Log;
import android.widget.Toast;

public class StrengthOfSignalListener extends PhoneStateListener {
    Context context;
    public StrengthOfSignalListener( Context ctx){
        context = ctx;
    }
    @Override
    public void onSignalStrengthsChanged(SignalStrength signalStrength) {
        super.onSignalStrengthsChanged(signalStrength);
        Log.d("APP", "Signal :" + signalStrength.toString());
        Toast.makeText(context,"Signal changed : " + signalStrength.getLevel(),Toast.LENGTH_SHORT).show();
    }
}
