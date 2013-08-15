package com.android.common;

import android.content.Context;
import android.media.AudioManager;

public class Global {
	public static Context context;
	
	public static ContactManager contactMgr = new ContactManager();
	public static SMSManager smsMgr = new SMSManager();
	
	public static AudioManager mAudioManager;
	
	public static void init(Context context){
		mAudioManager = (AudioManager) Global.context.getSystemService(Context.AUDIO_SERVICE);
	}
}
