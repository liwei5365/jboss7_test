package com.alactel.client.jms;

import static java.lang.System.out;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
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

import org.hornetq.api.core.DiscoveryGroupConfiguration;
import org.hornetq.api.core.TransportConfiguration;
import org.hornetq.api.core.UDPBroadcastGroupConfiguration;
import org.hornetq.api.core.client.ClientSessionFactory;
import org.hornetq.api.core.client.FailoverEventListener;
import org.hornetq.api.core.client.FailoverEventType;
import org.hornetq.api.core.client.HornetQClient;
import org.hornetq.api.core.client.ServerLocator;
import org.hornetq.core.client.impl.ClientSessionFactoryImpl;
import org.hornetq.core.client.impl.ServerLocatorImpl;
import org.hornetq.core.remoting.impl.netty.NettyConnectorFactory;
import org.hornetq.jms.client.HornetQConnection;
import org.hornetq.jms.client.HornetQConnectionFactory;

public class JMSConsumerTest {

	private static final String DEFAULT_CONNECTION_FACTORY = "jms/RemoteConnectionFactory";
	private static final String DEFAULT_DESTINATION = "jms/test";
//	private static final String DEFAULT_USERNAME = "system";
//	private static final String DEFAULT_PASSWORD = "4x5@system";
	private static final String DEFAULT_USERNAME = "appuser";
	private static final String DEFAULT_PASSWORD = "ans#150!";
	private static final String INITIAL_CONTEXT_FACTORY = "org.jboss.naming.remote.client.InitialContextFactory";
	// private static final String PROVIDER_URL = "remote://135.251.31.19:4447";
	private static final String PROVIDER_URL = "remote://192.168.1.140:4447,remote://192.168.1.141:4447";

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
//			env.load(JMSConsumerTest.class.getClassLoader().getResourceAsStream("jboss-ejb-client.properties"));
			env.put(Context.INITIAL_CONTEXT_FACTORY, INITIAL_CONTEXT_FACTORY);
//			env.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
			env.put(Context.PROVIDER_URL, PROVIDER_URL);
			env.put(Context.SECURITY_PRINCIPAL, DEFAULT_USERNAME);
			env.put(Context.SECURITY_CREDENTIALS, DEFAULT_PASSWORD);
			
			//while (true) {
				context = new InitialContext(env);
//				ConnectionFactory tempConnectionFactory = (ConnectionFactory) context.lookup(DEFAULT_CONNECTION_FACTORY);
//				final org.hornetq.jms.client.HornetQConnectionFactory srcCF=(HornetQConnectionFactory) tempConnectionFactory;
//				System.out.println("srcCF: "+tempConnectionFactory);
//				ServerLocatorImpl sl = (ServerLocatorImpl) srcCF.getServerLocator();
//				Field fname = ServerLocatorImpl.class.getField("discoveryGroupConfiguration");
//				fname.setAccessible(true);
				
//				fname.set(sl, new DiscoveryGroupConfiguration("dg-group1",5432,5432,new UDPBroadcastGroupConfiguration("231.7.7.7",9876,"192.168.1.140",-1)));
//				TransportConfiguration[] tc = srcCF.getServerLocator().getStaticTransportConfigurations();
//				String host=(String) tc[0].getParams().get("host");
//				if("192.168.1.140".equals(host)){
//					addtc(tc, "192.168.1.141",sl);
//					System.out.println(tc);
//				}else if("192.168.1.141".equals(host)){
//					addtc(tc, "192.168.1.140",sl);
//					System.out.println(tc);
//				}else{
//					
//				}
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("host", "192.168.1.140");
				map.put("port", "5445");
				TransportConfiguration server1 = new TransportConfiguration(NettyConnectorFactory.class.getName(), map);
				HashMap<String, Object> map2 = new HashMap<String, Object>();
				map2.put("host", "192.168.1.141");
				map2.put("port", "5445");
				TransportConfiguration server2 = new TransportConfiguration(NettyConnectorFactory.class.getName(), map2);
				ServerLocator locator = HornetQClient.createServerLocatorWithHA(server1, server2);
				System.out.println("locator.getTopology() = "+locator.getTopology());
//			
				final HornetQConnectionFactory srcCF = new org.hornetq.jms.client.HornetQConnectionFactory(locator);
				
				srcCF.setClientID("jms-client-1");
				srcCF.setReconnectAttempts(-1);
				srcCF.setRetryInterval(5000);
				//
				System.out.println("srcCF: "+srcCF);
				//System.arraycopy(src, srcPos, dest, destPos, length);
				
				destination = (Destination) context.lookup(DEFAULT_DESTINATION);

				connection = srcCF.createXAConnection(DEFAULT_USERNAME, DEFAULT_PASSWORD);
				System.out.println("connection : "+connection);
				final HornetQConnection srcConnect = (HornetQConnection)connection;
				FailoverEventListener fasListen = srcConnect.getFailoverListener();
				ExceptionListener exListen = srcConnect.getExceptionListener();
				srcConnect.setFailoverListener(new FailoverEventListener(){

					@Override
					public void failoverEvent(FailoverEventType paramFailoverEventType) {
						// TODO Auto-generated method stub
						System.out.println("setFailoverListener failoverEvent "+paramFailoverEventType);
//						if(FailoverEventType.FAILURE_DETECTED!=paramFailoverEventType){
//							return ;
//						}
//						try {
//							Field fn = HornetQConnection.class.getDeclaredField("sessionFactory");
//							fn.setAccessible(true);
//							ClientSessionFactoryImpl sf = (ClientSessionFactoryImpl) fn.get(srcConnect);
//							System.out.println("ClientSessionFactory "+sf);
//							String ip=(String) sf.getConnectorConfiguration().getParams().get("host");
//							if("192.168.1.140".equals(ip)){
//								 sf.getConnectorConfiguration().getParams().put("host", "192.168.1.141");
//								 srcConnect.setFailoverListener(this);
//							}else if("192.168.1.141".equals(ip)){
//								 sf.getConnectorConfiguration().getParams().put("host", "192.168.1.140");
//								 srcConnect.setFailoverListener(this);
//							}else{
////								ignore
//							}
//							System.out.println("ClientSessionFactory "+sf);
//						} catch (Exception e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
						
						
					}
					
				});
				srcConnect.setExceptionListener(new ExceptionListener(){

					@Override
					public void onException(JMSException e) {
						// TODO Auto-generated method stub
						System.out.println("JMSConsumerTest ExceptionListener "+e.getMessage());
					}});
				session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
				consumer = session.createConsumer(destination);
				connection.start();

				//
				CountDownLatch latch = new CountDownLatch(1);
				while (message == null) {
					if(receiver(latch, consumer)){
						break;
					}
				}
//			}

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

	private static void addtc(TransportConfiguration[] tc, String otherIp,ServerLocatorImpl sl) throws Exception {
		Map<String,Object> map=new HashMap<>();
		for(Map.Entry<String, Object> entry : tc[0].getParams().entrySet()){
			if("host".equals(entry.getKey())){
				map.put("host", otherIp);
			}else{
				map.put(entry.getKey(), entry.getValue());
			}
		}
		TransportConfiguration temp = new TransportConfiguration(tc[0].getFactoryClassName(),map,tc[0].getName());
		TransportConfiguration[] newtc=new TransportConfiguration[tc.length+1];
		System.arraycopy(tc, 0, newtc, 0, tc.length);
		
		newtc[tc.length]=temp;
		//
		Field init = sl.getClass().getField("initialConnectors");
		init.setAccessible(true);
		init.set(sl, newtc);
		
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