package com.example.bookstoreapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.widget.Toast;

public class SMSReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        SmsMessage[] messages = Telephony.Sms.Intents.getMessagesFromIntent(intent);
        for (int i = 0; i < messages.length; i ++) {
            SmsMessage currentMessage = messages[i];
            String message = currentMessage.getMessageBody();
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

            Intent myIntent = new Intent();
            myIntent.setAction("MySMS");
            myIntent.putExtra("KEY1", message);
            context.sendBroadcast(myIntent);
        }

    }
}
