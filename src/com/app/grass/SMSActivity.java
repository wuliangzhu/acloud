package com.app.grass;

import java.util.ArrayList;
import java.util.List;

import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.ActionBar.TabListener;
import com.actionbarsherlock.app.SherlockListFragment;
import com.android.common.Contact;
import com.android.common.Global;
import com.android.common.MessageGroup;
import com.android.common.ShortMessage;
import com.android.db.DbTools;

import android.app.ListActivity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class SMSActivity extends /*ListActivity*/ SherlockListFragment implements TabListener {
	ListView mListView;
	
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
			Global.context = this.getActivity().getBaseContext();  
			
			DbTools.createDb(Global.context);
			
			mListView = this.getListView();
			   
			  mListView.setOnItemClickListener(new ListView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> adapterView, View view,
						int position, long id) {
					    Log.d("ERROR", "add blackList start:");
						MyListAdapter2 adapter = (MyListAdapter2)adapterView.getAdapter();
						MessageGroup mg = adapter.contactList.get(position);
						
					    Global.contactMgr.addBlackList(mg.phoneNum);
					    Log.d("ERROR", "add blackList:" + mg.phoneNum);
						//return false;
				}
			   });
		  
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		/**得到手机通讯录联系人信息**/  
		ArrayList<MessageGroup> contactList = Global.smsMgr.getSmsInPhone(Global.context);
  
	   BaseAdapter myAdapter = new MyListAdapter2(contactList, Global.context);  
	   setListAdapter(myAdapter);  
	   Log.d("ERROR", "set click listener");

	  
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onTabReselected(Tab arg0, FragmentTransaction arg1) {
		
	}
	@Override
	public void onTabSelected(Tab arg0, FragmentTransaction arg1) {
		arg1.add(android.R.id.content, this,"android");
		arg1.attach(this);		
	}
	@Override
	public void onTabUnselected(Tab arg0, FragmentTransaction arg1) {
		arg1.detach(this);		
	}  
}
	class MyListAdapter2 extends BaseAdapter {  
		public List<MessageGroup> contactList;
		public Context mContext;
		
	    public MyListAdapter2(ArrayList<MessageGroup> contacts, Context context) {  
	        mContext = context;  
	        this.contactList = contacts;
	    }  
	  
	    public int getCount() {  
	        //设置绘制数量  
	        return contactList.size();  
	    }  
	  
	    @Override  
	    public boolean areAllItemsEnabled() {  
	        return false;  
	    }  
	  
	    public Object getItem(int position) {  
	        return position;  
	    }  
	  
	    public long getItemId(int position) {  
	        return position;  
	    }  
	  
	    public View getView(int position, View convertView, ViewGroup parent) {  
	        ImageView image = null;  
	        TextView title = null;  
	        TextView text = null;  
	        if (convertView == null || position < contactList.size()) {  
		        convertView = LayoutInflater.from(mContext).inflate(  
		            R.layout.sms, null);  
		        image = (ImageView) convertView.findViewById(R.id.smsContactPhoto);  
		        title = (TextView) convertView.findViewById(R.id.smsPhoneNum);  
		        text = (TextView) convertView.findViewById(R.id.smsText);  
	        }  
	        
	        MessageGroup sms = contactList.get(position);
	        image.setImageBitmap(BitmapFactory.decodeResource(
					mContext.getResources(), R.drawable.contact_photo));
	        text.setText(sms.lastMessage);
	        
	        String pn = sms.phoneNum;
	        StringBuffer sb = new StringBuffer();
	        if (pn.startsWith("+86")) {
	        	pn = pn.substring(3);
	        }
	        if (pn.length() == 11) {
	        	sb.append(pn.substring(0, 3));
	        	sb.append('-');
	        	sb.append(pn.substring(3, 7));
	        	sb.append('-');
	        	sb.append(pn.substring(7, 11));
	        }else {
	        	sb.append(pn);
	        }
	        
	        title.setText(sb.toString());
	   
	        return convertView;  
	    }  
}
