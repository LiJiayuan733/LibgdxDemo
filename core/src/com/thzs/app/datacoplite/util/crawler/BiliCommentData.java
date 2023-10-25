package com.thzs.app.datacoplite.util.crawler;

import com.alibaba.fastjson.JSONObject;

public class BiliCommentData {
	public String rpid;
	public String message;
	public int count;
	public int like;
	//0无 1点赞 2点踩
	public int action;
	public BiliUserData member;
	public BiliCommentData(JSONObject jo) {
		rpid=jo.getString("rpid");
		message=jo.getJSONObject("content").getString("message");
		count=jo.getInteger("count");
		like=jo.getInteger("like");
		action=jo.getInteger("action");
		if(!jo.get("member").equals("null")){
			member=new BiliUserData().loadFromMember(jo.getJSONObject("member"));
		}
	}
}
