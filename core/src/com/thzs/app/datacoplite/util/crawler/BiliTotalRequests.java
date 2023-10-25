package com.thzs.app.datacoplite.util.crawler;

public class BiliTotalRequests extends BiliRequests{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5887420725025871503L;
	public static String URL="https://api.bilibili.com/x/player/online/total?aid=721226816&cid=429695616&bvid=BV1TQ4y1q7b6&ts=54600970";
	public BiliTotalRequests(String mid) {
		super(URL);
		this.RequestsCODE=8;
	}
}
