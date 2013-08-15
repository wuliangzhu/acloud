package com.android.common;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.media.AudioManager;
import android.net.Uri;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

public class SMSManager {
	 private final static String BLOCKED_NUMBER = "15555215556";  
	 String SENT_SMS_ACTION = "SENT_SMS_ACTION";  
	 String DELIVERED_SMS_ACTION = "DELIVERED_SMS_ACTION";  
	 private IncomingCallReceiver mIncomingCallReceiver;  
	   // private ITelephony mITelephony;  
	    private AudioManager mAudioManager; 
	    
	 private BroadcastReceiver sendMessage = new BroadcastReceiver() {  
		  
		    @Override  
		    public void onReceive(Context context, Intent intent) {  
		        //判断短信是否发送成功  
		        switch (getResultCode()) {  
		        case Activity.RESULT_OK:  
		        Toast.makeText(context, "短信发送成功", Toast.LENGTH_SHORT).show();  
		        break;  
		        default:  
		        Toast.makeText(Global.context, "发送失败", Toast.LENGTH_LONG).show();  
		        break;  
		        }  
		    }  
		    };  
		      
		     
		    private BroadcastReceiver receiver = new BroadcastReceiver() {  
		  
		    @Override  
		    public void onReceive(Context context, Intent intent) {  
		        //表示对方成功收到短信  
		        Toast.makeText(Global.context, "对方接收成功",Toast.LENGTH_LONG).show();  
		    }  
		    };  
		    
		    private class IncomingCallReceiver extends BroadcastReceiver{  
		          
		        @Override  
		        public void onReceive(Context context, Intent intent) {  
		            String action = intent.getAction();  
		            Log.d("mayingcai", "Action:" + action);  
		              
		            if("android.intent.action.PHONE_STATE".equals(action)){//拦截电话  
		                String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);  
		                Log.i("mayingcai", "State: "+ state);  
		                  
		                String number = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);  
		                Log.d("mayingcai", "Incomng Number: " + number);  
		                  
