package com.thzs.app.datacoplite.util.crawler;

public class BiliNoteRequests extends BiliRequests{

	/**
	 * 
	 */
	public static String URL="https://api.bilibili.com/x/note/publish/list/archive?oid=&oid_type=0&pn=1&ps=10";
	private static final long serialVersionUID = 3454913713695433597L;
	public BiliNoteRequests(String oid) {
		super(URL.replace("oid=","oid="+oid));
		this.RequestsCODE=4;
	}
}
