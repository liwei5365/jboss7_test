package com.asb.ponnbi.t1;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

public class MessageConsumer implements MessageListener {

	public void onMessage(Message message) {
		System.out.println("MessageConsumer onMessage : " + message);
		try {
			TextMessage text = (TextMessage) message;
			String strMessage;
			strMessage = text.getText();
			System.out.println("Message received: " + strMessage);
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}
}