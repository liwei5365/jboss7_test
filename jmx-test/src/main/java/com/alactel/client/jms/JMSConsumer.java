package com.alactel.client.jms;

import static java.lang.System.out;

import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;

public class JMSConsumer {
 
    private static final String DEFAULT_CONNECTION_FACTORY = "jms/RemoteConnectionFactory";
    private static final String DEFAULT_DESTINATION = "jms/queue/test";
	private static final String DEFAULT_USERNAME = "appuser";
	private static final String DEFAULT_PASSWORD = "ans#150!";
    private static final String INITIAL_CONTEXT_FACTORY = "org.jboss.naming.remote.client.InitialContextFactory";
//	private static final String PROVIDER_URL = "remote://135.251.31.19:4447";
	private static final String PROVIDER_URL = "remote://135.251.246.207:4447";
 
    public static void main(String[] args) throws Exception {
 
        ConnectionFactory connectionFactory = null;
        Connection connection = null;
        Session session = null;
        MessageConsumer consumer = null;
        Destination destination = null;
        TextMessage message = null;
        Context context = null;
 
        try {
            final Properties env = new Properties();
            env.put(Context.INITIAL_CONTEXT_FACTORY, INITIAL_CONTEXT_FACTORY);
            env.put(Context.PROVIDER_URL, PROVIDER_URL);
            env.put(Context.SECURITY_PRINCIPAL, DEFAULT_USERNAME);
            env.put(Context.SECURITY_CREDENTIALS, DEFAULT_PASSWORD);
            context = new InitialContext(env);
 
            connectionFactory = (ConnectionFactory) context.lookup(DEFAULT_CONNECTION_FACTORY);
            destination = (Destination) context.lookup(DEFAULT_DESTINATION);
 
            connection = connectionFactory.createConnection(DEFAULT_USERNAME, DEFAULT_PASSWORD);
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            consumer = session.createConsumer(destination);
            connection.start();
 
            // �ȴ�30���˳�
            CountDownLatch latch = new CountDownLatch(1);
            while (message == null) {
                out.println("��ʼ��JBOSS�˽�����Ϣ-----");
                message = (TextMessage) consumer.receive(5000);
                latch.await(1, TimeUnit.SECONDS);
            }
            out.println("���յ�����Ϣ������:" + message.getText());
        } catch (Exception e) {
        	out.println(e.getMessage());
            throw e;
        } finally {
            if (context != null) {
                context.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }
}