package com.thzs.app.datacoplite.util.crawler;


import com.alibaba.fastjson.JSONObject;

import java.awt.*;

public class BiliUserData {
	public String mid=null;
	public String uname=null;
	//性别
	public String sex=null;
	//发送者签名
	public String sign=null;
	//发送者头像
	public String avatar=null;
	//当前等级
	public String current_level=null;
	public User_sailing user_sailing=null;
	public class User_sailing{
		public Cardbg cardbg;
		public class Cardbg{
			public String id=null;
			public String name=null;
			public String image=null;
			public class Fan{
				public int is_fan=-1;
				public String num_desc=null;
				public Color color=null;
				public String name=null;
				public Fan(JSONObject jo) {
					is_fan=jo.getInteger("is_fan");
					num_desc=jo.getString("num_desc");
					color=Color.decode(jo.getString("color"));
					name=jo.getString("name");
				}
			}
			public Fan fan=null;
			public String type=null;
			public Cardbg(JSONObject jo) {
				id=jo.getString("id");
				name=jo.getString("name");
				image=jo.getString("image");
				if(!jo.getString("fan").equals("null")) {
					fan=new Fan(jo.getJSONObject("fan"));
				}
				type=jo.getString("type");
			}
		}
		public User_sailing(JSONObject jo){
			if(!jo.getString("cardbg").equals("null")) {
				this.cardbg=new Cardbg(jo.getJSONObject("cardbg"));
			}
		}
	}
	public BiliUserData() {
		
	}
	public BiliUserData loadFromMember(JSONObject jo) {
		mid=jo.getString("mid");
		uname=jo.getString("uname");
		sex=jo.getString("sex");
		sign=jo.getString("sign");
		avatar=jo.getString("avatar");
		current_level=jo.getJSONObject("level_info").getString("current_level");
		if(!jo.containsKey("user_sailing")) {
			user_sailing=new User_sailing(jo.getJSONObject("user_sailing"));
		}
		return this;
	}
}
