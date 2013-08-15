package com.android.common;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.android.internal.telephony.ITelephony;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.util.Log;

public class SmsReceiver extends BroadcastReceiver {
    public SmsReceiver() {
    	
    }

    @Override
    public void onReceive(Context context, Intent intent) {
    	String act = intent.getAction();
    	 Log.i("ERROR", "receive message:" + act);
    	 if ("android.intent.action.PHONE_STATE".equals(act)) {
    		 String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);   
             String number = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);  
             TelephonyManager tm = (TelephonyManager)context.getSystemService(Service.TELEPHONY_SERVICE);
             
             if (Global.contactMgr.isBlack(number)) {
            	 endCall(tm); 
            	 return;
             }
    	}
    	 
       Object[] pdus = (Object[]) intent.getExtras().get("pdus");
       if (pdus != null && pdus.length > 0) {
           SmsMessage[] messages = new SmsMessage[pdus.length];
           for (int i = 0; i < pdus.length; i++) {
               byte[] pdu = (byte[]) pdus[i];
               messages[i] = SmsMessage.createFromPdu(pdu);
           }
           for (SmsMessage message : messages) {
               String content = message.getMessageBody();// 得到短信内容
                String sender = message.getOriginatingAddress();// 得到发信息的号码
                Log.i("ERROR", "from message:" + sender);
               if (Global.contactMgr.isBlack(sender)) {
                   abortBroadcast();// 中止发送
                   break;
               }
               
               Date date = new Date(message.getTimestampMillis());
               SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
               String sendContent = format.format(date) + ":" + sender + "--"
                       + content;
              // SmsManager smsManager = SmsManager.getDefault();// 发信息时需要的
              //  smsManager.sendTextMessage("", null, sendContent, null,
               //        null);// 转发给
           }
       }
   }
    
    /** 
     * 挂断电话 
     */  
    private void endCall(TelephonyManager telMgr) {  
        Class<TelephonyManager> c = TelephonyManager.class;           
        try  
        {  Global.mAudioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
            Method getITelephonyMethod = c.getDeclaredMethod("getITelephony", (Class[]) null);  
            getITelephonyMethod.setAccessible(true);  
            ITelephony iTelephony = null;  
            iTelephony = (ITelephony) getITelephonyMethod.invoke(telMgr, (Object[]) null);  
            iTelephony.endCall();  
            Global.mAudioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        }  
        catch (Exception e)  
        {  
            Log.e("ERROR", "Fail to answer ring call.", e);  
        }          
    }  
}

