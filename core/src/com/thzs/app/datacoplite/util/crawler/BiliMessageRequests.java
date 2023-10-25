package com.thzs.app.datacoplite.util.crawler;

import us.codecraft.webmagic.model.HttpRequestBody;
import us.codecraft.webmagic.utils.HttpConstant;

import java.util.HashMap;
import java.util.Map;

public class BiliMessageRequests extends BiliRequests {

	/**
	 * 
	 */
	public static String URL="https://api.bilibili.com/x/v2/reply/add";
	private static final long serialVersionUID = 5335767403495307939L;
	public BiliMessageRequests(String oid,String message) {
		super(URL);
		this.RequestsCODE=2;
		this.setMethod(HttpConstant.Method.POST);
		
		
		Map<String,Object> params=new HashMap<>();
		params.put("oid", oid);
		params.put("type", "1");
		params.put("message", message);
		params.put("ordering", "heat");
		params.put("jsonp", "jsonp");
		params.put("plat", "1");
		params.put("csrf", this.getCookies().get("bili_jct"));
		params.put("SESSDATA",this.getCookies().get("SESSDATA"));	
		params.put("buvid3", this.getCookies().get("buvid3"));
		setRequestBody(HttpRequestBody.form(params, "utf-8"));
	}
}
