package com.thzs.app.datacoplite.util.crawler;

public class BiliUserCardRequests extends BiliRequests {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4445890751961845395L;
	public static String URL="https://api.bilibili.com/x/web-interface/card?mid=&photo=1&jsonp=jsonp";
	public BiliUserCardRequests(String mid) {
		super(URL.replace("mid=", "mid="+mid));
		this.RequestsCODE=7;
	}
}
