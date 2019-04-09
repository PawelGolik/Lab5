package com.example.lab05209319;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private TelephonyManager telMgr;
    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        telMgr = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        setContentView(R.layout.activity_main);
        telMgr.listen(new StrengthOfSignalListener(this), PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
        textView = (TextView) this.findViewById(R.id.serviceID);
        if(!isSmsReadPermissionGranted()) {
            requestReadSmsPermission();
        }
        if(!isSmsRecivePermissionGranted()){
            requestReciveSmsPermission();
        }
        if(!isSmsSendPermissionGranted()){
            requestSendSmsPermission();
        }
        if(!isReceiveMmsPermissionGranted()){
            requestReceiveMmsPermission();
        }
        if(!isReciveWapPushRecivePermissionGranted()){
            requestReciveWapPushPermission();
        }
        if(!isReciveOutgoingCallProcessingPermissionGranted()){
            requestOutgoingCallProcessingPermission();
        }
        if(!isReciveAnsweringCallProcessingPermissionGranted()){
            requestAnweringCallProcessingPermission();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        textView.setText(getTelephonyOverview(telMgr));
    }
    public String getTelephonyOverview(TelephonyManager telMgr) {
        StringBuilder sb = new StringBuilder();
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_PHONE_STATE)) {
                sb.append("Permision not granted");
            }
            else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
            }
        }
        else{
            try {
                sb.append("Software version : " + telMgr.getDeviceSoftwareVersion() + "\n");
                sb.append("Linel number" + telMgr.getLine1Number() + "\n");
                sb.append("Network Country Iso" + telMgr.getNetworkCountryIso() + "\n");
                sb.append("Network operator " + telMgr.getNetworkOperator() + "\n");
                sb.append("Network operator name " + telMgr.getNetworkOperatorName() + "\n");
            } catch (SecurityException e) {
                sb.append(e.getMessage());
            }
        }
        return sb.toString();
    }
    private void requestAnweringCallProcessingPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ANSWER_PHONE_CALLS)) {
        }
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ANSWER_PHONE_CALLS}, 1);
    }
    public boolean isReciveAnsweringCallProcessingPermissionGranted(){
        return (ContextCompat.checkSelfPermission(this, Manifest.permission.ANSWER_PHONE_CALLS) == PackageManager.PERMISSION_GRANTED);
    }
    private void requestOutgoingCallProcessingPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.PROCESS_OUTGOING_CALLS)) {
        }
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.PROCESS_OUTGOING_CALLS}, 1);
    }
    public boolean isReciveOutgoingCallProcessingPermissionGranted(){
        return (ContextCompat.checkSelfPermission(this, Manifest.permission.PROCESS_OUTGOING_CALLS) == PackageManager.PERMISSION_GRANTED);
    }
    public boolean isReciveWapPushRecivePermissionGranted(){
        return (ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED);
    }
    public boolean isReceiveMmsPermissionGranted(){
        return (ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED);
    }

    public boolean isSmsRecivePermissionGranted(){
        return (ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED);
    }
    public boolean isSmsReadPermissionGranted(){
        return (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED);
    }
    public boolean isSmsSendPermissionGranted() {
        return (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED);
    }
    private void requestReciveSmsPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECEIVE_SMS)) {
        }
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECEIVE_SMS}, 1);
    }
    private void requestReceiveMmsPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_SMS)) {
        }
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_SMS}, 2);
    }
    private void requestReciveWapPushPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_SMS)) {
        }
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_SMS}, 2);
    }

    private void requestReadSmsPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_SMS)) {
        }
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_SMS}, 2);
    }
    private void requestSendSmsPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.SEND_SMS)) {
        }
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, 3);
    }
    public void SendMessage(View view) {
        if(isSmsSendPermissionGranted()) {
            String phone = ((EditText) this.findViewById(R.id.phoneID)).getText().toString();
            String message = ((EditText) this.findViewById(R.id.messageID)).getText().toString();
            try{
                SmsManager.getDefault().sendTextMessage(phone, null, message , null, null);
            }
            catch (Exception e) {
                Toast.makeText(this,e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }
        else{
            requestSendSmsPermission();
        }
    }
}