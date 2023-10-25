package com.thzs.app.datacoplite.util.crawler;

public class BiliVideoPageListRequests extends BiliRequests{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3026501095860402522L;
	public static String URL="https://api.bilibili.com/x/player/pagelist?bvid=&jsonp=jsonp";
	public BiliVideoPageListRequests(String bvid) {
		super(URL.replace("bvid=", "bvid="+bvid));
		this.RequestsCODE=6;
	}
}
