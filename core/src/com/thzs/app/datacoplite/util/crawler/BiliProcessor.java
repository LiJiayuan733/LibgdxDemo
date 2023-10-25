package com.thzs.app.datacoplite.util.crawler;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

public class BiliProcessor implements PageProcessor {
	private Site mysite = Site.me().setRetryTimes(3).setUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/95.0.4638.69 Safari/537.36").setRetrySleepTime(300);

	public BiliProcessor() {
		super();
		BiliSpriderConfig.AddCookies(mysite);
	}

	@Override
	public void process(Page page) {
		BiliRequests bq = (BiliRequests) page.getRequest();
		if(bq.function!=null) {
			bq.function.call(this,page);
		}
	}

	@Override
	public Site getSite() {
		return mysite;
	}

}
