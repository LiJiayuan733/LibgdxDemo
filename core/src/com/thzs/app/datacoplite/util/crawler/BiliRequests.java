package com.thzs.app.datacoplite.util.crawler;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;

public class BiliRequests extends Request{
	public interface Function{
		public abstract void call(BiliProcessor process, Page page);
	}
	public Function function=null;
	public BiliRequests() {
		super();
		addHeader("accept", "*/*");
        addHeader("accept-language", "zh-CN,zh;q=0.9,en;q=0.8");
        addHeader("origin", "https://www.bilibili.com/");
        addHeader("referer", "https://www.bilibili.com");
        addHeader("sec-ch-ua", "\"Google Chrome\";v=\"93\", \" Not;A Brand\";v=\"99\", \"Chromium\";v=\"93\"");
        addHeader("user-agent","Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/95.0.4638.69 Safari/537.36");
        addHeader("sec-ch-ua-mobile", "?0");
        addHeader("sec-ch-ua-platform", "\"macOS\"");
        addHeader("sec-fetch-dest","empty");
        addHeader("sec-fetch-mode","cors");
        addHeader("sec-fetch-site","same-site");
		BiliSpriderConfig.AddCookies(this);
	}
	public BiliRequests(String string) {
		super(string);
		BiliSpriderConfig.AddCookies(this);
		addHeader("accept", "application/json, text/javascript, */*; q=0.01");
        addHeader("accept-language", "zh-CN,zh;q=0.9,en;q=0.8");
        addHeader("content-type","application/x-www-form-urlencoded; charset=utf-8");
        addHeader("origin", "https://www.bilibili.com/");
        addHeader("referer", "https://www.bilibili.com/");
        addHeader("user-agent","Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/95.0.4638.69 Safari/537.36");
        addHeader("sec-ch-ua", "\"Google Chrome\";v=\"93\", \" Not;A Brand\";v=\"99\", \"Chromium\";v=\"93\"");
        addHeader("sec-ch-ua-mobile", "?0");
        addHeader("sec-ch-ua-platform", "\"macOS\"");
        addHeader("sec-fetch-dest","empty");
        addHeader("sec-fetch-mode","cors");
        addHeader("sec-fetch-site","same-site");
        addHeader("cookie",CrawlerConfigData.Cookie);
	}
	public BiliRequests setFunction(Function fun) {
		this.function=fun;
		return this;
	}
	public int RequestsCODE=0;
	private static final long serialVersionUID = 8060560123641803537L;

}
