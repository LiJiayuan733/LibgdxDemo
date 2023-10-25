//https://api.bilibili.com/x/v2/reply/main
/**jsonp: jsonp
    next: 1
    type: 1
    oid: 719454545
    mode: 2
    plat: 1
_: 1638087076527
 * 
 */
package com.thzs.app.datacoplite.util.crawler;

public class BiliCommentsRequests extends BiliRequests {
	public static String URL="https://api.bilibili.com/x/v2/reply/main?oid=&plat=1&mode=2&type=1&next=1";
	public static String URL2="http://api.bilibili.com/x/v2/reply/reply?type=1&oid=&root=&pn=";
	private static final long serialVersionUID = 5335767403495307940L;
	public BiliCommentsRequests(String oid,String next) {
		super(URL.replace("oid=","oid="+oid).replace("next=1","next="+next));
		this.RequestsCODE=8;
	}
	public BiliCommentsRequests(String oid,String root,String pn) {
		super(URL2.replace("oid=","oid="+oid).replace("pn=","pn="+pn).replace("root=", "root="+root));
		this.RequestsCODE=8;
	}
}