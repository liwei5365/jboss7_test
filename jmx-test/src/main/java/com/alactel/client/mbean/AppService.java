package com.alactel.client.mbean;

import java.io.IOException;

import javax.management.MalformedObjectNameException;

import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.alactel.client.jmx.RemoteJmxClient;
import com.alactel.client.jmx.RemoteJmxClient205;

public class AppService implements AppServiceMBean {

	private static final String DEFAULT_SPRING_XML = "META-INF/applicationContext.xml";
	private static final Logger logger = Logger.getLogger(AppService.class);
	// default app name
	private String appName = "JMX JMS TEST";
	private ClassPathXmlApplicationContext ctx;

	//
	public void start() throws Exception {
		// TODO Auto-generated method stub
		logger.info(this.getAppName()+" System starting...");
		long start = System.currentTimeMillis();
		ctx = new ClassPathXmlApplicationContext(DEFAULT_SPRING_XML);
		logger.info("Starting Application Service " + getAppName() + " took " + (System.currentTimeMillis() - start));

	}

	public void stop() {

		if (ctx != null) {
			ctx.close();
			ctx = null;
		}
		logger.info("AppService stop() ");
	}

	@Override
	public void setAppName(String name) {
		this.appName = name;
	}

	@Override
	public String getAppName() {
		return appName;
	}

	@Override
	public void testjmx() {
		// TODO Auto-generated method stub
		try {
			RemoteJmxClient.invoke();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void testjms() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void testjmx205() {
		// TODO Auto-generated method stub
		try {
			RemoteJmxClient205.invoke();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