		                if(state.equalsIgnoreCase(TelephonyManager.EXTRA_STATE_RINGING)){//电话正在响铃  
		                    if(number.equals(BLOCKED_NUMBER)){//拦截指定的电话号码  
		                        //先静音处理  
		                        mAudioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);  
		                        Log.d("mayingcai", "Turn Ringtone Silent");  
		                          
		                        try {  
		                            //挂断电话  
		                        //   mITelephony.endCall();  
		                        } catch (Exception e) {  
		                            e.printStackTrace();  
		                        }  
		                          
		                        //再恢复正常铃声  
		                        mAudioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);  
		                    }  
		                }  
		            }else if("android.provider.Telephony.SMS_RECEIVED".equals(action)){//拦截短信  
		                SmsMessage sms = null;//getMessagesFromIntent(intent)[0];  
		                String number = sms.getOriginatingAddress();  
		                Log.d("mayingcai", "Incomng Number: " + number);  
		              //  number = trimSmsNumber("+86", number);//把国家代码去除掉  
		                if(number.equals(BLOCKED_NUMBER)){  
		                    abortBroadcast();//这句很重要，中断广播后，其他要接收短信的应用都没法收到短信广播了  
		                }  
		            }  
		        }  
		    }  
		public void init(){
			 // 注册广播 发送消息  
		    Global.context.registerReceiver(sendMessage, new IntentFilter(SENT_SMS_ACTION));  
		    Global.context.registerReceiver(receiver, new IntentFilter(DELIVERED_SMS_ACTION)); 
		    
		    mAudioManager = (AudioManager) Global.context.getSystemService(Context.AUDIO_SERVICE);  
	        //利用反射获取隐藏的endcall方法  
	        TelephonyManager mTelephonyManager = (TelephonyManager) Global.context.getSystemService(Context.TELEPHONY_SERVICE);  
		}
		
		public void send(String phoneNum, String text){
			SmsManager sm = SmsManager.getDefault();
			// create the sentIntent parameter  
			Intent sentIntent = new Intent(SENT_SMS_ACTION);  
			PendingIntent sentPI = PendingIntent.getBroadcast(Global.context, 0, sentIntent,  
			    0);  
			  
			// create the deilverIntent parameter  
			Intent deliverIntent = new Intent(DELIVERED_SMS_ACTION);  
			PendingIntent deliverPI = PendingIntent.getBroadcast(Global.context, 0, deliverIntent, 0);  
			
			List<String> msgs = sm.divideMessage(text);
			for (String msg : msgs) {
				sm.sendTextMessage(phoneNum, null, msg, sentPI, deliverPI);
			}
		}
		
		public ArrayList<MessageGroup> getSmsInPhone(Context context) {  
	        final String SMS_URI_ALL = "content://sms/";  
	        final String SMS_URI_INBOX = "content://sms/inbox";  
	        final String SMS_URI_SEND = "content://sms/sent";  
	        final String SMS_URI_DRAFT = "content://sms/draft";  
	        final String SMS_URI_OUTBOX = "content://sms/outbox";  
	        final String SMS_URI_FAILED = "content://sms/failed";  
	        final String SMS_URI_QUEUED = "content://sms/queued";  
	  
	        ArrayList<MessageGroup> messages = new ArrayList<MessageGroup>();  
	        Map<String, MessageGroup> msgMap = new HashMap<String, MessageGroup>();
	  
	        try {  
	            Uri uri = Uri.parse(SMS_URI_ALL);  
	            String[] projection = new String[] { "_id", "thread_id", "read", "status", "protocol ", "address", "person", "body", "date", "type", "service_center" };  
	            Cursor cur = context.getContentResolver().query(uri, projection, null, null, "date desc");      // 获取手机内部短信  
	  
	            if (cur.moveToFirst()) {  
	                int index_Address = cur.getColumnIndex("address");  
	                int index_Person = cur.getColumnIndex("person");  
	                int index_Body = cur.getColumnIndex("body");  
	                int index_Date = cur.getColumnIndex("date");  
	                int index_Type = cur.getColumnIndex("type"); 
	                
	                int index_read = cur.getColumnIndex("read");
	                int index_id = cur.getColumnIndex("_id");
	                int index_threadId = cur.getColumnIndex("thread_id");
	                int index_protocol = cur.getColumnIndex("protocol");
	                int index_sc = cur.getColumnIndex("service_center");
	                int index_status = cur.getColumnIndex("status");
	  
	                do {  
	                    String strAddress = cur.getString(index_Address);  
	                    int intPerson = cur.getInt(index_Person);  
	                    String strbody = cur.getString(index_Body);  
	                    long longDate = cur.getLong(index_Date);  
	                    int intType = cur.getInt(index_Type);  
	  
	                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");  
	                    Date d = new Date(longDate);  
	                    String strDate = dateFormat.format(d);  
	  
	                    String strType = "";  
	                    if (intType == 1) {  
	                        strType = "接收";  
	                    } else if (intType == 2) {  
	                        strType = "发送";  
	                    } else {  
	                        strType = "null";  
	                    }  
	  
	                    ShortMessage sm = new ShortMessage();
	                    sm.address = strAddress;
	                    sm.body = strbody;
	                    sm.date = new Date(longDate);
	                    sm.type = intType;
	                    
	                    MessageGroup mg = msgMap.get(strAddress);
	                    if (mg == null) {
	                    	mg = new MessageGroup();
	                    	mg.phoneNum = strAddress;
	                    	
	                    	msgMap.put(strAddress, mg);
	                    }
	                    mg.lastMessage = strbody;
	                    mg.messages.add(sm);	                    
	                } while (cur.moveToNext());  
	  
	                if (!cur.isClosed()) {  
	                    cur.close();  
	                    cur = null;  
	                }  
	            } else {  

	            } // end if  
	  
	        } catch (SQLiteException ex) {  
	            Log.d("SQLiteException in getSmsInPhone", ex.getMessage());  
	        }  
	        messages.addAll(msgMap.values());
	        
	        return messages;  
	    }  
}  
