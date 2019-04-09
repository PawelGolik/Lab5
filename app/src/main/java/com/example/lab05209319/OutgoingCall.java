package com.example.lab05209319;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;
import android.telecom.TelecomManager;
import android.util.Log;
import android.widget.Toast;
public class OutgoingCall extends BroadcastReceiver {

    public static String ABORT_THIS_CALL = "5554";
    public static boolean StartBlocking = true;
    @Override
    public void onReceive(final Context context, final Intent intent) {
        if(StartBlocking){
            return;
        }
        Log.d("APP", "ACTION:" + intent.getAction());
        if (Intent.ACTION_NEW_OUTGOING_CALL.equals(intent.getAction())) {
            String originalNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
            Log.d("APP", "outgoing,ringing:" + originalNumber);
            if (originalNumber.equals(ABORT_THIS_CALL)) {
                TelecomManager tm = (TelecomManager) context.getSystemService(Context.TELECOM_SERVICE);
                if(tm != null){
                    if(ContextCompat.checkSelfPermission(context, Manifest.permission.ANSWER_PHONE_CALLS) == PackageManager.PERMISSION_GRANTED) {
                        boolean sucess = tm.endCall();
                        Toast.makeText(context, ABORT_THIS_CALL + " - aborting call " +  sucess, Toast.LENGTH_LONG).show();
                    }
                }
            }
        }
    }
}
