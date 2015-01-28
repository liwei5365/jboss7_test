package com.alcatel.axs.app.cnnbi.api;


public interface CommonAmsService {
	
	public String getOltTypeByIp(String oltIP)throws Exception;
	
	public String getOltTypeByName(String neName)throws Exception;
	
	public String getOltVersionByIp(String neIP)throws Exception;
	
	public String getOltVersionByName(String neName)throws Exception;

    public String getActualTypeByIPAndLt(String oltIP, String ltAid)throws Exception ;

    public String getPonTypeByOlt(String neIp, String slot)throws Exception ;

    public String getOltIPByOltName(String oltName)throws Exception;  

}

