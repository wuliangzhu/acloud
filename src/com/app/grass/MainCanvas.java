package com.app.grass;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockListFragment;
import com.actionbarsherlock.app.ActionBar.TabListener;
import com.android.common.Contact;
import com.android.common.Global;

public class MainCanvas extends /*ListActivity*/ SherlockListFragment implements TabListener {
	//ListView mListView;
	@Override  
    public void onCreate(Bundle savedInstanceState) {  
		//Global.context = this.getActivity().getBaseContext();  
		//mListView = this.getListView();  
	    /**得到手机通讯录联系人信息**/  
		//ArrayList<Contact> contactList = Global.contactMgr.getPhoneContacts(Global.context);
  
	  // BaseAdapter myAdapter = new MyListAdapter(contactList, Global.context);  
	   //setListAdapter(myAdapter);  
  
	   
	   /* mListView.setOnItemClickListener(new OnItemClickListener() {  
	  
	        @Override  
	        public void onItemClick(AdapterView<?> adapterView, View view,  
	            int position, long id) {  
	        //调用系统方法拨打电话  
	        Intent dialIntent = new Intent(Intent.ACTION_CALL, Uri  
	            .parse("tel:" + mContactsNumber.get(position)));  
	        startActivity(dialIntent);  
	        }  
	    });  */
  
	    super.onCreate(savedInstanceState);  
    }
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        /** Creating array adapter to set data in listview */
		Global.context = this.getActivity().getBaseContext();  
		//mListView = this.getListView();  
	    /**得到手机通讯录联系人信息**/  
		ArrayList<Contact> contactList = Global.contactMgr.getPhoneContacts(Global.context);
  
	   BaseAdapter myAdapter = new MyListAdapter(contactList, Global.context);  
	   setListAdapter(myAdapter);  
 
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
	class MyListAdapter extends BaseAdapter {  
		public List<Contact> contactList;
		public Context mContext;
		
	    public MyListAdapter(ArrayList<Contact> contacts, Context context) {  
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
		            R.layout.contact, null);  
		        image = (ImageView) convertView.findViewById(R.id.contactPhoto);  
		        title = (TextView) convertView.findViewById(R.id.displayName);  
		        text = (TextView) convertView.findViewById(R.id.phoneNum);  
	        }  
	        
	        Contact contact = contactList.get(position);
	        image.setImageBitmap(contact.photo);
	        title.setText(contact.displayName);
	        
	        String pn = contact.phoneNum;
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
	        
	        text.setText(sb.toString());
	   
	        return convertView;  
	    }  
}
