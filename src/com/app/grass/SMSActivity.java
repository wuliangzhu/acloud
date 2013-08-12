package com.app.grass;

import java.util.ArrayList;
import java.util.List;

import com.android.common.Contact;
import com.android.common.Global;
import com.android.common.ShortMessage;

import android.app.ListActivity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class SMSActivity extends ListActivity {
	ListView mListView;
	@Override  
    public void onCreate(Bundle savedInstanceState) {  
		Global.context = this;  
		mListView = this.getListView();  
	    /**得到手机通讯录联系人信息**/  
		ArrayList<ShortMessage> contactList = Global.smsMgr.getSmsInPhone(this);
  
	   BaseAdapter myAdapter = new MyListAdapter2(contactList, this);  
	   setListAdapter(myAdapter);  
  
  
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
}
	class MyListAdapter2 extends BaseAdapter {  
		public List<ShortMessage> contactList;
		public Context mContext;
		
	    public MyListAdapter2(ArrayList<ShortMessage> contacts, Context context) {  
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
	        
	        ShortMessage sms = contactList.get(position);
	        image.setImageBitmap(BitmapFactory.decodeResource(
					mContext.getResources(), R.drawable.contact_photo));
	        text.setText(sms.body);
	        
	        String pn = sms.address;
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
