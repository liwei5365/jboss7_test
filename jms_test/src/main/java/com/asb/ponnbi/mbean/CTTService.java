package com.asb.ponnbi.mbean;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.asb.ponnbi.t1.MessageProducer;

public class CTTService implements CTTServiceMBean {

	private String appName = "ctin-jms-jboss7";
	private static ConfigurableApplicationContext ctx;

	@Override
	public void setAppName(String name) {
		// TODO Auto-generated method stub
		appName = name;
	}

	@Override
	public String getAppName() {
		// TODO Auto-generated method stub
		return appName;
	}

	@Override
	public void send(String str) {
		// TODO Auto-generated method stub
		String sendStr = null;
		if (str == null || str.trim().length() == 0) {
			sendStr = appName;
		} else {
			sendStr = str;
		}
		MessageProducer producer=(MessageProducer) ctx.getBean("producer");
		System.out.println("producer : "+producer);
		if(producer!=null){
			producer.send(sendStr);
		}
	}

	public void start() throws Exception {
		System.out.println(appName + "  : start ");
		ctx = new ClassPathXmlApplicationContext("applicationContext_jms_jboss7_test.xml");

	}

	public void stop() throws Exception {
		if (ctx != null) {
			ctx.close();
			ctx = null;
		}
	}

	@Override
	public void selectClasspath(String clazzName)  {
		Class clazz;
		try {
			clazz = this.getClass().getClassLoader().loadClass(clazzName);
			System.out.println("clazz : "+clazz);
			System.out.println("clazz.getClassLoader : "+clazz.getClassLoader());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
