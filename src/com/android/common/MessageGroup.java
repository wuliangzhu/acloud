package com.android.common;

import java.util.ArrayList;
import java.util.Comparator;

public class MessageGroup implements Comparator<MessageGroup>{
	public String phoneNum;
	public String lastMessage;
	
	public ArrayList<ShortMessage> messages = new ArrayList<ShortMessage>();

	@Override
	public boolean equals(Object o) {
		MessageGroup mg = MessageGroup.class.cast(o);
		
		return this.phoneNum.equals(mg.phoneNum);
	}

	@Override
	public int compare(MessageGroup lhs, MessageGroup rhs) {
		MessageGroup l = (MessageGroup)lhs;
		MessageGroup r = (MessageGroup)rhs;
		
		return l.phoneNum.compareTo(r.phoneNum);
	}

}
