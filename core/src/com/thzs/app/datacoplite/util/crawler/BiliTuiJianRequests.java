package com.thzs.app.datacoplite.util.crawler;

public class BiliTuiJianRequests extends BiliRequests{
	public static String URL="https://api.bilibili.com/x/web-interface/index/top/rcmd?fresh_type=3";
	private static final long serialVersionUID = 3318362504577418332L;
	public BiliTuiJianRequests() {
		super(URL);
		this.RequestsCODE=1;
	}
}
