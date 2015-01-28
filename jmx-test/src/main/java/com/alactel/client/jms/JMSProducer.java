package com.alactel.client.jms;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import java.util.Properties;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import static java.lang.System.out;

/**
 * <p>
 * Description:JMS�ͻ�����Ϣ�����
 * </p>
 */
public class JMSProducer {
//	private static final Logger log = Logger.getLogger(JMSProducer.class.getName());

	private static final String DEFAULT_MESSAGE = "fz,hello world!";
	// xml�ļ�272��
	private static final String DEFAULT_CONNECTION_FACTORY = "jms/RemoteConnectionFactory";
	// xml�ļ�293��,������JBOSS��JNDI̫��������
	private static final String DEFAULT_DESTINATION = "jms/queue/test";
	private static final String DEFAULT_MESSAGE_COUNT = "1";

	private static final String DEFAULT_USERNAME = "appuser";
	private static final String DEFAULT_PASSWORD = "ans#150!";
	private static final String INITIAL_CONTEXT_FACTORY = "org.jboss.naming.remote.client.InitialContextFactory";
	private static final String PROVIDER_URL = "remote://135.251.246.207:4447";
//	private static final String PROVIDER_URL = "remote://135.251.31.19:4447";
	public static void main(String[] args) throws Exception {
		Context context = null;
		Connection connection = null;
		try {
			// ���������ĵ�JNDI����
			System.out.println("����JNDI���ʻ�����ϢҲ��������Ӧ�÷���������������Ϣ!");
			final Properties env = new Properties();
			env.put(Context.INITIAL_CONTEXT_FACTORY, INITIAL_CONTEXT_FACTORY);// ��KEY��ֵΪ��ʼ��Context�Ĺ�����,JNDI�������
			env.put(Context.PROVIDER_URL, PROVIDER_URL);// ��KEY��ֵΪContext�����ṩ�ߵ�URL.��������ṩ�ߵ�URL
			env.put(Context.SECURITY_PRINCIPAL, DEFAULT_USERNAME);
			env.put(Context.SECURITY_CREDENTIALS, DEFAULT_PASSWORD);// Ӧ���û��ĵ�¼��,����.
			// ��ȡ��InitialContext����.
			context = new InitialContext(env);
			out.println("��ʼ��������,'JNDI������','�����ṩ��URL','Ӧ���û����˻�','����'���.");

			out.println("��ȡ���ӹ���!");
			ConnectionFactory connectionFactory = (ConnectionFactory) context.lookup(DEFAULT_CONNECTION_FACTORY);
			out.println("��ȡĿ�ĵ�!");
			Destination destination = (Destination) context.lookup(DEFAULT_DESTINATION);

			// ����JMS���ӡ��Ự������ߺ������
			connection = connectionFactory.createConnection(DEFAULT_USERNAME, DEFAULT_PASSWORD);
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			MessageProducer producer = session.createProducer(destination);
			connection.start();

			int count = Integer.parseInt(DEFAULT_MESSAGE_COUNT);
			// �����ض���Ŀ����Ϣ
			TextMessage message = null;
			for (int i = 0; i < count; i++) {
				message = session.createTextMessage(DEFAULT_MESSAGE);
				producer.send(message);
				out.println("message:" + message);
				out.println("message:" + DEFAULT_MESSAGE);
			}
			// �ȴ�30���˳�
			CountDownLatch latch = new CountDownLatch(1);
			latch.await(30, TimeUnit.SECONDS);

		} catch (Exception e) {
			out.println(e.getMessage());
			throw e;
		} finally {
			if (context != null) {
				context.close();
			}
			// �ر����Ӹ���Ự,����̺������
			if (connection != null) {
				connection.close();
			}
		}
	}
}