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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    private TelephonyManager telMgr;
    private TextView textView;
    private TextView number;
    private EditText textViewNrToBlock;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        telMgr = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        setContentView(R.layout.activity_main);
        telMgr.listen(new StrengthOfSignalListener(this), PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
        textView = (TextView) this.findViewById(R.id.serviceID);
        number = (TextView) this.findViewById(R.id.blockedNr);
        textViewNrToBlock = (EditText) this.findViewById(R.id.numberToBlock);
        number.setText("Actualy blocked nr: "+OutgoingCall.ABORT_THIS_CALL);
        ((Button)this.findViewById(R.id.startBlocking)).setText(!OutgoingCall.StartBlocking ? "Stop Blocking Number" :"Start Blocking Number");
        ((Button)this.findViewById(R.id.startListenignSignalStrength)).setText(StrengthOfSignalListener.StartListening ? "Start Listenign Signal Strenght":"Stop Listenign Signal Strenght" );
        textViewNrToBlock.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {}
            @Override
            public void beforeTextChanged(CharSequence s, int start,int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() != 0) {
                    number.setText("Actualy blocked nr: " + s);
                    OutgoingCall.ABORT_THIS_CALL = s.toString();
                }else{
                    number.setText("Actualy blocked nr: 5554");
                    OutgoingCall.ABORT_THIS_CALL = "5554";
                }
            }
        });
    }

    private StringBuilder telephonyOverview(TelephonyManager telMgr){
        StringBuilder sb = new StringBuilder();
        try {
            sb.append("Software version : " + telMgr.getDeviceSoftwareVersion() + "\n");
            sb.append("Linel number" + telMgr.getLine1Number() + "\n");
            sb.append("Network Country Iso" + telMgr.getNetworkCountryIso() + "\n");
            sb.append("Network operator " + telMgr.getNetworkOperator() + "\n");
            sb.append("Network operator name " + telMgr.getNetworkOperatorName() + "\n");
        } catch (SecurityException e) {
            sb.append(e.getMessage());
        }
        return sb;
    }

    public String getTelephonyOverview(TelephonyManager telMgr) {
        StringBuilder sb = new StringBuilder();
        if(!isReciveGranted(Manifest.permission.READ_PHONE_STATE)) {
            requestPermission(new String[]{Manifest.permission.READ_PHONE_STATE},1);
            sb.append("Permision not granted");
        } else {
            sb = telephonyOverview(telMgr);
        }

        return sb.toString();
    }
    public void requestPermission(String[] inputRequests, int RequestCode) {
        ArrayList<String> output = new ArrayList<String>();
        for(String request : inputRequests) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this, request)) {
                output.add(request);
            }
        }
        String[] str = new String[output.size()];
        for (int i = 0; i < output.size() ; i++){
            str[i] = output.get(i);
        }
        ActivityCompat.requestPermissions(this,str, RequestCode);

    }
    public boolean isReciveGranted(String request){
        return (ContextCompat.checkSelfPermission(this,request) == PackageManager.PERMISSION_GRANTED);
    }
    public void SendMessage(View view) {
        if(isReciveGranted(Manifest.permission.SEND_SMS) && isReciveGranted(Manifest.permission.RECEIVE_SMS)) {
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
            requestPermission(new String[]{Manifest.permission.SEND_SMS,Manifest.permission.RECEIVE_SMS},2);
        }
    }

    public void GetInfo(View view) {
        textView.setText(getTelephonyOverview(telMgr));
    }

    public void StartBlocking(View view) {
        StringBuilder SystemNotifi = new StringBuilder();
        if(isReciveGranted(Manifest.permission.PROCESS_OUTGOING_CALLS) && isReciveGranted(Manifest.permission.ANSWER_PHONE_CALLS)) {
            OutgoingCall.StartBlocking = !OutgoingCall.StartBlocking;
            ((Button)view).setText(OutgoingCall.StartBlocking ? "Start Blocking Number":"Stop Blocking Number");
            return;
        }
        else {
            requestPermission(new String[]{Manifest.permission.ANSWER_PHONE_CALLS,Manifest.permission.PROCESS_OUTGOING_CALLS},3);
            SystemNotifi.append("Missing permision PROCESS_OUTGOING_CALLS \n");
            SystemNotifi.append("Missing permision ANSWER_PHONE_CALLS \n");
        }
        Toast.makeText(this,SystemNotifi.toString(),Toast.LENGTH_LONG).show();
    }

    public void StartLisSignalStr(View view) {
        StringBuilder SystemNotifi = new StringBuilder();
        if(isReciveGranted(Manifest.permission.READ_PHONE_STATE)) {
            StrengthOfSignalListener.StartListening = !StrengthOfSignalListener.StartListening;
            ((Button)view).setText(StrengthOfSignalListener.StartListening ? "Start Listenign Signal Strenght":"Stop Listenign Signal Strenght" );
        }
        else{
            requestPermission(new String[]{Manifest.permission.READ_PHONE_STATE},5);
            SystemNotifi.append("Missing permision READ_PHONE_STATE \n");
            Toast.makeText(this,SystemNotifi,Toast.LENGTH_LONG);
        }
    }
}