package com.app.grass;

import java.util.ArrayList;
import java.util.List;

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

import com.actionbarsherlock.app.SherlockListFragment;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.ActionBar.TabListener;
import com.android.common.Global;
import com.android.common.MessageGroup;
import com.android.db.DbTools;

public class BlackList extends /*ListActivity*/ SherlockListFragment implements TabListener {
	ListView mListView;
	
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		mListView = this.getListView();
			   
		mListView.setOnItemClickListener(new ListView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> adapterView, View view,
						int position, long id) {
					    Log.d("ERROR", "del blackList start:");
						MyListAdapter3 adapter = (MyListAdapter3)adapterView.getAdapter();
						String mg = adapter.contactList.get(position);
						
					    Global.contactMgr.delBlackList(mg);
					    Log.d("ERROR", "del blackList:" + mg);
						//return false;
				}
			   });
		  
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Global.context = this.getActivity().getBaseContext();  
		
		DbTools.createDb(Global.context);
		
		/**得到手机通讯录联系人信息**/  
		ArrayList<String> blackList = Global.contactMgr.getBlackList();
  
	   BaseAdapter myAdapter = new MyListAdapter3(blackList, Global.context);  
	   setListAdapter(myAdapter);  
	   Log.d("ERROR", "set click listener");

	  
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onTabReselected(Tab arg0, FragmentTransaction arg1) {
		
	}
	@Override
	public void onTabSelected(Tab arg0, FragmentTransaction arg1) {
		arg1.add(android.R.id.content, this,"android2");
		arg1.attach(this);		
	}
	@Override
	public void onTabUnselected(Tab arg0, FragmentTransaction arg1) {
		arg1.detach(this);		
	}  
}
	class MyListAdapter3 extends BaseAdapter {  
		public List<String> contactList;
		public Context mContext;
		
	    public MyListAdapter3(ArrayList<String> contacts, Context context) {  
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
		            R.layout.blacklist, null);  
		      //  image = (ImageView) convertView.findViewById(R.id.smsContactPhoto);  
		        title = (TextView) convertView.findViewById(R.id.blacklistPhone);  
		        text = (TextView) convertView.findViewById(R.id.blacklistState);  
	        }  
	        
	        String sms = contactList.get(position);
	      //  image.setImageBitmap(BitmapFactory.decodeResource(
				//	mContext.getResources(), R.drawable.contact_photo));
	        title.setText(sms);
	        text.setText("blocked");
	        
	        String pn = sms;
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

