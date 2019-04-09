package com.example.lab05209319;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.widget.Toast;

public class RecieveSms extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle recived = intent.getExtras();
        if(recived != null){
            Object[] pdus = (Object[]) recived.get("pdus");
            if (pdus.length == 0) {
                return;
            }
            for (Object pdu : pdus) {
                String format = recived.getString("format");
                SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdu, format);
                String number = currentMessage.getOriginatingAddress();
                String message = currentMessage.getDisplayMessageBody();
                if( Integer.parseInt(message) == 10){
                    Toast.makeText(context, "VICTORY", Toast.LENGTH_SHORT).show();

                }
                try {
                    message = Integer.toString(Integer.parseInt(message) + 1);
                    if(ContextCompat.checkSelfPermission(context, Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED && Integer.parseInt(message) <= 10){
                        SmsManager.getDefault().sendTextMessage(number, null, message , null, null);
                        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
                        Thread.sleep(1000);
                    }
                }
                catch (Exception e) {
                    Toast.makeText(context,e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
