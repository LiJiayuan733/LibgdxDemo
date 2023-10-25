package com.thzs.app.datacoplite.util.crawler;

public class BiliTagRequests extends BiliRequests{

	private static final long serialVersionUID = 603854454835337048L;
	public static String URL="https://api.bilibili.com/x/web-interface/view/detail/tag?aid=";
	public BiliTagRequests(String aid) {
		super(URL.replace("aid=", "aid="+aid));
		this.RequestsCODE=5;
	}
}
