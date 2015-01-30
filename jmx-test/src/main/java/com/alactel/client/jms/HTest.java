package com.alactel.client.jms;

import static java.lang.System.out;

import java.util.HashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;

import org.hornetq.api.core.TransportConfiguration;
import org.hornetq.api.core.client.ClientConsumer;
import org.hornetq.api.core.client.ClientSession;
import org.hornetq.api.core.client.ClientSessionFactory;
import org.hornetq.api.core.client.HornetQClient;
import org.hornetq.api.core.client.ServerLocator;
import org.hornetq.core.remoting.impl.netty.NettyConnectorFactory;

public class HTest {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("host", "myhost");
		map.put("port", "5445");
		TransportConfiguration server1 = new TransportConfiguration(NettyConnectorFactory.class.getName(), map);
		HashMap<String, Object> map2 = new HashMap<String, Object>();
		map2.put("host", "myhost2");
		map2.put("port", "5446");
		TransportConfiguration server2 = new TransportConfiguration(NettyConnectorFactory.class.getName(), map2);
		ServerLocator locator = HornetQClient.createServerLocatorWithHA(server1, server2);
		new org.hornetq.jms.client.HornetQConnectionFactory(locator);
		ClientSessionFactory factory = locator.createSessionFactory();
		ClientSession session = factory.createSession();
		ClientConsumer consumer = session.createConsumer("");
		//connection.start();

		//
		CountDownLatch latch = new CountDownLatch(1);
		//while (message == null) {
//			if (receiver(latch, consumer)) {
//				break;
//			}
		//}
	}

	private static boolean receiver(CountDownLatch latch, MessageConsumer consumer) {
		// TODO Auto-generated method stub
		out.println("JBOSS 收到-----");
		Message message;
		try {
			message = consumer.receive(5000);
			out.println("message :" + message);
			latch.await(1, TimeUnit.SECONDS);
			return false;
		} catch (JMSException | InterruptedException ignore) {
			// TODO Auto-generated catch block
			out.print("ignore =" + ignore.getMessage());
			return true;
		}

	}

}
