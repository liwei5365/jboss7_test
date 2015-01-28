package com.alactel.client.jmx;
import java.io.IOException;
import java.util.HashMap;

import javax.management.JMX;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import com.alcatel.axs.app.cnnbi.api.CommonAmsService;


public class RemoteJmxClient205 {
    public static void invoke() throws Exception {
        
            //Get a connection to the JBoss AS MBean server on localhost
//            String host = "135.251.31.19";
    	 	String host = "135.251.246.205";
//            String host = "localhost";
            int port = 9999;  // management-native port
            String urlString =System.getProperty("jmx.service.url","service:jmx:remoting-jmx://" + host + ":" + port);
            JMXServiceURL serviceURL = new JMXServiceURL(urlString);
            JMXConnector jmxConnector = JMXConnectorFactory.connect(serviceURL, new HashMap<String,Object>(){
            	{
            		 String[] creds = new String[2];
            	        creds[0] = "admin";
            	        creds[1] = "ans#150!";
            	        this.put(JMXConnector.CREDENTIALS, creds);         		
            	}
            } );
//            JMXConnector jmxConnector = JMXConnectorFactory.connect(serviceURL, null);
            MBeanServerConnection connection = jmxConnector.getMBeanServerConnection();
     
            // list domains
            String[] domains = connection.getDomains();
            for (String domain : domains) {
                System.out.println("domain : " + domain);
            }
//
//            // list ObjectNames
//            Set<ObjectName> names = connection.queryNames(null, null);
//            for (ObjectName name : names) {
//                System.out.println("ObjectName : " + name);
//            }

            // remote operation
            ObjectName name = new ObjectName("application.cnnbi:service=CommonAmsService,type=vaddapp");
            boolean flag = connection.isRegistered(name);
            System.out.println("flag = "+flag);
            CommonAmsService app = JMX.newMBeanProxy(connection, name,  CommonAmsService.class);
			System.out.println("getOltIPByOltName = "+app.getOltIPByOltName("GPON37"));
			System.out.println("getOltTypeByName = "+app.getOltTypeByName("GPON37"));
			System.out.println("getOltVersionByIp = "+app.getOltVersionByIp("135.251.201.37"));

            // close connection
            jmxConnector.close();
            
//            mbean.getClass().getClassLoader();
           
        }
  
}