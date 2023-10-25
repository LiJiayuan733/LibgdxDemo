package com.thzs.app.datacoplite.util.crawler;

import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;

public class BiliSpriderConfig {
	//public static String Cookie="_uuid=BE0B30EE-5354-06A1-3001-89F095338D1285044infoc; buvid3=BEDE10E2-091A-44D4-A428-F1A113686462167626infoc; blackside_state=0; rpdid=|(YYYJ|)k)k0J'uYJJRk~|lu; buvid_fp=BEDE10E2-091A-44D4-A428-F1A113686462167626infoc; CURRENT_QUALITY=120; LIVE_BUVID=AUTO8716335116097477; fingerprint=a547c14d247386b0d24976d7cf853946; buvid_fp_plain=B6268698-7572-4580-AE33-E65A04E6457A148793infoc; SESSDATA=ee58b510,1651059000,0435d*a1; bili_jct=929e7fb8ca5cc3646ebc76473b8fda8c; DedeUserID=384909437; DedeUserID__ckMd5=6ce3928ced4abcb4; sid=bj9cqo7y; CURRENT_BLACKGAP=0; bp_video_offset_384909437=592116715560622310; video_page_version=v_old_home; bp_t_offset_384909437=597801946587494810; CURRENT_FNVAL=976; PVID=1; innersign=1; b_lsid=DFE9F6F1_17D63BFE704";
	public static void AddCookies(Request req) {
		for(String i:CrawlerConfigData.Cookie.split("; ")) {
			String[] p=i.split("=");
			req.addCookie(p[0], p[1]);
		}
	}
	public static void AddCookies(Site site) {
		for(String i:CrawlerConfigData.Cookie.split("; ")) {
			String[] p=i.split("=");
			site.addCookie(p[0], p[1]);
		}
	}
}
