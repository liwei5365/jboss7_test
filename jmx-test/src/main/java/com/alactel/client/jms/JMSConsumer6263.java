package com.alactel.client.jms;

import static java.lang.System.out;

import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;

public class JMSConsumer6263 {

	private static final String DEFAULT_CONNECTION_FACTORY = "jms/XAThroughputConnectionFactory";
	private static final String DEFAULT_DESTINATION = "topic/Inventory";
	private static final String DEFAULT_USERNAME = "system";
	private static final String DEFAULT_PASSWORD = "4x5@system";
	// private static final String DEFAULT_USERNAME = "appuser";
	// private static final String DEFAULT_PASSWORD = "ans#150!";
	private static final String INITIAL_CONTEXT_FACTORY = "org.jboss.naming.remote.client.InitialContextFactory";
	// private static final String PROVIDER_URL =
	// "remote://135.252.247.91:4447";
	private static final String PROVIDER_URL = "remote://135.252.247.90:4447,remote://135.252.247.91:4447";

	public static void main(String[] args) throws Exception {

		ConnectionFactory connectionFactory = null;
		Connection connection = null;
		Session session = null;
		MessageConsumer consumer = null;
		Destination destination = null;
		TextMessage message = null;
		Context context = null;

		final Properties env = new Properties();
		env.put(Context.INITIAL_CONTEXT_FACTORY, INITIAL_CONTEXT_FACTORY);
		env.put("java.naming.factory.url.pkgs", "org.jboss.naming=org.jnp.interfaces”");
		env.put(Context.PROVIDER_URL, PROVIDER_URL);
		env.put(Context.SECURITY_PRINCIPAL, DEFAULT_USERNAME);
		env.put(Context.SECURITY_CREDENTIALS, DEFAULT_PASSWORD);
		//
		if (args.length >= 1) {
			env.put("jnp.partitionName", args[0]);
		} else {
			env.put("jnp.partitionName", "192.168.1.90");
		}
		env.put("jnp.discoveryGroup", "225.1.2.5");
		env.put("jnp.discoveryPort", "1102");
		env.put("jnp.discoveryTTL", "16");
		env.put("jnp.discoveryTimeout", "50000");
		env.put("jnp.disableDiscovery", "false");
		env.put("jnp.maxRetries", "1");
		env.put("jnp.timeout", "30000");

		while (true) {
			try {
				context = new InitialContext(env);
				connectionFactory = (ConnectionFactory) context.lookup(DEFAULT_CONNECTION_FACTORY);
				System.out.println(connectionFactory);
				destination = (Destination) context.lookup(DEFAULT_DESTINATION);
				connection = connectionFactory.createConnection(DEFAULT_USERNAME, DEFAULT_PASSWORD);
				session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
				consumer = session.createConsumer(destination);
				connection.setExceptionListener(new ExceptionListener() {

					@Override
					public void onException(JMSException arg0) {
						// TODO Auto-generated method stub
						System.out.println("JMSConsumer6263 get JMSException " + arg0.getMessage());
					}
				});
				connection.start();

				//
				CountDownLatch latch = new CountDownLatch(1);
				while (message == null) {
					if (receiver(latch, consumer)) {
						break;
					}
				}
			} catch (Exception e) {
				out.println(e.getMessage());
			} finally {
				if (connection != null) {
					connection.close();
				}
				if (context != null) {
					context.close();
				}
			}
		}

	}

	private static boolean receiver(CountDownLatch latch, MessageConsumer consumer) {
		// TODO Auto-generated method stub
		out.println("JBOSS receiver-----");
		Message message;
		try {
			message = consumer.receive(5000);
			out.println("message :" + message);
			latch.await(2, TimeUnit.SECONDS);
			return false;
		} catch (JMSException | InterruptedException ignore) {
			// TODO Auto-generated catch block
			out.print("ignore =" + ignore.getMessage());
			return true;
		}

	}
}