package com.thzs.app.datacoplite.util.crawler;

public class BiliNAVRequests extends BiliRequests{

	/**
	 * 
	 */
	public static String URL="https://api.bilibili.com/x/web-interface/nav";
	private static final long serialVersionUID = 8213698083992657932L;
	public BiliNAVRequests() {
		super(URL);
		this.RequestsCODE=3;
	}
}
