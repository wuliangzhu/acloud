package com.android.common;

import java.io.InputStream;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts.Photo;
import android.text.TextUtils;

import com.android.db.DbTools;
import com.app.grass.R;

/**
 * 
 * @author wuliangzhu
 * 
 */
public class ContactManager {
	/** 获取库Phon表字段 **/
	private static final String[] PHONES_PROJECTION = new String[] {
			Phone.DISPLAY_NAME, Phone.NUMBER, Photo.PHOTO_ID, Phone.CONTACT_ID };

	/** 联系人显示名称 **/
	private static final int PHONES_DISPLAY_NAME_INDEX = 0;

	/** 电话号码 **/
	private static final int PHONES_NUMBER_INDEX = 1;

	/** 头像ID **/
	private static final int PHONES_PHOTO_ID_INDEX = 2;

	/** 联系人的ID **/
	private static final int PHONES_CONTACT_ID_INDEX = 3;

	
	/** 得到手机通讯录联系人信息 **/
	public ArrayList<Contact> getPhoneContacts(Context mContext) {
		ArrayList<Contact> contactList = new ArrayList<Contact>();
		
		ContentResolver resolver = mContext.getContentResolver();

		// 获取手机联系人
		Cursor phoneCursor = resolver.query(Phone.CONTENT_URI,
				PHONES_PROJECTION, null, null, null);

		if (phoneCursor != null) {
			while (phoneCursor.moveToNext()) {

				// 得到手机号码
				String phoneNumber = phoneCursor.getString(PHONES_NUMBER_INDEX);
				// 当手机号码为空的或者为空字段 跳过当前循环
				if (TextUtils.isEmpty(phoneNumber))
					continue;

				// 得到联系人名称
				String contactName = phoneCursor
						.getString(PHONES_DISPLAY_NAME_INDEX);

				// 得到联系人ID
				Long contactid = phoneCursor.getLong(PHONES_CONTACT_ID_INDEX);

				// 得到联系人头像ID
				Long photoid = phoneCursor.getLong(PHONES_PHOTO_ID_INDEX);

				// 得到联系人头像Bitamp
				Bitmap contactPhoto = null;

				// photoid 大于0 表示联系人有头像 如果没有给此人设置头像则给他一个默认的
				if (photoid > 0) {
					Uri uri = ContentUris.withAppendedId(
							ContactsContract.Contacts.CONTENT_URI, contactid);
					InputStream input = ContactsContract.Contacts
							.openContactPhotoInputStream(resolver, uri);
					contactPhoto = BitmapFactory.decodeStream(input);
				} else {
					contactPhoto = BitmapFactory.decodeResource(
							mContext.getResources(), R.drawable.contact_photo);
				}
				
				Contact contact = new Contact();
				contact.contactId = contactid.toString();
				contact.displayName = contactName;
				contact.phoneNum = phoneNumber;
				contact.photo = contactPhoto;
				
				contactList.add(contact);
			}

			phoneCursor.close();
 	}
		
		return contactList;
	}

	/** 得到手机SIM卡联系人人信息 **/
	public ArrayList<Contact> getSIMContacts(Context mContext) {
		ArrayList<Contact> contactList = new ArrayList<Contact>();
		ContentResolver resolver = mContext.getContentResolver();
		// 获取Sims卡联系人
		Uri uri = Uri.parse("content://icc/adn");
		Cursor phoneCursor = resolver.query(uri, PHONES_PROJECTION, null, null,
				null);

		if (phoneCursor != null) {
			while (phoneCursor.moveToNext()) {

				// 得到手机号码
				String phoneNumber = phoneCursor.getString(PHONES_NUMBER_INDEX);
				// 当手机号码为空的或者为空字段 跳过当前循环
				if (TextUtils.isEmpty(phoneNumber))
					continue;
				// 得到联系人名称
				String contactName = phoneCursor
						.getString(PHONES_DISPLAY_NAME_INDEX);

				// Sim卡中没有联系人头像

				Contact contact = new Contact();
				contact.displayName = contactName;
				contact.phoneNum = phoneNumber;
				
				contactList.add(contact);
			}

			phoneCursor.close();
		}
		
		return contactList;
	}
	
	
	public ArrayList<CallRecord> getCallLog(Context mContext){
		ArrayList<CallRecord> records = new ArrayList<CallRecord>();
		ContentResolver cr = mContext.getContentResolver();
		final Cursor cursor = cr.query(CallLog.Calls.CONTENT_URI, new String[]{CallLog.Calls.NUMBER,CallLog.Calls.CACHED_NAME,CallLog.Calls.TYPE, CallLog.Calls.DATE}, null, null,CallLog.Calls.DEFAULT_SORT_ORDER);
        for (int i = 0; i < cursor.getCount(); i++) {  
        	CallRecord cRecord = new CallRecord();
            cursor.moveToPosition(i);
            cRecord.phoneNum = cursor.getString(0);
            cRecord.displayName = cursor.getString(1);
            cRecord.callType = cursor.getInt(2);
            SimpleDateFormat sfd = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            cRecord.callTime = new Date(Long.parseLong(cursor.getString(3)));
            
            records.add(cRecord);
           }
        
        return records;
	}
	
	/**
	 * 加入黑名单，黑名单的电话和短信都会自动拦截
	 */
	public void addBlackList(String phoneNum){
		if (this.isBlack(phoneNum)){
			return;
		}
		
		ContentValues cv = new ContentValues();
		cv.put("phoneNum", phoneNum);
		
		DbTools.insert("BlackList", cv);
	}
	
	public void delBlackList(String phoneNum){
		DbTools.delete("BlackList", "phoneNum = ?", new String[]{phoneNum});
	}
	
	public ArrayList<String> getBlackList() {
		return DbTools.selectAllBlackList();
	}
	
	public boolean isBlack(String phoneNum){
		String pn = DbTools.selectBlackList(phoneNum);
		
		return pn != null;
	}
}
