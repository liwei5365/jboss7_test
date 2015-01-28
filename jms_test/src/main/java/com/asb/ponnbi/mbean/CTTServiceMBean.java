package com.asb.ponnbi.mbean;


public interface CTTServiceMBean{
	
	public void setAppName(String name);

	public String getAppName();
	
	public void send(String str);
	
	public void selectClasspath(String clazzName);
}
