package com.thzs.app.datacoplite.util.crawler;

public class BiliViewRequests extends BiliRequests{

	private static final long serialVersionUID = 4158155623273691335L;
	public static String URL="http://api.bilibili.com/x/web-interface/view?";
	public BiliViewRequests(String BVID) {
		super(URL+"bvid="+BVID);
		this.RequestsCODE=412;
	}
}
