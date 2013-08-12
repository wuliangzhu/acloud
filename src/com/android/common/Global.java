package com.android.common;

import android.content.Context;

public class Global {
	public static Context context;
	
	public static ContactManager contactMgr = new ContactManager();
	public static SMSManager smsMgr = new SMSManager();
}
